private fun part1(program: List<String>) {
    val registers = hashMapOf<String, Long>()

    var eip = 0
    var lastPlayedFrequency: Long? = null

    while (eip in 0 until program.size) {
        val line = program[eip]
        val pieces = line.split(" ")
        val instruction = pieces[0]

        val op1: String = pieces[1]
        val op2: Any? = when {
            instruction in listOf("snd", "rcv") -> null
            pieces[2].first() in 'a'..'z' -> pieces[2]
            else -> pieces[2].toLong()
        }

        val op1Val: Long = when(op1.first()) {
            in 'a'..'z' -> registers.getOrDefault(op1, 0L)
            else -> op1.toLong()
        }
        val op2Val: Long? = when(op2) {
            null -> null
            is Long -> op2
            is String -> registers.getOrDefault(op2, 0L)
            else -> throw IllegalStateException()
        }

        var offset = 1
        when (instruction) {
            "snd" -> lastPlayedFrequency = op1Val
            "set" -> registers[op1] = op2Val!!
            "add" -> registers[op1] = op1Val + op2Val!!
            "mul" -> registers[op1] = op1Val * op2Val!!
            "mod" -> registers[op1] = op1Val % op2Val!!
            "rcv" -> if (op1Val != 0L) {
                println("Answer to part 1: $lastPlayedFrequency")
                return
            }
            "jgz" -> if (op1Val > 0) offset = op2Val!!.toInt()
        }
        eip += offset
    }
}

//Not the code I'm most proud of, but I had enough of this problem.

private class DuetCPU(val pid: Int, val program: List<String>) {
    val registers = hashMapOf<String, Long>()
    var eip = 0
        private set

    var waiting = false //true if the CPU stopped on an incomplete rcv instruction
        private set
    var terminated = false
        private set

    var countSends = 0 //count of sent values
        private set

    val canRun //true if the CPU is not terminated or waiting
        get() = !waiting && !terminated

    private var receivedValue: Long? = null
    private val sentValues = mutableListOf<Long>()

    init {
        registers["p"] = pid.toLong()
    }

    fun receiveValue(value: Long) {
        if (!waiting)
            throw IllegalStateException("CPU $pid received a value but it was not waiting")
        if (terminated)
            throw IllegalStateException("CPU $pid received a value but it already terminated")
        receivedValue = value
        waiting = false
    }

    fun getSentValue(): Long? = when(sentValues.size) {
        0 -> null
        else -> sentValues.removeAt(0)
    }

    fun hasSentValues() = sentValues.size != 0

    //Run one step
    fun step() {
        val line = program[eip]
        val pieces = line.split(" ")
        val instruction = pieces[0]

        val op1: String = pieces[1]
        val op2: Any? = when {
            instruction in listOf("snd", "rcv") -> null
            pieces[2].first() in 'a'..'z' -> pieces[2]
            else -> pieces[2].toLong()
        }

        val op1Val: Long = when(op1.first()) {
            in 'a'..'z' -> registers.getOrDefault(op1, 0L)
            else -> op1.toLong()
        }
        val op2Val: Long? = when(op2){
            null -> null
            is Long -> op2
            is String -> registers.getOrDefault(op2, 0L)
            else -> throw IllegalStateException()
        }

        waiting = false
        var offset = 1
        when (instruction) {
            "set" -> registers[op1] = op2Val!!
            "add" -> registers[op1] = op1Val + op2Val!!
            "mul" -> registers[op1] = op1Val * op2Val!!
            "mod" -> registers[op1] = op1Val % op2Val!!
            "jgz" -> if (op1Val > 0) offset = op2Val!!.toInt()
            "snd" -> {
                sentValues.add(op1Val)
                countSends++
            }
            "rcv" ->
                if (receivedValue != null) {
                    registers[op1] = receivedValue!!
                    receivedValue = null
                } else {
                    waiting = true
                    offset = 0 //instruction not yet executed
                }
        }
        eip += offset

        if (eip !in 0 until program.size){
            terminated = true
        }
    }
}

private fun part2(program: List<String>) {
    val cpu0 = DuetCPU(0, program)
    val cpu1 = DuetCPU(1, program)
    val cpus = listOf(cpu0, cpu1)

    while (true) {
        for ((pid, cpu) in cpus.withIndex()) {
            val other = cpus[1 - pid]

            if (cpu.waiting) {
                val value = other.getSentValue()
                if (value != null)
                    cpu.receiveValue(value)
            }

            while(cpu.canRun) //run as many steps as possible
                cpu.step()
        }

        if (cpu0.terminated || cpu1.terminated)
            break

        val deadlock = cpu0.waiting && cpu1.waiting && !cpu0.hasSentValues() && !cpu1.hasSentValues()
        if (deadlock)
            break //both cpus waiting, exit
    }

    println("Answer to part 2: ${ cpu1.countSends }")
}

fun main(args: Array<String>) {
    val program = getInputLines().toList()
    part1(program)
    part2(program)
}
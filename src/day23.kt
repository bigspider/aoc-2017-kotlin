private fun Int.isPrime(): Boolean {
    if (this < 2)
        return false

    var d = 2
    while (d * d <= this) {
        if (this % d == 0)
            return false

        ++d
    }
    return true
}

fun main(args: Array<String>) {
    val program = getInputLines().toList()

    var eip = 0
    val registers = mutableMapOf<Char, Int>()

    var mulCount = 0

    fun step() {
        val (instruction, op1, op2) = program[eip].split(" ")

        fun opValue(op: String): Int = when(op.first()) {
            in 'a'..'z' -> registers.getOrDefault(op.first(), 0)
            else -> op.toInt()
        }

        val op1val = opValue(op1)
        val op2val = opValue(op2)

        var offset = 1

        if (instruction == "mul")
            ++mulCount

        when (instruction) {
            "set" -> registers[op1.first()] = op2val
            "sub" -> registers[op1.first()] = op1val - op2val
            "mul" -> registers[op1.first()] = op1val * op2val
            "jnz" -> if (op1val != 0) offset = op2val
            else -> throw IllegalArgumentException("Wrong instruction")
        }

        eip += offset
    }

    while (eip in 0 until program.size)
        step()

    println("mul was invoked $mulCount times.")

    //Run the first 7 instructions to get start and end of the interval
    registers.clear()
    eip = 0
    registers['a'] = 1
    repeat(7){ step() }
    val start = registers['b']!!
    val end = registers['c']!!

    val nComposites = (start..end step 17).count { !it.isPrime() }
    println("h is $nComposites")
}
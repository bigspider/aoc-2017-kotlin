fun main(args: Array<String>) {
    val input = getInputLines().map { it.toInt() }.toList()

    //Part 1
    var eip = 0
    var count = 0

    var instructions = input.toMutableList()

    while (eip in 0 until instructions.size) {
        val instr = instructions[eip]
        instructions[eip] += 1

        eip += instr
        ++count
    }
    println("Answer to part 1: $count")

    //Part 2
    eip = 0
    count = 0
    instructions = input.toMutableList()
    while (eip in 0 until instructions.size) {
        val instr = instructions[eip]
        if (instr >= 3)
            instructions[eip] -= 1
        else
            instructions[eip] += 1

        eip += instr
        ++count
    }
    println("Answer to part 2: $count")
}
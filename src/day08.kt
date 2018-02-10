private data class Instruction(val register: String, val increment: Int, val conditionRegister: String, val condition: (Int) -> Boolean) {
    fun execute(state: MutableMap<String, Int>) {
        if (condition(state.getOrDefault(conditionRegister, 0))) {
            val oldValue = state.getOrDefault(register, 0)
            state.set(register, oldValue + increment)
        }
    }
}

private fun parseAsInstruction(str: String): Instruction {
    val pieces = str.split(" ")

    val register = pieces[0]
    val increment = (if (pieces[1] == "inc") +1 else -1) * pieces[2].toInt()
    val conditionRegister = pieces[4]
    val operator = pieces[5]
    val compareValue = pieces[6].toInt()
    val condition: (Int) -> Boolean = when (operator) {
        "==" -> { n: Int -> n == compareValue }
        "!=" -> { n: Int -> n != compareValue }
        ">" -> { n: Int -> n > compareValue }
        "<" -> { n: Int -> n < compareValue }
        ">=" -> { n: Int -> n >= compareValue }
        "<=" -> { n: Int -> n <= compareValue }
        else -> throw IllegalStateException("Unknown operator: $operator")
    }
    return Instruction(register, increment, conditionRegister, condition)
}

fun main(args: Array<String>) {
    val state = HashMap<String, Int>()
    val registers = HashSet<String>()
    var maxValue = Int.MIN_VALUE
    for (line in getInputLines()) {
        val instr = parseAsInstruction(line)
        registers.add(instr.register)
        registers.add(instr.conditionRegister)
        instr.execute(state)

        maxValue = maxOf(maxValue, state.getOrDefault(instr.register, 0), state.getOrDefault(instr.conditionRegister, 0))

    }
    println("Maximum at the end: ${registers.map { state.getOrDefault(it, 0) }.max()}")
    println("Maximum ever: $maxValue")
}

private data class Rule(val curValue: Int, val newValue: Int, val direction: Direction, val newState: Char){
    enum class Direction { LEFT(), RIGHT()}

    companion object {
        fun read(): Rule {
            val curValue = if (readLine()!!.endsWith("0:")) 0 else 1
            val newValue = if (readLine()!!.endsWith("0.")) 0 else 1
            val direction = if (readLine()!!.endsWith("left.")) Rule.Direction.LEFT else Rule.Direction.RIGHT
            val newState = readLine()!!.takeLast(2).first()

            return Rule(curValue, newValue, direction, newState)
        }
    }
}


fun main(args: Array<String>) {
    val startingState = readLine()!!.takeLast(2).first()
    val checksumStep = readLine()!!.split(' ').takeLast(2).first().toInt()

    readLine() //skip empty line

    val rules = hashMapOf<Pair<Char, Int>, Rule>()

    while (true) {
        val line = readLine()
        if (line == null || line.length == 0)
            break

        val initialState = line.takeLast(2).first()
        repeat(2) {
            val rule = Rule.read()
            rules[Pair(initialState, rule.curValue)] = rule
        }
        readLine() //skip empty line
    }

    val tape = hashMapOf<Int, Int>()
    var state = startingState
    var position = 0
    repeat(checksumStep) {
        val curValue = tape.getOrDefault(position, 0)

        val rule = rules[Pair(state, curValue)] ?: throw IllegalStateException("No rule for state $state and curValue $curValue")
        tape[position] = rule.newValue
        position += if (rule.direction == Rule.Direction.LEFT) -1 else +1
        state = rule.newState
    }

    println(tape.count { (_, value) -> value == 1 })
}
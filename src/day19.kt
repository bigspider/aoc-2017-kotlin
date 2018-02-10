
private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = Pair(first + other.first, second + other.second)
private operator fun Pair<Int, Int>.plus(other: Direction) = this + Pair(other.dy, other.dx)

fun main(args: Array<String>) {
    val diagram = getInputLines().toList().map { it.toCharArray() }

    var oldPos: Pair<Int, Int> = Pair(0, diagram[0].indexOf('|'))
    var direction = Direction.SOUTH
    var position = oldPos + direction

    val letters = mutableListOf<Char>()

    var steps = 1
    while (diagram[position.first][position.second] != ' ') {
        val cur = diagram[position.first][position.second]
        if (cur in 'A'..'Z')
            letters.add(cur)
        else if (cur == '+') {
            direction = enumValues<Direction>().first {
                val newPos = position + it

                newPos != oldPos && diagram[newPos.first][newPos.second] != ' '
            }
        }

        oldPos = position
        position += direction

        ++steps
    }

    println(letters.joinToString(""))
    println("$steps steps")

}
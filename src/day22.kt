private fun Pair<Int, Int>.turnRight() = Pair(-second, first)
private fun Pair<Int, Int>.turnLeft() = Pair(second, -first)
private fun Pair<Int, Int>.reverse() = Pair(-first, -second)

private const val CLEAN = 0
private const val WEAKENED = 1
private const val INFECTED = 2
private const val FLAGGED = 3

private fun part1(
    initialPosition: Pair<Int, Int>,
    initialDirection: Pair<Int, Int>,
    initialStatus: Map<Pair<Int, Int>, Int>
) {
    var (position, direction) = Pair(initialPosition, initialDirection)

    val status = initialStatus.toMutableMap()
    var nInfections = 0
    repeat(10_000) {
        val initialNodeStatus = status.getOrDefault(position, 0)

        direction = if (initialNodeStatus == INFECTED)
            direction.turnRight()
        else
            direction.turnLeft()

        if (initialNodeStatus == CLEAN)
            ++nInfections

        status[position] = if (initialNodeStatus == CLEAN) INFECTED else CLEAN

        position = Pair(position.first + direction.first, position.second + direction.second)
    }

    println("Answer to part 1: $nInfections")
}

private fun part2(
    initialPosition: Pair<Int, Int>,
    initialDirection: Pair<Int, Int>,
    initialStatus: Map<Pair<Int, Int>, Int>
) {
    var (position, direction) = Pair(initialPosition, initialDirection)

    val status = initialStatus.toMutableMap()
    var nInfections = 0
    repeat(10_000_000) {
        val initialState = status.getOrDefault(position, CLEAN)

        direction = when (initialState) {
            CLEAN -> direction.turnLeft()
            WEAKENED -> direction
            INFECTED -> direction.turnRight()
            FLAGGED -> direction.reverse()
            else -> throw IllegalStateException("Something went wrong")
        }

        if (initialState == WEAKENED)
            ++nInfections

        status[position] = when (initialState) {
            CLEAN -> WEAKENED
            WEAKENED -> INFECTED
            INFECTED -> FLAGGED
            FLAGGED -> CLEAN
            else -> throw IllegalStateException("Something went wrong")
        }

        position = Pair(position.first + direction.first, position.second + direction.second)
    }

    println("Answer to part 2: $nInfections")
}

fun main(args: Array<String>) {
    val input = getInputLines().toList()
            .map { line -> line.map { if (it == '.') CLEAN else INFECTED } }
            .asReversed()

    val (nRows, nCols) = Pair(input.size, input[0].size)

    val initialStatus = mutableMapOf<Pair<Int, Int>, Int>()
    for (row in 0 until input.size)
        for (col in 0 until input[row].size)
            initialStatus[Pair(row, col)] = input[row][col]

    val initialPosition = Pair((nRows - 1)/2, (nCols - 1)/2)
    val initialDirection = Pair(1, 0)

    part1(initialPosition, initialDirection, initialStatus)
    part2(initialPosition, initialDirection, initialStatus)
}
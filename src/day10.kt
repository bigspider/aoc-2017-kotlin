private fun part1(input: String) {
    val lengths = input.split(',').map { it.toInt() }

    val numbers = (0..255).toMutableList()
    var curPos = 0
    var skipSize = 0

    for (l in lengths) {
        numbers.twist(curPos, l)
        curPos += l + skipSize
        skipSize += 1
    }

    println(numbers[0] * numbers[1])
}

private fun part2(input: String) {
    print(input.trim().knotHash())
}

fun main(args: Array<String>) {
    val input = readLine()!!
    part1(input)
    part2(input)
}
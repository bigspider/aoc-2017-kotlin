//set and get  that allow indices bigger than the size (wrapping around)
private fun<T> List<T>.getWithWrap(index: Int) = get(index % size)
private fun<T> MutableList<T>.setWithWrap(index: Int, newValue: T) = set(index % size, newValue)

private fun<T> MutableList<T>.twist(start: Int, length: Int) {
    val sublist = (start until start+length).map{ getWithWrap(it) }.asReversed()
    for (i in 0 until length)
        setWithWrap(start + i, sublist[i])
}

private fun Byte.toPositiveInt() = toInt() and 0xFF

//Byte does not support bitwise operators
private infix fun Byte.xor(other: Byte): Byte = (this.toPositiveInt() xor other.toPositiveInt()).toByte()

//Convert to 2-characters hexadecimal string
private fun Byte.toHexString(): String {
    val p = toPositiveInt()
    val hex = p.toString(16)
    return if (p < 16) "0" + hex else hex
}


fun part1(input: String) {
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


//Public, also used in problem 14
fun String.knotHash(rounds: Int = 64): String {

    val lengths = map { it.toByte() }.toMutableList()
    lengths.addAll(listOf(17, 31, 73, 47, 23))

    val numbers = (0..255).map { it.toByte() }.toMutableList()
    var curPos = 0
    var skipSize = 0
    for (round in 0 until rounds) {
        for (l in lengths) {
            numbers.twist(curPos, l.toInt())
            curPos += l + skipSize
            skipSize += 1
        }
    }

    val denseHash = numbers.chunked(16).map { chunk -> chunk.reduce { acc, el -> acc xor el } }
    return denseHash.map { it.toHexString() }.joinToString("")
}


fun part2(input: String) {
    print(input.trim().knotHash())
}

fun main(args: Array<String>) {
    val input = readLine()!!
    part1(input)
    part2(input)
}
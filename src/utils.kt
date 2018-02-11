import kotlin.coroutines.experimental.buildSequence

fun getInputLines() = buildSequence<String> {
    while(true){
        val line = readLine()
        if (line != null && line != "")
            yield(line)
        else
            break
    }
}


//Implementation of the Knot Hash, used in problems 10 and 14

private fun Byte.toPositiveInt() = toInt() and 0xFF

//Byte does not support bitwise operators
private infix fun Byte.xor(other: Byte): Byte = (this.toPositiveInt() xor other.toPositiveInt()).toByte()

//Convert to 2-characters hexadecimal string
private fun Byte.toHexString(): String {
    val p = toPositiveInt()
    val hex = p.toString(16)
    return if (p < 16) "0" + hex else hex
}

//set and get  that allow indices bigger than the size (wrapping around)
private fun<T> List<T>.getWithWrap(index: Int) = get(index % size)
private fun<T> MutableList<T>.setWithWrap(index: Int, newValue: T) = set(index % size, newValue)

fun<T> MutableList<T>.twist(start: Int, length: Int) {
    val sublist = (start until start + length).map{ getWithWrap(it) }.asReversed()
    for (i in 0 until length)
        setWithWrap(start + i, sublist[i])
}

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

//Disjoint sets data structure (with union-by-rank and path compression)
class UnionFind {
    private val parentMap = hashMapOf<Int, Int>()
    private val rank = hashMapOf<Int, Int>()

    private fun parent(element: Int): Int = parentMap[element]!!

    fun makeSet(element: Int) {
        if (element !in parentMap) {
            parentMap[element] = element
            rank[element] = 0
        }
    }

    //Union by rank
    private fun link(el1: Int, el2: Int) {
        if (rank[el1]!! > rank[el2]!!){
            parentMap[el2] = parent(el1)
        } else {
            parentMap[el1] = parent(el2)
            if (rank[el1]!! == rank[el2]!!)
                rank[el2] = rank[el2]!! + 1
        }
    }

    fun find(element: Int): Int {
        makeSet(element)

        val p = parent(element)
        if (p == parent(p)) {
            return p
        } else {
            parentMap[element] = find(p) //path compression
            return parentMap[element]!!
        }
    }

    fun union(el1: Int, el2: Int) {
        makeSet(el1)
        makeSet(el2)

        link(find(el1), find(el2))
    }
}


private fun countOnes(row: String): Int {
    return row.chunked(8)
            .map { it.toLong(16) }
            .sumBy { java.lang.Long.bitCount(it) }
}

private fun Char.hexToBitList(): List<Int> {
    val num = if (this <= '9') this - '0' else this.toLowerCase() - 'a' + 10
    return generateSequence(num, { it shr 1 })
            .take(4)
            .map { it and 1 }
            .toList()
            .asReversed()
}

private fun String.hexToBitArray(): Array<Int> = flatMap { it.hexToBitList() }.toTypedArray()

private fun pack128(x: Int, y: Int): Int = x * 128 + y

fun main(args: Array<String>) {
    val input = readLine()!!

    //Part 1
    println((0..127).sumBy { countOnes((input + "-" + it).knotHash()) })


    //Part 2
    val disk = (0..127).map { ((input + "-" + it).knotHash()).hexToBitArray() }.toTypedArray()

    //A graph visit would be slightly better, but we have UnionFind from day 12.
    val uf = UnionFind()
    for (i in 0..127) {
        for (j in 0..127) {
            if (disk[i][j] == 1) {
                uf.makeSet(pack128(i, j))
                if (i > 0 && disk[i - 1][j] == 1) {
                    uf.union(pack128(i, j), pack128(i - 1, j))
                }
                if (j > 0 && disk[i][j - 1] == 1) {
                    uf.union(pack128(i, j), pack128(i, j - 1))
                }
            }
        }
    }

    val unique = HashSet<Int>()
    for (i in 0..127)
        for (j in 0..127)
            if (disk[i][j] == 1)
                unique.add(uf.find(pack128(i, j)))

    println(unique.size)
}
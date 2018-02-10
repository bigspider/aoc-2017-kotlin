
fun main(args: Array<String>) {
    val uf = UnionFind()
    val elements = HashSet<Int>()
    for (line in getInputLines()) {
        val (srcStr, rest) = line.split(" <-> ")
        val src = srcStr.toInt()
        val destinations = rest.split(", ").map{ it.toInt() }

        elements.addAll(destinations + src)

        destinations.forEach { dst -> uf.union(src, dst) }
    }

    val firstGroupSize = elements.count { uf.find(0) == uf.find(it) }
    val nGroups = elements.distinctBy { uf.find(it) }.size

    println("$firstGroupSize elements reachable from 0")
    println("$nGroups disjoint groups")
}
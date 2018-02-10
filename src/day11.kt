//Clearly this could be done better.
private fun distance(movesMap: Map<String, Int>): Int {
    var ne = movesMap.getOrDefault("ne", 0)
    var se = movesMap.getOrDefault("se", 0)
    var nw = movesMap.getOrDefault("nw", 0)
    var sw = movesMap.getOrDefault("sw", 0)
    var n = movesMap.getOrDefault("n", 0)
    var s = movesMap.getOrDefault("s", 0)

    var t: Int

    // ne and sw cancel each other
    t = minOf(ne, sw)
    ne -= t
    sw -= t

    // nw and se cancel each other
    t = minOf(nw, se)
    nw -= t
    se -= t

    // n and s cancel each other
    t = minOf(n, s)
    n -= t
    s -= t


    // ne + nw simplifies to n
    t = minOf(ne, nw)
    ne -= t
    nw -= t
    n += t

    // ne + nw simplifies to n
    t = minOf(se, sw)
    se -= t
    sw -= t
    s += t

    // ne + s simplifies to se
    t = minOf(ne, s)
    ne -= t
    s -= t
    se += t

    // nw + s simplifies to sw
    t = minOf(nw, s)
    nw -= t
    s -= t
    sw += t

    // se + n simplifies to ne
    t = minOf(se, n)
    se -= t
    n -= t
    ne += t

    // sw + n simplifies to nw
    t = minOf(sw, n)
    sw -= t
    n -= t
    nw += t


    //no more simplifications possible
    return ne + se + nw + sw + n + s
}

fun main(args: Array<String>) {
    val moves = readLine()!!.split(',')

    val movesMap = HashMap<String, Int>()
    var maxDistance = 0
    for (move in moves) {
        movesMap[move] = movesMap.getOrDefault(move, 0) + 1

        maxDistance = maxOf(maxDistance, distance(movesMap))

        println(distance(movesMap))
    }

    println("Final distance: ${distance(movesMap)}")
    println("Maximum distance: $maxDistance")
}
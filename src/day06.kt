fun main(args: Array<String>) {
    val banks = readLine()!!.split(" ", "\t").map { it.toInt() }.toMutableList()
    val history = hashMapOf(banks.joinToString("-") to 0)

    var count = 0
    val cycleLength: Int
    while (true) {
        val idxMax = banks.indices.maxBy { banks[it] }!!
        val blocks = banks[idxMax]
        banks[idxMax] = 0

        var idxCur = idxMax + 1
        for (i in 1..blocks) {
            banks[idxCur % banks.size] += 1
            ++idxCur
        }

        ++count

        val str = banks.joinToString("-")
        val t = history[str]
        if (t != null) {
            cycleLength = count - t
            break
        } else {
            history[str] = count
        }
    }

    println("Answer to part 1: $count")
    println("Answer to part 2: $cycleLength")
}
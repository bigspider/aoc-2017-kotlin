fun main(args : Array<String>) {
    var res1 = 0
    var res2 = 0
    for (line in getInputLines()) {
        val numbers = line.split(" ", "\t").filter { it != "" }.map { it.toInt() }

        res1 += numbers.max()!! - numbers.min()!!

        val sortedNumbers = numbers.sorted()
        for (i in sortedNumbers.indices)
            res2 += (i + 1 until sortedNumbers.size)
                    .filter { j -> sortedNumbers[j] % sortedNumbers[i] == 0 }
                    .sumBy { j ->  sortedNumbers[j] / sortedNumbers[i] }
    }

    println("Answer to part 1: $res1")
    println("Answer to part 2: $res2")
}
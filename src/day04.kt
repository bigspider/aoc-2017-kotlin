fun main(args: Array<String>) {
    val input = getInputLines().map { it.split(" ", "\t")  }.toList()

    //Part 1
    println(input.count { it.toSet().size == it.size })

    //Part 2
    println(input.count {
        val words = it.map { word -> word.toList().sorted().joinToString() }

        words.toSet().size == words.size
    })

}

private interface Item {
    val content: String

    val length: Int
        get() = content.length
}

private class Garbage(str: String) : Item {
    override val content: String
    val garbageLength: Int

    init {
        assert(str[0] == '<')
        var pos = 1
        var ignoring = false
        var count = 0 //count all characters, except the starting '<', ending '>' and ignored characters
        while (true) {
            if (ignoring){
                ignoring = false
            } else {
                val c = str[pos]
                if (c == '!') {
                    ignoring = true //will ignore the next character
                } else if (c == '>') {
                    break
                } else {
                    ++count
                }
            }
            ++pos
        }
        content = str.substring(0, pos+1)
        garbageLength = count
    }

}

private class Group(str: String) : Item {
    override val content: String
    val items = mutableListOf<Item>()

    init {
        assert(str[0] == '{')
        var pos = 1
        while (str[pos] != '}') {
            //At the beginning of each iteration, pos points to '{' or '<'
            val firstChar = str[pos]
            val rest = str.substring(pos)

            assert(charArrayOf('{', '<').contains(firstChar))

            val newItem: Item = if (firstChar.equals('{')) Group(rest) else Garbage(rest)

            if (newItem.length == 0)
                throw IllegalStateException()

            items.add(newItem)

            pos += newItem.length

            if (str[pos] != ',')
                pos += 1
        }
        assert(str[pos] == '}')
        content = str.substring(0, pos+1)
    }
}


//Calculate the score of the group assuming that its dept is
private fun Group.getScore(depth: Int = 0): Int {
    return 1 + depth + items.sumBy {
        when (it) {
            is Garbage -> 0
            is Group -> it.getScore(depth + 1)
            else -> throw IllegalStateException()
        }
    }
}

//Calculate the score of the group assuming that its dept is
private fun Group.getGarbageLength(): Int {
    return items.sumBy {
        when (it) {
            is Garbage -> it.garbageLength
            is Group -> it.getGarbageLength()
            else -> throw IllegalStateException()
        }
    }
}


fun main(args: Array<String>) {
    val g = Group(readLine()!!)

    println("Score is ${g.getScore(0)}")
    println("Garbage length is ${g.getGarbageLength()}")
}
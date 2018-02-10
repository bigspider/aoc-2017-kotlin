fun main(args: Array<String>) {
    val skipSize = readLine()!!.toInt()

    //Part 1: emulate the whole buffer
    val buffer = mutableListOf(0)
    var pos = 0

    for (i in 1..2017) {
        pos = (pos + skipSize) % buffer.size
        buffer.add(pos+1, i)

        pos++
    }
    println("Answer to part 1: ${ buffer[(pos + 1) % buffer.size] }")

    //Part 2: only keep track of the value after 0
    pos = 0    //current position
    var result = 0 //value currently after 0
    var pos0 = 0   //index of 0

    for (size in 1..50000000) {
        pos = ((pos + skipSize) % size) + 1 //new position after inserting element 'size'

        if (pos == pos0 + 1)
            result = size       //inserted right after '0'
        else if (pos <= pos0)
            pos0++              //inserted somewhere before '0'
    }
    println("Answer to part 2: $result")
}
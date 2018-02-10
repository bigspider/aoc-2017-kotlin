
fun main(args: Array<String>) {
    val inp = readLine()!!.map { it - '0'}
    println(inp.indices.filter { inp[it] == inp[(it + 1) % inp.size] }.sumBy { inp[it] })
    println(inp.indices.filter { inp[it] == inp[(it + inp.size / 2) % inp.size] }.sumBy { inp[it] })
}
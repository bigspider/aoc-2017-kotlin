private fun isCaught(layer: Int, range: Int, time: Int = 0) = (layer + time) % (2 * range - 2) == 0

fun main(args: Array<String>) {
    val layers = getInputLines().map { line -> line.split(": ").map { it.toInt() } }.toList()

    val severity = layers
            .filter{ (layer, range) -> isCaught(layer, range)}
            .sumBy { (layer, range) -> layer * range }

    println("Severity: $severity")

    val delay = generateSequence(0, {it + 1})
            .first { time -> layers.none { (layer, range) -> isCaught(layer, range, time) } }

    println("Minimum safe delay: $delay")
}
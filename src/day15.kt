
private fun generator(seed: Long, multiplier: Long): Sequence<Long> =
    generateSequence( seed, { it * multiplier % 2147483647} )

private fun Pair<Long, Long>.matches() = first and 0xFFFF == second and 0xFFFF

fun main(args: Array<String>) {
    val (a, b) = getInputLines().map { it.split(" ").last().toLong() }.toList()

    val A = generator(a, 16807)
    val B = generator(b, 48271)

    val result1 = A.zip(B)
            .take(40_000_000)
            .count { it.matches() }

    println(result1)

    val result2 = A.filter { it % 4 == 0L }
            .zip(B.filter {it % 8 == 0L })
            .take(5_000_000)
            .count { it.matches() }

    println(result2)
}
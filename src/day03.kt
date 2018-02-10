import kotlin.coroutines.experimental.buildSequence
import kotlin.math.absoluteValue

//Part 1: we don't actually need the spiral!
private fun f(n : Int): Int {
    if (n == 1)
        return 0

    //Find the smallest odd integer m such that m^2 >= n
    val t = Math.ceil(Math.sqrt(n.toDouble())).toInt()
    val m = if (t % 2 == 0) t + 1 else t

    return (m - 1) / 2 + (((n - 2) % (m - 1) - (m - 3)/2)).absoluteValue  //that was fun
}

//Part 2: now we need to build the spiral
private fun spiral() = buildSequence {
    var x = 0;
    var y = 0;

    var dx = 0
    var dy = 1

    yield(Pair(0, 0))

    var steps = 2 //steps before turning
    while (true) {
        x += 1
        for (i in 1..4) {
            for (j in 1..steps) {

                yield(Pair(x, y))

                if (j < steps){
                    x += dx
                    y += dy
                }
            }

            //Turn counterclockwise
            val t = dy
            dy = dx
            dx = -t

            if (i < 4) {
                x += dx
                y += dy
            }
        }
        steps += 2
    }
}

private fun g(n: Int): Int {
    val H = hashMapOf(Pair(0, 0) to 1)

    for (p in spiral()) {
        var sum = 0
        for (x in p.first - 1 .. p.first + 1)
            for (y in p.second - 1 .. p.second + 1)
                sum += H.getOrDefault(Pair(x, y), 0)

        if (sum > n)
            return sum

        H[p] = sum
    }

    throw IllegalStateException("This code should be unreachable")
}

fun main(args: Array<String>) {
    val n = readLine()!!.toInt()
    println("Answer to part 1: ${ f(n) }")
    println("Answer to part 2: ${ g(n) }")
}
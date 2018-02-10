import kotlin.coroutines.experimental.buildSequence

private val Triple<Int, Int, Int>.L1norm
    get() = Math.abs(first) + Math.abs(second) + Math.abs(third)

private data class Particle(val p: Triple<Int, Int, Int>, val v: Triple<Int, Int, Int>, val a: Triple<Int, Int, Int>) {
    companion object {
        fun parseFromString(str: String): Particle {
            val (p, v, a) = str.split(", ")
                    .map {
                        val values = it.drop(3).dropLast(1).split(",").map(String::toInt)

                        Triple(values[0], values[1], values[2])
                    }
            return Particle(p, v, a)
        }
    }

    //Returns the 1-dimensional projection of each component on 1 axis
    fun component(axis: Int): Triple<Int, Int, Int> =
            if (axis !in 0..2)
                throw IllegalArgumentException("Axis must be 0, 1 or 2")
            else
                Triple(p.toList()[axis], v.toList()[axis], a.toList()[axis])
}


//Finds floor(sqrt(this)), that is, the largest integer k such that k^2 <= this
private fun Int.isqrt(): Int =
        if (this < 0)
            throw IllegalArgumentException("Negative numbers do not have a square root")
        else {
            val rt = Math.sqrt(this.toDouble()).toInt()

            if ((rt + 1) * (rt + 1) > this)
                rt
            else
                rt + 1
        }

private fun Int.isPerfectSquare(): Boolean =
        if (this < 0)
            false
        else {
            val rt = isqrt()

            rt * rt == this
        }


//Finds all integers i >= 0 such that the particles would overlap on this axis after i ticks. If the particles would
//always overlap on this axis (identical position, velocity and acceleration), throws IllegalArgumentException.
private fun collisionTimes1D(comp1: Triple<Int, Int, Int>, comp2: Triple<Int, Int, Int>): Set<Int> {
    val (p1, v1, a1) = comp1
    val (p2, v2, a2) = comp2
    val (dp, dv, da) = listOf(p1 - p2, v1 - v2, a1 - a2)

    return if (da == 0){
        if (dv == 0){
            emptySet() //either never overlapping, or always overlapping (identical) particles
        } else {
            if (dp % dv != 0 || -dp/dv < 0)
                emptySet()
            else
                setOf(-dp/dv)
        }
    } else {
        //solve the following second degree equation for integer n: n² * da + n*(2*dv + da) + 2*dp = 0
        val delta = (2*dv + da) * (2*dv + da) - 8 * da * dp
        if (!delta.isPerfectSquare())
            emptySet()
        else {
            val solutions = mutableSetOf<Int>()
            val sqrtDelta = delta.isqrt()
            val den = 2 * da

            listOf(-(2*dv + da) + sqrtDelta, -(2*dv + da) - sqrtDelta).forEach { num ->
                if (num % den == 0 && num / den >= 0)
                    solutions.add(num / den)
            }

            solutions
        }
    }
}

//Returns the smallest integer n >= 0 such that p1 and p2 collide after n steps, or null if they don't.
private fun collisionTime(p1: Particle, p2: Particle): Int? {
    assert(p1 != p2)

    return (0..2)
            .map { axis -> collisionTimes1D(p1.component(axis), p2.component(axis)) }
            .reduce { set1, set2 -> set1.intersect(set2) }.min()
}

//iterate over all (unordered) pairs in a list (size * (size - 1) / 2 pairs in total)
private fun<T> List<T>.pairs() = buildSequence {
    for (i in 0 until size)
        for (j in i + 1 until size)
            yield(Pair(get(i), get(j)))
}

fun main(args: Array<String>) {
    val particles = getInputLines().map { Particle.parseFromString(it) }.toList()
    val n = particles.size

    //Eventually, the closest will be the one with minimum absolute acceleration;
    //break ties by initial absolute speed; break further ties by initial absolute position.
    val closestIdx = particles.indices.minWith(
            compareBy({ particles[it].a.L1norm }, { particles[it].v.L1norm }, { particles[it].p.L1norm })
    )
    println("Eventually, the closest particle will be $closestIdx")


    //List of all (potential) collisions, sorted by collision time
    val collisions = (0 until n).toList().pairs().map { (i, j) ->
                Triple(i, j, collisionTime(particles[i], particles[j]))
            }
            .filter { it.third != null }
            .sortedBy { it.third }

    //For each possible collision time, remove all the indices of particles that actually collide
    val survivors = collisions.groupBy { it.third }.toList().fold((0 until n).toSet()) { survivorsPartial, (_, group) ->
        survivorsPartial.minus(
                group.filter { (i, j, _) -> i in survivorsPartial && j in survivorsPartial}
                .flatMap { listOf(it.first, it.second) }.toSet())
    }

    println("${survivors.size} particles remaining after collisions")
}
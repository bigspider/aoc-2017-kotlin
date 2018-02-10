
private interface Move
private data class Spin(val X: Int) : Move
private data class Exchange(val A: Int, val B: Int) : Move
private data class Partner(val A: Char, val B: Char) : Move

private fun parseMove(moveStr: String) = when (moveStr.first()) {
    's' -> Spin(moveStr.drop(1).toInt())
    'x' -> {
        val (A, B) = moveStr.drop(1).split("/").map { it.toInt() }

        Exchange(A, B)
    }
    'p' -> {
        val (A, B) = moveStr.drop(1).split("/").map { it.single() }

        Partner(A, B)
    }
    else -> throw IllegalStateException("Wrong move received.")
}

private class ProgramsState(var programs: MutableList<Char> = "abcdefghijklmnop".toMutableList()) {
    fun perform(move: Move): Unit = when(move) {
        is Spin -> programs = (programs.takeLast(move.X) + programs.dropLast(move.X)).toMutableList()
        is Exchange -> {
            val t = programs[move.A]
            programs[move.A] = programs[move.B]
            programs[move.B] = t
        }
        is Partner -> perform(Exchange(programs.indexOf(move.A), programs.indexOf(move.B)))
        else -> throw IllegalStateException("Unknown move")
    }

    fun asPermutation() = Permutation(programs.map { it - 'a' })

    override fun toString(): String = programs.joinToString("")
}

//Permutation of integers 0, 1, ... size - 1
private class Permutation(val numbers: List<Int>) {
    val size = numbers.size

    companion object {
        fun identity(size: Int): Permutation = Permutation((0 until size).toList())
    }

    operator fun<T> invoke(list: List<T>): List<T> =
        if (list.size != size)
            throw IllegalArgumentException("Size mismatch")
        else
            numbers.map { list[it] }

    operator fun invoke(string: String): String = this(string.toList()).joinToString("")

    override fun toString() = numbers.joinToString(" ", "(", ")")

    //Returns the permutation that first applies other, and than this.
    operator fun times(other: Permutation) = Permutation(other.numbers.map { numbers[it] })

    //Exponentiation by squaring
    fun pow(n: Int): Permutation {
        var result = identity(size)
        var multiplier = this
        var t = n
        while (true) {
            if ((t and 1) != 0)
                result *= multiplier
            t = t shr 1
            if (t == 0)
                break

            multiplier *= multiplier
        }
        return result
    }
}

fun main(args: Array<String>) {
    val state = ProgramsState()

    val stateIgnoringPartners = ProgramsState()
    val stateOnlyPartners = ProgramsState()

    for (move in readLine()!!.split(",").map { parseMove(it) }) {
        state.perform(move)
        if (move !is Partner)
            stateIgnoringPartners.perform(move)
        else
            stateOnlyPartners.perform(move)
    }

    println("State after 1 dance: $state")

    /*
     If we don't have Partner moves, the rest can be represented with a permutation of the indices.
     Similarly, Partner moves generate a permutation of the _names_, and can be treated separately.
     Thus, we compute the two permutations separately.
    */

    val permNames = stateOnlyPartners.asPermutation()
    val permIndices = stateIgnoringPartners.asPermutation()
    val n = 1_000_000_000

    //Starting from the string "abcdefghijklmnop", we first apply the names permutation, then we permute the indices.
    val finalPerm = permNames.pow(n) * permIndices.pow(n)
    val finalState = finalPerm("abcdefghijklmnop")

    println("State after $n dances: $finalState")
}
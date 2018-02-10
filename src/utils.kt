import kotlin.coroutines.experimental.buildSequence

fun getInputLines() = buildSequence<String> {
    while(true){
        val line = readLine()
        if (line != null && line != "")
            yield(line)
        else
            break
    }
}

//Disjoint sets data structure (with union-by-rank and path compression)
class UnionFind {
    private val parentMap = hashMapOf<Int, Int>()
    private val rank = hashMapOf<Int, Int>()

    private fun parent(element: Int): Int = parentMap[element]!!

    fun makeSet(element: Int) {
        if (element !in parentMap) {
            parentMap[element] = element
            rank[element] = 0
        }
    }

    //Union by rank
    private fun link(el1: Int, el2: Int) {
        if (rank[el1]!! > rank[el2]!!){
            parentMap[el2] = parent(el1)
        } else {
            parentMap[el1] = parent(el2)
            if (rank[el1]!! == rank[el2]!!)
                rank[el2] = rank[el2]!! + 1
        }
    }

    fun find(element: Int): Int {
        makeSet(element)

        val p = parent(element)
        if (p == parent(p)) {
            return p
        } else {
            parentMap[element] = find(p) //path compression
            return parentMap[element]!!
        }
    }

    fun union(el1: Int, el2: Int) {
        makeSet(el1)
        makeSet(el2)

        link(find(el1), find(el2))
    }
}


enum class Direction(val dy: Int, val dx: Int) {
    NORTH(-1, 0),
    SOUTH(+1, 0),
    WEST(0, -1),
    EAST(0, +1);

    fun turnRight(): Direction = when(this) {
        Direction.NORTH -> Direction.EAST
        Direction.EAST -> Direction.SOUTH
        Direction.SOUTH -> Direction.WEST
        Direction.WEST -> Direction.NORTH
    }

    fun turnLeft(): Direction = when(this) {
        Direction.NORTH -> Direction.WEST
        Direction.EAST -> Direction.NORTH
        Direction.SOUTH -> Direction.EAST
        Direction.WEST -> Direction.SOUTH
    }
}

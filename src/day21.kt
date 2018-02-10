

private class Grid<T>(val size: Int, val values: List<List<T>>) {

    constructor(size: Int, initializer: (Int, Int) -> T) : this(size,
            (0 until size).map { row ->
                (0 until size).map { col ->
                    initializer(row, col)
                }.toList()
            }.toList()
    )

    operator fun get(row: Int) = values[row]
    operator fun get(row: Int, col: Int) = values[row][col]

    fun subgrid(rows: IntRange, cols: IntRange): Grid<T> = when {
        (rows.last - rows.first != cols.last - cols.first) -> throw IllegalArgumentException("The subgrid must be a square grid")
        else -> Grid(rows.last - rows.first, { r, c -> values[rows.first + r][cols.first + c] })
    }

    fun rotateClockwise() = Grid(size, { row, col -> get(size - col - 1, row) })

    fun flipX() = Grid(size, { row, col -> get(row, size - col - 1) })

    fun<R> map (transform: (T) -> R): Grid<R> = Grid(size, {r, c -> transform(values[r][c]) })

    override fun toString() = values.map { row -> row.joinToString(" ") }.joinToString( "\n", "[", "]")
}

private fun Grid<Int>.concatenate() = values.map { row -> row.joinToString("") }.joinToString("")

private fun Grid<Int>.getSignature(): String {
    val candidates = mutableListOf<String>()
    var t = this

    repeat(2) {
        repeat(4) {
            candidates.add(t.concatenate())
            t = t.rotateClockwise()
        }
        t = t.flipX()
    }
    return candidates.max()!!
}

private fun<T> Grid<T>.partition(innerSize: Int): Grid<Grid<T>> = when {
    innerSize <= 0 || size % innerSize != 0 -> throw IllegalArgumentException("Invalid innerSize")
    else -> Grid(size / innerSize, {r, c ->
        Grid(innerSize, { i, j -> values[r * innerSize + i][c * innerSize + j]})
    })
}

private fun<T> Grid<Grid<T>>.flatten(): Grid<T> {
    val innerSize = values[0][0].size

    return Grid(size * innerSize, {row, col ->
        (values[row / innerSize][ col / innerSize])[row % innerSize][col % innerSize]
    })
}

private fun parseGrid(str: String): Grid<Int> {
    val rows = str.split("/")
    val size = rows[0].length
    return Grid(size, { row, col -> if (rows[row][col] ==  '.') 0 else 1})
}

fun main(args: Array<String>) {
    val rules = hashMapOf(2 to mutableMapOf<String, Grid<Int>>(), 3 to mutableMapOf<String, Grid<Int>>())

    for (line in getInputLines()){
        val (srcGrid, dstGrid) = line.split(" => ").map { parseGrid (it) }
        rules[srcGrid.size]!![srcGrid.getSignature()] = dstGrid
    }

    fun transform(grid: Grid<Int>, rules: Map<Int, Map<String, Grid<Int>>>): Grid<Int> {
        val size = if (grid.size % 2 == 0) 2 else 3
        return grid.partition(size).map { rules[size]!![it.getSignature()]!! }.flatten()
    }

    var cur = parseGrid(".#./..#/###")

    repeat(5) { cur = transform(cur, rules) }
    println(cur.values.flatten().sum())

    repeat(18-5) { cur = transform(cur, rules) }
    println(cur.values.flatten().sum())
}
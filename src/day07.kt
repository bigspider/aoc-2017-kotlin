
private fun part1(input: List<String>) {
    val leftProgs = HashSet<String>() //programs on the left side of ->
    val rightProgs = HashSet<String>() //programs on the right side of ->

    for (line in input) {
        val holdingProg = line.substring(0, line.indexOf(" "))
        val arrowPos = line.indexOf("->")
        leftProgs.add(holdingProg)
        if (arrowPos != -1) {
            val heldProgs = line.substring(arrowPos + 3).split(", ")
            rightProgs.addAll(heldProgs)
        }
    }

    //print elements in L not in R (only one for correct inputs)
    val bottomProg = leftProgs.first { !rightProgs.contains(it) }
    println("The bottom program is '$bottomProg'")
}


private data class Prog(val name: String, val weight: Int, val heldProgNames: MutableList<String> = mutableListOf())


private fun Prog.rebalance(progs: Map<String, Prog>): Int {
    val childrenWeightsMap = mutableMapOf<String, Int>()
    for (childName in heldProgNames) {
        val childWeight = progs.get(childName)!!.rebalance(progs)
        childrenWeightsMap.put(childName, childWeight)
    }

    var delta = 0

    if (heldProgNames.size > 0) {
        val min = childrenWeightsMap.values.min()!!
        val max = childrenWeightsMap.values.max()!!

        if (min != max) {

            //either the min or the max (or both) appears only once; the other is the target
            val uniqueTotalWeight = if (childrenWeightsMap.values.count { it == min } == 1) min else max
            val targetTotalWeight = max + min - uniqueTotalWeight

            delta = targetTotalWeight - uniqueTotalWeight

            val wrongKey = childrenWeightsMap.filter { it.value == uniqueTotalWeight }.keys.first()
            val wrongProg = progs.get(wrongKey)!!
            println("Change the weight of '${wrongProg.name}' from ${wrongProg.weight} to ${wrongProg.weight + delta}")
        }
    }

    return weight + childrenWeightsMap.values.sum() + delta
}

private fun part2(input: List<String>) {
    val progs = HashMap<String, Prog>()

    val rightProgs = HashSet<String>() //names of programs on the right side of ->

    for (line in input) {
        val name = line.substring(0, line.indexOf(" "))
        val weight = line.substring(1+line.indexOf("("), line.indexOf(")")).toInt()

        val prog = Prog(name, weight)
        progs[name] = prog

        val arrowPos = line.indexOf("->")
        if (arrowPos != -1) {
            val held = line.substring(arrowPos + 3).split(", ").toList()
            prog.heldProgNames.addAll(held)
            rightProgs.addAll(held)
        }
    }

    //The root is the only program that is never to the right of ->
    val rootName = progs.keys.first { !rightProgs.contains(it) }
    val root: Prog = progs.get(rootName)!!
    root.rebalance(progs)
}

fun main(args: Array<String>) {
    val input = getInputLines().toList()

    part1(input)
    part2(input)
}
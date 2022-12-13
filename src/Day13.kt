fun main() {
    fun parseArray(arrayText: String): List<Any> {
        fun nodeToList(node: Node): List<Any> {
            return node.children.map { child ->
                if (child is Leaf) {
                    child.value ?: emptyList<Any>()
                } else {
                    nodeToList(child)
                }
            }
        }

        fun addLeaf(builder: StringBuilder, node: Node?) {
            if (builder.isNotEmpty()) {
                node?.addChild(Leaf(node, builder.toString().toInt()))
                builder.clear()
            }
        }

        var node: Node? = null
        val numberBuilder = StringBuilder()
        arrayText.forEach {
            when {
                it == '[' -> {
                    val newNode = Node(node)
                    node?.addChild(newNode)
                    node = newNode
                }

                it.isDigit() -> {
                    numberBuilder.append(it)
                }

                it == ',' -> {
                    addLeaf(numberBuilder, node)
                }

                it == ']' -> {
                    addLeaf(numberBuilder, node)
                    node = node?.parent ?: node
                }
            }
        }
        return node?.let { nodeToList(it) } ?: emptyList()
    }

    fun compare(first: Any?, second: Any?): Int {
        if (first is Int && second is Int) {
            return first.compareTo(second)
        } else if (first is Int && second is List<*>) {
            return compare(listOf(first), second)
        } else if (first is List<*> && second is Int) {
            return compare(first, listOf(second))
        } else if (first is List<*> && second is List<*>) {
            val pairs = first.zip(second)
            for (pair in pairs) {
                val result = compare(pair.first, pair.second)
                if (result != 0) {
                    return result
                }
            }
            return first.size.compareTo(second.size)
        } else {
            return 0
        }
    }

    fun part1(inputName: String) =
        readGroupedInput(inputName)
            .map { group ->
                group.split("\n").map { parseArray(it) }.pair()
            }.map {
                compare(it.first, it.second)
            }.mapIndexed { index, it ->
                if (it < 0) index + 1 else 0
            }.sum()

    fun part2(inputName: String): Int {
        val dividerPackets = listOf(listOf(listOf(2)), listOf(listOf(6)))
        val packets = readInput(inputName)
            .filter { it.isNotBlank() }
            .map { parseArray(it) }
            .toMutableList()
            .also { it.addAll(dividerPackets) }

        val sortedPackets = packets.sortedWith { o1, o2 -> compare(o1, o2) }
        return dividerPackets.map { sortedPackets.indexOf(it) + 1 }.factorial()
    }

    val testInputName = "Day13_test"
    check(part1(testInputName) == 13)
    check(part2(testInputName) == 140)

    val inputName = "Day13"
    println(part1(inputName))
    println(part2(inputName))

}

private open class Node(parent: Node?) {
    var parent: Node? = parent
        private set

    private val _children: MutableList<Node> = mutableListOf()
    val children: List<Node>
        get() = _children.toList()

    fun addChild(child: Node) {
        child.parent = this
        _children.add(child)
    }
}

private class Leaf(parent: Node?, val value: Int?) : Node(parent)
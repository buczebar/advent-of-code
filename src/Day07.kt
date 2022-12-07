private const val TOTAL_DISK_SPACE = 70000000L
private const val SPACE_NEEDED_FOR_UPDATE = 30000000L
fun main() {
    fun part1(fileTree: TreeNode): Long {
        return fileTree.getAllSubdirectories().filter { it.totalSize <= 100_000 }.sumOf { it.totalSize }
    }

    fun part2(fileTree: TreeNode): Long {
        val spaceAvailable = TOTAL_DISK_SPACE - fileTree.totalSize
        val spaceToBeFreed = SPACE_NEEDED_FOR_UPDATE - spaceAvailable
        return fileTree.getAllSubdirectories().sortedBy { it.totalSize }
            .firstOrNull { it.totalSize > spaceToBeFreed }?.totalSize ?: 0L
    }

    val testInput = readInput("Day07_test")
    val testInputFileTree = buildFileTree(testInput)
    check(part1(testInputFileTree) == 95_437L)
    check(part2(testInputFileTree) == 24_933_642L)

    val input = readInput("Day07")
    val inputFileTree = buildFileTree(input)
    println(part1(inputFileTree))
    println(part2(inputFileTree))
}

private fun buildFileTree(consoleLog: List<String>): TreeNode {
    val rootNode = DirNode("/", null)
    var currentNode = rootNode as TreeNode
    consoleLog.forEach { line ->
        when {
            line.isCdCommand() -> {
                val (_, _, name) = line.splitWords()
                currentNode = when (name) {
                    ".." -> currentNode.parent ?: return@forEach
                    "/" -> rootNode
                    else -> currentNode.getChildByName(name)
                }
            }
            line.isDir() -> {
                val (_, name) = line.splitWords()
                currentNode.addChild(DirNode(name))
            }
            line.isFile() -> {
                val (size, name) = line.splitWords()
                currentNode.addChild(FileNode(name, size.toLong()))
            }
        }
    }
    return rootNode
}

private fun String.isDir() = startsWith("dir")
private fun String.isFile() = "\\d+ [\\w.]+".toRegex().matches(this)
private fun String.isCdCommand() = startsWith("$ cd")

private abstract class TreeNode(private val name: String, var parent: TreeNode?, val children: MutableList<TreeNode>?) {
    open val totalSize: Long
        get() {
            return children?.sumOf { it.totalSize } ?: 0
        }

    fun addChild(node: TreeNode) {
        node.parent = this
        children?.add(node)
    }

    fun getChildByName(name: String): TreeNode {
        return children?.firstOrNull { it.name == name } ?: this
    }

    fun getAllSubdirectories(): List<DirNode> {
        return getAllSubNodes().filterIsInstance<DirNode>()
    }

    private fun getAllSubNodes(): List<TreeNode> {
        val result = mutableListOf<TreeNode>()
        if (children != null) {
            for (child in children) {
                result.addAll(child.getAllSubNodes())
                result.add(child)
            }
        }
        return result
    }
}

private class DirNode(name: String, parent: TreeNode? = null, children: MutableList<TreeNode> = mutableListOf()) :
    TreeNode(name, parent, children)

private class FileNode(name: String, override val totalSize: Long, parent: DirNode? = null) :
    TreeNode(name, parent, null)


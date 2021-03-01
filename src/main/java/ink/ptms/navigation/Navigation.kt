package ink.ptms.navigation

import ink.ptms.navigation.pathfinder.NodeEntity
import ink.ptms.navigation.pathfinder.NodeReader
import ink.ptms.navigation.pathfinder.PathFinder
import io.izzel.taboolib.loader.Plugin

object Navigation {

    fun create(nodeEntity: NodeEntity): PathFinder {
        return PathFinder(NodeReader(nodeEntity))
    }
}

package ink.ptms.navigation

import ink.ptms.navigation.pathfinder.NodeEntity
import ink.ptms.navigation.pathfinder.NodeReader
import ink.ptms.navigation.pathfinder.PathFinder
import ink.ptms.navigation.pathfinder.RandomPositionGenerator

object Navigation {

    fun create(nodeEntity: NodeEntity): PathFinder {
        return PathFinder(NodeReader(nodeEntity))
    }

    fun randomPositionGenerator() = RandomPositionGenerator
}

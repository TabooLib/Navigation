package ink.ptms.navigation

import ink.ptms.navigation.pathfinder.NodeEntity
import ink.ptms.navigation.pathfinder.NodeReader
import ink.ptms.navigation.pathfinder.PathFinder
import io.izzel.taboolib.Version
import io.izzel.taboolib.module.dependency.TDependency

object Navigation {

    init {
        if (Version.isBefore(Version.v1_16)) {
            TDependency.requestLib("it.unimi.dsi:fastutil:8.5.2", TDependency.MAVEN_REPO, "")
        }
    }

    fun create(nodeEntity: NodeEntity): PathFinder {
        return PathFinder(NodeReader(nodeEntity))
    }
}

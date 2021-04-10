package ink.ptms.navigation

import ink.ptms.navigation.pathfinder.NodeEntity
import ink.ptms.navigation.pathfinder.NodeReader
import ink.ptms.navigation.pathfinder.PathFinder
import ink.ptms.navigation.pathfinder.RandomPositionGenerator
import io.izzel.taboolib.Version
import io.izzel.taboolib.module.dependency.TDependency
import io.izzel.taboolib.module.inject.TFunction

object Navigation {

    @TFunction.Load
    private fun load() {
        if (Version.isBefore(Version.v1_16)) {
            TDependency.requestLib("it.unimi.dsi:fastutil:8.5.2", TDependency.MAVEN_REPO, "http://ptms.ink:8081/repository/maven-releases/public/fastutil/8.5.2/fastutil-8.5.2.jar")
        }
    }

    fun create(nodeEntity: NodeEntity): PathFinder {
        return PathFinder(NodeReader(nodeEntity))
    }

    fun randomPositionGenerator() = RandomPositionGenerator
}

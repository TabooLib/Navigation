package ink.ptms.navigation

import ink.ptms.navigation.pathfinder.NodeEntity
import io.izzel.taboolib.kotlin.Tasks
import io.izzel.taboolib.module.command.base.BaseCommand
import io.izzel.taboolib.module.command.base.BaseMainCommand
import io.izzel.taboolib.module.command.base.CommandType
import io.izzel.taboolib.module.command.base.SubCommand
import io.izzel.taboolib.module.inject.PlayerContainer
import io.izzel.taboolib.util.Coerce
import io.izzel.taboolib.util.Pair
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

/**
 * Navigation
 * ink.ptms.navigation.NavigationTool
 *
 * @author sky
 * @since 2021/2/23 3:38 下午
 */
@BaseCommand(name = "navigation", permission = "*")
class NavigationCommand : BaseMainCommand() {

    @SubCommand(description = "设置起点", type = CommandType.PLAYER)
    fun pos1(player: Player, args: Array<String>) {
        pos.computeIfAbsent(player.name) { Pair(null, null) }.key = player.location
        player.sendMessage("§c[Navigation] §7起点已设置.")
    }

    @SubCommand(description = "设置终点", type = CommandType.PLAYER)
    fun pos2(player: Player, args: Array<String>) {
        pos.computeIfAbsent(player.name) { Pair(null, null) }.value = player.location
        player.sendMessage("§c[Navigation] §7终点已设置.")
    }

    @SubCommand(description = "寻路", arguments = ["高度?", "宽度?", "最大距离?"], type = CommandType.PLAYER)
    fun find(player: Player, args: Array<String>) {
        val pair = pos[player.name]
        if (pair == null || pair.key == null || pair.value == null) {
            player.sendMessage("§c[Navigation] §7未设置坐标点.")
        }
        Tasks.task {
            val pathFinder = Navigation.create(NodeEntity(pair!!.key!!, Coerce.toDouble(args.getOrNull(0) ?: 2), Coerce.toDouble(args.getOrNull(1) ?: 1)))
            val time = System.currentTimeMillis()
            val findPath = pathFinder.findPath(pair.value!!, Coerce.toFloat(args.getOrNull(2) ?: 16))
            player.sendMessage("§c[Navigation] §7寻路结果: ${findPath}, 耗时 ${System.currentTimeMillis() - time}ms")
            findPath?.nodes?.forEach {
                it.display(player)
            }
        }
    }

    companion object {

        @PlayerContainer
        val pos = ConcurrentHashMap<String, Pair<Location?, Location?>>()
    }
}
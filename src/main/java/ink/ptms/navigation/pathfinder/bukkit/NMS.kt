package ink.ptms.navigation.pathfinder.bukkit

import io.izzel.taboolib.Version
import io.izzel.taboolib.module.inject.TInject
import org.bukkit.block.Block

/**
 * Navigation
 * ink.ptms.navigation.bukkit.NMS
 *
 * @author sky
 * @since 2021/2/21 11:57 下午
 */
abstract class NMS {

    abstract fun getBlockHeight(block: Block): Double

    companion object {

        @TInject(asm = "ink.ptms.navigation.pathfinder.bukkit.NMSImpl")
        lateinit var INSTANCE: NMS
        internal val version = Version.getCurrentVersionInt()
    }
}
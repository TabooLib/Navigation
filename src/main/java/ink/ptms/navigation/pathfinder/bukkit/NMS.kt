package ink.ptms.navigation.pathfinder.bukkit

import io.izzel.taboolib.module.inject.TInject
import org.bukkit.block.Block
import org.bukkit.entity.Entity

/**
 * Navigation
 * ink.ptms.navigation.bukkit.NMS
 *
 * @author sky
 * @since 2021/2/21 11:57 下午
 */
abstract class NMS {

    abstract fun getBoundingBox(entity: Entity): BoundingBox?

    abstract fun getBoundingBox(block: Block): BoundingBox?

    abstract fun getBlockHeight(block: Block): Double

    abstract fun isDoorOpened(block: Block): Boolean

    companion object {

        @TInject(asm = "ink.ptms.navigation.pathfinder.bukkit.NMSImpl")
        lateinit var INSTANCE: NMS
    }
}
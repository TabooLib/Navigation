package ink.ptms.navigation.pathfinder.bukkit

import net.minecraft.server.v1_11_R1.BlockTorch
import net.minecraft.server.v1_11_R1.IBlockAccess

/**
 * Navigation
 * ink.ptms.navigation.bukkit.NMSIMpl
 *
 * @author sky
 * @since 2021/2/21 11:57 下午
 */
class NMSImpl : NMS() {

    override fun getBlockHeight(block: org.bukkit.block.Block): Double {
        return if (version >= 11300) {
            if (block.type.isSolid) {
                (block.boundingBox.maxY - block.y).coerceAtLeast(0.0)
            } else {
                0.0
            }
        } else {
            when (version) {
                11200 -> {
                    val p = net.minecraft.server.v1_12_R1.BlockPosition(block.x, block.y, block.z)
                    val b = (block.world as org.bukkit.craftbukkit.v1_12_R1.CraftWorld).handle.getType(p)
                    if (block.type.isSolid) {
                        b.d((block.world as org.bukkit.craftbukkit.v1_12_R1.CraftWorld).handle, p)?.e ?: 0.0
                    } else {
                        0.0
                    }
                }
                11100 -> {
                    val p = net.minecraft.server.v1_11_R1.BlockPosition(block.x, block.y, block.z)
                    val b = (block.world as org.bukkit.craftbukkit.v1_11_R1.CraftWorld).handle.getType(p)
                    (b.block as BlockTorch).a(b, (block.world as org.bukkit.craftbukkit.v1_11_R1.CraftWorld).handle as IBlockAccess, p)
                    if (block.type.isSolid) {
                        b.c((block.world as org.bukkit.craftbukkit.v1_11_R1.CraftWorld).handle, p)?.e ?: 0.0
                    } else {
                        0.0
                    }
                }
                else -> {
                    if (block.isEmpty) {
                        0.0
                    } else {
                        val p = net.minecraft.server.v1_9_R2.BlockPosition(block.x, block.y, block.z)
                        val b = (block.world as org.bukkit.craftbukkit.v1_9_R2.CraftWorld).handle.getType(p)
                        if (block.type.isSolid) {
                            b.c((block.world as org.bukkit.craftbukkit.v1_9_R2.CraftWorld).handle, p)?.e ?: 0.0
                        } else {
                            0.0
                        }
                    }
                }
            }
        }
    }
}
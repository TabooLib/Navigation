package ink.ptms.navigation.util

import ink.ptms.navigation.pathfinder.bukkit.NMS
import io.izzel.taboolib.Version
import io.izzel.taboolib.module.nms.impl.Position
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.material.Openable
import org.bukkit.util.NumberConversions
import org.bukkit.util.Vector

fun World.getBlockAt(position: Position) = getBlockAt(position.x, position.y, position.z)

fun World.getBlockAtIfLoaded(position: Position): Block? {
    return if (position.toLocation(this).chunk.isLoaded) {
        getBlockAt(position.x, position.y, position.z)
    } else {
        null
    }
}

fun Position.toBlock(world: World) = toLocation(world).block

fun Position.down() = Position(x, y - 1, z)

fun Position.up() = Position(x, y + 1, z)

fun Position.hash() = x.toLong() and 67108863L shl 38 or (y.toLong() and 4095L) or (z.toLong() and 67108863L shl 12)

fun Position.set(x: Int, y: Int, z: Int): Position {
    setX(x)
    setY(y)
    setZ(z)
    return this
}

fun Position.distSqr(double1: Double, double2: Double, double3: Double, boolean4: Boolean): Double {
    val double9 = if (boolean4) 0.5 else 0.0
    val double11 = this.x.toDouble() + double9 - double1
    val double13 = this.y.toDouble() + double9 - double2
    val double15 = this.z.toDouble() + double9 - double3
    return double11 * double11 + double13 * double13 + double15 * double15
}

fun Position.set(x: Double, y: Double, z: Double): Position {
    setX(NumberConversions.floor(x))
    setY(NumberConversions.floor(y))
    setZ(NumberConversions.floor(z))
    return this
}

fun Position.distSqr(position: Position): Double {
    return this.distSqr(position.x.toDouble(), position.y.toDouble(), position.z.toDouble(), true)
}

fun Position.bottomCenter(): Vector {
    return Vector(this.x + 0.5, this.y.toDouble(), this.z + 0.5)
}

fun Position.distSqr(position: Position, boolean2: Boolean): Double {
    return this.distSqr(position.x.toDouble(), position.y.toDouble(), position.z.toDouble(), boolean2)
}

fun Position.closerThan(position: Position, double2: Double): Boolean {
    return this.distSqr(position.x.toDouble(), position.y.toDouble(), position.z.toDouble(), true) < double2 * double2
}

fun Location.toPosition(): Position {
    return Position(this.blockX, this.blockY, this.blockZ)
}

fun Block.isDoor(): Boolean {
    return type.name.run { endsWith("DOOR") || endsWith("DOOR_BLOCK") }
}

fun Block.isIronDoor(): Boolean {
    return type.name.run { endsWith("IRON_DOOR") || endsWith("IRON_DOOR_BLOCK") }
}

fun Block.isClimbable(): Boolean {
    return type.name.run { endsWith("VINE") || endsWith("VINES") || endsWith("LADDER") }
}

fun Block.isOpened(): Boolean {
    return if (Version.isAfter(Version.v1_13)) {
        (blockData as org.bukkit.block.data.Openable).isOpen
    } else {
        NMS.INSTANCE.isDoorOpened(this)
    }
}

fun Material.isAirLegacy(): Boolean {
    return when {
        Version.isAfter(Version.v1_15) -> isAir
        Version.isAfter(Version.v1_13) -> {
            when (this) {
                Material.AIR, Material.CAVE_AIR, Material.VOID_AIR, Material.LEGACY_AIR -> true
                else -> false
            }
        }
        else -> this == Material.AIR
    }
}

fun Material.isWater(): Boolean {
    return name.contains("WATER")
}
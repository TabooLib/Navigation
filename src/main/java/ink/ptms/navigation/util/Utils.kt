package ink.ptms.navigation.util

import io.izzel.taboolib.Version
import io.izzel.taboolib.module.nms.impl.Position
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.material.Openable
import org.bukkit.util.NumberConversions
import org.bukkit.util.Vector
import kotlin.math.sqrt

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

fun Position.set(x: Double, y: Double, z: Double): Position {
    setX(NumberConversions.floor(x))
    setY(NumberConversions.floor(y))
    setZ(NumberConversions.floor(z))
    return this
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
        (state as Openable).isOpen
    }
}
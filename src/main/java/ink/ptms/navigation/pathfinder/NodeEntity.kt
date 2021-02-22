package ink.ptms.navigation.pathfinder

import com.google.common.collect.Maps
import ink.ptms.navigation.pathfinder.bukkit.BoundingBox
import org.bukkit.Location
import org.bukkit.block.BlockFace
import java.util.*

/**
 * Navigation
 * ink.ptms.navigation.pathfinder.Original
 *
 * @author sky
 * @since 2021/2/21 6:35 下午
 */
open class NodeEntity(
    val location: Location,
    val height: Double,
    val width: Double = 1.0,
    val depth: Double = width,
    val canPassDoors: Boolean = true,
    val canOpenDoors: Boolean = false,
    val canFloat: Boolean = true,
) {

    val x: Double
        get() = location.x

    val y: Double
        get() = location.y

    val z: Double
        get() = location.z

    val boundingBox = BoundingBox(
        location.x - width / 2,
        location.y,
        location.z - width / 2,
        location.x + width / 2,
        location.y + height,
        location.z + width / 2
    )

    /**
     * 不知道是什么东西
     */
    var G: Float = 0.6f
    val pathfindingMalus: EnumMap<PathType, Float> = Maps.newEnumMap(PathType::class.java)

    fun getPathfindingMalus(pathType: PathType): Float {
        return pathfindingMalus[pathType] ?: pathType.malus
    }

    fun setPathfindingMalus(pathType: PathType, malus: Float) {
        pathfindingMalus[pathType] = malus
    }

    open fun canCutCorner(pathType: PathType): Boolean {
        return false
    }

    open fun canStandOnFluid(fluid: Fluid): Boolean {
        return false
    }

    open fun isInWater(): Boolean {
        return location.block.isLiquid
    }

    open fun isOnGround(): Boolean {
        return location.block.getRelative(BlockFace.DOWN).type.isSolid
    }

    open fun getAirSupply(): Int {
        return 3
    }
}
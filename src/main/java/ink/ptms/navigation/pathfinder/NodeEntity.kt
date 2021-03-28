package ink.ptms.navigation.pathfinder

import com.google.common.collect.Maps
import ink.ptms.navigation.pathfinder.bukkit.BoundingBox
import ink.ptms.navigation.util.distSqr
import ink.ptms.navigation.util.toPosition
import io.izzel.taboolib.module.nms.impl.Position
import org.bukkit.Location
import org.bukkit.World
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
    val random: Random = Random(),
    var restrictCenter: Position = Position(0, 0, 0),
    var restrictRadius: Float = -1f,
) {
    val hasRestriction: Boolean
        get() = restrictRadius != -1.0f

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

    open fun getPathfindingMalus(pathType: PathType): Float {
        return pathfindingMalus[pathType] ?: pathType.malus
    }

    fun setPathfindingMalus(pathType: PathType, malus: Float) {
        pathfindingMalus[pathType] = malus
    }

    open fun isWithinRestriction(): Boolean {
        return this.isWithinRestriction(this.location.toPosition())
    }

    open fun isWithinRestriction(pos: Position): Boolean {
        return if (restrictRadius == -1.0f) true else restrictCenter.distSqr(pos) < (restrictRadius * restrictRadius).toDouble()
    }

    fun getWalkTargetValue(pos: Position): Double {
        return this.getWalkTargetValue(pos, location.world)
    }

    /**
     * 重写后不允许返回 -1 值
     * 否则会影响随机游荡算法价值计算
     */
    open fun getWalkTargetValue(pos: Position, world: World): Double {
        return -1.0
    }

    open fun restrictTo(pos: Position, radius: Int) {
        restrictCenter = pos
        restrictRadius = radius.toFloat()
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
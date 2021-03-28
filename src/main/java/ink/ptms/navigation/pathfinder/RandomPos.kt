package ink.ptms.navigation.pathfinder

import com.sun.istack.internal.Nullable
import ink.ptms.navigation.util.bottomCenter
import ink.ptms.navigation.util.closerThan
import ink.ptms.navigation.util.toPosition
import ink.ptms.navigation.util.up
import io.izzel.taboolib.module.nms.impl.Position
import org.bukkit.Material
import org.bukkit.util.Vector
import java.util.*
import java.util.function.Predicate
import java.util.function.ToDoubleFunction
import kotlin.math.*

object RandomPos {
    private const val SQRT_OF_TWO = 1.4142135623730951

    @Nullable
    fun getPos(nodeEntity: NodeEntity, integer2: Int, integer3: Int): Vector? {
        return generateRandomPos(
            nodeEntity,
            integer2,
            integer3,
            0,
            null,
            true,
            1.5707963705062866,
            nodeEntity::getWalkTargetValue,
            false,
            0,
            0,
            true
        )
    }

    @Nullable
    fun getAirPos(
        nodeEntity: NodeEntity,
        integer2: Int,
        integer3: Int,
        integer4: Int,
        vector: Vector,
        double6: Double
    ): Vector? {
        return generateRandomPos(
            nodeEntity,
            integer2,
            integer3,
            integer4,
            vector,
            true,
            double6,
            nodeEntity::getWalkTargetValue,
            true,
            0,
            0,
            false
        )
    }

    @Nullable
    fun getLandPos(nodeEntity: NodeEntity, integer2: Int, integer3: Int): Vector? {
        return getLandPos(nodeEntity, integer2, integer3, nodeEntity::getWalkTargetValue)
    }

    @Nullable
    fun getLandPos(
        nodeEntity: NodeEntity,
        integer2: Int,
        integer3: Int,
        toDoubleFunction: ToDoubleFunction<Position>
    ): Vector? {
        return generateRandomPos(
            nodeEntity,
            integer2,
            integer3,
            0,
            null,
            false,
            0.0,
            toDoubleFunction,
            true,
            0,
            0,
            true
        )
    }

    @Nullable
    fun getAboveLandPos(
        nodeEntity: NodeEntity,
        integer2: Int,
        integer3: Int,
        vector: Vector,
        float5: Float,
        integer6: Int,
        integer7: Int
    ): Vector? {
        return generateRandomPos(
            nodeEntity, integer2, integer3, 0, vector, false,
            float5.toDouble(), nodeEntity::getWalkTargetValue, true, integer6, integer7, true
        )
    }

    @Nullable
    fun getLandPosTowards(nodeEntity: NodeEntity, integer2: Int, integer3: Int, vector: Vector): Vector? {
        val dck5 = vector.subtract(Vector(nodeEntity.x, nodeEntity.y, nodeEntity.z))
        return generateRandomPos(
            nodeEntity,
            integer2,
            integer3,
            0,
            dck5,
            false,
            1.5707963705062866,
            nodeEntity::getWalkTargetValue,
            true,
            0,
            0,
            true
        )
    }

    @Nullable
    fun getPosTowards(nodeEntity: NodeEntity, integer2: Int, integer3: Int, vector: Vector): Vector? {
        val dck5 = vector.subtract(Vector(nodeEntity.x, nodeEntity.y, nodeEntity.z))
        return generateRandomPos(
            nodeEntity,
            integer2,
            integer3,
            0,
            dck5,
            true,
            1.5707963705062866,
            nodeEntity::getWalkTargetValue,
            false,
            0,
            0,
            true
        )
    }

    @Nullable
    fun getPosTowards(nodeEntity: NodeEntity, integer2: Int, integer3: Int, vector: Vector, double5: Double): Vector? {
        val dck7 = vector.subtract(Vector(nodeEntity.x, nodeEntity.y, nodeEntity.z))
        return generateRandomPos(
            nodeEntity,
            integer2,
            integer3,
            0,
            dck7,
            true,
            double5,
            nodeEntity::getWalkTargetValue,
            false,
            0,
            0,
            true
        )
    }

    @Nullable
    fun getAirPosTowards(
        nodeEntity: NodeEntity,
        integer2: Int,
        integer3: Int,
        integer4: Int,
        vector: Vector,
        double6: Double
    ): Vector? {
        val dck8 = vector.subtract(Vector(nodeEntity.x, nodeEntity.y, nodeEntity.z))
        return generateRandomPos(
            nodeEntity,
            integer2,
            integer3,
            integer4,
            dck8,
            false,
            double6,
            nodeEntity::getWalkTargetValue,
            true,
            0,
            0,
            false
        )
    }

    @Nullable
    fun getPosAvoid(nodeEntity: NodeEntity, integer2: Int, integer3: Int, vector: Vector): Vector? {
        val dck5 = nodeEntity.location.subtract(vector).toVector()
        return generateRandomPos(
            nodeEntity,
            integer2,
            integer3,
            0,
            dck5,
            true,
            1.5707963705062866,
            nodeEntity::getWalkTargetValue,
            false,
            0,
            0,
            true
        )
    }

    @Nullable
    fun getLandPosAvoid(nodeEntity: NodeEntity, integer2: Int, integer3: Int, vector: Vector): Vector? {
        val dck5 = nodeEntity.location.subtract(vector).toVector()
        return generateRandomPos(
            nodeEntity,
            integer2,
            integer3,
            0,
            dck5,
            false,
            1.5707963705062866,
            nodeEntity::getWalkTargetValue,
            true,
            0,
            0,
            true
        )
    }

    private fun generateRandomPos(
        nodeEntity: NodeEntity,
        integer2: Int,
        integer3: Int,
        integer4: Int,
        vector: Vector?,
        boolean6: Boolean,
        double7: Double,
        toDoubleFunction: ToDoubleFunction<Position>,
        boolean9: Boolean,
        integer10: Int,
        integer11: Int,
        boolean12: Boolean
    ): Vector? {
        val random: Random = nodeEntity.random

        val navigation = PathTypeFactory(nodeEntity)

        val hasRestriction: Boolean = if (nodeEntity.hasRestriction) {
            nodeEntity.restrictCenter.closerThan(
                nodeEntity.location.toPosition(),
                (nodeEntity.restrictRadius + integer2.toFloat()).toDouble() + 1.0
            )
        } else {
            false
        }

        var bool = false

        var double18 = Double.NEGATIVE_INFINITY

        var position = nodeEntity.location.toPosition()

        for (i in 0..9) {
            val delta: Position? = getRandomDelta(random, integer2, integer3, integer4, vector, double7)
            if (delta != null) {
                var x = delta.x
                val y = delta.y
                var z = delta.z
                var result: Position
                if (nodeEntity.hasRestriction && integer2 > 1) {
                    result = nodeEntity.restrictCenter
                    if (nodeEntity.x > result.x) {
                        x -= random.nextInt(integer2 / 2)
                    } else {
                        x += random.nextInt(integer2 / 2)
                    }
                    if (nodeEntity.z > result.z) {
                        z -= random.nextInt(integer2 / 2)
                    } else {
                        z += random.nextInt(integer2 / 2)
                    }
                }

                result = Position(
                    (x + nodeEntity.x).toInt(),
                    (y + nodeEntity.y).toInt(),
                    (z + nodeEntity.z).toInt()
                )
                if (result.y >= 0 && (!hasRestriction || nodeEntity.isWithinRestriction(
                        result
                    )) && (!boolean12 || (navigation.getTypeAsWalkable(
                        nodeEntity.location.world,
                        result
                    )) == PathType.WALKABLE)
                ) {
                    if (boolean9) {
                        result = moveUpToAboveSolid(
                            result, random.nextInt(integer10 + 1) + integer11, 256
                        ) { pos ->
                            nodeEntity.location.world.getBlockAt(pos.toLocation(nodeEntity.location.world)).type.isSolid
                        }
                    }
                    if (boolean6 || nodeEntity.location.world.getBlockAt(result.toLocation(nodeEntity.location.world)).type != Material.WATER) {
                        val type = PathTypeFactory.getBlockPathTypeStatic(nodeEntity.location.world, result)
                        if (nodeEntity.getPathfindingMalus(type) == 0.0f) {
                            val double28 = toDoubleFunction.applyAsDouble(result)
                            if (double28 > double18) {
                                double18 = double28
                                position = result
                                bool = true
                            }
                        }
                    }
                }
            }
        }
        return if (bool) {
            position.bottomCenter()
        } else {
            null
        }
    }

    private fun getRandomDelta(
        random: Random,
        integer2: Int,
        integer3: Int,
        integer4: Int,
        vector: Vector?,
        double6: Double
    ): Position? {
        return if (vector != null && double6 < Math.PI) {
            val double8: Double = atan2(vector.z, vector.x) - 1.5707963705062866
            val double10 = double8 + (2.0f * random.nextFloat() - 1.0f).toDouble() * double6
            val double12 = sqrt(random.nextDouble()) * SQRT_OF_TWO * integer2.toDouble()
            val double14 = -double12 * sin(double10)
            val double16 = double12 * cos(double10)
            if (abs(double14) <= integer2.toDouble() && abs(double16) <= integer2.toDouble()) {
                val integer18 = random.nextInt(2 * integer3 + 1) - integer3 + integer4
                Position(double14.toInt(), integer18, double16.toInt())
            } else {
                null
            }
        } else {
            val x = random.nextInt(2 * integer2 + 1) - integer2
            val y = random.nextInt(2 * integer3 + 1) - integer3 + integer4
            val z = random.nextInt(2 * integer2 + 1) - integer2
            Position(x, y, z)
        }
    }

    fun moveUpToAboveSolid(position: Position, integer2: Int, integer3: Int, predicate: Predicate<Position>): Position {
        return if (integer2 < 0) {
            throw IllegalArgumentException("aboveSolidAmount was $integer2, expected >= 0")
        } else if (!predicate.test(position)) {
            position
        } else {
            var pos2: Position
            pos2 = position.up()
            while (pos2.y < integer3 && predicate.test(pos2)) {
                pos2 = pos2.up()
            }
            var result: Position
            var temp: Position
            result = pos2
            while (result.y < integer3 && result.y - pos2.y < integer2) {
                temp = result.up()
                if (predicate.test(temp)) {
                    break
                }
                result = temp
            }
            result
        }
    }

}
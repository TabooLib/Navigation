package ink.ptms.navigation.pathfinder

import com.sun.istack.internal.Nullable
import ink.ptms.navigation.util.bottomCenter
import ink.ptms.navigation.util.closerThan
import ink.ptms.navigation.util.toPosition
import ink.ptms.navigation.util.up
import io.izzel.taboolib.module.nms.impl.Position
import org.bukkit.Material
import org.bukkit.util.Vector
import java.lang.IllegalArgumentException
import java.util.*
import java.util.function.Predicate
import java.util.function.ToDoubleFunction
import kotlin.math.*

object RandomPos {
    private const val SQRT_OF_TWO = 1.4142135623730951

    @Nullable
    fun getPos(aqr: NodeEntity, integer2: Int, integer3: Int): Vector? {
        return generateRandomPos(
            aqr,
            integer2,
            integer3,
            0,
            null,
            true,
            1.5707963705062866,
            aqr::getWalkTargetValue,
            false,
            0,
            0,
            true
        )
    }

    @Nullable
    fun getAirPos(
        aqr: NodeEntity,
        integer2: Int,
        integer3: Int,
        integer4: Int,
        dck: Vector,
        double6: Double
    ): Vector? {
        return generateRandomPos(
            aqr,
            integer2,
            integer3,
            integer4,
            dck,
            true,
            double6,
            aqr::getWalkTargetValue,
            true,
            0,
            0,
            false
        )
    }

    @Nullable
    fun getLandPos(aqr: NodeEntity, integer2: Int, integer3: Int): Vector? {
        return getLandPos(aqr, integer2, integer3, aqr::getWalkTargetValue)
    }

    @Nullable
    fun getLandPos(aqr: NodeEntity, integer2: Int, integer3: Int, toDoubleFunction: ToDoubleFunction<Position>): Vector? {
        return generateRandomPos(
            aqr,
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
        aqr: NodeEntity,
        integer2: Int,
        integer3: Int,
        dck: Vector,
        float5: Float,
        integer6: Int,
        integer7: Int
    ): Vector? {
        return generateRandomPos(
            aqr, integer2, integer3, 0, dck, false,
            float5.toDouble(), aqr::getWalkTargetValue, true, integer6, integer7, true
        )
    }

    @Nullable
    fun getLandPosTowards(aqr: NodeEntity, integer2: Int, integer3: Int, dck: Vector): Vector? {
        val dck5 = dck.subtract(Vector(aqr.x, aqr.y, aqr.z))
        return generateRandomPos(
            aqr,
            integer2,
            integer3,
            0,
            dck5,
            false,
            1.5707963705062866,
            aqr::getWalkTargetValue,
            true,
            0,
            0,
            true
        )
    }

    @Nullable
    fun getPosTowards(aqr: NodeEntity, integer2: Int, integer3: Int, dck: Vector): Vector? {
        val dck5 = dck.subtract(Vector(aqr.x, aqr.y, aqr.z))
        return generateRandomPos(
            aqr,
            integer2,
            integer3,
            0,
            dck5,
            true,
            1.5707963705062866,
            aqr::getWalkTargetValue,
            false,
            0,
            0,
            true
        )
    }

    @Nullable
    fun getPosTowards(aqr: NodeEntity, integer2: Int, integer3: Int, dck: Vector, double5: Double): Vector? {
        val dck7 = dck.subtract(Vector(aqr.x, aqr.y, aqr.z))
        return generateRandomPos(
            aqr,
            integer2,
            integer3,
            0,
            dck7,
            true,
            double5,
            aqr::getWalkTargetValue,
            false,
            0,
            0,
            true
        )
    }

    @Nullable
    fun getAirPosTowards(
        aqr: NodeEntity,
        integer2: Int,
        integer3: Int,
        integer4: Int,
        dck: Vector,
        double6: Double
    ): Vector? {
        val dck8 = dck.subtract(Vector(aqr.x, aqr.y, aqr.z))
        return generateRandomPos(
            aqr,
            integer2,
            integer3,
            integer4,
            dck8,
            false,
            double6,
            aqr::getWalkTargetValue,
            true,
            0,
            0,
            false
        )
    }

    @Nullable
    fun getPosAvoid(aqr: NodeEntity, integer2: Int, integer3: Int, dck: Vector): Vector? {
        val dck5 = aqr.location.subtract(dck).toVector()
        return generateRandomPos(
            aqr,
            integer2,
            integer3,
            0,
            dck5,
            true,
            1.5707963705062866,
            aqr::getWalkTargetValue,
            false,
            0,
            0,
            true
        )
    }

    @Nullable
    fun getLandPosAvoid(aqr: NodeEntity, integer2: Int, integer3: Int, dck: Vector): Vector? {
        val dck5 = aqr.location.subtract(dck).toVector()
        return generateRandomPos(
            aqr,
            integer2,
            integer3,
            0,
            dck5,
            false,
            1.5707963705062866,
            aqr::getWalkTargetValue,
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
        dck: Vector?,
        boolean6: Boolean,
        double7: Double,
        toDoubleFunction: ToDoubleFunction<Position>,
        boolean9: Boolean,
        integer10: Int,
        integer11: Int,
        boolean12: Boolean
    ): Vector? {
        val random15: Random = nodeEntity.random

        val navigation = PathTypeFactory(nodeEntity)

        val boolean16: Boolean = if (nodeEntity.hasRestriction) {
            nodeEntity.restrictCenter.closerThan(nodeEntity.location.toPosition(), (nodeEntity.restrictRadius + integer2.toFloat()).toDouble() + 1.0)
        } else {
            false
        }

        var boolean17 = false

        var double18 = Double.NEGATIVE_INFINITY

        var fx20: Position = nodeEntity.location.toPosition()

        for (integer21 in 0..9) {
            val fx22: Position? = getRandomDelta(random15, integer2, integer3, integer4, dck, double7)
            if (fx22 != null) {
                var integer23: Int = fx22.x
                val integer24: Int = fx22.y
                var integer25: Int = fx22.z
                var fx26: Position
                if (nodeEntity.hasRestriction && integer2 > 1) {
                    fx26 = nodeEntity.restrictCenter
                    if (nodeEntity.x > fx26.x) {
                        integer23 -= random15.nextInt(integer2 / 2)
                    } else {
                        integer23 += random15.nextInt(integer2 / 2)
                    }
                    if (nodeEntity.z > fx26.z) {
                        integer25 -= random15.nextInt(integer2 / 2)
                    } else {
                        integer25 += random15.nextInt(integer2 / 2)
                    }
                }

                fx26 = Position(
                    (integer23 + nodeEntity.x).toInt(),
                    (integer24 + nodeEntity.y).toInt(),
                    (integer25 + nodeEntity.z).toInt()
                )
                if (fx26.y >= 0 && (!boolean16 || nodeEntity.isWithinRestriction(
                        fx26
                    )) && (!boolean12 || (navigation.getTypeAsWalkable(nodeEntity.location.world ,fx26)) == PathType.WALKABLE)
                ) {
                    if (boolean9) {
                        fx26 = moveUpToAboveSolid(
                            fx26, random15.nextInt(integer10 + 1) + integer11, 256
                        ) { fx ->
                            nodeEntity.location.world.getBlockAt(fx.toLocation(nodeEntity.location.world)).type.isSolid
                        }
                    }
                    if (boolean6 || nodeEntity.location.world.getBlockAt(fx26.toLocation(nodeEntity.location.world)).type != Material.WATER) {
                        val cww27: PathType = PathTypeFactory.getBlockPathTypeStatic(nodeEntity.location.world, fx26)
                        if (nodeEntity.getPathfindingMalus(cww27) == 0.0f) {
                            val double28 = toDoubleFunction.applyAsDouble(fx26)
                            if (double28 > double18) {
                                double18 = double28
                                fx20 = fx26
                                boolean17 = true
                            }
                        }
                    }
                }
            }
        }
        return if (boolean17) {
            fx20.bottomCenter()
        } else {
            null
        }
    }

    private fun getRandomDelta(random: Random, integer2: Int, integer3: Int, integer4: Int, dck: Vector?, double6: Double): Position? {
        return if (dck != null && double6 < Math.PI) {
            val double8: Double = atan2(dck.z, dck.x) - 1.5707963705062866
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
            val integer8 = random.nextInt(2 * integer2 + 1) - integer2
            val integer9 = random.nextInt(2 * integer3 + 1) - integer3 + integer4
            val integer10 = random.nextInt(2 * integer2 + 1) - integer2
            Position(integer8, integer9, integer10)
        }
    }

    fun moveUpToAboveSolid(fx: Position, integer2: Int, integer3: Int, predicate: Predicate<Position>): Position {
        return if (integer2 < 0) {
            throw IllegalArgumentException("aboveSolidAmount was $integer2, expected >= 0")
        } else if (!predicate.test(fx)) {
            fx
        } else {
            var fx5: Position
            fx5 = fx.up()
            while (fx5.y < integer3 && predicate.test(fx5)) {
                fx5 = fx5.up()
            }
            var fx6: Position
            var fx7: Position
            fx6 = fx5
            while (fx6.y < integer3 && fx6.y - fx5.y < integer2) {
                fx7 = fx6.up()
                if (predicate.test(fx7)) {
                    break
                }
                fx6 = fx7
            }
            fx6
        }
    }

}
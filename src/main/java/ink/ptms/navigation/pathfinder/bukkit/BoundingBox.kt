package ink.ptms.navigation.pathfinder.bukkit

import org.bukkit.util.Vector

/**
 * ClayCombat
 * ink.ptms.navigation.bukkit.BoundingBox
 *
 * @author sky
 * @since 2021/1/25 3:02 上午
 */
class BoundingBox(val minX: Double, val minY: Double, val minZ: Double, val maxX: Double, val maxY: Double, val maxZ: Double) {

    fun move(vector: Vector): BoundingBox {
        return move(vector.x, vector.y, vector.z)
    }

    fun move(x: Double, y: Double, z: Double): BoundingBox {
        return BoundingBox(minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z)
    }

    fun contains(v: Vector): Boolean {
        return contains(v.x, v.y, v.z)
    }

    fun contains(x: Double, y: Double, z: Double): Boolean {
        return x >= minX && x < maxX && y >= minY && y < maxY && z >= minZ && z < maxZ
    }

    fun getSize(): Double {
        return (getXSize() + getYSize() + getZSize()) / 3.0
    }

    fun getXSize(): Double {
        return maxX - minX
    }

    fun getYSize(): Double {
        return maxY - minY
    }

    fun getZSize(): Double {
        return maxZ - minZ
    }

    override fun toString(): String {
        return "BoundingBox(minX=$minX, minY=$minY, minZ=$minZ, maxX=$maxX, maxY=$maxY, maxZ=$maxZ)"
    }

    companion object {

        fun zero() = BoundingBox(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    }
}
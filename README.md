# Navigation (no-entity)
Minecraft 无实体寻路（部分代码来自 Minecraft 核心）  

## 前言
_最早在 Adyeshach 开发的时候就想写这个东西了，不过那个时候写了一点没坚持下来。  
这一次又轮到 Chemdah 需要，但是实在是不想用 Adyeshach 的那个脑残办法。  
硬着头皮对着 mcp 耗了 48 小时才读了不到 1000 行，直到现在 BinaryHeap 还是反编译过来的。  
刚开始写想着偷个懒直接复制，没想到测试的时候不是寻不出来就是卡爆，还不知道哪里的问题。草了。  
所以又耗一天把 WalkNodeEvaluator 照着写了一份带注释的版本，不过现在也仅仅支持标准陆地生物的寻路方式。_

_不管是 Minecraft 还是 Paper 的寻路 API 都必须要依赖一个当前世界中存在实体，导致 Adyeshach 其本身的发包实体直接爆死了。  
加上 Minecraft 本身的寻路在低版本的表现实在有些拉胯，直接否掉了 Unsafe 造 EntityInsentient 越过实体生成去寻路的想法。  
直接自己写，还不用考虑跨版本，Nice！_


## 使用方法
```kotlin
// 起点
val start: Location = ...
// 终点
val end: Location = ...
// 创 Minecraft 代理实体，并具有高度宽度等属性。
val entity = NodeEntity(start, height = 2.0, width = 1.0)
// 创建 Pathfinder
val pathfinder = Navigation.create(entity)
// 开始寻路并返回路径结果
val path = pathfinder.findPath(end, distance = 16)
// 打印路径
if (path != null) {
    path.nodes.forEach { node ->
        println(node)
    }
}
```

# 随机坐标生成器
```kotlin
// 创建 Minecraft 代理实体，并具有高度宽度等属性。
val entity = NodeEntity(start, height = 2.0, width = 1.0)
// 生成随机坐标
val random = Navigation.randomPositionGenerator().generateLand(entity, 10, 6)
```
# Navigation (no-entity)
> Minecraft 无实体寻路（部分代码来自 Minecraft 核心）

## 使用方法
```kotlin
// 起点
val start: Location = ...
// 终点
val end: Location = ...
// 创建单位代替 Minecraft 实体，并具有高度宽度等。
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

> 当前仅支持陆地生物（WalkNodeEvaluator）其他类型等待有缘人。  
> 逆向核心不容易，路过给个小星星。
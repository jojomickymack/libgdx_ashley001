package com.central.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool
import com.central.views.MyGameObj
import com.central.components.AiControlledComponent
import com.central.components.PhysicsComponent
import com.central.components.UserControlledComponent

class MapControllerSystem : EntitySystem() {

    val map = TmxMapLoader().load("map/map.tmx")
    val mr = OrthogonalTiledMapRenderer(map, MyGameObj.unitScale)

    val solid = map.layers.get("solid") as TiledMapTileLayer
    val background = map.layers.get("background") as TiledMapTileLayer

    private val tiles = Array<Rectangle>()

    private var player = ImmutableArray(Array<Entity>())
    private var ai = ImmutableArray(Array<Entity>())

    // create a pool of rectangle objects for collision detection
    private val rectPool = object : Pool<Rectangle>() {
        override fun newObject(): Rectangle = Rectangle()
    }

    init {

    }

    override fun addedToEngine(engine: Engine) {
        player = engine.getEntitiesFor(Family.all(PhysicsComponent::class.java, UserControlledComponent::class.java).get())
        ai = engine.getEntitiesFor(Family.all(PhysicsComponent::class.java, AiControlledComponent::class.java).get())
    }

    fun getTiles(startX: Int, startY: Int, endX: Int, endY: Int, tileLayer: TiledMapTileLayer): Array<Rectangle> {
        rectPool.freeAll(tiles)

        tiles.clear()

        for (y in startY..endY) {
            for (x in startX..endX) {
                val cell = tileLayer.getCell(x, y)
                if (cell != null) {
                    val rect = rectPool.obtain()
                    rect.set(x.toFloat(), y.toFloat(), 1f, 1f)
                    tiles.add(rect)
                }
            }
        }
        return tiles
    }

    fun getHorizNeighbourTiles(velocity: Vector2, rect: Rectangle, tileLayer: TiledMapTileLayer): Array<Rectangle> {
        val startY = rect.y.toInt()
        val endY = (rect.y + rect.height).toInt()

        // if the sprite is moving right, get the tiles to its right side
        // if the sprite is moving left, get the tiles to its left side
        val startX = if (velocity.x > 0) (rect.x + rect.width).toInt() else rect.x.toInt()
        val endX = startX

        return getTiles(startX, startY, endX, endY, tileLayer)
    }

    fun getVertNeighbourTiles(velocity: Vector2, rect: Rectangle, tileLayer: TiledMapTileLayer): Array<Rectangle> {
        val startX = rect.x.toInt()
        val endX = (rect.x + rect.width).toInt()
        // if sprite is moving up, get the tiles above it
        // if sprite is moving down, get the tiles below it
        val startY = if (velocity.y > 0) (rect.y + rect.height).toInt() else rect.y.toInt()
        val endY = startY

        return getTiles(startX, startY, endX, endY, tileLayer)
    }

    override fun update(deltaTime: Float) {

        player.forEach {
            val physics = it.getComponent(PhysicsComponent::class.java)

            val testRect = Rectangle()

            with(physics) {
                testRect.set(rect.x * MyGameObj.unitScale, rect.y * MyGameObj.unitScale, w * MyGameObj.unitScale, h * MyGameObj.unitScale)

                var myTiles = getVertNeighbourTiles(vel, testRect, solid)

                myTiles.forEach {
                    if (testRect.overlaps(it)) {
                        if (vel.y > 0) {
                            pos.y = it.y / MyGameObj.unitScale - testRect.height / MyGameObj.unitScale
                            rect.y = it.y - rect.height
                        } else if (vel.y < 0) {
                            pos.y = it.y / MyGameObj.unitScale + it.height / MyGameObj.unitScale
                            rect.y = it.y + it.height
                        }
                        vel.y = 0f
                        return@forEach
                    }
                }

                testRect.set(pos.x * MyGameObj.unitScale, pos.y * MyGameObj.unitScale, w * MyGameObj.unitScale, h * MyGameObj.unitScale)

                myTiles = getHorizNeighbourTiles(vel, testRect, solid)

                myTiles.forEach {
                    if (testRect.overlaps(it)) {
                        vel.x = 0f
                        rect.x -= vel.x * MyGameObj.unitScale
                        return@forEach
                    }
                }
            }
        }

        ai.forEach {
            val physics = it.getComponent(PhysicsComponent::class.java)

            val testRect = Rectangle()

            with(physics) {
                testRect.set(rect.x * MyGameObj.unitScale, rect.y * MyGameObj.unitScale, w * MyGameObj.unitScale, h * MyGameObj.unitScale)

                var myTiles = getVertNeighbourTiles(vel, testRect, solid)

                myTiles.forEach {
                    if (testRect.overlaps(it)) {
                        if (vel.y > 0) {
                            pos.y = it.y / MyGameObj.unitScale - testRect.height / MyGameObj.unitScale
                            rect.y = it.y - rect.height
                        } else if (vel.y < 0) {
                            pos.y = it.y / MyGameObj.unitScale + it.height / MyGameObj.unitScale
                            rect.y = it.y + it.height
                        }
                        vel.y = 0f
                        return@forEach
                    }
                }

                testRect.set(pos.x * MyGameObj.unitScale, pos.y * MyGameObj.unitScale, w * MyGameObj.unitScale, h * MyGameObj.unitScale)

                myTiles = getHorizNeighbourTiles(vel, testRect, solid)

                myTiles.forEach {
                    if (testRect.overlaps(it)) {
                        direction *= -1
                        rect.x -= vel.x * MyGameObj.unitScale
                        return@forEach
                    }
                }
            }
        }

    }
}

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
import com.central.MyGameObj
import com.central.components.AiControlledComponent
import com.central.components.PhysicsComponent
import com.central.components.UserControlledComponent

class MapControllerSystem : EntitySystem() {

    val map = TmxMapLoader().load("map/map.tmx")
    val mr = OrthogonalTiledMapRenderer(map, MyGameObj.unitScale)

    val solid = map.layers.get("solid") as TiledMapTileLayer
    val background = map.layers.get("background") as TiledMapTileLayer

    private val tiles = Array<Rectangle>()

    private var player: ImmutableArray<Entity> = ImmutableArray(Array<Entity>())
    private var ai: ImmutableArray<Entity> = ImmutableArray(Array<Entity>())

    // create a pool of rectangle objects for collision detection
    private val rectPool = object : Pool<Rectangle>() {
        override fun newObject(): Rectangle {
            return Rectangle()
        }
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
        val startX: Int
        val startY: Int
        val endX: Int
        val endY: Int
        // if the sprite is moving right, get the tiles to its right side
        if (velocity.x > 0) {
            endX = (rect.x + rect.width).toInt()
            startX = endX
        } else { // if the sprite is moving left, get the tiles to its left side
            endX = rect.x.toInt()
            startX = endX
        }
        startY = rect.y.toInt()
        endY = (rect.y + rect.height).toInt()

        return getTiles(startX, startY, endX, endY, tileLayer)
    }

    fun getVertNeighbourTiles(velocity: Vector2, rect: Rectangle, tileLayer: TiledMapTileLayer): Array<Rectangle> {
        val startX: Int
        val startY: Int
        val endX: Int
        val endY: Int
        // if sprite is moving up, get the tiles above it
        if (velocity.y > 0) {
            endY = (rect.y + rect.height).toInt()
            startY = endY
        } else { // if sprite is moving down, get the tiles below it
            endY = rect.y.toInt()
            startY = endY
        }
        startX = rect.x.toInt()
        endX = (rect.x + rect.width).toInt()

        return getTiles(startX, startY, endX, endY, tileLayer)
    }

    override fun update(deltaTime: Float) {

        player.forEach {
            val physics = it.getComponent(PhysicsComponent::class.java)

            val testRect = Rectangle()

            testRect.set(physics.rect.x * MyGameObj.unitScale, physics.rect.y * MyGameObj.unitScale, physics.w * MyGameObj.unitScale, physics.h * MyGameObj.unitScale)

            var myTiles = getVertNeighbourTiles(physics.vel, testRect, this.solid)

            myTiles.forEach {
                if (testRect.overlaps(it)) {
                    if (physics.vel.y > 0) {
                        physics.pos.y = it.y / MyGameObj.unitScale - testRect.height / MyGameObj.unitScale
                        physics.rect.y = it.y - physics.rect.height
                    } else if (physics.vel.y < 0) {
                        physics.pos.y = it.y / MyGameObj.unitScale + it.height / MyGameObj.unitScale
                        physics.rect.y = it.y + it.height
                    }
                    physics.vel.y = 0f
                    return@forEach
                }
            }

            testRect.set(physics.pos.x * MyGameObj.unitScale, physics.pos.y * MyGameObj.unitScale, physics.w * MyGameObj.unitScale, physics.h * MyGameObj.unitScale)

            myTiles = getHorizNeighbourTiles(physics.vel, testRect, this.solid)

            myTiles.forEach {
                if (testRect.overlaps(it)) {
                    physics.vel.x = 0f
                    physics.rect.x -= physics.vel.x * MyGameObj.unitScale
                    return@forEach
                }
            }
        }

        ai.forEach {
            val physics = it.getComponent(PhysicsComponent::class.java)

            val testRect = Rectangle()

            testRect.set(physics.rect.x * MyGameObj.unitScale, physics.rect.y * MyGameObj.unitScale, physics.w * MyGameObj.unitScale, physics.h * MyGameObj.unitScale)

            var myTiles = getVertNeighbourTiles(physics.vel, testRect, this.solid)

            myTiles.forEach {
                if (testRect.overlaps(it)) {
                    if (physics.vel.y > 0) {
                        physics.pos.y = it.y / MyGameObj.unitScale - testRect.height / MyGameObj.unitScale
                        physics.rect.y = it.y - physics.rect.height
                    } else if (physics.vel.y < 0) {
                        physics.pos.y = it.y / MyGameObj.unitScale + it.height / MyGameObj.unitScale
                        physics.rect.y = it.y + it.height
                    }
                    physics.vel.y = 0f
                    return@forEach
                }
            }

            testRect.set(physics.pos.x * MyGameObj.unitScale, physics.pos.y * MyGameObj.unitScale, physics.w * MyGameObj.unitScale, physics.h * MyGameObj.unitScale)

            myTiles = getHorizNeighbourTiles(physics.vel, testRect, this.solid)

            myTiles.forEach {
                if (testRect.overlaps(it)) {
                    physics.direction *= -1
                    physics.rect.x -= physics.vel.x * MyGameObj.unitScale
                    return@forEach
                }
            }
        }

    }
}

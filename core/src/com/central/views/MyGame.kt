package com.central.views

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import ktx.app.KtxScreen
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.central.App
import com.central.components.*
import com.central.systems.*

class MyGame(val application: App) : KtxScreen {

    var ashleyEngine = Engine()

    var alex = Entity()
    var enemy = Entity()

    val alexTex = TextureComponent()
    val alexPhys = PhysicsComponent()
    val enemyTex = TextureComponent()
    val enemyPhys = PhysicsComponent()

    val userControlled = UserControlledComponent()
    val cameraFollow = CameraFollowComponent()
    val aiControlled = AiControlledComponent()

    val mapControllerSystem = MapControllerSystem()
    val physicsSystem = PhysicsSystem()
    val renderSystem = RenderSystem()
    val userControlledSystem = UserControlledSystem()
    val aiControlledSystem = AiControlledSystem()
    val cameraFollowSystem = CameraFollowSystem()

    init {
        with(ashleyEngine) {
            addSystem(physicsSystem)
            addSystem(renderSystem)
            addSystem(userControlledSystem)
            addSystem(aiControlledSystem)
            addSystem(mapControllerSystem)
            addSystem(cameraFollowSystem)
        }

        alexTex.region = TextureRegion(MyGameObj.alexTex)

        with(alexPhys) {
            w = 30f
            h = 50f
            pos.set(200f, 200f)
        }

        with(alex) {
            add(alexTex)
            add(alexPhys)
            add(userControlled)
            add(cameraFollow)
        }

        enemyTex.region = TextureRegion(MyGameObj.enemyTex)

        with(enemyPhys) {
            w = 40f
            h = 40f
            topSpeed = 100f
            pos.set(100f, 200f)
        }

        with(enemy) {
            add(enemyTex)
            add(enemyPhys)
            add(aiControlled)
            // add(cameraFollow)
        }

        with(ashleyEngine) {
            addEntity(alex)
            addEntity(enemy)
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        ashleyEngine.update(delta)
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun dispose() {
        MyGameObj.dispose()
        println("all disposable memory freed")
        super.dispose()
    }
}

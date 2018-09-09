package com.central

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import ktx.app.KtxScreen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.central.components.*
import com.central.systems.*

class MyGame(val application: Application) : KtxScreen {

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
        ashleyEngine.addSystem(physicsSystem)
        ashleyEngine.addSystem(renderSystem)
        ashleyEngine.addSystem(userControlledSystem)
        ashleyEngine.addSystem(aiControlledSystem)
        ashleyEngine.addSystem(mapControllerSystem)
        ashleyEngine.addSystem(cameraFollowSystem)

        alexTex.region = TextureRegion(Texture(Gdx.files.internal("alex.png")))
        alexPhys.w = 30f
        alexPhys.h = 50f
        alexPhys.pos.set(200f, 200f)

        alex.add(alexTex)
        alex.add(alexPhys)
        alex.add(userControlled)
        alex.add(cameraFollow)

        ashleyEngine.addEntity(alex)

        enemyTex.region = TextureRegion(Texture(Gdx.files.internal("enemy.png")))
        enemyPhys.w = 40f
        enemyPhys.h = 40f
        enemyPhys.topSpeed = 100f
        enemyPhys.pos.set(100f, 200f)

        enemy.add(enemyTex)
        enemy.add(enemyPhys)
        enemy.add(aiControlled)
        // enemy.add(cameraFollow)

        ashleyEngine.addEntity(enemy)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        ashleyEngine.update(delta)
    }

    override fun resize(width: Int, height: Int) {

    }
}

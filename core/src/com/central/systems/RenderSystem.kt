package com.central.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.utils.Array
import com.central.views.MyGameObj
import com.central.components.TextureComponent
import com.central.components.PhysicsComponent

class RenderSystem : EntitySystem() {
    private var textures: ImmutableArray<Entity> = ImmutableArray(Array<Entity>())

    private val mm = ComponentMapper.getFor(PhysicsComponent::class.java)
    private val tm = ComponentMapper.getFor(TextureComponent::class.java)

    init {

    }

    override fun addedToEngine(engine: Engine) {
        textures = engine.getEntitiesFor(Family.all(TextureComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        val map = engine.getSystem(MapControllerSystem::class.java)
        map.mr.setView(MyGameObj.cam)
        map.mr.render()

        MyGameObj.stg.batch.projectionMatrix = MyGameObj.cam.combined

        MyGameObj.stg.batch.begin()

        textures.forEach {
            val physics = mm.get(it)
            val texture = tm.get(it)

            with(physics) {
                MyGameObj.stg.batch.draw(texture.region,
                        pos.x * MyGameObj.unitScale, pos.y * MyGameObj.unitScale,
                        w * MyGameObj.unitScale, h * MyGameObj.unitScale,
                        w * MyGameObj.unitScale, h * MyGameObj.unitScale,
                        scl.x, scl.y,
                        rot)
            }
        }

        MyGameObj.stg.batch.end()
    }
}
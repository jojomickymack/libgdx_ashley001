package com.central.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.central.views.MyGameObj
import com.central.components.CameraFollowComponent
import com.central.components.PhysicsComponent

class CameraFollowSystem : IteratingSystem(Family.all(PhysicsComponent::class.java, CameraFollowComponent::class.java).get()) {
    private val pm = ComponentMapper.getFor(PhysicsComponent::class.java)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val physics = pm.get(entity)

        // MyGameObj.cam.position.set(physics.pos.x * MyGameObj.unitScale, physics.pos.y * MyGameObj.unitScale, 0f)
        MyGameObj.cam.position.x = physics.pos.x * MyGameObj.unitScale
        MyGameObj.cam.update()
    }
}
package com.central.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.central.components.PhysicsComponent
import com.central.components.AiControlledComponent

class AiControlledSystem : IteratingSystem(Family.all(PhysicsComponent::class.java, AiControlledComponent::class.java).get()) {
    private val pm = ComponentMapper.getFor(PhysicsComponent::class.java)

    init {

    }

    public override fun processEntity(entity: Entity, deltaTime: Float) {
        val physics = pm.get(entity)

        physics.vel.x = physics.topSpeed * physics.direction
    }
}
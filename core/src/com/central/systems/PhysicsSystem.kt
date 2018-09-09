package com.central.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.central.MyGameObj
import com.central.components.PhysicsComponent
import java.math.RoundingMode

class PhysicsSystem : IteratingSystem(Family.all(PhysicsComponent::class.java).get()) {
    private val pm = ComponentMapper.getFor(PhysicsComponent::class.java)
    private val dampening = 0.9f

    public override fun processEntity(entity: Entity, deltaTime: Float) {
        val physics = pm.get(entity)

        physics.vel.y -= MyGameObj.grav

        physics.vel.x *= dampening
        if (Math.abs(physics.vel.x) < 5) physics.vel.x = 0f

        physics.pos.x += physics.vel.x * deltaTime
        physics.pos.y += physics.vel.y * deltaTime

        physics.pos.x = physics.pos.x.toBigDecimal().setScale(2, RoundingMode.DOWN).toFloat()
        physics.pos.y = physics.pos.y.toBigDecimal().setScale(2, RoundingMode.DOWN).toFloat()

        physics.rect.set(physics.pos.x, physics.pos.y, physics.w, physics.h)
    }
}
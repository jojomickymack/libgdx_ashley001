package com.central.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.central.views.MyGameObj
import com.central.components.PhysicsComponent
import java.math.RoundingMode

class PhysicsSystem : IteratingSystem(Family.all(PhysicsComponent::class.java).get()) {
    private val pm = ComponentMapper.getFor(PhysicsComponent::class.java)
    private val dampening = 0.9f

    public override fun processEntity(entity: Entity, deltaTime: Float) {
        val physics = pm.get(entity)

        with(physics) {
            vel.y -= MyGameObj.grav

            vel.x *= dampening
            if (Math.abs(vel.x) < 5) vel.x = 0f

            pos.x += vel.x * deltaTime
            pos.y += vel.y * deltaTime

            pos.x = pos.x.toBigDecimal().setScale(2, RoundingMode.DOWN).toFloat()
            pos.y = pos.y.toBigDecimal().setScale(2, RoundingMode.DOWN).toFloat()

            rect.set(pos.x, pos.y, w, h)
        }
    }
}
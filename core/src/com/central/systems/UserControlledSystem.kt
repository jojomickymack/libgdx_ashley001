package com.central.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.central.components.PhysicsComponent
import com.central.components.UserControlledComponent

class UserControlledSystem : IteratingSystem(Family.all(PhysicsComponent::class.java, UserControlledComponent::class.java).get()) {
    private val pm = ComponentMapper.getFor(PhysicsComponent::class.java)

    init {

    }

    public override fun processEntity(entity: Entity, deltaTime: Float) {
        val physics = pm.get(entity)

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            physics.vel.x = -physics.topSpeed
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            physics.vel.x = physics.topSpeed
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            physics.vel.y = physics.topSpeed
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            physics.vel.y = -physics.topSpeed
        }
    }
}
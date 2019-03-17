package com.central.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport

object MyGameObj {
    var width = Gdx.graphics.height.toFloat()
    var height = Gdx.graphics.width.toFloat()

    val sb = SpriteBatch()
    val cam = OrthographicCamera(width, height)
    val view = StretchViewport(480f, 360f, cam)
    val stg = Stage(view, sb)

    val unitScale = 1 / 16f
    val grav = 10f

    val map = TmxMapLoader().load("map/map.tmx")
    val mr = OrthogonalTiledMapRenderer(map, unitScale)

    val alexTex = Texture(Gdx.files.internal("alex.png"))
    val enemyTex = Texture(Gdx.files.internal("enemy.png"))

    init {
        cam.setToOrtho(false, 25f, 25f)
    }

    fun dispose() {
        this.sb.dispose()
        this.stg.dispose()
        this.map.dispose()
        this.mr.dispose()
        this.alexTex.dispose()
        this.enemyTex.dispose()
    }
}
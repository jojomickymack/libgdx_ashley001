package com.central

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport

object MyGameObj {
    var width = Gdx.graphics.height.toFloat()
    var height = Gdx.graphics.width.toFloat()

    val sb = SpriteBatch()
    val cam = OrthographicCamera(width, height)
    val view = FitViewport(480f, 360f, cam)
    val stg = Stage(view, sb)

    val unitScale = 1 / 16f
    val grav = 10f

    init {
        cam.setToOrtho(false, 25f, 25f)
    }
}
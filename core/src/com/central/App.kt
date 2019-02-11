package com.central

import com.badlogic.gdx.Screen
import com.central.views.MyGame
import ktx.app.KtxGame

class App : KtxGame<Screen>() {

    override fun create() {
        val myGame = MyGame(this)

        addScreen(myGame)
        setScreen<MyGame>()
    }
}

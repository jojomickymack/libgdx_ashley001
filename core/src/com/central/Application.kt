package com.central

import com.badlogic.gdx.Screen
import ktx.app.KtxGame
import ktx.async.enableKtxCoroutines

class Application : KtxGame<Screen>() {

    override fun create() {
        enableKtxCoroutines(asynchronousExecutorConcurrencyLevel = 1)

        val myGame = MyGame(this)

        addScreen(myGame)
        setScreen<MyGame>()
    }
}

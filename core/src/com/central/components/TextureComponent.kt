package com.central.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

class TextureComponent : Component {
    var defaultTex = Texture("default.png")
    var region = TextureRegion(defaultTex, 0, 0, defaultTex.width, defaultTex.height)
}
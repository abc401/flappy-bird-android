package com.example.flappybird

data class Vec2(var x: Float = 0F, var y: Float = 0F)

operator fun Vec2.plus(other: Vec2) = Vec2(this.x + other.x, this.y + other.y)
operator fun Vec2.unaryMinus() = Vec2(-this.x, -this.y)
operator fun Vec2.minus(other: Vec2) = this + (-other)

package com.example.flappybird

import kotlin.math.sqrt

class Vec2(var x: Float = 0F, var y: Float = 0F) {
}

operator fun Vec2.plus(other: Vec2) = Vec2(x + other.x, y + other.y)
operator fun Vec2.unaryMinus() = Vec2(-x, -y)
operator fun Vec2.minus(other: Vec2) = this + (-other)
operator fun Vec2.minus(other: Float) = this - Vec2(other, other)
operator fun Vec2.times(other: Number) = Vec2(
    x * other.toFloat(),
    y * other.toFloat()
)
operator fun Vec2.div(other: Number) = Vec2(
    x / other.toFloat(),
    y / other.toFloat()
)

fun Vec2.magnitude(): Float {
    return sqrt(x*x + y*y)
}

fun Vec2.normalize() = this / this.magnitude()

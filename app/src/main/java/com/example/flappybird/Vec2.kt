package com.example.flappybird

import kotlin.math.sqrt

class Vec2(var x: Float, var y: Float) {
    constructor(x: Number, y: Number) : this(x.toFloat(), y.toFloat())
    constructor(n: Number) : this(n.toFloat(), n.toFloat())
    constructor() : this(0, 0)
}

operator fun Vec2.plus(other: Vec2) = Vec2(x + other.x, y + other.y)
operator fun Vec2.unaryMinus() = Vec2(-x, -y)
operator fun Vec2.minus(other: Vec2) = this + (-other)
operator fun Vec2.minus(other: Number) = this - Vec2(other)
operator fun Vec2.times(other: Number) = Vec2(
    x * other.toFloat(),
    y * other.toFloat()
)
operator fun Vec2.div(other: Number) = Vec2(
    x / other.toFloat(),
    y / other.toFloat()
)

fun Vec2.magnitude() = sqrt(x*x + y*y)

fun Vec2.normalize() = this / this.magnitude()

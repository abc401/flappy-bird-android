package com.example.flappybird

fun lerp(a: Float, b: Float, t: Float): Float {
    return a + (b - a) * t
}
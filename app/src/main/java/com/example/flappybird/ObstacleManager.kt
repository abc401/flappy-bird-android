package com.example.flappybird

import java.util.*
import kotlin.math.abs

class ObstacleManager(private val viewDimensions: Vec2) {
    var obstacles: Queue<Obstacle> = LinkedList()
    var score = 0
    private var nextObstacleToSpawn = Obstacle(viewDimensions)
    private var timeSinceLastObstacleSpawn = 0L

    init {
        obstacles.add(Obstacle(viewDimensions))
    }

    private fun timeToNextObstacleSpawn(): Long {
        val last = obstacles.last()
        val distBetweenOpenings = abs(
            last.openingLocation - nextObstacleToSpawn.openingLocation
        )

        return 200L + distBetweenOpenings.toLong()*2
    }

    private fun removeOutOfBoundsObstacles() {
        // TODO: Handle the case where the obstacles queue is empty
        var obstacle = obstacles.element()
        while (obstacle.isUseless()) {
            obstacles.remove()
            obstacle = obstacles.element()
        }
    }

    private fun spawnObstacle() {
        obstacles.add(nextObstacleToSpawn)
        nextObstacleToSpawn = Obstacle(viewDimensions)
        timeSinceLastObstacleSpawn = 0L
    }

    private fun trySpawnNewObstacle() {
        if (timeToNextObstacleSpawn() > timeSinceLastObstacleSpawn) {
            return
        }
        spawnObstacle()
    }

    private fun updateObstacles(deltaT: Long) {
        for (obstacle in obstacles) {
            obstacle.update(deltaT)
        }
    }

    private fun updateScoreMetaData(flappyPos: Vec2) {
        for (obstacle in obstacles) {
            obstacle.updateFlappyData(flappyPos)
        }
    }

    private fun updateScore() {
        for (obstacle in obstacles) {
            if (!obstacle.flappyJustCrossedObstacle()) continue
            score += 1
            break
        }
    }

    inline fun forEachObstacle(action: (Obstacle) -> Unit) {
        obstacles.forEach(action)
    }

    fun update(flappyPos: Vec2, deltaT: Long) {
        timeSinceLastObstacleSpawn += deltaT

        updateObstacles(deltaT)
        removeOutOfBoundsObstacles()

        updateScoreMetaData(flappyPos)
        updateScore()

        trySpawnNewObstacle()
    }
}
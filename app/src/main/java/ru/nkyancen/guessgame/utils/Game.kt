package ru.nkyancen.guessgame.utils

import ru.nkyancen.guessgame.utils.GameState.*


class Game(
    val attemptsAmount: Int = 10,
    val downLimit: Int = 0,
    val upperLimit: Int = 1000
) {
    var randomNumber = (downLimit..upperLimit).random()
        private set
    var attemptsCount = attemptsAmount
        private set

    fun restartGame() {
        randomNumber = (downLimit..upperLimit).random()
        attemptsCount = attemptsAmount
    }

    fun analyzeInput(userNumber: Int): GameState {
        return if ((userNumber < downLimit) or (userNumber > upperLimit)) {
            OUT
        } else {
            analyzeNumbers(userNumber)
        }
    }

    private fun analyzeNumbers(userNumber: Int): GameState {
        attemptsCount -= 1
        return if (userNumber == randomNumber) {
            WIN
        } else if (attemptsCount == 0) {
            LOSE
        } else if (userNumber > randomNumber) {
            MORE
        } else {
            LESS
        }
    }

}
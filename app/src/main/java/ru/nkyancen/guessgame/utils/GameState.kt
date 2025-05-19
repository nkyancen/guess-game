package ru.nkyancen.guessgame.utils

enum class GameState (
    val index: Int
){
    EMPTY(0),
    WIN(1),
    LOSE(2),
    MORE(3),
    LESS(4),
    OUT(5),
    LONG(6)
}

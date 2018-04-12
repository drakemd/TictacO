package edu.upi.cs.drake.tictaco.model

sealed class GameState<T>{
    abstract val data: T?

    class Loading<T>(override val data: T?): GameState<T>()
    class YourTurn<T>(override val data: T?): GameState<T>()
    class EnemyTurn<T>(override val data: T?): GameState<T>()
}
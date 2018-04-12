package edu.upi.cs.drake.tictaco.model

data class GameData(
        var gameId: String,
        var totalPlayer: String,
        var player1Name: String,
        var player2Name: String,
        var player1Score: String,
        var player2Score: String,
        var player1Ready: String,
        var player2Ready: String,
        var state: List<Int>,
        var turn: String,
        var newMove: Int
)
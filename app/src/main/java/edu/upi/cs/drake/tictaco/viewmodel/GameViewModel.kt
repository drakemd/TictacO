/**
 * Copyright 2018 Indraga Martiyana D
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.upi.cs.drake.tictaco.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import edu.upi.cs.drake.tictaco.firestore.FireStoreConstants
import edu.upi.cs.drake.tictaco.firestore.FirestoreHelper
import edu.upi.cs.drake.tictaco.model.GameData
import edu.upi.cs.drake.tictaco.model.GameState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameViewModel @Inject constructor(private val firestoreHelper: FirestoreHelper): ViewModel(){
    private var gameState: MutableLiveData<GameState<GameData>> = MutableLiveData()
    private var compositeDisposable = CompositeDisposable()

    //player 1 or 2 default is 0
    var player = 0

    init {
        gameState.value = GameState.Loading(null)
    }

    //subscribe data from firestore helper and put it on the appropriate game state (LOADING, WIN, LOSE, etc)
    fun subscribeGame(): LiveData<GameState<GameData>>{
        val disposable = firestoreHelper.getCurrentDocumentData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    val gameData = it as GameData
                    if(gameData.totalPlayer.toInt() < 2){
                        gameState.value = GameState.Cancelled(gameData)
                    }else{
                        val winner = checkWin(gameData.state)
                        if(winner > 0){
                            updateScore(winner, gameData)
                            when(winner){
                                player -> gameState.value = GameState.Win(gameData)
                                else -> gameState.value = GameState.Lose(gameData)
                            }
                        }else{
                            if(gameData.player1Ready.toBoolean() && gameData.player2Ready.toBoolean()){
                                if(gameData.turn.toInt() == player)
                                    gameState.value = GameState.YourTurn(gameData)
                                else
                                    gameState.value = GameState.EnemyTurn(gameData)
                            }else
                                gameState.value = GameState.Loading(gameData)
                        }
                    }
                }, {
                    Log.d("error", "GameViewModel " + it.message)}
                )
        compositeDisposable.add(disposable)
        return gameState
    }

    //handle player movement by updating boardstate in the firestore
    fun move(position: Int): String{
        //create new board state
        val boardState: MutableList<Int> = gameState.value?.data?.state as MutableList<Int>
        boardState[position] = player

        val newState: MutableMap<String, Any> = HashMap()
        newState[FireStoreConstants.TURN] = if(player == 1) 2 else 1
        newState[FireStoreConstants.NEW_MOVE] = position
        newState[FireStoreConstants.STATE] = boardState
        firestoreHelper.update(newState)

        return if(player == 1) "X" else "O"
    }

    //set player ready to true
    fun playerReady(){
        val newState: MutableMap<String, Any> = HashMap()
        if(player == 1)
            newState[FireStoreConstants.PLAYER1_READY] = true
        else
            newState[FireStoreConstants.PLAYER2_READY] = true
        firestoreHelper.update(newState)
    }

    //this method handle game cancellation and update data accordingly
    fun cancelGame(){
        val newVal: MutableMap<String, Any> = HashMap()
        newVal[FireStoreConstants.TOTAL_PLAYER] = 1
        firestoreHelper.update(newVal)
        unsubscribe()
    }

    fun playAgain(){
        val newState: MutableMap<String, Any> = HashMap()
        newState[if(player == 1) FireStoreConstants.PLAYER1_READY else FireStoreConstants.PLAYER2_READY] = true
        firestoreHelper.update(newState)
    }

    fun quitGame(){
        firestoreHelper.delete()
        unsubscribe()
    }

    private fun checkWin(board: List<Int>): Int{
        var winner = 0
        when {
            checkHorizontal(board) > 0 -> winner = checkHorizontal(board)
            checkVertical(board) > 0 -> winner = checkVertical(board)
            checkDiagonal(board) > 0 -> winner = checkDiagonal(board)
        }
        return winner
    }

    //these methods check if a player win the game
    private fun checkHorizontal(board: List<Int>): Int{
        return if(board[0] == board[1] && board[1] == board[2])
                board[0]
            else if(board[3] == board[4] && board[4] == board[5])
                board[3]
            else if(board[6] == board[7] && board[7] == board[8])
                board[6]
            else
                0
    }

    private fun checkVertical(board: List<Int>): Int{
        return if(board[0] == board[3] && board[3] == board[6])
                board[0]
            else if(board[1] == board[4] && board[4] == board[7])
                board[1]
            else if(board[2] == board[5] && board[5] == board[8])
                board[2]
            else
                0
    }

    private fun checkDiagonal(board: List<Int>): Int{
        return if(board[0] == board[4] && board[4] == board[8])
                board[0]
            else if(board[2] == board[4] && board[4] == board[6])
                board[2]
            else
                0
    }

    private fun updateScore(winner: Int, gameData: GameData){
        val newState: MutableMap<String, Any> = HashMap()
        newState[FireStoreConstants.PLAYER1_READY] = false
        newState[FireStoreConstants.PLAYER2_READY] = false
        newState[FireStoreConstants.STATE] = arrayListOf(0,0,0,0,0,0,0,0,0)
        when(winner){
            1 -> newState[FireStoreConstants.PLAYER1_SCORE] = gameData.player1Score.toInt() + 1
            2 -> newState[FireStoreConstants.PLAYER2_SCORE] = gameData.player2Score.toInt() + 1
        }
        firestoreHelper.update(newState)
    }

    fun unsubscribe(){
        compositeDisposable.clear()
        firestoreHelper.unsubscribe()
    }
}
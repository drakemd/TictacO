package edu.upi.cs.drake.tictaco.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import edu.upi.cs.drake.tictaco.common.FireStoreConstants
import edu.upi.cs.drake.tictaco.common.FirestoreHelper
import edu.upi.cs.drake.tictaco.model.GameData
import edu.upi.cs.drake.tictaco.model.GameState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameViewModel @Inject constructor(private val firestoreHelper: FirestoreHelper): ViewModel(){
    private var gameState: MutableLiveData<GameState<GameData>> = MutableLiveData()
    private var compositeDisposable = CompositeDisposable()

    var player = 0

    init {
        gameState.value = GameState.Loading(null)
    }

    fun subscribeGame(): LiveData<GameState<GameData>>{
        val disposable = firestoreHelper.getCurrentDocumentData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    val gameData = it as GameData
                    if(gameData.player1Ready.toBoolean() && gameData.player2Ready.toBoolean()){
                        if(gameData.turn.toInt() == player)
                            gameState.value = GameState.YourTurn(it)
                        else
                            gameState.value = GameState.EnemyTurn(it)
                    }else
                        gameState.value = GameState.Loading(null)
                }, {
                    Log.d("error", "GameViewModel " + it.message)}
                )
        compositeDisposable.add(disposable)
        return gameState
    }

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

    fun playerReady(){
        val newState: MutableMap<String, Any> = HashMap()
        if(player == 1)
            newState[FireStoreConstants.PLAYER1_READY] = true
        else
            newState[FireStoreConstants.PLAYER2_READY] = true
        firestoreHelper.update(newState)
    }

    fun unsubscribe(){
        compositeDisposable.dispose()
        firestoreHelper.unsubscribe()
    }
}
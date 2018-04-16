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

import android.arch.lifecycle.ViewModel
import android.util.Log
import edu.upi.cs.drake.tictaco.firestore.FireStoreConstants
import edu.upi.cs.drake.tictaco.firestore.FirestoreHelper
import edu.upi.cs.drake.tictaco.firestore.UserService
import edu.upi.cs.drake.tictaco.model.GameData
import edu.upi.cs.drake.tictaco.view.mainmenu.PlayerWaitCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainMenuViewModel @Inject constructor(userService: UserService, private val firestoreHelper: FirestoreHelper): ViewModel(){

    private val currentUser = userService.getCurrentUser()
    private val compositeDisposable = CompositeDisposable()

    fun addNewGame(password: String, playerWaitCallback: PlayerWaitCallback){
        val newGame: MutableMap<String, Any> = HashMap()
        val state: List<Int> = arrayListOf(0,0,0,0,0,0,0,0,0)
        currentUser?.let {
            newGame.apply {
                put(FireStoreConstants.GAME_ID, it.uid?: "xxx")
                put(FireStoreConstants.TOTAL_PLAYER, 1)
                put(FireStoreConstants.PLAYER1_NAME, it.displayName?: "xxx")
                put(FireStoreConstants.PLAYER2_NAME, 0)
                put(FireStoreConstants.PLAYER1_SCORE, 0)
                put(FireStoreConstants.PLAYER2_SCORE, 0)
                put(FireStoreConstants.PLAYER1_READY, false)
                put(FireStoreConstants.PLAYER2_READY, false)
                put(FireStoreConstants.STATE, state)
                put(FireStoreConstants.TURN, 1)
                put(FireStoreConstants.NEW_MOVE, -1)
            }
        }
        firestoreHelper.setCurrentDocument(password)
        firestoreHelper.setCurrentDocumentData(newGame)

        val disposable = firestoreHelper.getCurrentDocumentData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    val gameData = it as GameData
                    if(gameData.totalPlayer.toInt() == 2){
                        playerWaitCallback.onWaitFinish(password)
                    }
                },{
                    Log.d("error", "MainMenuViewModel " + it.message)
                })

        compositeDisposable.add(disposable)
    }

    fun joinGame(password: String, playerWaitCallback: PlayerWaitCallback){
        val newData: MutableMap<String, Any> = HashMap()
        newData[FireStoreConstants.TOTAL_PLAYER] = 2
        newData[FireStoreConstants.PLAYER2_NAME] = currentUser?.displayName.toString()

        firestoreHelper.setCurrentDocument(password)
        val disposable = firestoreHelper.getCurrentDocumentData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    val gameData = it as GameData
                    if(gameData.totalPlayer.toInt() < 2){
                        firestoreHelper.update(newData).addOnCompleteListener {
                            playerWaitCallback.onJoinSuccess(password)
                        }
                    }else{
                        playerWaitCallback.onJoinFailed()
                    }
                },{
                    Log.d("error", "MainMenuViewModel " + it.message)
                })

        compositeDisposable.add(disposable)
    }

    fun cancelNewGame(){
        firestoreHelper.delete()
    }

    fun unsubscribe(){
        firestoreHelper.unsubscribe()
        compositeDisposable.clear()
    }
}
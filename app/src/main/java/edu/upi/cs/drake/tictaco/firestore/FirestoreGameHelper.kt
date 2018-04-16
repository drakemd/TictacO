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

package edu.upi.cs.drake.tictaco.firestore

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import edu.upi.cs.drake.tictaco.common.toIntList
import edu.upi.cs.drake.tictaco.model.GameData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * helper class to manage firestore CRUD
 */
class FirestoreGameHelper: FirestoreHelper {
    private val db = FirebaseFirestore.getInstance()
    private var listener: ListenerRegistration? = null
    private var currentDocument = db.document("")

    override fun setCurrentDocument(document: String){
        currentDocument = db.collection(FireStoreConstants.COLLECTION).document(document)
    }

    override fun setCurrentDocumentData(data: Map<String, Any>){
        currentDocument.set(data)
                .addOnSuccessListener { Log.d("Firestore", "success creating game")}
                .addOnFailureListener { Log.d("Firestore", "fail creating game")}
    }

    override fun getCurrentDocumentData(): Observable<GameData>{
        val gameData: PublishSubject<GameData> = PublishSubject.create()
        listener = currentDocument.addSnapshotListener{
            documentSnapshot, firebaseFirestoreException ->
            try {
                val data: Map<String, Any> = documentSnapshot.data
                val newGameData = GameData(
                        gameId = data[FireStoreConstants.GAME_ID].toString(),
                        totalPlayer = data[FireStoreConstants.TOTAL_PLAYER].toString(),
                        player1Name = data[FireStoreConstants.PLAYER1_NAME].toString(),
                        player2Name = data[FireStoreConstants.PLAYER2_NAME].toString(),
                        player1Score = data[FireStoreConstants.PLAYER1_SCORE].toString(),
                        player2Score = data[FireStoreConstants.PLAYER2_SCORE].toString(),
                        player1Ready = data[FireStoreConstants.PLAYER1_READY].toString(),
                        player2Ready = data[FireStoreConstants.PLAYER2_READY].toString(),
                        state = data[FireStoreConstants.STATE].toString().toIntList(),
                        turn = data[FireStoreConstants.TURN].toString(),
                        newMove = data[FireStoreConstants.NEW_MOVE].toString().toInt()
                )
                gameData.onNext(newGameData)
            }catch (ex: Exception){
                gameData.onError(Throwable(ex.message))
            }
        }
        return gameData
    }

    override fun update(newState: Map<String, Any>): Task<Void>{
         return currentDocument.update(newState)
    }

    override fun delete(){
        currentDocument.delete()
    }

    override fun unsubscribe(){
        listener?.remove()
    }
}
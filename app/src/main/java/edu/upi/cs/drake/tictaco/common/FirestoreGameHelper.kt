package edu.upi.cs.drake.tictaco.common

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import edu.upi.cs.drake.tictaco.model.GameData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class FirestoreGameHelper: FirestoreHelper{
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
                gameData.onError(
                        Throwable(message =
                            if(firebaseFirestoreException.message != null)
                                firebaseFirestoreException.message
                            else
                                ex.message
                        )
                )
            }
        }
        return gameData
    }

    override fun update(newState: Map<String, Any>): Task<Void>{
         return currentDocument.update(newState)
    }

    override fun unsubscribe(){
        listener?.remove()
    }
}
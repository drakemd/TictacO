package edu.upi.cs.drake.tictaco.common

import com.google.android.gms.tasks.Task
import io.reactivex.Observable

interface FirestoreHelper {
    fun setCurrentDocument(document: String)
    fun setCurrentDocumentData(data: Map<String, Any>)
    fun getCurrentDocumentData(): Observable<*>
    fun update(newState: Map<String, Any>): Task<Void>
    fun unsubscribe()
}
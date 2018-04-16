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

import com.google.android.gms.tasks.Task
import io.reactivex.Observable

interface FirestoreHelper {
    fun setCurrentDocument(document: String)
    fun setCurrentDocumentData(data: Map<String, Any>)
    fun getCurrentDocumentData(): Observable<*>
    fun update(newState: Map<String, Any>): Task<Void>
    fun delete()
    fun unsubscribe()
}
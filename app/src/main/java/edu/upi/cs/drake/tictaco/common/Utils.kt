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

package edu.upi.cs.drake.tictaco.common

object Utils{
    //convert state from firestore (0,1,2) to real board state (X,O)
    fun getRealState(listIn: List<Int>): List<String>{
        val listOut: MutableList<String> = arrayListOf()
        listIn.forEach {
            when(it){
                0 -> listOut.add("")
                1 -> listOut.add("X")
                2 -> listOut.add("O")
            }
        }
        return listOut
    }
}
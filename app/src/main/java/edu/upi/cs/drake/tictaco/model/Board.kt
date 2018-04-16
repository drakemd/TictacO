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

package edu.upi .cs.drake.tictaco.model

/**
 * this class represents the board's current state
 */
class Board(list: List<String>?){
    var cell00: String = list?.get(0) ?:""
    var cell10: String = list?.get(1) ?:""
    var cell20: String = list?.get(2) ?:""
    var cell01: String = list?.get(3) ?:""
    var cell11: String = list?.get(4) ?:""
    var cell21: String = list?.get(5) ?:""
    var cell02: String = list?.get(6) ?:""
    var cell12: String = list?.get(7) ?:""
    var cell22: String = list?.get(8) ?:""
}
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

package edu.upi.cs.drake.tictaco.model

/**
 * this class hold game state and game data from firestore
 */
sealed class GameState<out T>{
    abstract val data: T?

    data class Loading<out T>(override val data: T?): GameState<T>()
    data class YourTurn<out T>(override val data: T?): GameState<T>()
    data class EnemyTurn<out T>(override val data: T?): GameState<T>()
    data class Cancelled<out T>(override val data: T?): GameState<T>()
    data class Win<out T>(override val data: T?): GameState<T>()
    data class Lose<out T>(override val data: T?): GameState<T>()
}
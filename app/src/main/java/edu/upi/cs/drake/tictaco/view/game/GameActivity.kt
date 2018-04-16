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

package edu.upi.cs.drake.tictaco.view.game

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.TextView
import dagger.android.AndroidInjection
import edu.upi.cs.drake.tictaco.R
import edu.upi.cs.drake.tictaco.common.Utils
import edu.upi.cs.drake.tictaco.databinding.ActivityGameBinding
import edu.upi.cs.drake.tictaco.model.Board
import edu.upi.cs.drake.tictaco.model.GameState
import edu.upi.cs.drake.tictaco.viewmodel.GameViewModel
import edu.upi.cs.drake.tictaco.viewmodel.ViewModelFactory
import edu.upi.cs.drake.tictaco.common.make
import javax.inject.Inject

/**
 * this activity is where the real game takes place
 * it display score and game board from firestore in real time
 */
class GameActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: ActivityGameBinding
    private lateinit var gameViewModel: GameViewModel

    //list to hold all state of the game's board 3x3 = 9
    private val cell: MutableList<TextView> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?){
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)
        initCell()
        gameViewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        gameViewModel.player = intent.extras.getInt(PLAYER)
        gameViewModel.playerReady()
        gameViewModel.subscribeGame().observe(this, Observer{
            it?.let {
                binding.gamedata = it.data
                binding.state = Board(it.data?.state?.let {it1 -> Utils.getRealState(it1)})
                when(it){
                    is GameState.Cancelled -> onCancelGame()
                    is GameState.Loading -> onLoading()
                    is GameState.YourTurn -> onYourTurn(it.data?.state)
                    is GameState.EnemyTurn -> onEnemyTurn()
                    is GameState.Win -> onWin()
                    is GameState.Lose -> onLose()
                }
            }
        })
    }

    private fun initCell(){
        cell.clear()
        cell.add(binding.board.cell00)
        cell.add(binding.board.cell10)
        cell.add(binding.board.cell20)
        cell.add(binding.board.cell01)
        cell.add(binding.board.cell11)
        cell.add(binding.board.cell21)
        cell.add(binding.board.cell02)
        cell.add(binding.board.cell12)
        cell.add(binding.board.cell22)
    }

    private fun setClickListener(list: List<Int>?){
        if(list != null){
            for(index in 0..8){
                if(list[index] == 0){
                    cell[index].setOnClickListener {
                        (it as TextView).text = gameViewModel.move(index)
                        removeOnClickListener()
                    }
                }
            }
        }
    }

    private fun removeOnClickListener(){
        cell.forEach {
            if(it.hasOnClickListeners()){
                it.setOnClickListener(null)
            }
        }
    }

    private fun onCancelGame(){
        val callback = object: DialogCallback{
            override fun onClickOk() {
                gameViewModel.quitGame()
                finish()
            }
            override fun onCancel(dialog: DialogInterface){}
        }
        AlertDialog.Builder(this).make("Your Opponent Quits", "OK", callback, false)
    }

    private fun onLoading(){
        Log.d("status", "Loading")
        binding.gameTurn.text = getString(R.string.game_wait_player)
    }

    private fun onYourTurn(board: List<Int>?){
        Log.d("status", "YourTurn")
        setClickListener(board)
        binding.gameTurn.text = getString(R.string.game_your_turn)
    }

    private fun onEnemyTurn(){
        binding.gameTurn.text = getString(R.string.game_enemy_turn)
        Log.d("status", "EnemyTurn")
    }

    private fun onWin(){
        val callback = object: DialogCallback{
            override fun onClickOk(){
                gameViewModel.playAgain()
            }
            override fun onCancel(dialog: DialogInterface){
                gameViewModel.cancelGame()
                finish()
                dialog.dismiss()
            }
        }
        AlertDialog.Builder(this).make("You Win! Play Again?", "Yes", callback, true)
    }

    private fun onLose(){
        val callback = object: DialogCallback{
            override fun onClickOk(){
                gameViewModel.playAgain()
            }
            override fun onCancel(dialog: DialogInterface){
                gameViewModel.cancelGame()
                finish()
                dialog.dismiss()
            }
        }
        AlertDialog.Builder(this).make("You Lose! Play Again?", "Yes", callback, true)
    }

    override fun onBackPressed(){
        val callback = object: DialogCallback{
            override fun onClickOk(){
                gameViewModel.cancelGame()
                finish()
            }
            override fun onCancel(dialog: DialogInterface) {
                dialog.dismiss()
            }
        }
        AlertDialog.Builder(this).make("Quit the game?", "Yes", callback, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        gameViewModel.unsubscribe()
    }

    companion object {
        private const val PLAYER = "PLAYER"
        private const val PASSWORD = "PASSWORD"

        fun newIntent(context: Context, player: Int, password: String): Intent{
            val intent = Intent(context, GameActivity::class.java)
            intent.putExtra(PLAYER, player)
            intent.putExtra(PASSWORD, password)
            return intent
        }
    }
}

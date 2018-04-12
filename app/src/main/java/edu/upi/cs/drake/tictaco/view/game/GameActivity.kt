package edu.upi.cs.drake.tictaco.view.game

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
import javax.inject.Inject

class GameActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: ActivityGameBinding
    private lateinit var gameViewModel: GameViewModel
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
                    is GameState.Loading -> {
                        Log.d("status", "Loading")
                        binding.gameTurn.text = "Waiting another player"
                    }
                    is GameState.YourTurn -> {
                        Log.d("status", "YourTurn")
                        setClickListener(it.data?.state)
                        binding.gameTurn.text = "Your Turn"
                    }
                    is GameState.EnemyTurn -> {
                        binding.gameTurn.text = "Enemy Turn"
                        Log.d("status", "EnemyTurn")
                    }
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

    override fun onDestroy() {
        super.onDestroy()
        gameViewModel.unsubscribe()
    }

    companion object {
        private const val PLAYER = "PLAYER"
        private const val PASSWORD = "PASSWORD"

        fun newInstent(context: Context, player: Int, password: String): Intent{
            val intent = Intent(context, GameActivity::class.java)
            intent.putExtra(PLAYER, player)
            intent.putExtra(PASSWORD, password)
            return intent
        }
    }
}

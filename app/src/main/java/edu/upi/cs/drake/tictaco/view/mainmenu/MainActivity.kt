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

package edu.upi.cs.drake.tictaco.view.mainmenu

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import dagger.android.AndroidInjection
import edu.upi.cs.drake.tictaco.R
import edu.upi.cs.drake.tictaco.databinding.ActivityMainBinding
import edu.upi.cs.drake.tictaco.view.game.GameActivity
import edu.upi.cs.drake.tictaco.viewmodel.MainMenuViewModel
import edu.upi.cs.drake.tictaco.viewmodel.ViewModelFactory
import javax.inject.Inject

/**
 * this activity contains main menu of the app
 * the menus are new game and join game
 * new game creates new game in the firestore
 * join game join the game already created in firestore
 */
class MainActivity: AppCompatActivity(), OnOkCallback, PlayerWaitCallback{
    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainMenuViewModel: MainMenuViewModel
    //loading dialog
    private var waitPlayerDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?){
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainMenuViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainMenuViewModel::class.java)

        binding.btnNewGame.setOnClickListener{
            showAlertDialog("Create New Game", this, "new_game")
        }
        binding.btnJoinGame.setOnClickListener{
            showAlertDialog("Join a Game", this, "join_game")
        }
    }

    override fun newGame(password: String){
        mainMenuViewModel.addNewGame(password, this)
        showLoadingDialog(1) //mode 1 new game
    }

    override fun joinGame(password: String){
        mainMenuViewModel.joinGame(password, this)
        showLoadingDialog(2) //mode 2 join game
    }

    override fun onWaitFinish(password: String) {
        waitPlayerDialog?.dismiss()
        startActivity(GameActivity.newIntent(this, 1, password))
        mainMenuViewModel.unsubscribe()
    }

    override fun onJoinSuccess(password: String){
        Log.d("success", "success")
        waitPlayerDialog?.dismiss()
        startActivity(GameActivity.newIntent(this, 2, password))
        mainMenuViewModel.unsubscribe()
    }

    override fun onJoinFailed() {
        Log.d("MainActivity", "failed to join")
    }

    private fun showLoadingDialog(mode: Int){
        waitPlayerDialog = AlertDialog.Builder(this)
                .setView(R.layout.progress_layout)
                .setCancelable(false)
                .setNegativeButton("Cancel", {
                    dialog, _ ->
                    if(mode == 1) mainMenuViewModel.cancelNewGame()
                    dialog.dismiss()
                })
                .show()
    }

    @SuppressLint("InflateParams")
    private fun showAlertDialog(title: String, onOkCallback: OnOkCallback, mode: String): AlertDialog{
        val layoutInflater: LayoutInflater = layoutInflater
        val dialogView = layoutInflater.inflate(R.layout.dialog, null)
        val password: EditText = dialogView.findViewById(R.id.et_game_name)

        return AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle(title)
                .setPositiveButton("OK", {
                    dialog, _ ->
                    when(mode){
                        "new_game" -> onOkCallback.newGame(password.text.toString())
                        "join_game" -> onOkCallback.joinGame(password.text.toString())
                    }
                    dialog.dismiss()
                })
                .setNegativeButton("Cancel", {
                    dialog, _ ->
                    dialog.dismiss()
                }).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainMenuViewModel.unsubscribe()
    }

    companion object{
        fun newInstance(context: Context) = Intent(context, MainActivity::class.java)
    }
}

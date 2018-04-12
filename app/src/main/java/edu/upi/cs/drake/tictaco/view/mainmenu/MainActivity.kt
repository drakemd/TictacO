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

class MainActivity: AppCompatActivity(), OnOkCallback, PlayerWaitCallback{
    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainMenuViewModel: MainMenuViewModel
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
        showLoadingDialog()
    }

    override fun joinGame(password: String){
        mainMenuViewModel.joinGame(password, this)
        showLoadingDialog()
    }

    override fun onWaitFinish(password: String) {
        waitPlayerDialog?.dismiss()
        startActivity(GameActivity.newInstent(this, 1, password))
        mainMenuViewModel.unsubscribe()
    }

    override fun onJoinSuccess(password: String){
        Log.d("success", "success")
        waitPlayerDialog?.dismiss()
        startActivity(GameActivity.newInstent(this, 2, password))
        mainMenuViewModel.unsubscribe()
    }

    override fun onJoinFailed() {
        Log.d("MainActivity", "failed to join")
    }

    private fun showLoadingDialog(){
        waitPlayerDialog = AlertDialog.Builder(this)
                .setView(R.layout.progress_layout)
                .setCancelable(false)
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

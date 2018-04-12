package edu.upi.cs.drake.tictaco.view.mainmenu

interface OnOkCallback {
    fun newGame(password: String)
    fun joinGame(password: String)
}
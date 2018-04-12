package edu.upi.cs.drake.tictaco.view.mainmenu

interface PlayerWaitCallback{
    fun onWaitFinish(password: String)
    fun onJoinSuccess(password: String)
    fun onJoinFailed()
}
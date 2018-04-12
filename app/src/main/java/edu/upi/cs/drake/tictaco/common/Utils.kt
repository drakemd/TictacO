package edu.upi.cs.drake.tictaco.common

object Utils{
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
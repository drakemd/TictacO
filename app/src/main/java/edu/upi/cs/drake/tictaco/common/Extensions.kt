package edu.upi.cs.drake.tictaco.common

fun String.toIntList(): List<Int> {
    var string = removeSurrounding("[", "]")
    string = string.replace("\\s".toRegex(), "")
    val splitVal = string.split(",")
    val result: MutableList<Int> = arrayListOf()
    splitVal.forEach{
        result.add(it.toInt())
    }
    return result
}
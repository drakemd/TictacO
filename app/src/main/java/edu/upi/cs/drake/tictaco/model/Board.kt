package edu.upi .cs.drake.tictaco.model

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
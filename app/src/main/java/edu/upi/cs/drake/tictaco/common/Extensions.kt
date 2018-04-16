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

package edu.upi.cs.drake.tictaco.common

import android.support.v7.app.AlertDialog
import edu.upi.cs.drake.tictaco.view.game.DialogCallback


//conver string with format "[xx, xx, xx, xx]" to list of Int
fun String.toIntList(): List<Int> {
    //remove square bracket
    var string = removeSurrounding("[", "]")
    //remove all space
    string = string.replace("\\s".toRegex(), "")
    val splitVal = string.split(",")
    val result: MutableList<Int> = arrayListOf()
    splitVal.forEach{
        result.add(it.toInt())
    }
    return result
}

//simplify alert dialog builder
fun AlertDialog.Builder.make(title: String, positiveButton: String?, callback: DialogCallback, cancelable: Boolean){
    setTitle(title)
    setCancelable(false)

    if(cancelable){
        setNegativeButton("Cancel", {
            dialog, _ ->
            callback.onCancel(dialog)
        })
    }

    if(positiveButton != null){
        setPositiveButton(positiveButton, {
            dialog, _ ->
            callback.onClickOk()
            dialog.dismiss()
        })
    }
    show()
}
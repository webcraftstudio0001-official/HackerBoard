package com.hackerboard.keyboard
 
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
 
enum class PanelMode { NONE, SYMBOL, CLIPBOARD, MACRO, SNIPPET, HACKER, AI }
 
class KeyboardState {
    var shiftOn     by mutableStateOf(false)
    var capsLock    by mutableStateOf(false)
    var ctrlHeld    by mutableStateOf(false)
    var altHeld     by mutableStateOf(false)
    var panelMode   by mutableStateOf(PanelMode.NONE)
    var layout      by mutableStateOf(LayoutId.QWERTY)
    var suggestions by mutableStateOf(emptyList<String>())
    var composing   by mutableStateOf("")
 
    fun toggleShift() {
        if (shiftOn && !capsLock) { capsLock = true }
        else if (capsLock)       { capsLock = false; shiftOn = false }
        else                     { shiftOn = true }
    }
 
    fun isUpperCase(): Boolean = shiftOn || capsLock
 
    fun onCharCommitted() {
        if (shiftOn && !capsLock) shiftOn = false
    }
 
    fun resetModifiers() {
        ctrlHeld = false
        altHeld  = false
    }
}
package com.example.kotlintexttospeech

import android.os.Build
import android.speech.tts.TextToSpeech
import android.widget.EditText
import android.widget.SeekBar
import androidx.annotation.RequiresApi

class AndroidTextToSpeech {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun speak(editText : EditText, seekBarPitch : SeekBar, seekBarSpeed : SeekBar, tts : TextToSpeech) {
        val text = editText.text.toString()
        var pitch = (seekBarPitch.progress.div(50).toFloat());
        var speed = (seekBarSpeed.progress.div(50).toFloat());
        if(pitch < 0.1){
            pitch = 0.1f
        }
        if(speed < 0.1){
            speed = 0.1f
        }
        tts.setPitch(pitch)
        tts.setSpeechRate(speed)
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")

    }
}
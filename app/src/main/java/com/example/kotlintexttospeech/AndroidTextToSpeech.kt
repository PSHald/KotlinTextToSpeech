package com.example.kotlintexttospeech

import android.graphics.Color
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi


class AndroidTextToSpeech{

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun speak(editText : TextView, seekBarPitch : SeekBar, seekBarSpeed : SeekBar, tts : TextToSpeech) {
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
//        tts.setOnUtteranceProgressListener(speechListener)
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

}
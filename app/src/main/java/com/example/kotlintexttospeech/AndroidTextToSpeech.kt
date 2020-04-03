package com.example.kotlintexttospeech

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import org.w3c.dom.Text
import kotlin.concurrent.thread


class AndroidTextToSpeech(editText: TextView, seekBarPitch: SeekBar, seekBarSpeed: SeekBar, tts: TextToSpeech, activity: Activity){

    var editText : TextView = editText
    var seekBarPitch : SeekBar = seekBarPitch
    var seekBarSpeed : SeekBar = seekBarSpeed
    var tts : TextToSpeech = tts
    var text = editText.text.toString()
    var passedText : String = ""

    private var sentenceList : List<String> = ArrayList()
    var counter = 0
    private var speechListener = object : UtteranceProgressListener(){
        var end : Int = 0
        override fun onDone(utteranceId: String?) {
            Log.i("XXX", "utterance done");
        }

        override fun onError(utteranceId: String?) {
            Log.i("XXX", "utterance error");
        }

        override fun onStart(utteranceId: String?) {
            Log.i("XXX", "utterance started");
        }

        override fun onRangeStart(utteranceId: String?, start: Int, end: Int, frame: Int) {
            Log.i(
                "XXX", "onRangeStart() ... utteranceId: " + utteranceId.toString() + ", start: " + start
                    .toString() + ", end: " + end.toString() + ", frame: " + frame
            )
            // onRangeStart (and all UtteranceProgressListener callbacks) do not run on main thread
            // ... so we explicitly manipulate views on the main thread:

            activity.runOnUiThread(Runnable {
                val textWithHighlights: Spannable = SpannableString(text)
                textWithHighlights.setSpan(
                    BackgroundColorSpan(Color.YELLOW),
                    counter + start,
                    counter + end,
                    Spanned.SPAN_COMPOSING
                    //Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
                this.end = end
                editText?.text =  passedText + textWithHighlights
            })
        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun start() {

        sentenceList = text.split(".")
        passedText = text.subSequence(passedText.length, counter).toString()
        text = text.substring(counter)
        tts?.setOnUtteranceProgressListener(speechListener)

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
        speak();

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun speak(){
        if(editText.text.length <= counter){
            counter = 0;
            text = editText.text.toString()
        }
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")


    }

    fun pause(){
        tts.stop()
        counter = speechListener.end
    }
}
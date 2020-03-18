package com.example.kotlintexttospeech

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.example.androidtexttospeechtest.R
import java.util.*

class MainActivity : FragmentActivity(),
    TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var editText: EditText? = null
    private var seekBarPitch: SeekBar? = null
    private var seekBarSpeed: SeekBar? = null
    private var button: Button? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText = findViewById(R.id.edit_text)
        seekBarPitch = findViewById(R.id.seek_bar_pitch)
        seekBarSpeed = findViewById(R.id.seek_bar_speed)
        button = findViewById(R.id.button_speak)
        tts = TextToSpeech(this, this)

        button?.setOnClickListener(View.OnClickListener  {
                var aTTS = AndroidTextToSpeech()
                    aTTS.speak(editText!!, seekBarPitch!!, seekBarSpeed!!, tts!!)
                }
        )
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS) {
            var result = findLanguage();
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported")
            } else {
                button?.isEnabled = true
            }
        }else{
            Log.e("TTS", "Initialisation failed")
        }
    }
    public override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    fun findLanguage() : Int?{
        var Sprog : Int? = tts?.isLanguageAvailable(Locale("da", "DK"))
        if(Sprog != TextToSpeech.LANG_MISSING_DATA){
            tts?.language = Locale("da", "DK")
        } else{
            tts?.language = Locale.ENGLISH
        }
        return Sprog
    }
}
package com.example.kotlintexttospeech

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.example.androidtexttospeechtest.R
import java.util.*

class MainActivity : FragmentActivity(),
    TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var seekBarPitch: SeekBar? = null
    private var seekBarSpeed: SeekBar? = null
    private var button: Button? = null
    private var textView : TextView? = null
    private var text : String = "Der var så dejligt ude på landet; det var sommer, kornet stod gult, havren grøn, " +
            "høet var rejst i stakke nede i de grønne enge, og der gik storken på sine lange, røde ben og snakkede ægyptisk, " +
            "for det sprog havde han lært af sin moder. Rundt om ager og eng var der store skove, og midt i skovene dybe søer," +
            "jo, der var rigtignok dejligt derude på landet! Midt i solskinnet lå der en gammel herregård med dybe kanaler rundt om, " +
            "og fra muren og ned til vandet voksede store skræppeblade, der var så høje, at små børn kunne stå oprejste under de største; " +
            "der var lige så vildsomt derinde, som i den tykkeste skov, og her lå en and på sin rede; hun skulle ruge sine små ællinger ud, " +
            "men nu var hun næsten ked af det, fordi det varede så længe, og hun sjælden fik visit; de andre ænder holdt mere af at svømme om i kanalerne, " +
            "end at løbe op og sidde under et skræppeblad for at snadre med hende."

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.TextView)
        textView?.setText(text)
        seekBarPitch = findViewById(R.id.seek_bar_pitch)
        seekBarSpeed = findViewById(R.id.seek_bar_speed)
        button = findViewById(R.id.button_speak)
        tts = TextToSpeech(this, this)

        var speechListener = object : UtteranceProgressListener(){
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
                runOnUiThread(Runnable {
                    val textWithHighlights: Spannable = SpannableString(text)
                    textWithHighlights.setSpan(
                        BackgroundColorSpan(Color.YELLOW),
                        start,
                        end,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE
                    )
                    textView?.text = textWithHighlights
                })
            }
        }
        tts?.setOnUtteranceProgressListener(speechListener)


        button?.setOnClickListener(View.OnClickListener  {
                var aTTS = AndroidTextToSpeech()
                    aTTS.speak(textView!!, seekBarPitch!!, seekBarSpeed!!, tts!!)
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

    private fun findLanguage() : Int?{
        var language : Int? = tts?.isLanguageAvailable(Locale("da", "DK"))
        if(language != TextToSpeech.LANG_MISSING_DATA){
            tts?.language = Locale("da", "DK")
        } else{
            tts?.language = Locale.ENGLISH
        }
        return language
    }



}
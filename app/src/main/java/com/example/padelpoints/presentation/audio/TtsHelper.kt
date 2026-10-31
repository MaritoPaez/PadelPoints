package com.example.padelpoints.presentation.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TtsHelper(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isReady = false
    var isEnabled = true

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val defaultLocale = Locale.getDefault()
            val targetLocale = if (defaultLocale.language == "es") defaultLocale else Locale.forLanguageTag("es-ES")
            val result = tts?.setLanguage(targetLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Intento fallback con español genérico
                val genericResult = tts?.setLanguage(Locale.forLanguageTag("es"))
                if (genericResult == TextToSpeech.LANG_MISSING_DATA || genericResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TtsHelper", "El idioma español no está soportado en este dispositivo.")
                    isReady = false
                    return
                }
            }
            tts?.setSpeechRate(0.95f)
            tts?.setPitch(1.0f)
            isReady = true
        } else {
            Log.e("TtsHelper", "Error al inicializar TextToSpeech. Estado: $status")
            isReady = false
        }
    }

    fun speak(text: String) {
        if (!isEnabled || !isReady || text.isBlank()) return
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "PadelPointsTTS")
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isReady = false
    }
}

package com.example.padelpoints.presentation.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import java.io.File

class SoundManager(private val context: Context) {

    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(2)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val soundMap = HashMap<String, Int>()
    private val ttsHelper = TtsHelper(context)

    var isEnabled: Boolean
        get() = ttsHelper.isEnabled
        set(value) {
            ttsHelper.isEnabled = value
        }

    init {
        loadRawSounds()
    }

    private fun loadRawSounds() {
        // Intenta cargar audios pregrabados desde res/raw/ si existen
        val soundKeys = listOf(
            "por_el_saque", "saca_equipo_1", "saca_equipo_2",
            "puntos_0_0", "puntos_0_15", "puntos_0_30", "puntos_0_40",
            "puntos_15_0", "puntos_15_15", "puntos_15_30", "puntos_15_40",
            "puntos_30_0", "puntos_30_15", "puntos_30_30", "puntos_30_40",
            "puntos_40_0", "puntos_40_15", "puntos_40_30",
            "punto_de_oro", "juego_equipo_1", "juego_equipo_2",
            "partido_equipo_1", "partido_equipo_2", "punto_anulado",
            "partido_reiniciado", "a_3", "a_4", "a_5", "a_6", "sonido_activado"
        )

        for (key in soundKeys) {
            val resId = context.resources.getIdentifier(key, "raw", context.packageName)
            if (resId != 0) {
                try {
                    val soundId = soundPool.load(context, resId, 1)
                    soundMap[key] = soundId
                } catch (e: Exception) {
                    Log.e("SoundManager", "Error cargando sonido raw $key", e)
                }
            }
        }
    }

    fun playSoundOrSpeak(soundKey: String, fallbackText: String) {
        if (!isEnabled) return

        val soundId = soundMap[soundKey]
        if (soundId != null && soundId != 0) {
            // Reproducción instantánea con SoundPool (cero latencia)
            soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
        } else {
            // Fallback directo a TextToSpeech
            ttsHelper.speak(fallbackText)
        }
    }

    fun stop() {
        soundPool.autoPause()
        ttsHelper.stop()
    }

    fun shutdown() {
        soundPool.release()
        ttsHelper.shutdown()
    }
}

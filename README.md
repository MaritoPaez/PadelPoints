# PadelPoints 🎾⌚

**PadelPoints** es una aplicación nativa para **Wear OS** (diseñada y optimizada para **Samsung Galaxy Watch7** y relojes inteligentes con pantalla circular) que permite llevar la puntuación de partidos de pádel de forma sencilla, veloz y con síntesis de voz en tiempo real.

---

## 🚀 Características Principales

* 👁️ **Diseño de Alto Contraste 50/50**:
  - **Equipo 1**: Números amarillos sobre fondo negro (`#FFE500` sobre `#000000`).
  - **Equipo 2**: Números negros sobre fondo amarillo (`#000000` sobre `#FFE500`).
  - Números gigantes en pantalla central para una lectura inmediata sin esfuerzo visual (ideal para presbicia o lectura rápida durante el juego).
* 🎾 **Lógica de Pádel Oficial**:
  - **Modo "POR EL SAQUE"**: Al iniciar o reiniciar, el primer toque asigna qué equipo empieza sacando.
  - **Canto con Prioridad de Servicio**: Los puntos se muestran y cantan nombrando **siempre primero el puntaje del equipo que tiene el saque**.
  - **Punto de Oro**: Detección automática al empatar en 40-40, mostrando la palabra **ORO** en ambas mitades.
  - **Rotación de Saque**: El servicio se alterna automáticamente al finalizar cada juego.
  - **Juegos Configurables**: Meta de juegos por set ajustable a **3, 4, 5 o 6 juegos** (4 por defecto).
* 🔊 **Anuncio por Voz (Motor Híbrido SoundPool & TTS)**:
  - Canta cada punto al instante (*"Cero - Quince"*, *"Treinta iguales"*, *"Punto de Oro"*, *"Juego Equipo 1"*).
  - Incluye reproducción sin latencia (0 ms) mediante archivos de audio pregrabados en `app/src/main/res/raw/` y respaldo automático por TextToSpeech en español.
* 🖐️ **Gestos Táctiles Intuitivos**:
  - **Toque corto (Tap)**: Suma un punto al equipo presionado.
  - **Toque prolongado (Long Press)**: Resta / deshace el punto del equipo para corregir toques accidentales en segundos.
* 💡 **Pantalla Siempre Activa (`FLAG_KEEP_SCREEN_ON`)**:
  - La pantalla no se apaga ni se suspende durante el partido, manteniendo el marcador siempre visible hasta que cierres la app.
* 🔋 **Ultra Eficiente en Batería (AMOLED)**:
  - Aprovecha los paneles Super AMOLED apoderando el 50% de la pantalla en negro puro (`#000000`), manteniendo los píxeles apagados con un consumo de solo 5-8% de batería por hora de juego.
* 🕒 **Reloj Analógico Integrado**:
  - Esfera analógica minimalista en la parte superior con agujas blancas sobre fondo negro para consultar la hora de un vistazo.

---

## 🛠️ Requisitos de Compilación e Instalación

* **Android Studio**: Ladybug / 2024.2.1 o superior.
* **JDK**: 11 o superior.
* **Wear OS**: Compatible con Wear OS 3, 4 y 5 (minSdk 36, targetSdk 37).
* **Dispositivo**: Samsung Galaxy Watch7 / Galaxy Watch4, 5, 6, Pixel Watch u otros smartwatches Wear OS.

---

## 📲 Cómo Instalar en tu Reloj Físico (Wi-Fi ADB Debugging)

1. **Activar Opciones de Desarrollador en el Reloj**:
   - Ve a `Ajustes` -> `Acerca del reloj` -> `Información de software`.
   - Toca 7 veces seguidas en **Versión de software** hasta activar las Opciones de desarrollador.
   - Vuelve a `Ajustes` -> `Opciones de desarrollador`.
   - Activa **Depuración por ADB** y **Depuración por Wi-Fi / Inalámbrica**.
2. **Emparejar con Android Studio**:
   - Asegúrate de que el reloj y la PC estén en la **misma red Wi-Fi**.
   - En el reloj, selecciona **Emparejar nuevo dispositivo**.
   - En Android Studio, abre el desplegable de dispositivos -> `Pair Devices Using Wi-Fi` e ingresa la IP, puerto y código de 6 dígitos.
3. **Desplegar e Instalar**:
   - Selecciona tu reloj en la barra superior de Android Studio y haz clic en **Run (▶)** (`Shift + F10`).

---

## 📁 Archivos de Voz Humana (`app/src/main/res/raw/`)

La aplicación incluye los 30 archivos de audio `.wav` pregrabados para todos los marcadores y eventos en español:
* `por_el_saque.wav`, `saca_equipo_1.wav`, `saca_equipo_2.wav`
* `puntos_0_0.wav` a `puntos_40_30.wav` (todos los estados del juego)
* `punto_de_oro.wav`, `juego_equipo_1.wav`, `juego_equipo_2.wav`, `partido_equipo_1.wav`, `partido_equipo_2.wav`
* `punto_anulado.wav`, `partido_reiniciado.wav`, `a_3.wav`, `a_4.wav`, `a_5.wav`, `a_6.wav`

*Puedes reemplazar estos archivos con tus propios audios grabados o creados con IA (estilo ElevenLabs o ChatGPT) conservando los mismos nombres.*

---

## 📄 Licencia

Este proyecto es de código abierto bajo la licencia **MIT**. ¡Siéntete libre de clonarlo, mejorarlo y compartirlo con la comunidad de pádel y desarrolladores Android!

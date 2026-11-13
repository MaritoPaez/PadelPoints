# PadelPoints 🎾⌚ (v1.3.1)

**PadelPoints** es una aplicación nativa para **Wear OS** (diseñada y optimizada para **Samsung Galaxy Watch7 en adelante** y relojes inteligentes con pantalla circular) que permite llevar la puntuación de partidos de pádel (o tenis) de forma sencilla, veloz y con síntesis de voz e interfaz adaptada para lectura rápida en la cancha.

---

## 🚀 Novedades de la Versión 1.3.1 (v1.3.1)

* 🎨 **Rediseño de Círculos de Juegos (Máximo Contraste y Legibilidad)**:
  - **Ubicación Optimizada**: Círculos de juegos desplazados más abajo y hacia el centro de la pantalla, aprovechando el ancho de la curvatura inferior del smartwatch.
  - **Diámetro Ampliado**: Aumento de tamaño de 48dp a **54dp**, con números más grandes en negrita máxima (**34sp**).
  - **Contraste 100%**: Fondo negro sólido con borde e interior en amarillo brillante (`#FFE500`) para ambos equipos, garantizando lectura nítida e inmediata bajo luz solar directa.
* 🎾 **Modo Tiebreak Oficial**:
  - Activación/desactivación de Tiebreak desde el menú de configuración.
  - Rotación oficial de saque en Tiebreak (primer saque de 1 punto, luego rotación cada 2 puntos).
  - Opción de **"Muere en 7"**: Finalización del Tiebreak al llegar a 7 puntos directos sin diferencia de 2.
* 🔊 **Audios de Tiebreak Unificados**:
  - Audios pregrabados en español para todas las combinaciones de Tiebreak hasta 15 puntos por lado (`tiebreak_puntos_0_0` a `tiebreak_puntos_15_15`).
  - Pronunciación natural *"Taibrek activado/desactivado"*.
* 🕒 **Reloj Analógico Minimalista**:
  - Esfera analógica negra con agujas blancas para consultar la hora de un vistazo sin depender de números pequeños.

---

## 🌟 Características Principales

* 👁️ **Diseño de Alto Contraste 50/50**:
  - **Equipo 1**: Números amarillos sobre fondo negro (`#FFE500` sobre `#000000`).
  - **Equipo 2**: Números negros sobre fondo amarillo (`#000000` sobre `#FFE500`).
  - Marcador en fuente gigante alineada y sin solapamientos.
* 🎾 **Lógica de Pádel Oficial**:
  - **Modo "POR EL SAQUE"**: Asignación inicial de servicio.
  - **Canto con Prioridad de Servicio**: El marcador y audio nombran siempre primero el puntaje del equipo que tiene el saque.
  - **Punto de Oro**: Detección automática en 40-40, mostrando la palabra **ORO**.
  - **Rotación Automática de Saque**: Cambio automático de servicio al ganar cada juego.
  - **Juegos Configurables**: Meta por set de **3, 4, 5 o 6 juegos** (4 por defecto).
* 🔊 **Motor de Audio Híbrido (SoundPool & TTS)**:
  - Canta los puntos al instante (0 ms de latencia) mediante los archivos de audio `.wav` incluidos en `app/src/main/res/raw/`.
* 🖐️ **Gestos Táctiles Intuitivos**:
  - **Toque corto (Tap)**: Suma punto al equipo presionado.
  - **Toque prolongado (Long Press)**: Resta / deshace el punto para corregir errores.
* 💡 **Pantalla Siempre Activa (`FLAG_KEEP_SCREEN_ON`)**:
  - La pantalla no se apaga durante el partido hasta cerrar la app.
* 🔋 **Ultra Eficiente en Batería (AMOLED)**:
  - Apagado de píxeles en el 50% de la pantalla (negro puro) con consumo de solo 5-8% de batería por hora de juego.

---

## 🛠️ Requisitos de Compilación e Instalación

* **Android Studio**: Ladybug / 2024.2.1 o superior.
* **JDK**: 11 o superior.
* **Wear OS**: Compatible con Wear OS 3, 4 y 5 (minSdk 36, targetSdk 37).
* **Dispositivos**: Samsung Galaxy Watch7 / Galaxy Watch4, 5, 6, Pixel Watch u otros relojes Wear OS.

---

## 📲 Cómo Instalar en tu Reloj Físico (Wi-Fi ADB Debugging)

1. **Activar Opciones de Desarrollador en el Reloj**:
   - Ve a `Ajustes` -> `Acerca del reloj` -> `Información de software`.
   - Toca 7 veces seguidas en **Versión de software**.
   - Vuelve a `Ajustes` -> `Opciones de desarrollador`.
   - Activa **Depuración por ADB** y **Depuración por Wi-Fi / Inalámbrica**.
2. **Emparejar con Android Studio**:
   - Asegúrate de que el reloj y la PC estén en la **misma red Wi-Fi**.
   - En el reloj, selecciona **Emparejar nuevo dispositivo**.
   - En Android Studio, abre el desplegable de dispositivos -> `Pair Devices Using Wi-Fi` e ingresa la IP, puerto y código de 6 dígitos.
3. **Desplegar e Instalar**:
   - Selecciona tu reloj en la barra superior de Android Studio y haz clic en **Run (▶)** (`Shift + F10`).

---

## 📄 Licencia

Este proyecto es de código abierto bajo la licencia **MIT**. Creado por **@MaritoPaez**. ¡Siéntete libre de clonarlo, mejorarlo y compartirlo con la comunidad!

# PadelPoints 🎾⌚ (v1.3.2)

**PadelPoints** es una aplicación nativa para **Wear OS** (diseñada y optimizada para **Samsung Galaxy Watch7 en adelante** y relojes inteligentes con pantalla circular) que permite llevar la puntuación de partidos de pádel (o tenis) de forma sencilla, veloz y con síntesis de voz e interfaz adaptada para lectura rápida en la cancha.

---

## 🚀 Novedades de la Versión 1.3.2 (v1.3.2)

* 🔍 **Optimización de Legibilidad para Presbicia**:
  - **Selector de Juegos Gigante**: El número de juegos por set en el menú de configuración se aumentó a **26sp ExtraBold Black** en amarillo brillante, removiendo textos redundantes.
  - **Centrado Universal**: Todos los botones y opciones de configuración ahora cuentan con alineación 100% centrada.
* 🎨 **Círculos de Juegos de Máximo Contraste**:
  - Círculos de juegos ganados en **54dp** con números gigantes de **34sp Black** sobre fondo negro sólido y borde amarillo, desplazados sutilmente hacia el centro para evitar cualquier recorte por la curvatura del bisel.
* 🎾 **Modo Tiebreak Oficial y "Muere en 7"**:
  - Activación/desactivación de Tiebreak y regla opcional "Muere en 7" desde el menú de configuración.
  - Rotación de saque oficial cada 2 puntos en Tiebreak.
* 🔊 **Audios de Tiebreak Unificados (Hasta 15 Puntos)**:
  - Audios pregrabados en español para todas las combinaciones de Tiebreak hasta 15 puntos por lado (`tiebreak_puntos_0_0` a `tiebreak_puntos_15_15`).
  - Pronunciación natural *"Taibrek activado/desactivado"*.
* 🕒 **Reloj Analógico Minimalista**:
  - Esfera analógica negra con agujas blancas para consultar la hora de un vistazo.

---

## 🌟 Características Principales

* 👁️ **Diseño de Alto Contraste 50/50**:
  - **Equipo 1**: Números amarillos sobre fondo negro (`#FFE500` sobre `#000000`).
  - **Equipo 2**: Números negros sobre fondo amarillo (`#000000` sobre `#FFE500`).
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

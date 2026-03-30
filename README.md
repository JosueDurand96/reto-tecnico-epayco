# Countries — Android (Jetpack Compose)

Aplicación nativa en Kotlin que consume la API pública [REST Countries](https://restcountries.com/), permite explorar países, buscar con debounce, ver detalle y gestionar favoritos persistidos localmente. Pensada como entrega de prueba técnica con arquitectura limpia y stack moderno de Android.

## Cumplimiento con la prueba técnica (documento Epayco)

Referencia: requisitos del documento *Prueba Android Epayco* (objetivo, vistas, entrega).

### Objetivo del documento

Demostrar capacidad para **interactuar con APIs externas**, **gestionar el estado** de la aplicación y **persistir datos localmente**, con una UI intuitiva y alineada al diseño de referencia.

### Requisitos funcionales

| # | Requisito (PDF) | Cómo se cubre en el proyecto |
|---|-----------------|------------------------------|
| **1a** | Listado completo desde `https://restcountries.com/v3.1/all` | `GetAllCountriesUseCase` → `CountriesApiService.getAllCountries()` |
| **1b** | Cada ítem con información esencial (nombre, bandera, etc.) y navegación al detalle | `CountryListItem` (nombre oficial, capital, bandera, escudo) + `onCountryClick` → ruta `detail/{cca3}` |
| **1c** | Búsqueda con `https://restcountries.com/v3.1/name/{name}?fullText=false`; activación automática **tras el segundo carácter** | `CountriesViewModel`: umbral 2 caracteres + debounce; `searchByName(..., fullText = false)` |
| **1d** | Favoritos claramente distinguibles en el listado | Icono de marcador cuando el `cca3` está en favoritos (`observeFavoriteCca3Ids`) |
| **2a** | Detalle al seleccionar un país | `CountryDetailScreen` vía `NavHost` |
| **2b** | Marcar/desmarcar favorito con control claro; persistencia entre sesiones | Icono de favorito en top bar + **Room** (`FavoriteCountryEntity`); datos del país guardados localmente |
| **3a** | Barra inferior para alternar listado general / favoritos | `CountriesBottomBar`: pestañas Countries y Favorites |
| **3b** | Desde favoritos, navegar al detalle | Misma ruta `detail/{cca3}` que desde el listado principal |

### Requisitos técnicos y de diseño (PDF)

| # | Requisito (PDF) | Enfoque adoptado |
|---|-----------------|------------------|
| **API** | Consumo eficiente y robusto; gestión de carga y errores de red | Retrofit + corrutinas; estados `Loading` / `Success` / `Empty` / `Error`; manejo de `IOException` / `HttpException` y 404 en búsqueda |
| **Persistencia** | Mecanismo local apropiado para favoritos | **Room** (SQLite) con snapshot del país para uso **offline** en favoritos y detalle si aplica |
| **Arquitectura** | Código limpio, modular y escalable (MVVM, Clean, etc.) | **Clean Architecture** (capas) + **MVVM** + **Repository** + **casos de uso** |
| **UX/UI** | Fidelidad al diseño de referencia (screenshots) | Material 3, Compose, lista tipo tarjeta, detalle con rejilla de dos columnas, barra inferior |

### Forma de entrega (recomendaciones del PDF)

| Entregable | Notas |
|------------|--------|
| **Código fuente** | Repositorio público o `.zip`; nombre del repo sin revelar literalmente la solución de la prueba (recomendación del documento). |
| **Video demostración (.mp4)** | Mostrar la app y argumentar decisiones: arquitectura, persistencia, API y errores, otras decisiones técnicas. |
| **APK (recomendado)** | `assembleDebug` (o release firmado) para facilitar revisión. |

---

## Por qué estas tecnologías y patrones

### Clean Architecture

- **Capas** (`data`, `domain`, `presentation`): el dominio no depende de Android ni de Retrofit; los casos de uso orquestan reglas y el repositorio abstrae el origen de datos (red + Room).
- **Beneficio**: pruebas más sencillas, cambios de API o de BD localizados, escalabilidad si el proyecto crece.

### MVVM + ViewModel

- **ViewModel** conserva estado ante rotaciones y navegación; la UI es una función del estado (`StateFlow`).
- **Beneficio**: separación UI / lógica de presentación, alineado con las guías de Android y con lo que el documento valora (MVVM).

### Gestión de estado (UI)

- ** Estados sellados** (`CountriesListUiState`, `CountryDetailUiState`, etc.): cubren carga, éxito, vacío y error de forma explícita.
- **StateFlow + `collectAsStateWithLifecycle`**: la UI solo observa flujos; menos riesgo de fugas y comportamiento correcto con el ciclo de vida.
- **Beneficio**: cumple el requisito de “gestionar el estado” y facilita manejo de errores de red de forma visible para el usuario.

### Repository + casos de uso

- **Un solo repositorio** implementado en `data` que habla con API y DAO; la interfaz vive en `domain`.
- **Casos de uso** por acción (`GetAllCountries`, `SearchCountries`, etc.): una responsabilidad por clase, reutilizable desde ViewModels.
- **Beneficio**: reglas de negocio y puntos de entrada claros para tests y para explicar el diseño en la demo en video.

### Retrofit + Gson + OkHttp

- **Retrofit** modela endpoints REST de forma tipada; **Gson** deserializa DTOs; **OkHttp** logging en debug.
- **Beneficio**: consumo de API estable y mantenible; encaja con “manejo eficiente y robusto” del PDF.

### Room

- **SQLite** con API declarativa; favoritos como **filas con snapshot** del país para listado y detalle sin red.
- **Beneficio**: persistencia entre sesiones requerida por el PDF; opción adecuada frente a solo `SharedPreferences` (estructurado, consultas, offline).

### Hilt

- Inyección de dependencias en constructores (repositorio, DAO, API, use cases).
- **Beneficio**: menos acoplamiento manual, módulos de red/BD claros, alineado con proyectos Android profesionales.

### Jetpack Compose + Material 3 + Navigation

- UI declarativa; **Navigation Compose** para rutas y argumentos; **Coil** para imágenes.
- **Beneficio**: desarrollo rápido de pantallas y navegación alineada con el stack moderno de Android.

### Corrutinas y Flow

- Llamadas de red y Room en corrutinas; **Flow** para observar favoritos.
- **Beneficio**: código asíncrono legible y sin bloquear el hilo principal.

---

## Requisitos

- **Android Studio** Koala (2024.1.1) o superior (recomendado)
- **JDK 17**
- **Android SDK 35** (compileSdk / targetSdk)

## Cómo abrir y compilar

1. Clonar o descomprimir el proyecto.
2. Abrir la carpeta raíz en Android Studio (el módulo es `:app`).
3. Sincronizar Gradle.
4. Ejecutar en dispositivo/emulador o generar APK:
   ```bash
   ./gradlew assembleDebug
   ```
   El APK queda en `app/build/outputs/apk/debug/`.

## Funcionalidad (resumen)

| Característica | Detalle |
|----------------|---------|
| Listado global | `GET https://restcountries.com/v3.1/all` |
| Búsqueda | `GET https://restcountries.com/v3.1/name/{name}?fullText=false` con **debounce 300 ms** cuando el texto tiene **2 o más caracteres**; con 0–1 caracteres se muestra el listado completo |
| Detalle | Navegación por `cca3`; datos desde API (`v3.1/alpha/{code}`) o, si el país está en favoritos, **snapshot en Room** (offline) |
| Favoritos | **Room** (`FavoriteCountryEntity`): se guarda el país completo para listado y detalle **sin conexión**; orden por fecha de guardado |
| Login opcional | Pantalla de acceso con usuario/contraseña y **biometría** (`BiometricPrompt`); tras éxito, flujo principal |
| UI | Material 3, lista con bandera y escudo, marcador de favorito, detalle con dos columnas |

## Arquitectura (resumen)

- **Clean Architecture** en capas: `data` (Retrofit, Room, DTOs, mappers), `domain` (modelos, contratos de repositorio, casos de uso), `presentation` (Compose, ViewModels, navegación).
- **MVVM** con **StateFlow** y corrutinas.
- **Inyección de dependencias** con **Hilt** (módulos de red, base de datos y repositorio).
- **Patrón Repository** con implementación única que orquesta API y DAO.

```
presentation  →  domain (use cases)  →  repository interface
                      ↑                        ↑
                      |                        |
                 data: API + Room + mappers
```

## Tecnologías, herramientas y dependencias

Inventario de lo utilizado para construir el proyecto (versiones según `gradle/libs.versions.toml` y `gradle-wrapper.properties`).

### Entorno de compilación

| Herramienta | Versión / detalle |
|-------------|-------------------|
| **Gradle** (wrapper) | 9.1.0 |
| **Android Gradle Plugin (AGP)** | 8.7.2 |
| **Kotlin** | 2.0.21 |
| **KSP** (Kotlin Symbol Processing) | 2.0.21-1.0.28 |
| **JDK** | 17 (toolchain y `compileOptions`) |
| **Catálogo de versiones** | `gradle/libs.versions.toml` (Gradle Version Catalog) |
| **Repositorios** | Google Maven, Maven Central (`settings.gradle.kts`) |

### Plugins de Gradle aplicados al módulo `app`

| Plugin | Uso |
|--------|-----|
| `com.android.application` | Módulo Android aplicación |
| `org.jetbrains.kotlin.android` | Soporte Kotlin en Android |
| `org.jetbrains.kotlin.plugin.compose` | Compilador de Compose |
| `com.google.dagger.hilt.android` | Inyección de dependencias |
| `com.google.devtools.ksp` | Procesamiento en tiempo de compilación (Hilt, Room) |

### Configuración Android (`app/build.gradle.kts`)

| Parámetro | Valor |
|-----------|--------|
| `compileSdk` / `targetSdk` | 35 |
| `minSdk` | 24 |
| `namespace` | `com.example.reto_tecnico_epayco` |
| `applicationId` | `com.example.reto_tecnico_epayco` |
| **Build features** | `compose = true`, `buildConfig = true` |
| **Empaquetado** | Exclusión de `META-INF/{AL2.0,LGPL2.1}` en recursos |

### Dependencias de producción (`implementation`)

| Librería | Versión | Rol en el proyecto |
|----------|---------|---------------------|
| **AndroidX Core KTX** | 1.15.0 | Extensiones Kotlin sobre APIs Android |
| **AndroidX Lifecycle Runtime KTX** | 2.8.7 | Ciclo de vida |
| **AndroidX Activity Compose** | 1.9.3 | `ComponentActivity` + Compose |
| **Compose BOM** | 2024.10.01 | Alineación de versiones de Compose |
| **Compose UI** | (BOM) | UI declarativa |
| **Compose UI Graphics** | (BOM) | Gráficos |
| **Compose UI Tooling Preview** | (BOM) | `@Preview` |
| **Compose Material 3** | (BOM) | Componentes Material 3 |
| **Compose Material Icons Extended** | 1.7.5 | Iconos adicionales (p. ej. `Public`, `Favorite`) |
| **Navigation Compose** | 2.8.4 | `NavHost`, rutas, argumentos |
| **Lifecycle ViewModel Compose** | 2.8.7 | `hiltViewModel()`, ViewModels en Compose |
| **Lifecycle Runtime Compose** | 2.8.7 | `collectAsStateWithLifecycle`, `LocalLifecycleOwner` |
| **Hilt Android** | 2.52 | DI en runtime |
| **Hilt Navigation Compose** | 1.2.0 | Integración Hilt + Navigation |
| **Retrofit** | 2.11.0 | Cliente HTTP / API REST |
| **Retrofit Converter Gson** | 2.11.0 | Deserialización JSON → DTOs |
| **OkHttp Logging Interceptor** | 4.12.0 | Logs de red en debug (`BuildConfig.DEBUG`) |
| **Gson** | 2.11.0 | JSON (usado por el converter de Retrofit) |
| **Room Runtime** | 2.6.1 | Base de datos local |
| **Room KTX** | 2.6.1 | Extensiones Kotlin y corrutinas para Room |
| **Biometric** (AndroidX) | 1.1.0 | `BiometricPrompt` / `BiometricManager` (login opcional) |
| **Fragment KTX** | 1.8.5 | `FragmentActivity` para biometría |
| **Coil Compose** | 2.7.0 | Carga de imágenes (banderas, escudos) |
| **Kotlinx Coroutines Android** | 1.9.0 | Corrutinas en el hilo principal / background |

**Procesamiento con KSP (`ksp`)**

| Artefacto | Versión | Uso |
|-----------|---------|-----|
| **Hilt Compiler** (`hilt-android-compiler`) | 2.52 | Generación de código Hilt |
| **Room Compiler** | 2.6.1 | Implementación de DAOs y base de datos |

### Dependencias de test y depuración

| Alcance | Librerías |
|---------|-----------|
| `testImplementation` | **JUnit** 4.13.2 |
| `androidTestImplementation` | **AndroidX JUnit** 1.2.1, **Espresso Core** 3.6.1, **Compose UI Test JUnit4** (versión vía BOM) |
| `debugImplementation` | **Compose UI Tooling**, **Compose UI Test Manifest** |

### API y datos externos

| Recurso | Uso |
|---------|-----|
| **REST Countries** (`https://restcountries.com/`) | Endpoints `v3.1/all`, `v3.1/name/...`, `v3.1/alpha/...` |
| **Internet** | Permiso `INTERNET` en `AndroidManifest.xml` |

### Archivos de configuración del build

| Archivo | Función |
|---------|---------|
| `settings.gradle.kts` | Nombre del proyecto, `include(":app")`, repositorios (Google, Maven Central) |
| `build.gradle.kts` (raíz) | Declaración de plugins del proyecto (sin aplicar al `app`) |
| `app/build.gradle.kts` | Plugins del módulo, `android { }`, dependencias |
| `gradle/libs.versions.toml` | Version Catalog: versiones centralizadas de librerías y plugins |
| `gradle/wrapper/gradle-wrapper.properties` | Versión de Gradle del wrapper |
| `gradle.properties` | Opciones globales de Android/Kotlin/Gradle |
| `app/proguard-rules.pro` | Reglas ProGuard (release sin minify en la configuración actual) |

### Recursos y manifiesto (app)

- `AndroidManifest.xml` — `CountriesApplication`, `MainActivity`, permiso `INTERNET`, tema, `windowSoftInputMode`
- `res/values/strings.xml`, `themes.xml`, `colors.xml` — textos y tema XML base para la ventana
- `res/mipmap-*`, `res/drawable` — icono de la aplicación (`ic_launcher`, foreground/background adaptativos)
- `res/xml/backup_rules.xml`, `data_extraction_rules.xml` — reglas de backup / extracción de datos (plantilla Android)

### Propiedades del proyecto (`gradle.properties`)

- `android.useAndroidX=true`
- `android.nonTransitiveRClass=true`
- `kotlin.code.style=official`
- `org.gradle.jvmargs` (memoria y encoding UTF-8 para el daemon de Gradle)

### Patrones y APIs de código (resumen)

| Concepto | Implementación |
|----------|----------------|
| Lenguaje | **Kotlin** |
| UI | **Jetpack Compose** + **Material 3** |
| Navegación | **Navigation Compose** |
| Estado asíncrono | **Kotlin Coroutines**, **Flow**, **StateFlow** |
| UI reactiva | `collectAsStateWithLifecycle` |
| DI | **Hilt** (`@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`, módulos `@Module`) |
| Red | **Retrofit** + **OkHttp** + **Gson** |
| Persistencia | **Room** (SQLite) |
| Imágenes | **Coil** |
| Arquitectura | **Clean Architecture** + **MVVM** + **Repository** + **Use cases** |

## Paquetes principales

- `data.remote` — DTOs y `CountriesApiService`
- `data.local` — `FavoriteCountryEntity` (snapshot offline), `FavoriteDao`, `CountriesDatabase`
- `data.mapper` — `CountryMapper`, `FavoriteCountryEntityMapper`
- `data.repository` — `CountriesRepositoryImpl`
- `domain.model`, `domain.repository`, `domain.usecase`
- `presentation.navigation`, `presentation.screens`, `presentation.components`, `presentation.viewmodel`, `presentation.state`
- `di` — `NetworkModule`, `DatabaseModule`, `RepositoryModule`

## Estados de UI

- Listas: `Loading`, `Success`, `Empty`, `Error` (`CountriesListUiState`).
- Detalle: `Loading`, `Success`, `Error` (`CountryDetailUiState`).

## Decisiones técnicas adicionales

- **Búsqueda**: debounce en el ViewModel para no saturar la red; umbral de 2 caracteres según el PDF.
- **404 en búsqueda**: tratado como lista vacía (comportamiento habitual de REST Countries).
- **Favoritos**: snapshot completo del país en Room para cumplir persistencia y uso offline; el listado de favoritos no depende de la API en tiempo real.
- **Errores de red**: mensajes legibles vía `IOException` / `HttpException` en el repositorio.
- **Favoritos (UI)**: recarga desde Room sin forzar `Loading` en cada `refresh` para reducir parpadeo al cambiar de pestaña o volver del detalle.

## Licencia y créditos

Datos de países: [REST Countries](https://restcountries.com/).  
Proyecto generado para fines de evaluación técnica.

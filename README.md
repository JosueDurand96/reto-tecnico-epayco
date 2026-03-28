# Countries — Android (Jetpack Compose)

Aplicación nativa en Kotlin que consume la API pública [REST Countries](https://restcountries.com/), permite explorar países, buscar con debounce, ver detalle y gestionar favoritos persistidos localmente. Pensada como entrega de prueba técnica con arquitectura limpia y stack moderno de Android.

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

## Funcionalidad

| Característica | Detalle |
|----------------|---------|
| Listado global | `GET https://restcountries.com/v3.1/all` |
| Búsqueda | `GET https://restcountries.com/v3.1/name/{name}?fullText=false` activa con **debounce 300 ms** cuando el texto tiene **2 o más caracteres**; con 0–1 caracteres se muestra el listado completo |
| Detalle | Navegación por `cca3`; datos enriquecidos vía `GET https://restcountries.com/v3.1/alpha/{code}` |
| Favoritos | Persistencia con **Room**; listado de favoritos vía `GET https://restcountries.com/v3.1/alpha?codes=...` respetando el orden guardado |
| UI | Material 3, lista con bandera y escudo, marcador de favorito, pantalla de detalle con rejilla adaptable en pantallas anchas |

## Arquitectura

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
- `data.mapper` — `CountryMapper`
- `data.repository` — `CountriesRepositoryImpl`
- `domain.model`, `domain.repository`, `domain.usecase`
- `presentation.navigation`, `presentation.screens`, `presentation.components`, `presentation.viewmodel`, `presentation.state`
- `di` — `NetworkModule`, `DatabaseModule`, `RepositoryModule`

## Estados de UI

- Listas: `Loading`, `Success`, `Empty`, `Error` (`CountriesListUiState`).
- Detalle: `Loading`, `Success`, `Error` (`CountryDetailUiState`).

## Decisiones técnicas breves

- **Búsqueda**: debounce en el ViewModel para no saturar la red; umbral de 2 caracteres alineado con el enunciado.
- **404 en búsqueda**: tratado como lista vacía (comportamiento habitual de REST Countries).
- **Favoritos**: solo se guarda el `cca3` en Room; los datos mostrados se refrescan desde la API para mantener consistencia con el backend.
- **Errores de red**: mensajes legibles vía `IOException` / `HttpException` en el repositorio.

## Licencia y créditos

Datos de países: [REST Countries](https://restcountries.com/).  
Proyecto generado para fines de evaluación técnica.

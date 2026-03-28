package com.example.reto_tecnico_epayco.presentation.navigation

object Routes {
    const val LOGIN = "login"
    /** Grafo anidado que contiene el flujo principal (tabs + detalle) tras autenticación. */
    const val MAIN_GRAPH = "main"

    const val COUNTRIES = "countries"
    const val FAVORITES = "favorites"
    const val DETAIL = "detail/{${NavArgs.CCA3}}"

    fun detail(cca3: String): String = "detail/$cca3"
}

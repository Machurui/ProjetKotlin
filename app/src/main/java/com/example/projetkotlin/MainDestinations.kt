package com.example.projetkotlin

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

interface Destination {
    val route: String
}

/**
 * Rally app navigation destinations
 */
object Auth : Destination {
    override val route = "auth"
}

object Home : Destination {
    override val route = "home"
}

object NewNote : Destination {
    override val route = "newnote"
}

object SingleNote : Destination {
    override val route = "single_note"
    const val noteArg = "note_id"
    val routeWithArgs = "${route}/{${noteArg}}"
    val arguments = listOf(
        navArgument(noteArg) { type = NavType.StringType }
    )
    val deepLinks = listOf(
        navDeepLink { uriPattern = "myapp://$route/{$noteArg}"}
    )
}


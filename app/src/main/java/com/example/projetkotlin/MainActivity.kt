package com.example.projetkotlin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projetkotlin.ui.theme.ProjetKotlinTheme
import com.example.projetkotlin.view.HomeScreen
import com.example.projetkotlin.view.auth.AuthScreen
import com.example.projetkotlin.view.database.NoteDAO
import com.example.projetkotlin.view.notes.NewNoteScreen
import com.example.projetkotlin.view.notes.NoteHomeScreen
import com.example.projetkotlin.view.notes.SingleNoteScreen

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp()
        }
    }
}

@Composable
fun MainApp() {
    ProjetKotlinTheme {
        val navController = rememberNavController()

        val currentBackStack by navController.currentBackStackEntryAsState()

        val currentDestination = currentBackStack?.destination

//        val currentScreen = rallyTabRowScreens.find { it.route == currentDestination?.route } ?: Overview
        Scaffold(
            topBar = {

            }
        ) { innerPadding ->
            MainNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Auth.route,
        modifier = modifier
    ) {
        composable(route = Auth.route) {
            AuthScreen(
                onNavigateToHome = {
                    navController.navigateSingleTopTo(Home.route)
                }
            )
        }
        composable(route = Home.route) {
            NoteHomeScreen(
                onClickNewNote = {
                    navController.navigateSingleTopTo(NewNote.route)
                },
                onNavigateToDetail = { noteID ->
                    navController.navigateToSingleNote(noteID.id.toString())
                }
            )
        }
        composable(route = NewNote.route) {
            NewNoteScreen(
                onClickNotes = {
                    navController.navigateSingleTopTo(Home.route)
                }
            )
        }
        composable(
            route = SingleNote.routeWithArgs,
            arguments = SingleNote.arguments,
            deepLinks = SingleNote.deepLinks
        ) {navBackStackEntry ->
            val noteId = navBackStackEntry.arguments?.getString(SingleNote.noteArg)
            if (noteId != null) {
                Log.e("DEBUG", "noteId: $noteId")
                SingleNoteScreen(
                    noteId,
                    onClickNotes = {
                        navController.navigateSingleTopTo(Home.route)
                    }
                )
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }

private fun NavHostController.navigateToSingleNote(noteId: String) {
    this.navigateSingleTopTo("${SingleNote.route}/$noteId")
}

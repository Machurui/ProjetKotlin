package com.example.projetkotlin.view.auth

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.projetkotlin.model.Note
import kotlinx.coroutines.delay

@Composable
fun AuthScreen(
    onNavigateToHome: () -> Unit
) {
    val context = LocalContext.current
    val activity = LocalContext.current as FragmentActivity
    val executor = ContextCompat.getMainExecutor(activity)

    // Définition des informations pour la boîte de dialogue d'authentification biométrique
    var promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Authentification requise")
        .setSubtitle("Veuillez authentifier pour continuer")
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .build()

    // Initialisation de BiometricPrompt avec un callback pour gérer les événements d'authentification
    val biometricPrompt = BiometricPrompt(activity, executor,
        object: BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(context, "Erreur d'authentification : $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(context, "Authentification réussie !", Toast.LENGTH_LONG).show()
                onNavigateToHome()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(context, "Échec de l'authentification", Toast.LENGTH_LONG).show()
            }
        }
    )

    // Texte d'accueil ajouté
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Bienvenue")
        Text("Veuillez utiliser votre empreinte digitale ou votre schéma pour vous authentifier.", textAlign = TextAlign.Center)
    }

    // Lancement de l'authentification biométrique après un court délai
    LaunchedEffect(Unit){
        delay(500) // Un léger délai pour permettre à l'utilisateur de lire le message
        biometricPrompt.authenticate(promptInfo)
    }
}

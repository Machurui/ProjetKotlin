package com.example.projetkotlin.view.notes

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.projetkotlin.model.Note
import com.example.projetkotlin.view.database.NoteDAO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun NewNoteScreen(
    onClickNotes: () -> Unit = {},
) {
    val context = LocalContext.current
    val title = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }
    val tag = remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {

        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Titre") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = content.value,
            onValueChange = { content.value = it },
            label = { Text("Contenu") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = tag.value,
            onValueChange = { tag.value = it },
            label = { Text("Tag") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (title.value.isNotEmpty() && content.value.isNotEmpty() && tag.value.isNotEmpty()) {
                    val noteDAO = NoteDAO(context)

                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val current = LocalDateTime.now().format(formatter)

                    val tags = tag.value.split(",")
                    val result = noteDAO.insert(Note(1  ,title.value, content.value, tags, current))
                    Log.e("DEBUG", "result: $result")
                    if (result != -1L) {
                        onClickNotes()
                    } else {
                        Log.e("DEBUG", "An error occurred")
                    }
                } else {
                    Log.e("DEBUG", "All fields are required")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add new note")
        }

        Button(onClick = {
            onClickNotes()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}



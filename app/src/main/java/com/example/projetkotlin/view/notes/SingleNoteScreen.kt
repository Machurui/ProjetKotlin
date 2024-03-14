package com.example.projetkotlin.view.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.projetkotlin.model.Note
import com.example.projetkotlin.view.database.NoteDAO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun SingleNoteScreen(
    noteId: String,
    onClickNotes: () -> Unit = {},
) {
    val context = LocalContext.current
    val noteDAO = remember { NoteDAO(context) }
    var note by remember { mutableStateOf<Note?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(noteId) {
        note = noteDAO.getNoteById(noteId.toInt())
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else if (note != null) {
        NoteForm(note!!, noteDAO, onClickNotes)
    } else {
        Text("Note not found")
    }
}

@Composable
fun NoteForm(
    note: Note,
    noteDAO: NoteDAO,
    onClickNotes: () -> Unit
) {
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.content) }
    var tagsString by remember { mutableStateOf(note.tag.joinToString(separator = ",")) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = tagsString,
            onValueChange = { tagsString = it },
            label = { Text("Tag") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val tagsList = tagsString.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                val updatedNote = note.copy(title = title, content = content, tag = tagsList)
                noteDAO.updateNoteById(updatedNote)
                onClickNotes()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update note")
        }

        Button(
            onClick = {
                noteDAO.deleteNoteById(note.id)
                onClickNotes()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete note")
        }

        Button(
            onClick = onClickNotes,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}

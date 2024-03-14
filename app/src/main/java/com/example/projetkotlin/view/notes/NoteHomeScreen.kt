package com.example.projetkotlin.view.notes

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.projetkotlin.model.Note
import com.example.projetkotlin.view.database.NoteDAO
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch


@Composable
fun NoteHomeScreen(
    onClickNewNote: () -> Unit = {},
    onNavigateToDetail: (Note) -> Unit
) {
    val context = LocalContext.current
    val noteDAO by remember { mutableStateOf(NoteDAO(context)) }
    var notes by remember { mutableStateOf<List<Note>>(emptyList()) }
    var hasError by remember { mutableStateOf(false) }
    var selectedTag by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            hasError = true
            Log.e("DEBUG", "An error occurred: ${throwable.localizedMessage}")
        }
        coroutineScope.launch(exceptionHandler) {
            notes = noteDAO.getAllNotes()
        }
    }

    val allTags = notes.flatMap { it.tag }.distinct().sorted()


    fun deleteNote(note: Note) {
        coroutineScope.launch {
            noteDAO.deleteNoteById(note.id)
            notes = notes.filter { it.id != note.id }
        }
    }

    val filteredNotes = if (selectedTag != null) {
        notes.filter { note -> selectedTag in note.tag }
    } else {
        notes
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        DropdownSelector(allTags, selectedTag) { tag ->
            selectedTag = tag
        }
        Button(onClick = onClickNewNote) {
            Text("Créer une nouvelle note")
        }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Liste des notes")
                if (hasError) {
                    // Handle error state in UI
                    Text("An error occurred while loading notes")
                } else {
                    MyListScreen(filteredNotes, onDelete = { note ->
                        deleteNote(note)
                    }, onShowMore = { note ->
                        onNavigateToDetail(note)
                    })
                }
            }

    }
}

@Composable
fun DropdownSelector(tags: List<String>, selectedTag: String?, onTagSelected: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = selectedTag ?: "Select a Tag"

    Column {
        Text(
            text = displayText,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(16.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            DropdownMenuItem(
                text = {Text("All Tags")},
                onClick = {
                onTagSelected(null)
                expanded = false
                }
            )



            tags.forEach { tag ->
                DropdownMenuItem(
                    text = { Text(tag) },
                    onClick = {
                    onTagSelected(tag)
                    expanded = false
                    }
                )
            }
        }
    }
}



@Composable
fun MyListScreen(notes: List<Note>, onDelete: (Note) -> Unit, onShowMore: (Note) -> Unit) {
    Log.e("DEBUG", "notes: $notes")
    if (notes.isEmpty()) {
        Text("Aucune note trouvée")
    } else {
        LazyColumn {
            items(notes) { note ->
                ListItemView(note, onDelete, onShowMore)
            }
        }
    }
}

@Composable
fun ListItemView(note: Note, onDelete: (Note) -> Unit, onShowMore: (Note) -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 0.dp)
            ) {
                Text(text = "Titre: ${note.title}")
                Text(text = note.date)
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 0.dp)
            ) {
                ElevatedButton(
                    onClick = {
                        onShowMore(note)
                    }
                ) {
                    Text("Show more")
                }
                ElevatedButton(
                    onClick = {
                        onDelete(note)
                    }
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
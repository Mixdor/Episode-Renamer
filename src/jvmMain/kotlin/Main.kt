import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension
import java.awt.TextArea
import kotlin.math.acosh

@Composable
@Preview
fun App() {
    var serie by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Column(
            modifier = Modifier.padding(10.dp).fillMaxSize()
        ) {
            val modspaceDp = Modifier.fillMaxHeight(0.2f)

            rowTitle()
            Spacer(Modifier.size(10.dp))
            rowChapthers()
            Spacer(Modifier.size(10.dp))
            Text("Title 01x07. Ejemplo de capitulo", fontSize = 12.sp)
            Spacer(Modifier.size(10.dp))
            textMultiline()
            Spacer(modspaceDp)
            rowButtonsAction()
            Spacer(modspaceDp)
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun textMultiline() {
    var textarea by remember { mutableStateOf("") }
    TextField(
        value = textarea,
        textStyle = TextStyle(fontSize = 12.sp),
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.88f),
        onValueChange = {
            textarea = it
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun rowTitle() {
    var serie by remember { mutableStateOf("") }

    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        TextField(
            modifier = Modifier.height(52.dp).fillMaxWidth(0.70f),
            textStyle = TextStyle(fontSize = 12.sp),
            label = {Text("Nombre de la serie", fontSize = 12.sp)},
            value = serie,
            onValueChange = {
                serie = it
            }
        )
        Button(
            modifier = Modifier.fillMaxWidth(0.95f),
            onClick = {

            }
        ){
            Text("Cargar", fontSize = 12.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun rowChapthers() {
    var temporada by remember { mutableStateOf("") }
    var numCap by remember { mutableStateOf("") }
    var first by remember { mutableStateOf("") }

    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        TextField(
            modifier = Modifier.height(52.dp).fillMaxSize(0.3f),
            textStyle = TextStyle(fontSize = 12.sp),
            label = {Text(text = "Temporada", fontSize = 12.sp)},
            value = temporada,
            onValueChange = {
                temporada = it
            }
        )
        TextField(
            modifier = Modifier.height(52.dp).fillMaxSize(0.5f),
            textStyle = TextStyle(fontSize = 12.sp),
            label = {Text(text = "N° Capitulos", fontSize = 12.sp)},
            value = numCap,
            onValueChange = {
                numCap = it
            }
        )
        TextField(
            modifier = Modifier.height(52.dp).fillMaxSize(0.92f),
            textStyle = TextStyle(fontSize = 12.sp),
            label = {Text(text = "Iniciar en", fontSize = 12.sp)},
            value = first,
            onValueChange = {
                first = it
            }
        )
    }

}

@Composable
@Preview
fun rowButtonsAction() {
    Row(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        FilledTonalButton(
            onClick = {
                /*text = if (text == "Hello, World!") "Hello, Desktop!"
                else "Hello, World!"*/
            },
            modifier = Modifier.fillMaxWidth(0.45f)
        ) {
            Text("Ctrl Z", fontSize = 12.sp)
        }
        Button(
            onClick = {
                /*text = if (text == "Hello, World!") "Hello, Desktop!"
                else "Hello, World!"*/
            },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Aplicar", fontSize = 12.sp)
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        //resizable = false,
        title = "ReNomMasivo",
        state = rememberWindowState(size = DpSize(400.dp, 600.dp))
    ) {
        this.window.minimumSize = Dimension(400,600)
        App()
    }
}

package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import case.FileReader
import case.CoreActions
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import org.jetbrains.skia.impl.Log
import ui.theme.AppTheme
import ui.theme.md_theme_light_primary
import java.awt.Dimension
import java.nio.charset.StandardCharsets

private val fileReader = FileReader()
private val coreActions = CoreActions()

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
@Preview
fun App(window: ComposeWindow) {

    AppTheme(false) {

        var show by remember { mutableStateOf("") }
        var season by remember { mutableStateOf("") }
        var numCap by remember { mutableStateOf("") }
        var start by remember { mutableStateOf("") }

        var exampleEpisode by remember { mutableStateOf("Title 01x01. Ejemplo de capitulo") }

        var folderPath by remember { mutableStateOf("") }
        var filePath by remember { mutableStateOf("") }

        var textAreaFolder by remember { mutableStateOf("") }
        var numberLinesFolder by remember { mutableStateOf(0) }
        var colorNumberFolder by remember { mutableStateOf(Color(0xFF993426)) }
        var textAreaFile by remember { mutableStateOf("") }
        var numberLinesFile by remember { mutableStateOf(0) }
        var colorNumberFile by remember { mutableStateOf(Color(0xFF993426)) }

        var enableCtrlZ by remember { mutableStateOf(false) }
        var dialogEmpty by remember { mutableStateOf(false) }
        var dialogErrorEpisodes by remember { mutableStateOf(false) }
        var dialogChartIllegal by remember { mutableStateOf(false) }
        var showFilePicker by remember { mutableStateOf(false) }
        var showDirPicker by remember { mutableStateOf(false) }

        if(dialogEmpty){
            AlertDialog(
                modifier = Modifier.fillMaxWidth(0.5f),
                title = { Text(text = "Atención", fontWeight = FontWeight.Bold, color = md_theme_light_primary ) },
                text = { Text(text = "Debes rellenar todos los campos", fontSize = 14.sp) },
                onDismissRequest = { dialogEmpty = false },
                confirmButton = {
                    TextButton(onClick = { dialogEmpty = false })
                    { Text(text = "OK") }
                }

            )
        }

        if(dialogChartIllegal){
            AlertDialog(
                modifier = Modifier.fillMaxWidth(0.7f),
                title = { Text(text = "Atención", fontWeight = FontWeight.Bold, color = md_theme_light_primary ) },
                text = {
                    Column {
                        Text(
                            text = "Se ha encontrado en el archivo al menos uno de los siguientes caracteres ilegales:",
                            fontSize = 14.sp
                        )
                        Text(
                            text = "~ \" # % & * : < > ? / \\\\ { | } ",
                            color = md_theme_light_primary,
                            fontSize = 14.sp)
                    } },
                onDismissRequest = { dialogChartIllegal = false },
                confirmButton = {
                    TextButton(onClick = { dialogChartIllegal = false })
                    { Text(text = "OK") }
                }

            )
        }

        if(dialogErrorEpisodes){
            AlertDialog(
                modifier = Modifier.fillMaxWidth(0.5f),
                title = { Text(text = "Atención", fontWeight = FontWeight.Bold, color = md_theme_light_primary ) },
                text = { Text(text = "La cantidad de episodios no coincide", fontSize = 14.sp) },
                onDismissRequest = { dialogErrorEpisodes = false },
                confirmButton = {
                    TextButton(onClick = { dialogErrorEpisodes = false })
                    { Text(text = "OK") }
                }

            )
        }

        FilePicker(showFilePicker, fileExtension = "txt") { path ->
            showFilePicker = false

            try {
                filePath = path.toString()
                val content = fileReader.readTxtFile(filePath, StandardCharsets.UTF_8)
                if(!content.contains(Regex("[~\"#%&*:<>?/\\\\{|}]"))){
                    textAreaFile = content
                    numberLinesFile = if(textAreaFile!="") { textAreaFile.lines().size } else{ 0 }
                    colorNumberFile = setColorNumber(numberLinesFile, numCap.toInt())
                }
                else{
                    filePath = ""
                    dialogChartIllegal = true
                }

            }catch (error :Exception){
                Log.error(error.toString())
            }
        }

        DirectoryPicker(showDirPicker) { path ->
            showDirPicker = false

            if (path != null){
                try {
                    val dir = path.toString()
                    textAreaFolder = fileReader.listFolder(dir)
                    folderPath = dir
                    numberLinesFolder = if(textAreaFolder!="") {
                        textAreaFolder.lines().size
                    }else{
                        0
                    }

                    colorNumberFolder = setColorNumber(numberLinesFolder, numCap.toInt())
                }catch (error :Exception){
                    Log.error(error.toString())
                }
            }

        }

        Column(
            modifier = Modifier.padding(10.dp).fillMaxSize()
        ) {

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){

                TextField(
                    modifier = Modifier.height(52.dp).fillMaxWidth(0.4f),
                    textStyle = TextStyle(fontSize = 13.sp),
                    label = {Text("Nombre de la serie", fontSize = 12.sp)},
                    value = show,
                    singleLine = true,
                    onValueChange = {
                        show = it
                        exampleEpisode = reloadExample(show, season, start)
                    }
                )

                TextField(
                    modifier = Modifier.height(52.dp).fillMaxSize(0.35f),
                    textStyle = TextStyle(fontSize = 12.sp),
                    label = {Text(text = "Temporada", fontSize = 12.sp)},
                    value = season,
                    singleLine = true,
                    onValueChange = {
                        if (!it.contains(Regex("\\D"))) {
                            season = it
                            exampleEpisode = reloadExample(show, season, start)
                        }
                    },
                )

                TextField(
                    modifier = Modifier.height(52.dp).fillMaxSize(0.5f),
                    textStyle = TextStyle(fontSize = 13.sp),
                    label = {Text(text = "N° Capitulos", fontSize = 12.sp)},
                    value = numCap,
                    singleLine = true,
                    onValueChange = {
                        if (!it.contains(Regex("\\D"))){
                            numCap = it
                            exampleEpisode = reloadExample(show, season, start)
                            try {
                                colorNumberFolder = setColorNumber(numberLinesFolder, numCap.toInt())
                                colorNumberFile = setColorNumber(numberLinesFile, numCap.toInt())
                            }catch (error :Exception){
                                Log.error(error.toString())
                            }
                        }
                    }
                )

                TextField(
                    modifier = Modifier.height(52.dp).fillMaxSize(0.92f),
                    textStyle = TextStyle(fontSize = 13.sp),
                    label = {Text(text = "Iniciar en", fontSize = 12.sp)},
                    value = start,
                    singleLine = true,
                    onValueChange = {
                        if (!it.contains(Regex("\\D"))) {
                            start = it
                            exampleEpisode = reloadExample(show, season, start)
                        }
                    }
                )

            }

            Spacer(Modifier.size(15.dp))

            Text(exampleEpisode, fontSize = 12.sp)

            Spacer(Modifier.size(10.dp))

            Row(
                modifier = Modifier.fillMaxHeight(0.85f).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){

                Column(
                    modifier = Modifier.fillMaxHeight().fillMaxWidth(0.49f)
                ) {
                    Box (
                        modifier = Modifier.fillMaxHeight(0.82f)
                    ){

                        val stateAreaVertical = rememberScrollState(0)
                        val stateAreaHorizontal = rememberScrollState(0)

                        TextField(
                            value = textAreaFolder,
                            textStyle = TextStyle(fontSize = 12.sp),
                            modifier = Modifier
                                .fillMaxWidth().fillMaxHeight()
                                .verticalScroll(stateAreaVertical).horizontalScroll(stateAreaHorizontal),
                            onValueChange = { }
                        )
                        Text(
                            text = numberLinesFolder.toString(),
                            color = colorNumberFolder,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.BottomEnd).padding(horizontal = 5.dp, vertical = 3.dp)
                        )
                        VerticalScrollbar(
                            modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd),
                            adapter = ScrollbarAdapter(stateAreaVertical)
                        )
                        HorizontalScrollbar(
                            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                            adapter = ScrollbarAdapter(stateAreaHorizontal)
                        )
                    }

                    Spacer(Modifier.fillMaxHeight(0.1f))

                    Row (
                        modifier = Modifier.fillMaxHeight().fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        val stateHorizontal = rememberScrollState(0)

                        Box(Modifier.fillMaxWidth(0.7f)) {

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(stateHorizontal),
                                singleLine = true,
                                textStyle = TextStyle(fontSize = 12.sp),
                                value = folderPath,
                                onValueChange = {}
                            )
                            HorizontalScrollbar(
                                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                                adapter = ScrollbarAdapter(stateHorizontal)
                            )
                        }

                        Button(
                            modifier = Modifier.fillMaxWidth(0.95f),
                            onClick = {
                                showDirPicker = true
                            }
                        ){
                            Text("Carpeta", fontSize = 12.sp)
                        }
                    }
                }

                Column(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.97f)) {
                    Box (
                        modifier = Modifier.fillMaxHeight(0.82f)
                    ){

                        val stateAreaVertical = rememberScrollState(0)
                        val stateAreaHorizontal = rememberScrollState(0)

                        TextField(
                            value = textAreaFile,
                            textStyle = TextStyle(fontSize = 12.sp),
                            modifier = Modifier
                                .fillMaxWidth().fillMaxHeight()
                                .verticalScroll(stateAreaVertical).horizontalScroll(stateAreaHorizontal),
                            onValueChange = {
                                if (!it.contains(Regex("[~\"#%&*:<>?/\\\\{|}]"))) {
                                    textAreaFile = it
                                    try {
                                        numberLinesFile = if(textAreaFile!="") {
                                            textAreaFile.lines().size
                                        }else{
                                            0
                                        }
                                        colorNumberFile = setColorNumber(numberLinesFile, numCap.toInt())
                                    }catch (error :Exception){
                                        Log.error(error.toString())
                                    }
                                }
                            }
                        )
                        Text(
                            text = numberLinesFile.toString(),
                            color = colorNumberFile,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.BottomEnd).padding(horizontal = 5.dp, vertical = 3.dp)
                        )
                        VerticalScrollbar(
                            modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd),
                            adapter = ScrollbarAdapter(stateAreaVertical)
                        )
                        HorizontalScrollbar(
                            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                            adapter = ScrollbarAdapter(stateAreaHorizontal)
                        )
                    }

                    Spacer(Modifier.fillMaxHeight(0.1f))

                    Row (
                        modifier = Modifier.fillMaxHeight().fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        val stateHorizontal = rememberScrollState(0)

                        Box(Modifier.fillMaxWidth(0.7f)) {

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(stateHorizontal),
                                singleLine = true,
                                textStyle = TextStyle(fontSize = 12.sp),
                                value = filePath,
                                onValueChange = {}
                            )
                            HorizontalScrollbar(
                                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                                adapter = ScrollbarAdapter(stateHorizontal)
                            )
                        }

                        Button(
                            modifier = Modifier.fillMaxWidth(0.95f),
                            onClick = {
                                showFilePicker = true
                            }
                        ){
                            Text("Cargar", fontSize = 12.sp)
                        }
                    }
                }

            }

            Spacer(Modifier.fillMaxHeight(0.2f))

            Row(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                FilledTonalButton(
                    onClick = {
                        textAreaFolder = coreActions.ctrlZ("backup.txt", folderPath, StandardCharsets.UTF_8)
                        enableCtrlZ = false
                    },
                    enabled = enableCtrlZ,
                    modifier = Modifier.fillMaxWidth(0.45f)
                ) {
                    Text("Ctrl Z", fontSize = 12.sp)
                }
                Button(
                    onClick = {
                        try {
                            if (season.isNotEmpty()&&start.isNotEmpty()&&folderPath.isNotEmpty()&&textAreaFile.isNotEmpty()){

                                if(numberLinesFile==numberLinesFolder&&numberLinesFolder==numCap.toInt()){
                                    textAreaFolder = coreActions.apply(show, season, start.toInt(), textAreaFile, folderPath)
                                    enableCtrlZ = true
                                }
                                else{
                                    dialogErrorEpisodes = true
                                }
                            }
                            else{
                                dialogEmpty = true
                            }
                        }catch (e:Exception){
                            Log.error(e.toString())
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Aplicar", fontSize = 12.sp)
                }
            }

            Spacer(Modifier.fillMaxHeight(0.15f))
        }

    }
}

fun reloadExample(nameTvShow: String, numSeason: String, numStart: String): String {

    val start = if (numStart.length==1) { "0$numStart" } else { numStart }
    var season = if (numSeason.length==1) { "0$numSeason" } else { numSeason }
    season = if (numSeason=="0") { "" } else { "${season}x" }
    val show = if(nameTvShow!=""){ "$nameTvShow " } else {nameTvShow}

    return "$show${season}$start. Ejemplo de capitulo"
}

fun setColorNumber(numberLines:Int, numberEpisodes:Int): Color {
    return if(numberLines == numberEpisodes){
        Color(0xFF409947)
    } else{
        Color(0xFF993426)
    }
}


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        //resizable = false,
        title = "Renamer Episodes",
        state = rememberWindowState(size = DpSize(780.dp, 600.dp))
    ) {
        this.window.minimumSize = Dimension(780,600)
        App(window)
    }
}
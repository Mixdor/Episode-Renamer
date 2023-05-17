package case

import java.nio.charset.Charset

class CoreActions {

    private val fileWriter = FileWriter()
    private val fileReader = FileReader()

    fun ctrlZ(pathSrcFile: String, pathDesFolder:String, encoding: Charset): String {

        val content = fileReader.readTxtFile(pathSrcFile, encoding)
        fileWriter.renameWithBackup(pathDesFolder,content.split(Regex("\\n")))

        return fileReader.listFolder(pathDesFolder)
    }

    fun apply(nameTvShow:String, numSeason:String, start:Int, listNewNames:String, folderPath:String) : String {

        //Backup
        fileWriter.setBackup(folderPath)

        //Rename
        var listNames = listNewNames.split(Regex("\n"))
        listNames = listNames.map {

            var episodes = (listNames.indexOf(it)+start).toString()
            episodes = if (episodes.length==1) { "0$episodes" } else { episodes }
            var season = if (numSeason.length==1) { "0$numSeason" } else { numSeason }
            season = if (numSeason=="0") { "" } else { "${season}x" }
            val tvShow = if(nameTvShow!=""){ "$nameTvShow " } else {nameTvShow}

            "$tvShow${season}${episodes}. $it"
        }

        fileWriter.renameFiles(folderPath,listNames)

        return fileReader.listFolder(folderPath)
    }

}
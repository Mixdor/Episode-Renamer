package case

import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

class FileReader {

    fun listFolder(dir:String):String{

        val folder = File(dir)
        var textAreaFolder = ""
        val files = folder.listFiles() ?: emptyArray()

        for(index in files.indices){
            val type = if (files[index].isFile){ "File" } else { "Dir " }
            textAreaFolder += if (index!=files.lastIndex) "[ $type ]  ${files[index].name}\n"
            else "[ $type ]  ${files[index].name}"
        }

        return textAreaFolder
    }

    fun readTxtFile(path: String, encoding: Charset): String {
        val encoded = Files.readAllBytes(Paths.get(path))
        return String(encoded, encoding)
    }

}
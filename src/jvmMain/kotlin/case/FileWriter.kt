package case

import org.apache.commons.io.FilenameUtils
import java.io.File
import java.io.FileWriter

class FileWriter {
    fun renameFiles(folderPath:String, listNames:List<String>) {

        val folder = File(folderPath)
        val files = folder.listFiles() ?: emptyArray()

        for(index in files.indices){

            val fileSource = files[index]
            val fileExtension = FilenameUtils.getExtension(fileSource.name)
            val fileDest =  File("${fileSource.parent}/${listNames[index]}.${fileExtension}")
            fileSource.renameTo(fileDest)

        }

    }

    fun renameWithBackup(folderPath:String, listNames:List<String>){

        val folder = File(folderPath)
        val files = folder.listFiles() ?: emptyArray()

        for(index in files.indices){

            val fileSource = files[index]
            val fileDest =  File("${fileSource.parent}/${listNames[index]}")
            fileSource.renameTo(fileDest)

        }

    }

    fun setBackup(folderPath: String){

        var backupStr = ""
        val folder = File(folderPath)
        val files = folder.listFiles() ?: emptyArray()


        for (index in files.indices){
            backupStr += if (index!=files.lastIndex) { "${files[index].name}\n" } else { files[index].name }
        }

        val newFileBackup = FileWriter("backup.txt")
        newFileBackup.write(backupStr)
        newFileBackup.close()

    }

}
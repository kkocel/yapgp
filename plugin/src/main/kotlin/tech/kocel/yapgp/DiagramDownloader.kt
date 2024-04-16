package tech.kocel.yapgp

import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

class DiagramDownloader {
    companion object {
        const val PLANTUML_SERVER = "https://www.plantuml.com/plantuml/"
    }

    fun downloadFile(
        diagramContent: String,
        fileFormat: FileFormat,
        outputFileName: File,
    ) {
        val url = URL("$PLANTUML_SERVER${fileFormat.name.lowercase()}/$diagramContent}")
        url.openStream().use {
            Channels.newChannel(it).use { rbc ->
                FileOutputStream(outputFileName).use { fos ->
                    fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
                }
            }
        }
    }
}

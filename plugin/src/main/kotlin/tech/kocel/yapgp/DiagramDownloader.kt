package tech.kocel.yapgp

import com.github.michaelbull.retry.policy.RetryPolicy
import com.github.michaelbull.retry.policy.fullJitterBackoff
import com.github.michaelbull.retry.policy.stopAtAttempts
import com.github.michaelbull.retry.retry
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

class DiagramDownloader(private val serverPath: String = "https://www.plantuml.com/plantuml/") {
    fun downloadFile(
        diagramContent: String,
        fileFormat: FileFormat,
        outputFile: File,
    ) {
        runBlocking {
            val policy: RetryPolicy<Throwable> = RetryPolicy(stopAtAttempts(10), fullJitterBackoff(min = 10L, max = 5000L))
            retry(policy) {
                downloadContent(diagramContent, fileFormat, outputFile)
            }
        }
    }

    private fun downloadContent(
        diagramContent: String,
        fileFormat: FileFormat,
        outputFileName: File,
    ) {
        url(diagramContent, fileFormat).openStream().use {
            Channels.newChannel(it).use { rbc ->
                FileOutputStream(outputFileName).use { fos ->
                    fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
                }
            }
        }
    }

    fun url(
        diagramContent: String,
        fileFormat: FileFormat,
    ): URL {
        return URL("$serverPath${fileFormat.name.lowercase()}/~1$diagramContent")
    }
}

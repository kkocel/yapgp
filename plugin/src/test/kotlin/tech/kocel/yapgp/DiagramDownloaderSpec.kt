package tech.kocel.yapgp

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DiagramDownloaderSpec : StringSpec({
    "get url for diagram" {
        val output = DiagramDownloader().url("foo", FileFormat.SVG).toString()

        // Verify the result
        output.shouldBe("https://www.plantuml.com/plantuml/svg/~1foo")
    }
})

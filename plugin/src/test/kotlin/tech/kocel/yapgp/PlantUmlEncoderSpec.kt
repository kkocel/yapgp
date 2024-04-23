package tech.kocel.yapgp

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PlantUmlEncoderSpec : StringSpec({
    "encoder encodes input to plant uml Base64 variant" {
        val output = PlantUmlEncoder().encode("Hello, PlantUML!")

        // Verify the result
        output.shouldBe("UDhpICt9oTTH2CX9p2i9zVLH100ksWLF")
    }
})

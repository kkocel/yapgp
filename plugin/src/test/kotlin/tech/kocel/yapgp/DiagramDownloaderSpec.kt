package tech.kocel.yapgp

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.any
import com.github.tomakehurst.wiremock.client.WireMock.anyUrl
import com.github.tomakehurst.wiremock.common.Slf4jNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.stubbing.Scenario
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempfile
import io.kotest.extensions.wiremock.ListenerMode
import io.kotest.extensions.wiremock.WireMockListener
import io.kotest.matchers.shouldBe

class DiagramDownloaderSpec : FunSpec({
    val serviceServer =
        WireMockServer(
            WireMockConfiguration()
                .dynamicPort()
                .notifier(Slf4jNotifier(true)),
        )
    listener(WireMockListener(serviceServer, ListenerMode.PER_SPEC))

    test("verify retry logic") {

        val url = serviceServer.baseUrl() + "/"

        serviceServer.stubFor(
            any(anyUrl()).inScenario("First malformed response")
                .whenScenarioStateIs(Scenario.STARTED)
                .willSetStateTo("SERVER_HEALED")
                .willReturn(aResponse().withStatus(400)),
        )

        serviceServer.stubFor(
            any(anyUrl()).inScenario("First malformed response")
                .whenScenarioStateIs("SERVER_HEALED")
                .willReturn(aResponse().withBody("OK"))
                .willSetStateTo(Scenario.STARTED),
        )

        val outputFile = tempfile()
        DiagramDownloader(url).downloadFile("foo", FileFormat.SVG, outputFile)

        outputFile.readText() shouldBe "OK"
    }

    test("verify url for diagram") {
        val output = DiagramDownloader().url("foo", FileFormat.SVG).toString()

        // Verify the result
        output.shouldBe("https://www.plantuml.com/plantuml/svg/~1foo")
    }
})

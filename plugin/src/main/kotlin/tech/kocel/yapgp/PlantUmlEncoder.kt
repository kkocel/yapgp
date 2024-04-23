package tech.kocel.yapgp

import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.util.zip.Deflater

class PlantUmlEncoder {
    fun encode(input: String): String {
        val encodedString = input.toByteArray(StandardCharsets.UTF_8)

        val deflater = Deflater(9, false)
        deflater.setInput(encodedString)
        deflater.finish()

        val outputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)

        while (!deflater.finished()) {
            val compressedSize = deflater.deflate(buffer)
            outputStream.write(buffer, 0, compressedSize)
        }

        val deflated = outputStream.toByteArray()

        val encodeTable =
            charArrayOf(
                '0',
                '1',
                '2',
                '3',
                '4',
                '5',
                '6',
                '7',
                '8',
                '9',
                'A',
                'B',
                'C',
                'D',
                'E',
                'F',
                'G',
                'H',
                'I',
                'J',
                'K',
                'L',
                'M',
                'N',
                'O',
                'P',
                'Q',
                'R',
                'S',
                'T',
                'U',
                'V',
                'W',
                'X',
                'Y',
                'Z',
                'a',
                'b',
                'c',
                'd',
                'e',
                'f',
                'g',
                'h',
                'i',
                'j',
                'k',
                'l',
                'm',
                'n',
                'o',
                'p',
                'q',
                'r',
                's',
                't',
                'u',
                'v',
                'w',
                'x',
                'y',
                'z',
                '-',
                '_',
            )
                .map { it.code.toByte() }
                .toByteArray()

        val base64EncodedBytes =
            Base64(
                0,
                null,
                encodeTable,
                BaseNCodec.DECODING_POLICY_DEFAULT,
            ).encode(deflated)

        return base64EncodedBytes.toString(Charsets.UTF_8)
    }
}

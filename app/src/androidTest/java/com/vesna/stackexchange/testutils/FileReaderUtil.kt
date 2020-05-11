package com.vesna.stackexchange.testutils

import android.content.res.AssetManager
import java.io.*

fun AssetManager.fileAsString(relativeFilePath: String): String {
    var reader: Reader? = null
    try {
        reader = BufferedReader(InputStreamReader(this.open(relativeFilePath)))
        return reader.readLines()
            .fold(StringBuilder(), { builder, string -> builder.append(string) })
            .toString()
    } finally {
        reader?.close()
    }
}
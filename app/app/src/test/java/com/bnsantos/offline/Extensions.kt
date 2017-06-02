package com.bnsantos.offline

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

fun enqueueResponse(mockWebServer: MockWebServer, fileName: String, loader: ClassLoader){
    enqueueResponse(mockWebServer, resourceAsInputStream(loader, fileName), emptyMap<String, String>())
}

fun enqueueResponse(mockWebServer: MockWebServer, inputStream: InputStream, headers: Map<String, String>){
    val source = Okio.buffer(Okio.source(inputStream))
    val response = MockResponse()
    for (key in headers.keys) {
        response.addHeader(key, headers[key])
    }
    mockWebServer.enqueue(response.setBody(source.readString(StandardCharsets.UTF_8)))
}

fun <T> loadFromResource(fileName: String, loader: ClassLoader, type: Type): T {
    val resourceAsStream = resourceAsInputStream(loader, fileName)
    val targetReader = InputStreamReader(resourceAsStream)
    val reader = JsonReader(targetReader)
    val data = Gson().fromJson<T>(reader, type)
    targetReader.close()
    return data
}

private fun resourceAsInputStream(loader: ClassLoader, fileName: String) = loader.getResourceAsStream("api-response/" + fileName)


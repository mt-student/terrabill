package com.example.terrabill.data.http

import android.util.Log
import fi.iki.elonen.NanoHTTPD
import org.json.JSONObject

class HttpServer : NanoHTTPD(8080) {

    override fun serve(session: IHTTPSession): Response {
        Log.d("HTTP", "Request: ${session.method} ${session.uri}")

        if (session.method == Method.POST && session.uri == "/api/request") {
            val body = mutableMapOf<String, String>()
            return try {
                session.parseBody(body)
                val requestBody = body["postData"] ?: return newFixedLengthResponse(
                    Response.Status.BAD_REQUEST,
                    "text/plain",
                    "Fehlender Body"
                )

                val json = JSONObject(requestBody)
                val title = json.getString("title")
                val description = json.getString("description")

                Log.d("HTTP", "Empfangene Anfrage: $title - $description")

                newFixedLengthResponse(
                    Response.Status.OK,
                    "application/json",
                    """{"status":"received"}"""
                )
            } catch (e: Exception) {
                Log.e("HTTP", "Fehler beim Verarbeiten", e)
                newFixedLengthResponse(
                    Response.Status.BAD_REQUEST,
                    "text/plain",
                    "Ungültige JSON-Anfrage"
                )
            }
        }

        return newFixedLengthResponse(
            Response.Status.NOT_FOUND,
            "text/plain",
            "Pfad nicht gefunden: ${session.uri}"
        )
    }
}
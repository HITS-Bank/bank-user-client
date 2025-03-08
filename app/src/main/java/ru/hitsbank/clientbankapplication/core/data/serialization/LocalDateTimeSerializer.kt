package ru.hitsbank.clientbankapplication.core.data.serialization

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneOffset

object LocalDateTimeSerializer : JsonSerializer<LocalDateTime> {

    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        if (src != null) {
            return JsonPrimitive(src.toEpochSecond(ZoneOffset.UTC))
        }
        return JsonPrimitive(0)
    }
}

object LocalDateTimeDeserializer: JsonDeserializer<LocalDateTime> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime {
        if (json != null) {
            return LocalDateTime.ofEpochSecond(json.asLong,0, ZoneOffset.UTC)
        }
        return LocalDateTime.ofEpochSecond(0,0, ZoneOffset.UTC)
    }
}
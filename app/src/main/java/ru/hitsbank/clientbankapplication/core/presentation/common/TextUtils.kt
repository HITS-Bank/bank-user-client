package ru.hitsbank.clientbankapplication.core.presentation.common

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

const val RUB_SYMBOL = "₽"

// TODO обрезать копейки если они .00
fun String?.formatToSum(): String {
    if (this == null) return ""

    val integralPart = this.substringBefore(".")
    var fractionalPart = this.substringAfter(".", "")
    if (fractionalPart.length == fractionalPart.count { it == '0' }) {
        fractionalPart = ""
    }

    val digitsOnly = integralPart.replace("\\D".toRegex(), "")
    if (digitsOnly.isEmpty()) return "0 $RUB_SYMBOL"

    val formattedAmount = StringBuilder()
    var count = 0
    for (i in digitsOnly.length - 1 downTo 0) {
        formattedAmount.append(digitsOnly[i])
        count++
        if (count % 3 == 0 && i != 0) {
            formattedAmount.append(' ')
        }
    }

    return if (fractionalPart.isNotBlank()) {
        formattedAmount.reverse().toString() + "," + fractionalPart + " $RUB_SYMBOL"
    } else {
        formattedAmount.reverse().toString() + " $RUB_SYMBOL"
    }
}

fun String?.formatToSumWithoutSpacers(): String {
    if (this == null) return ""

    val digitsOnly = this.replace("[^\\d.]".toRegex(), "")
    if (digitsOnly.isEmpty()) return "0 $RUB_SYMBOL"

    return buildString {
        append(digitsOnly)
        append(" ")
        append(RUB_SYMBOL)
    }
}

fun String?.utcDateTimeToReadableFormat(): String {
    if (this == null) return ""
    return try {
        val localDateTime = LocalDateTime.parse(this)
        val zonedUtcDateTime = localDateTime.atZone(ZoneOffset.UTC)
        val localDateTimeInDefaultZone = zonedUtcDateTime.withZoneSameInstant(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("HH:mm, d MMMM yyyy", Locale("ru"))
        localDateTimeInDefaultZone.format(formatter)
    } catch (e: Exception) {
        ""
    }
}

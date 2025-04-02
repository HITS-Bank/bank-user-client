package ru.hitsbank.clientbankapplication.core.presentation.common

import ru.hitsbank.bank_common.domain.entity.CurrencyCode
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

private fun CurrencyCode.toSymbol(): Char {
    return when (this) {
        CurrencyCode.RUB -> '₽'
        CurrencyCode.KZT -> '₸'
        CurrencyCode.CNY -> '¥'
    }
}

// TODO обрезать копейки если они .00
fun String?.formatToSum(currencyCode: CurrencyCode, isWithoutSpacers: Boolean = false): String {
    if (this == null) return ""

    val integralPart = this.substringBefore(".")
    var fractionalPart = this.substringAfter(".", "")
    if (fractionalPart.length == fractionalPart.count { it == '0' }) {
        fractionalPart = ""
    }

    val digitsOnly = integralPart.replace("\\D".toRegex(), "")
    if (digitsOnly.isEmpty()) return "0 ${currencyCode.toSymbol()}"

    val formattedAmount = StringBuilder()
    var count = 0
    for (i in digitsOnly.length - 1 downTo 0) {
        formattedAmount.append(digitsOnly[i])
        count++
        if (count % 3 == 0 && i != 0) {
            formattedAmount.append(' ')
        }
    }

    val mainPart = formattedAmount.reverse().toString().also { if (isWithoutSpacers) this.replace(" ", "") }
    return if (fractionalPart.isNotBlank()) {
        "$mainPart,$fractionalPart ${currencyCode.toSymbol()}"
    } else {
        "$mainPart ${currencyCode.toSymbol()}"
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

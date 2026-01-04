package com.example.friendsindeed

import android.content.Context

object CurrencyManager {
    private const val PREFS_NAME = "CurrencyPrefs"
    private const val KEY_CURRENCY_SYMBOL = "currency_symbol"
    private const val KEY_CURRENCY_CODE = "currency_code"
    private const val DEFAULT_SYMBOL = "₹"
    private const val DEFAULT_CODE = "INR"

    data class Currency(val code: String, val symbol: String, val name: String)

    val supportedCurrencies = listOf(
        Currency("INR", "₹", "Indian Rupee"),
        Currency("USD", "$", "US Dollar"),
        Currency("EUR", "€", "Euro"),
        Currency("GBP", "£", "British Pound"),
        Currency("JPY", "¥", "Japanese Yen"),
        Currency("AUD", "A$", "Australian Dollar"),
        Currency("CAD", "C$", "Canadian Dollar")
    )

    fun getCurrencySymbol(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_CURRENCY_SYMBOL, DEFAULT_SYMBOL) ?: DEFAULT_SYMBOL
    }

    fun getCurrencyCode(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_CURRENCY_CODE, DEFAULT_CODE) ?: DEFAULT_CODE
    }

    fun setCurrency(context: Context, currency: Currency) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString(KEY_CURRENCY_SYMBOL, currency.symbol)
            putString(KEY_CURRENCY_CODE, currency.code)
            apply()
        }
    }

    fun getCurrentCurrency(context: Context): Currency {
        val code = getCurrencyCode(context)
        return supportedCurrencies.find { it.code == code } ?: supportedCurrencies.first()
    }
}

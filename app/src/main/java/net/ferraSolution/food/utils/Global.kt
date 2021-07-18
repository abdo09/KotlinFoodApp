package net.ferraSolution.food.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import timber.log.Timber

@SuppressLint("ObsoleteSdkInt")
fun Context.getCustomColor(context: Context, color: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(color, theme)
    } else {
        ContextCompat.getColor(context, color)
    }
}

@SuppressLint("ObsoleteSdkInt")
fun Context.getCustomColor(color: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(color, theme)
    } else {
        ContextCompat.getColor(this, color)
    }
}

fun String.digitsOnly(): String {
    val regex = Regex("[^0-9]")
    return regex.replace(this, "")
}

fun isOdd(value: Int): Boolean {
    return value and 0x01 != 0
}

fun getDoubleValue(value: Double): String {
    @SuppressLint("DefaultLocale")
    var enAmount = String.format("%.2f", value)
    enAmount =
        enAmount.replace(",".toRegex(), ".").replace("٫".toRegex(), ".").replace("٠".toRegex(), "0")
            .replace("١".toRegex(), "1").replace("٢".toRegex(), "2").replace("٣".toRegex(), "3")
            .replace("٤".toRegex(), "4")
            .replace("٥".toRegex(), "5").replace("٦".toRegex(), "6").replace("٧".toRegex(), "7")
            .replace("٨".toRegex(), "8").replace("٩".toRegex(), "9")
    return enAmount
}

fun getFloatValue(value: Float): String {
    @SuppressLint("DefaultLocale")
    var enAmount = String.format("%.2f", value)
    enAmount =
        enAmount.replace(",".toRegex(), ".").replace("٫".toRegex(), ".").replace("٠".toRegex(), "0")
            .replace("١".toRegex(), "1").replace("٢".toRegex(), "2").replace("٣".toRegex(), "3")
            .replace("٤".toRegex(), "4")
            .replace("٥".toRegex(), "5").replace("٦".toRegex(), "6").replace("٧".toRegex(), "7")
            .replace("٨".toRegex(), "8").replace("٩".toRegex(), "9")
    return enAmount
}

fun String.replaceAll(): String {
    return this.replace(",".toRegex(), ".").replace("٫".toRegex(), ".").replace("٠".toRegex(), "0")
        .replace("١".toRegex(), "1").replace("٢".toRegex(), "2").replace("٣".toRegex(), "3")
        .replace("٤".toRegex(), "4")
        .replace("٥".toRegex(), "5").replace("٦".toRegex(), "6").replace("٧".toRegex(), "7")
        .replace("٨".toRegex(), "8").replace("٩".toRegex(), "9")
}

fun String.replaceAllId(): String {
    return this.replace("01".toRegex(), "0").replace("02".toRegex(), "1")
        .replace("03".toRegex(), "2")
        .replace("04".toRegex(), "3").replace("05".toRegex(), "4")
        .replace("06".toRegex(), "5")
        .replace("07".toRegex(), "6").replace("08".toRegex(), "7")
        .replace("09".toRegex(), "8")
        .replace("10".toRegex(), "9")
        .replace("11".toRegex(), "10")
}

fun String.replaceAllNumbers(): String {
    return this.replace("0".toRegex(), "01").replace("1".toRegex(), "02")
        .replace("2".toRegex(), "03")
        .replace("3".toRegex(), "04").replace("4".toRegex(), "05")
        .replace("5".toRegex(), "06")
        .replace("6".toRegex(), "07").replace("7".toRegex(), "08")
        .replace("8".toRegex(), "09")
        .replace("9".toRegex(), "10")
        .replace("10".toRegex(), "11")
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)

fun Double.approachDoubleValue(): Double {
    val actualValue = this.format(1)
    val decimalToRound = actualValue.substring(actualValue.length - 1).toInt()
    var returnedValue = 0.0
    if (decimalToRound in 1..4 || decimalToRound == 0) {
        returnedValue = 0.0
    } else if (decimalToRound == 5) {
        returnedValue = 0.5
    } else if (decimalToRound in 6..9){
        returnedValue = 1.0
    }
    return returnedValue
}
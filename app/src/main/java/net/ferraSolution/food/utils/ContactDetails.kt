@file: JvmName("ContactDetails")
package net.ferraSolution.food.utils

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import androidx.fragment.app.Fragment
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

object ContactDetails {
     fun getContactDetails(mobilePhone: String,fragment:Fragment): HashMap<Int, String> {
        val phoneUtil = PhoneNumberUtil.getInstance()
        val hashMap = HashMap<Int, String>()
        val loc = Locale.getDefault()
        val telephony = fragment.requireContext()
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simCountryIso = telephony.simCountryIso
        val dNetworkCountryIso = telephony.networkCountryIso.toUpperCase(loc)

         val parsedNumber: Phonenumber.PhoneNumber?

         try {
            parsedNumber = phoneUtil.parse(mobilePhone, dNetworkCountryIso.toUpperCase(Locale.ROOT))
            val isMobileNumValid = phoneUtil.isValidNumber(parsedNumber)
            return if (isMobileNumValid) {
                val validNum =
                    phoneUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
                hashMap[parsedNumber.countryCode] = parsedNumber.nationalNumber.toString()
                hashMap
            } else {

                hashMap
            }


        } catch (e: NumberParseException) {
            e.printStackTrace()
            return hashMap
        }

    }

    fun isValidContact (mobilePhone: String,fragment: Fragment):Boolean {
        val phoneUtil = PhoneNumberUtil.getInstance()
        val hashMap = HashMap<Int, String>()
        val loc = Locale.getDefault()
        val telephony = fragment.requireContext()
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val dNetworkCountryIso = telephony.networkCountryIso.toUpperCase(loc)

        val parsedNumber: Phonenumber.PhoneNumber?

        return try {
            parsedNumber = phoneUtil.parse(mobilePhone, dNetworkCountryIso.toUpperCase(Locale.ROOT))
            val isMobileNumValid = phoneUtil.isValidNumber(parsedNumber)
            val isNotEarthLine = phoneUtil.getNumberType(parsedNumber) == PhoneNumberUtil.PhoneNumberType.MOBILE

            isMobileNumValid && isNotEarthLine


        } catch (e: NumberParseException) {
            false
        }

    }

    // Check if contact exist
    @SuppressLint("Recycle")
    fun contactExists(context: Context, number: String): Any {

        val lookupUri: Uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(number)
        )

        val mPhoneNumberProjection = arrayOf(
            ContactsContract.PhoneLookup._ID,
            ContactsContract.PhoneLookup.NUMBER,
            ContactsContract.PhoneLookup.DISPLAY_NAME
        )
        try {
            val cur: Cursor =
                context.contentResolver.query(
                    lookupUri,
                    mPhoneNumberProjection,
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    null
                )!!
            cur.use { cursor ->
                if (cursor.moveToFirst()) {
                    return cursor.getString(2)
                }
            }
        } catch (e: Exception) {
            return false
        }

        return false
    }
}
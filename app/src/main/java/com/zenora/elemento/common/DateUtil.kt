@file:Suppress("unused")

package com.zenora.elemento.common

import android.text.format.DateUtils
import android.util.Log
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility for Fetching and manipulate the Dates and Time
 */
class DateUtil private constructor() {
    private val sdf = SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault())

    companion object {
        const val DEFAULT_DATE_FORMAT = "dd/MM/yyyy"
        private const val DATE_FORMAT_MMMM_DD = "MMMM dd, yyyy"
        private const val DEFAULT_TIME_FORMAT = "hh.mm aa"
        private const val DEFAULT_TIME_AND_DATE_FORMAT = "hh:mm aa, dd/MM/yyyy"
        private val instance = DateUtil()
        private const val TAG = "DateUtil"
    }


    /**
     * Function for Getting Current Date
     *
     * @return : current date
     */
    val currentDate: String
        get() = sdf.format(Calendar.getInstance().time)


    /**
     * Function for Converting formatted date string to Milliseconds
     *
     * @param date : date in string
     * @param dateFormat : date format
     * @return :  converted date in milliseconds
     */
    fun convertDateToMilliseconds(date: String?, dateFormat: String): Long {
        if (date.isNullOrEmpty()) return 0
        var mDate = Date()
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        try {
            mDate = formatter.parse(date)!!
        } catch (e: ParseException) {
            Log.e(TAG, e.message!!)
        }
        return mDate.time
    }


    /**
     * Function for Changing Date Format
     *
     * @param mDate : date in string
     * @param inputDateFormat : current date format
     * @param outputDateFormat : needed date format
     * @return : converted date
     */
    fun changeDateFormat(
        mDate: String?,
        inputDateFormat: String,
        outputDateFormat: String
    ): String? {
        if (mDate.isNullOrEmpty()) return "null"
        val inputFormat: DateFormat = SimpleDateFormat(inputDateFormat, Locale.getDefault())
        val outputFormat: DateFormat = SimpleDateFormat(outputDateFormat, Locale.getDefault())
        val date: Date?
        try {
            date = inputFormat.parse(mDate)
            if (date != null) return outputFormat.format(date)
        } catch (e: ParseException) {
            Log.e(TAG, e.message!!)
        }
        return null
    }


    /**
     * get custom calender with UTC zone
     *
     * @param calendar : calender instance
     * @param date : date value
     * @param dateFormat : needed date format
     * @return : first day of current month
     */
    fun getCurrentMonthFirstDayToMilliSeconds(
        calendar: Calendar,
        date: Int,
        dateFormat: String
    ): String {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        calendar.timeZone = formatter.timeZone
        calendar[Calendar.DAY_OF_MONTH] = date
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        return convertDateToMilliseconds(formatter.format(calendar.time), dateFormat).toString()
    }


    /**
     * get formatted string of given date value
     *
     * @param time : date value in Date format
     * @param dateFormat : expected date format
     * @return : formatted string of the given date
     */
    fun convertDateToString(time: Date?, dateFormat: String): String {
        if (time == null) return "null"
        val format1 = SimpleDateFormat(dateFormat, Locale.getDefault())
        return format1.format(time)
    }


    /**
     * Converts Date object into string format as for e.g. **April 25th, 2012**
     *
     * @param date : date value
     * @return string format of provided date object
     */
    fun getCustomDateString(date: Date): String {
        var dateFormat = SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault())
        var dateString = dateFormat.format(date)
        dateString =
            dateString.substring(0, 1).uppercase(Locale.getDefault()) + dateString.substring(1)
        dateString = if (date.date in 11..13) dateString + "th, " else {
            when {
                dateString.endsWith("1") -> dateString + "st, "
                dateString.endsWith("2") -> dateString + "nd, "
                dateString.endsWith("3") -> dateString + "rd, "
                else -> dateString + "th, "
            }
        }
        dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        dateString += dateFormat.format(date)
        return dateString
    }


    /**
     * Function for Checking if The Date is Today's Date
     *
     * @param timeInMillis : time in milli seconds
     * @return : whether date is today or not
     */
    fun isToday(timeInMillis: Long): Boolean {
        return DateUtils.isToday(timeInMillis)
    }


    /**
     * Function for Checking if The Date is Tomorrow's Date
     *
     * @param date : time in milli seconds
     * @return : whether date is tomorrow or not
     */
    fun isTomorrow(date: Long): Boolean {
        val now = Calendar.getInstance()
        val currentDate = Calendar.getInstance()
        currentDate.timeInMillis = date
        now.add(Calendar.DATE, +1)
        return now[Calendar.YEAR] == currentDate[Calendar.YEAR]
                && now[Calendar.MONTH] == currentDate[Calendar.MONTH]
                && now[Calendar.DATE] == currentDate[Calendar.DATE]
    }


    /**
     * Checks if two dates are of the same day.
     *
     * @param millisFirst  The time in milliseconds of the first date.
     * @param millisSecond The time in milliseconds of the second date.
     * @return Whether {@param millisFirst} and {@param millisSecond} are off the same day.
     */
    fun isSameDate(millisFirst: Long, millisSecond: Long): Boolean {
        val dateFormat = SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(millisFirst) == dateFormat.format(millisSecond)
    }


    /**
     * Return date in specified format.
     *
     * @param milliSeconds Date in milliseconds
     * @return String representing date in specified format
     */
    fun convertMillisecondsToDate(milliSeconds: Long): String {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault())
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }


    /**
     * Return date and time in specified format.
     *
     * @param milliSeconds Date in milliseconds
     * @return String representing date in specified format
     */
    fun convertMillisecondsToDateTime(milliSeconds: Long): String {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(DEFAULT_TIME_AND_DATE_FORMAT, Locale.getDefault())
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time).uppercase(Locale.getDefault())
    }


    /**
     * Convert Milliseconds To HH:mm
     */
    fun convertMillisecondsToTime(milliseconds: Long): String {
        return SimpleDateFormat(DEFAULT_TIME_FORMAT, Locale.getDefault())
            .format(Date(milliseconds))
    }


    /**
     * Pass your date format and no of days for minus from current
     * If you want to get previous date then pass days with minus sign
     * else you can pass as it is for next date
     *
     * @param dateFormat : date format string
     * @param days       : number of days
     * @return Calculated Date
     */
    fun getCalculatedDate(dateFormat: String?, days: Int): String {
        val calendar = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat, Locale.getDefault())
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(calendar.timeInMillis))
    }


    /**
     * Date Validator for Checking if Start Date is < End Date
     *
     * @param startDate : start date
     * @param endDate   : end date
     * @return : date grater of lesser
     */
    fun dateComparator(startDate: String, endDate: String, dateFormat: String): Boolean {
        try {
            val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
            val date1 = sdf.parse(startDate)
            val date2 = sdf.parse(endDate)
            if (date1 != null && date1.after(date2)) return false
        } catch (e: Exception) {
            Log.e(TAG, e.message!!)
        }
        return true
    }


    /**
     * to check whether given date is past or not
     *
     * @return whether given date is today of future
     */
    fun isDateCurrentOrFuture(date: Long, dateFormat: String): Boolean {
        try {
            val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
            val dateValue = formatter.parse(convertMillisecondsToDate(date))
            val currentDate =
                formatter.parse(convertMillisecondsToDate(System.currentTimeMillis()))
            if (dateValue != null) {
                return !dateValue.before(currentDate)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message!!)
        }
        return false
    }


    /**
     * Function for getting timestamp for only date value
     *
     * @param timestamp : time in milli seconds
     * @param dateFormat : expected date format
     * @return : timestamp for date only without time
     */
    fun getTimeStampForDateOnly(timestamp: Long, dateFormat: String): Long {
        val dateString = convertMillisecondsToDate(timestamp)
        return instance.convertDateToMilliseconds(dateString, dateFormat)
    }
}
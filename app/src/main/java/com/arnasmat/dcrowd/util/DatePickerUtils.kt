package com.arnasmat.dcrowd.util

import android.app.DatePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Utility object for date picker functionality and Unix timestamp conversion.
 */
object DatePickerUtils {

    /**
     * Standard date format for display (ISO 8601 date format)
     */
    private val displayDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /**
     * Date format including time
     */
    private val displayDateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    /**
     * Converts a Unix timestamp (seconds) to a formatted date string
     *
     * @param unixTimestamp Unix timestamp in seconds
     * @param includeTime Whether to include time in the format
     * @return Formatted date string (e.g., "2024-01-15")
     */
    fun formatUnixTimestamp(unixTimestamp: Long, includeTime: Boolean = false): String {
        val date = Date(unixTimestamp * 1000L)
        return if (includeTime) {
            displayDateTimeFormat.format(date)
        } else {
            displayDateFormat.format(date)
        }
    }

    /**
     * Converts milliseconds to Unix timestamp (seconds)
     *
     * @param milliseconds Time in milliseconds
     * @return Unix timestamp in seconds
     */
    fun millisToUnixTimestamp(milliseconds: Long): Long {
        return milliseconds / 1000L
    }

    /**
     * Converts Unix timestamp (seconds) to milliseconds
     *
     * @param unixTimestamp Unix timestamp in seconds
     * @return Time in milliseconds
     */
    fun unixTimestampToMillis(unixTimestamp: Long): Long {
        return unixTimestamp * 1000L
    }

    /**
     * Gets the current Unix timestamp in seconds
     *
     * @return Current Unix timestamp
     */
    fun getCurrentUnixTimestamp(): Long {
        return System.currentTimeMillis() / 1000L
    }

    /**
     * Creates a Unix timestamp for a specific date at midnight
     *
     * @param year Year (e.g., 2024)
     * @param month Month (0-11, where 0 is January)
     * @param dayOfMonth Day of month (1-31)
     * @param useUTC Whether to use UTC timezone (default: false, uses local timezone)
     * @return Unix timestamp in seconds
     */
    fun createUnixTimestamp(
        year: Int,
        month: Int,
        dayOfMonth: Int,
        useUTC: Boolean = false
    ): Long {
        val calendar = if (useUTC) {
            Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        } else {
            Calendar.getInstance()
        }

        calendar.apply {
            set(year, month, dayOfMonth, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return calendar.timeInMillis / 1000L
    }

    /**
     * Shows a DatePickerDialog and returns the selected date as Unix timestamp
     *
     * @param context Android context
     * @param initialUnixTimestamp Initial date to show (defaults to current date)
     * @param minDate Minimum selectable date in Unix timestamp (optional)
     * @param maxDate Maximum selectable date in Unix timestamp (optional)
     * @param onDateSelected Callback with selected Unix timestamp in seconds
     */
    fun showDatePicker(
        context: Context,
        initialUnixTimestamp: Long = getCurrentUnixTimestamp(),
        minDate: Long? = null,
        maxDate: Long? = null,
        onDateSelected: (Long) -> Unit
    ) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = unixTimestampToMillis(initialUnixTimestamp)
        }

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedTimestamp = createUnixTimestamp(year, month, dayOfMonth)
                onDateSelected(selectedTimestamp)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Set min/max dates if provided
        minDate?.let {
            datePickerDialog.datePicker.minDate = unixTimestampToMillis(it)
        }
        maxDate?.let {
            datePickerDialog.datePicker.maxDate = unixTimestampToMillis(it)
        }

        datePickerDialog.show()
    }

    /**
     * Adds days to a Unix timestamp
     *
     * @param unixTimestamp Base Unix timestamp in seconds
     * @param days Number of days to add (can be negative)
     * @return New Unix timestamp in seconds
     */
    fun addDays(unixTimestamp: Long, days: Int): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = unixTimestampToMillis(unixTimestamp)
            add(Calendar.DAY_OF_MONTH, days)
        }
        return calendar.timeInMillis / 1000L
    }

    /**
     * Checks if a Unix timestamp represents a date in the past
     *
     * @param unixTimestamp Unix timestamp to check
     * @return true if the date is in the past
     */
    fun isPastDate(unixTimestamp: Long): Boolean {
        return unixTimestamp < getCurrentUnixTimestamp()
    }

    /**
     * Checks if a Unix timestamp represents today
     *
     * @param unixTimestamp Unix timestamp to check
     * @return true if the date is today
     */
    fun isToday(unixTimestamp: Long): Boolean {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val tomorrow = Calendar.getInstance().apply {
            timeInMillis = today.timeInMillis
            add(Calendar.DAY_OF_MONTH, 1)
        }

        val checkDate = unixTimestampToMillis(unixTimestamp)
        return checkDate >= today.timeInMillis && checkDate < tomorrow.timeInMillis
    }
}


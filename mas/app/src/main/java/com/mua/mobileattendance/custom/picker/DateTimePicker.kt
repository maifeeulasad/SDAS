package com.mua.mobileattendance.custom.picker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.View
import android.widget.Button
import com.mua.mobileattendance.listener.DateTimeChangeListener
import java.util.*

class DateTimePicker(
    button: Button,
    providedDateTime: Date?,
    context: Context,
    dateTimeChangeListener: DateTimeChangeListener
) : View.OnClickListener {
    private val context: Context
    private val dateTimeChangeListener: DateTimeChangeListener
    private var dateTime: Date

    init {
        button.setOnClickListener(this)
        this.context = context
        this.dateTimeChangeListener = dateTimeChangeListener
        if (providedDateTime != null)
            this.dateTime = providedDateTime
        else
            this.dateTime = Date(Calendar.getInstance().timeInMillis)
    }

    override fun onClick(p0: View?) {
        DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                val res = Date(pickedDateTime.timeInMillis)
                dateTimeChangeListener.onDateTimeChange(res)
            }, dateTime.hours, dateTime.minutes, false).show()
        }, dateTime.year + OFFSET_YEAR, dateTime.month, dateTime.day + OFFSET_DAY).show()
    }

    companion object {
        var OFFSET_DAY = 1
        var OFFSET_YEAR = 1900
    }

}
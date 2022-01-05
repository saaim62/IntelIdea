package com.example.fypintelidea.features.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.fypintelidea.R
import com.example.fypintelidea.core.utils.MyDateTimeStamp
import com.savvi.rangedatepicker.CalendarPickerView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateRangePickerActivity : AppCompatActivity() {

    internal lateinit var calendar: CalendarPickerView
    internal lateinit var button: Button
    private val datesRange = ArrayList<Date>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_range_picker)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = resources.getString(R.string.select_range_caps)
            actionBar.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.elevation = 0f
        }

        val bundle = intent.extras
        if (bundle != null) {
            val selectedDueDateGreaterThanEqualToDatePickerLong =
                bundle.getString(KEY_SELECTED_DUEDATE_GREATER_THAN_EQUAL_TO_DATE_PICKER)
            var selectedDueDateGreaterThanEqualToDatePickerDate: Date? = null
            if (selectedDueDateGreaterThanEqualToDatePickerLong != null) {
                selectedDueDateGreaterThanEqualToDatePickerDate =
                    MyDateTimeStamp.dateTimeStringToDate(
                        selectedDueDateGreaterThanEqualToDatePickerLong
                    )
            }
            val selectedDueDateLessThanEqualToDatePickerLong =
                bundle.getString(KEY_SELECTED_DUEDATE_LESS_THAN_EQUAL_TO_DATE_PICKER)
            var selectedDueDateLessThanEqualToDatePickerDate: Date? = null
            if (selectedDueDateLessThanEqualToDatePickerLong != null) {
                selectedDueDateLessThanEqualToDatePickerDate = MyDateTimeStamp.dateTimeStringToDate(
                    selectedDueDateLessThanEqualToDatePickerLong
                )
            }
            if (selectedDueDateGreaterThanEqualToDatePickerDate != null && selectedDueDateLessThanEqualToDatePickerDate != null) {
                datesRange.add(selectedDueDateGreaterThanEqualToDatePickerDate)
                datesRange.add(selectedDueDateLessThanEqualToDatePickerDate)
            }
        }

        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 10)

        val lastYear = Calendar.getInstance()
        lastYear.add(Calendar.YEAR, -10)

        calendar = findViewById(R.id.calendar_view)
        button = findViewById(R.id.get_selected_dates)
        val list = ArrayList<Int>()
        calendar.deactivateDates(list)
        val arrayList = ArrayList<Date>()
        try {
            val dateformat = SimpleDateFormat("dd-MM-yyyy")
            val strdate = "18-11-2018"
            val strdate2 = "20-11-2018"
            val newdate = dateformat.parse(strdate)
            val newdate2 = dateformat.parse(strdate2)
            arrayList.add(newdate)
            arrayList.add(newdate2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        calendar.init(
            lastYear.time,
            nextYear.time,
            SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        ) //
            .inMode(CalendarPickerView.SelectionMode.RANGE) //
            //                .withSelectedDate(new Date())
            .withSelectedDates(datesRange)
            .withDeactivateDates(list)
        //                .withHighlightedDates(arrayList);

        button.setOnClickListener {
            if (calendar.selectedDates.size > 0) {
                //                ArrayList<String> checkedList = new ArrayList<>();
                //                for (int i = 0; i < calendar.getSelectedDates().size(); i++) {
                //                    checkedList.add(calendar.getSelectedDates().get(i).getTime() + "");
                //                }
                val returnIntent = Intent()
                //sending as millis in string
                returnIntent.putExtra(
                    KEY_SELECTED_DUEDATE_GREATER_THAN_EQUAL_TO_DATE_PICKER,
                    calendar.selectedDates[0].time.toString()
                )
                returnIntent.putExtra(
                    KEY_SELECTED_DUEDATE_LESS_THAN_EQUAL_TO_DATE_PICKER,
                    calendar.selectedDates[calendar.selectedDates.size - 1].time.toString()
                )
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            } else {
                val returnIntent = Intent()
                setResult(Activity.RESULT_CANCELED, returnIntent)
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val KEY_SELECTED_DUEDATE_GREATER_THAN_EQUAL_TO_DATE_PICKER =
            "selected_due_date_greater_than_equal_to_date_picker"
        const val KEY_SELECTED_DUEDATE_LESS_THAN_EQUAL_TO_DATE_PICKER =
            "selected_due_date_less_than_equal_to_date_picker"
        const val DATE_RANGE = "date_range"
    }
}

package tk.only5.ptsmoodle;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by DHRUMESH on 10/11/2016.
 */

public class DatePickerEditText extends EditText implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    Calendar calendar;
    private Context context;
    private int year, monthOfYear, dayOfMonth;

    public DatePickerEditText(Context context) {
        super(context);
        init(context);
    }

    public DatePickerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DatePickerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public void onClick(View view) {
        DatePickerDialog dialog = new DatePickerDialog(context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
        updateDisplay();
    }

    private void init(Context context) {
        this.setOnClickListener(this);
        this.context = context;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
    }

    private void updateDisplay() {
        this.setText(new StringBuilder()
                .append(dayOfMonth)
                .append("/")
                .append(monthOfYear + 1)
                .append("/")
                .append(year)
        );
    }
}

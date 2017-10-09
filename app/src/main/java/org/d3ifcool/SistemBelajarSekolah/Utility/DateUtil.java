package org.d3ifcool.SistemBelajarSekolah.Utility;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

/**
 * Created by Zein Ersyad on 9/11/2017.
 */

public class DateUtil extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
}

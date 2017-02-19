package fr.piotr.reactions.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

import fr.piotr.reactions.EditRuleActivity;
import fr.piotr.reactions.events.time.HourMinute;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        Intent intent = new Intent(EditRuleActivity.EVENT_TIME_PICKED_UP);
        intent.putExtra(EditRuleActivity.EXTRA_TIME, new HourMinute(hourOfDay, minute));
        LocalBroadcastManager.getInstance(getDialog().getContext()).sendBroadcast(intent);
    }
}
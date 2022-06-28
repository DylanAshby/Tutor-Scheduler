package codewithcal.au.calendarappexample;

import static java.lang.Integer.parseInt;
import static codewithcal.au.calendarappexample.CalendarUtils.formattedTime;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.type.DateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class EventEditActivity extends AppCompatActivity
{
    private EditText eventNameET;
    private TextView eventDateTV;
    private Button eventStartButton, eventEndButton;
    private boolean edit;
    int startHour, startMinute, endHour, endMinute;

    private LocalTime time, oldStartTime, oldEndTime;
    private LocalDate date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        edit = getIntent().getBooleanExtra("isEdit", false);
        setContentView(R.layout.activity_event_edit);
        findViewById(R.id.deleteButton).setVisibility(View.INVISIBLE);
        initWidgets();
        time = LocalTime.now();
        date = CalendarUtils.selectedDate;
        startHour = time.getHour();
        startMinute = time.getMinute();
        endHour = time.plusHours(1).getHour();
        endMinute = time.getMinute();
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(date));
        eventStartButton.setText(formattedTime(time));
        eventEndButton.setText(formattedTime(time.plusHours(1)));
        if (edit) {
            findViewById(R.id.deleteButton).setVisibility(View.VISIBLE);
            String times = getIntent().getStringExtra("times");
            EditText editText = (EditText)findViewById(R.id.eventNameET);
            editText.setText(times.substring(37), TextView.BufferType.EDITABLE);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            oldStartTime = LocalTime.parse(times.substring(11,16), timeFormatter);
            oldEndTime = LocalTime.parse(times.substring(24,29), timeFormatter);
            startHour = oldStartTime.getHour();
            startMinute = oldStartTime.getMinute();
            endHour = oldEndTime.getHour();
            endMinute = oldEndTime.getMinute();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date = LocalDate.parse(times.substring(0, 10), dateFormatter);
            eventDateTV.setText("Date: " + CalendarUtils.formattedDate(date));
            eventStartButton.setText(formattedTime(oldStartTime));
            eventEndButton.setText(formattedTime(oldEndTime));
        }
    }

    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventStartButton = findViewById(R.id.eventStartButton);
        eventEndButton = findViewById(R.id.eventEndButton);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveEventAction(View view)
    {
        if (time.withHour(endHour).withMinute(endMinute).isBefore(time.withHour(startHour).withMinute(startMinute)))
        {
            Toast.makeText(this, "End time cannot be before Start time.", Toast.LENGTH_SHORT).show();
            return;
        }
        String eventName = eventNameET.getText().toString();
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time.withHour(startHour).withMinute(startMinute), time.withHour(endHour).withMinute(endMinute));
        if (edit) {
            for (int i = 0; i < Event.eventsList.size(); ++i) {
                if (Event.eventsList.get(i).getDate().equals(date) &&
                    Event.eventsList.get(i).getTimeStart().getHour() == oldStartTime.getHour() &&
                    Event.eventsList.get(i).getTimeStart().getMinute() == oldStartTime.getMinute() &&
                    Event.eventsList.get(i).getTimeEnd().getHour() == oldEndTime.getHour() &&
                    Event.eventsList.get(i).getTimeEnd().getMinute() == oldEndTime.getMinute())
                {
                    Event.eventsList.remove(i);
                    break;
                }
            }
        }
        // check here for if there is a conflict with this event and any other event
        Event.eventsList.add(newEvent);
        finish();
    }

    public void popTimePickerStart(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                startHour = selectedHour;
                startMinute = selectedMinute;
                eventStartButton.setText(formattedTime(time.withHour(startHour).withMinute(startMinute)));
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, startHour, startMinute, false);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void popTimePickerEnd(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                endHour = selectedHour;
                endMinute = selectedMinute;
                eventEndButton.setText(formattedTime(time.withHour(endHour).withMinute(endMinute)));
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, endHour, endMinute, false);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteEventAction(View view) {
        for (int i = 0; i < Event.eventsList.size(); ++i) {
            if (Event.eventsList.get(i).getDate().equals(date) &&
                Event.eventsList.get(i).getTimeStart().getHour() == oldStartTime.getHour() &&
                Event.eventsList.get(i).getTimeStart().getMinute() == oldStartTime.getMinute() &&
                Event.eventsList.get(i).getTimeEnd().getHour() == oldEndTime.getHour() &&
                Event.eventsList.get(i).getTimeEnd().getMinute() == oldEndTime.getMinute())
            {
                Event.eventsList.remove(i);
                break;
            }
        }
        finish();
    }
}
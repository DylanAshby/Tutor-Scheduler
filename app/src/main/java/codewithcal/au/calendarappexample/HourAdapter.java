package codewithcal.au.calendarappexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HourAdapter extends ArrayAdapter<HourEvent>
{

    public HourAdapter(@NonNull Context context, List<HourEvent> hourEvents)
    {
        super(context, 0, hourEvents);
    }


    public void editEventAction(View view)
    {
        Intent intent = new Intent(getContext(), EventEditActivity.class);
        intent.putExtra("isEdit", true);
        intent.putExtra("times", view.getContentDescription());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hour_cell, parent, false);

        HourEvent event = getItem(position);

        TextView event1 = convertView.findViewById(R.id.event1);
        event1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEventAction(view);
            }
        });

        setHour(convertView, event.time);
        setEvents(convertView, event.events);

        return convertView;
    }

    private void setHour(View convertView, LocalTime time)
    {
        TextView timeTV = convertView.findViewById(R.id.timeTV);
        timeTV.setText(CalendarUtils.formattedShortTime(time));
    }

    private void setEvents(View convertView, ArrayList<Event> events)
    {
        TextView event1 = convertView.findViewById(R.id.event1);

        if(events.size() == 0)
        {
            hideEvent(event1);
        }
        else
        {
            Event event = events.get(0);
            event1.setContentDescription(event.getDate() + " " + event.getTimeStart() + " " + event.getTimeEnd() + " " + event.getName());
            setEvent(event1, event);
        }
    }

    private void setEvent(TextView textView, Event event)
    {
        textView.setText(event.getName());
        textView.setVisibility(View.VISIBLE);
    }

    private void hideEvent(TextView tv)
    {
        tv.setVisibility(View.INVISIBLE);
    }

}














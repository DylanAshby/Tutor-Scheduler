package codewithcal.au.calendarappexample;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event
{
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate date)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<Event> eventsForDateAndTime(LocalDate date, LocalTime time)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList)
        {
            int eventStartHour = event.timeStart.getHour();
            int cellHour = time.getHour();
            if(event.getDate().equals(date) && eventStartHour == cellHour)
                events.add(event);
        }

        return events;
    }

    private String name;
    private LocalDate date;
    private LocalTime timeStart, timeEnd;

    public Event(String name, LocalDate date, LocalTime timeStart, LocalTime timeEnd)
    {
        this.name = name;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public LocalTime getTimeStart()
    {
        return timeStart;
    }

    public LocalTime getTimeEnd()
    {
        return timeEnd;
    }

    public void setTimeStart(LocalTime time)
    {
        timeStart = time;
    }

    public void setTimeEnd(LocalTime time)
    {
        timeEnd = time;
    }
}

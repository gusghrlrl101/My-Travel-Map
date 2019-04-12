package com.example.mytravelmap;


import android.content.Context;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;


import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import org.w3c.dom.CDATASection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {
    private MaterialCalendarView calendarView;
    private MyInterface myInterface;
    final private List<CalendarDay> days = new ArrayList<>();

    public interface MyInterface {
        List<LocalDate> getDates();
    }

    public static MyFragment newInstance() {
        Bundle args = new Bundle();

        MyFragment fragment = new MyFragment();

        fragment.setArguments(args);
        return fragment;
    }


    public MyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myInterface = (MyInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_my, container, false);
        calendarView = (MaterialCalendarView) layout.findViewById(R.id.calendarView);
        calendarView.setDateSelected(CalendarDay.today(), true);

        List<LocalDate> list = myInterface.getDates();
        for (LocalDate date : list) {
            CalendarDay day = CalendarDay.from(date);
            days.add(day);
        }
        calendarView.addDecorator(new Decorator(days));

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                if (days.contains(calendarDay)) {

                }
            }
        });

        return layout;
    }

    public void addItem(String id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDate date = LocalDate.parse(id, formatter);
        CalendarDay day = CalendarDay.from(date);
        days.add(day);

        calendarView.removeDecorators();
        calendarView.addDecorator(new Decorator(days));
    }

    public void deleteItem(String id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDate date = LocalDate.parse(id, formatter);
        CalendarDay day = CalendarDay.from(date);

        int index = 0;
        for (CalendarDay calendarDay : days) {
            if (calendarDay.equals(day)) {
                days.remove(index);
                break;
            }
            index++;
        }

        calendarView.removeDecorators();
        calendarView.addDecorator(new Decorator(days));
    }
}

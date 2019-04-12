package com.example.mytravelmap;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.w3c.dom.CDATASection;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {
    private MaterialCalendarView calendarView;
    private CalendarInterface calInterface;
    private CalendarDay selectedDay = CalendarDay.today();
    final private List<CalendarDay> days = new ArrayList<>();

    public interface CalendarInterface {
        List<LocalDate> getDates();

        ArrayList<ListViewItem> getList(String key);
    }

    public static CalendarFragment newInstance() {
        Bundle args = new Bundle();

        CalendarFragment fragment = new CalendarFragment();

        fragment.setArguments(args);
        return fragment;
    }


    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        calInterface = (CalendarInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = (MaterialCalendarView) layout.findViewById(R.id.calendarView);
        calendarView.setDateSelected(CalendarDay.today(), true);

        List<LocalDate> list = calInterface.getDates();
        for (LocalDate date : list) {
            CalendarDay day = CalendarDay.from(date);
            days.add(day);
        }
        calendarView.addDecorator(new Decorator(days));

        calendarView.setOnDateLongClickListener(new OnDateLongClickListener() {
            @Override
            public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
                selectedDay = date;
                if (days.contains(date))
                    gridView(date);
            }
        });

        return layout;
    }

    private void gridView(CalendarDay calendarDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String key = calendarDay.getDate().format(formatter);

        ArrayList<ListViewItem> list = calInterface.getList(key);

        Intent intent = new Intent(getActivity(), GridActivity.class);
        intent.putExtra("list", list);
        startActivityForResult(intent, 5111);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            getActivity().finishAffinity();
        }
    }

    public String getDay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String day = selectedDay.getDate().format(formatter);

        return day;
    }
}

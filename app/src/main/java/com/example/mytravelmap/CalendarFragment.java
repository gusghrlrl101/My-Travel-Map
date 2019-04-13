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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        calInterface = (CalendarInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_calendar, container, false);
        // 캘린더뷰 연결
        calendarView = layout.findViewById(R.id.calendarView);
        calendarView.setDateSelected(CalendarDay.today(), true);
        // 날짜 받아오기
        List<LocalDate> list = calInterface.getDates();
        for (LocalDate date : list) {
            CalendarDay day = CalendarDay.from(date);
            days.add(day);
        }
        // 점찍기
        calendarView.addDecorator(new Decorator(days));
        // 날짜 클릭 리스너
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedDay = date;
            }
        });
        // 날짜 롱클릭 리스너
        calendarView.setOnDateLongClickListener(new OnDateLongClickListener() {
            @Override
            public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
                selectedDay = date;
                // 사진이 있는 날짜인 경우 Grid 액티비티로 이동
                if (days.contains(date))
                    gridView(date);
            }
        });

        return layout;
    }

    private void gridView(CalendarDay calendarDay) {
        // 날짜 형식으로 바꿔줌
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String key = calendarDay.getDate().format(formatter);

        // 해당 날짜가 있는 data만 가져옴
        ArrayList<ListViewItem> list = calInterface.getList(key);

        // 해당 데이터들을 Grid 액티비티에 전달
        Intent intent = new Intent(getActivity(), GridActivity.class);
        intent.putExtra("list", list);
        startActivityForResult(intent, 5111);
    }

    public void addItem(String id) {
        // 시간 형식으로 바꿔줌
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDate date = LocalDate.parse(id, formatter);
        CalendarDay day = CalendarDay.from(date);
        // 날짜 추가
        days.add(day);
        // 점찍기 갱신
        calendarView.removeDecorators();
        calendarView.addDecorator(new Decorator(days));
    }

    public void deleteItem(String id) {
        // 시간 형식으로 바꿔줌
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDate date = LocalDate.parse(id, formatter);
        CalendarDay day = CalendarDay.from(date);
        // 해당 날짜 찾아서 지우기
        int index = 0;
        for (CalendarDay calendarDay : days) {
            if (calendarDay.equals(day)) {
                days.remove(index);
                break;
            }
            index++;
        }
        // 점찍기 갱신
        calendarView.removeDecorators();
        calendarView.addDecorator(new Decorator(days));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 공유로 넘어간 경우 액티비티 종료
        if (resultCode == RESULT_OK)
            getActivity().finishAffinity();
    }

    public String getDay() {
        // 선택된 날짜 반환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String day = selectedDay.getDate().format(formatter);

        return day;
    }
}

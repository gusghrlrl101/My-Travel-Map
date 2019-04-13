package com.example.mytravelmap;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.HashSet;
import java.util.List;

// 해당되는 날짜에 모두 점찍기
public class Decorator implements DayViewDecorator {
    private final HashSet<CalendarDay> dates;

    public Decorator(List<CalendarDay> days) {
        dates = new HashSet<>(days);
    }

    @Override
    public boolean shouldDecorate(CalendarDay calendarDay) {
        return dates.contains(calendarDay);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan());
    }
}

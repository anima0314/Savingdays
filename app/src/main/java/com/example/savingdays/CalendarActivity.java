package com.example.savingdays;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;

public class CalendarActivity extends AppCompatActivity  implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        initwidgets();
        selectedDate = LocalDate.now();
        setMonthView();

    }
    private void initwidgets()
    {
        calendarRecyclerView=findViewById(R.id.calendarRecycler);
        monthYearText=findViewById(R.id.mothYearTV);
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth=daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter=new CalendarAdapter(daysInMonth,this);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getApplicationContext(),7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }


    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String>daysInMonthArray=new ArrayList<>();
        YearMonth yearMonth= YearMonth.from(date);
        int daysInMonth =yearMonth.lengthOfMonth();
        LocalDate firstOfMonth=selectedDate.withDayOfMonth(1);
        int dayOfweek=firstOfMonth.getDayOfWeek().getValue();

        for (int i=1 ; i<=42; i++)
        {
            if (i<=dayOfweek||i>daysInMonth+dayOfweek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfweek));
            }
        }
        return daysInMonthArray;

    }


    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("MMMM yyyy");
        return  date.format(formatter);
    }

    public void previousMonthAction(View view)
    {
        selectedDate=selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        selectedDate=selectedDate.plusMonths(1);
        setMonthView();
    }


    @Override
    public void onItemClick(int position, String dayText)
    {
        if (!dayText.equals("")){
            String message = "Selected Date" +dayText+" "+monthYearFromDate(selectedDate);
            Toast.makeText(this,message,Toast.LENGTH_LONG).show();
        }
    }

}

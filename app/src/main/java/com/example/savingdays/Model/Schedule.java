package com.example.savingdays.Model;

import java.time.LocalDate;

public class Schedule {

    private final int id;
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Schedule(int id, String title, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Schedule(String title, LocalDate startDate, LocalDate endDate) {
        this.id = -1;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

}

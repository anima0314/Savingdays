package com.example.savingdays;

import java.time.LocalDate;

public class Food {



    private final int id;
    private final String title;

    private final LocalDate openDate;
    private final LocalDate dueDate;

    public Food(int id, String title,  LocalDate openDate, LocalDate dueDate) {
        this.id = id;
        this.title = title;

        this.openDate = openDate;
        this.dueDate = dueDate;
    }

    public Food(String title, LocalDate openDate, LocalDate dueDate) {
        this.id = -1;
        this.title = title;

        this.openDate = openDate;
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }



    public LocalDate getOpenDate() {
        return openDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }




}

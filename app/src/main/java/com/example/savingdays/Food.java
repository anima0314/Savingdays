package com.example.savingdays;

import java.time.LocalDate;

public class Food {

    public static final int TYPE_EGG = 1;
    public static final int TYPE_MILK = 2;

    private final int id;
    private final String title;
    private final int type;
    private final LocalDate openDate;
    private final LocalDate dueDate;

    public Food(int id, String title, int type, LocalDate openDate, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.openDate = openDate;
        this.dueDate = dueDate;
    }

    public Food(String title, int type, LocalDate openDate, LocalDate dueDate) {
        this.id = -1;
        this.title = title;
        this.type = type;
        this.openDate = openDate;
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public static String getTypeName(int type) {

        switch (type) {
            case TYPE_EGG:
                return "계란";
            case TYPE_MILK:
                return "유제품";
        }
        return "";
    }

    public static int[] getTypes() {
        return new int[] { TYPE_EGG, TYPE_MILK };
    }

}

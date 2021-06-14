package com.example.savingdays.Model;

import java.time.LocalDate;

public class Food {

    public static final int TYPE_EGG = 1;
    public static final int TYPE_MILK = 2;
    public static final int TYPE_BREAD = 3;
    public static final int TYPE_CHEEZE = 4;
    public static final int TYPE_BEANCURD = 5;


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
                return "우유";
            case TYPE_BREAD:
                return "식빵";
            case TYPE_CHEEZE:
                return "치즈";
            case TYPE_BEANCURD:
                return "두부";
        }
        return "";
    }

    public static int[] getTypes() {
        return new int[] { TYPE_EGG, TYPE_MILK ,TYPE_BREAD,TYPE_CHEEZE,TYPE_BEANCURD};
    }

}

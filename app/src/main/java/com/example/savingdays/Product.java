package com.example.savingdays;

import java.time.LocalDate;

public class Product {

    public static final int TYPE_COSMETIC = 1;
    public static final int TYPE_SHAMPOO = 2;

    private final int id;
    private final String title;
    private final int type;
    private final LocalDate openDate;
    private final LocalDate dueDate;

    public Product(int id, String title, int type, LocalDate openDate, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.openDate = openDate;
        this.dueDate = dueDate;
    }

    public Product(String title, int type, LocalDate openDate, LocalDate dueDate) {
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
            case TYPE_COSMETIC:
                return "화장품";
            case TYPE_SHAMPOO:
                return "샴푸";
        }
        return "";
    }

    public static int[] getTypes() {
        return new int[] { TYPE_COSMETIC, TYPE_SHAMPOO };
    }

}

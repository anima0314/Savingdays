package com.example.savingdays.Model;

import java.time.LocalDate;

public class Product {

    public static final int TYPE_COSMETIC_1 = 1;
    public static final int TYPE_COSMETIC_2 = 2;
    public static final int TYPE_SHAMPOO = 3;
    public static final int TYPE_TISSUE = 4;


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
            case TYPE_COSMETIC_1:
                return "기초 화장품";
            case TYPE_COSMETIC_2:
                return "립스틱";
            case TYPE_SHAMPOO:
                return "샴푸";
            case TYPE_TISSUE:
                return "물티슈";
        }
        return "";
    }

    public static int[] getTypes() {
        return new int[] { TYPE_COSMETIC_1,TYPE_COSMETIC_2, TYPE_SHAMPOO,TYPE_TISSUE};
    }

}

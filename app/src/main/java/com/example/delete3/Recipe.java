package com.example.delete3;

public class Recipe {
    private int id;
    private String title;
    private String ingredients;
    private String author;
    private String category;
    private int cookingTime;

    public Recipe(int id, String title, String ingredients, String author, String category, int cookingTime) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.author = author;
        this.category = category;
        this.cookingTime = cookingTime;
    }

    // Геттеры и сеттеры здесь
}

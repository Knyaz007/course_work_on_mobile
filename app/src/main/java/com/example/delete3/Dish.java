package com.example.delete3;

public class Dish {

    private int dishId;
    private String dishName;
    private String description;
    private int cookingTime;
    private byte[] imageData;

    public Dish(int dishId, String dishName, String description, int cookingTime, byte[] imageData) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.description = description;
        this.cookingTime = cookingTime;
        this.imageData = imageData;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}

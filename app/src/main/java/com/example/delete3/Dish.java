package com.example.delete3;

public class Dish {

    private int dishId;
    private String dishName;
    private String description;
    private int cookingTime;
    private String step_description;
    private String ingredients;
    private byte[] imageData;

    public String getStep_description() {
        return step_description;
    }

    public void setStep_description(String step_description) {
        this.step_description = step_description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Dish(int dishId, String dishName, String description, int cookingTime, String step_description, String ingredients, byte[] imageData) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.description = description;
        this.cookingTime = cookingTime;
        this.step_description = step_description;
        this.ingredients = ingredients;
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

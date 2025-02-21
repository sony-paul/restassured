package org.example;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ReceipeResponse {
    public ArrayList<Recipe> recipes;
    public int total;
    public int skip;
    public int limit;


    public static class Recipe{
        public int id;
        public String name;
        public ArrayList<String> ingredients;
        public ArrayList<String> instructions;
        public int prepTimeMinutes;
        public int cookTimeMinutes;
        public int servings;
        public String difficulty;
        public String cuisine;
        public int caloriesPerServing;
        public ArrayList<String> tags;
        public int userId;
        public String image;
        public double rating;
        public int reviewCount;
        public ArrayList<String> mealType;
    }


}

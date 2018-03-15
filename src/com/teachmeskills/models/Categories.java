package com.teachmeskills.models;

import com.teachmeskills.database.StorehouseConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Categories {
    private int id_categories;
    private String categories_name;

    public Categories(int id_categories, String categories_name) {
        this.id_categories = id_categories;
        this.categories_name = categories_name;
    }

    public Categories() {
    }

    public int getId_categories() {
        return id_categories;
    }

    public void setId_categories(int id_categories) {
        this.id_categories = id_categories;
    }

    public String getCategories_name() {
        return categories_name;
    }

    public void setCategories_name(String categories_name) {
        this.categories_name = categories_name;
    }

    @Override
    public String toString() {
        return categories_name;
    }

    public static ArrayList<Categories> getCategories() {
        try {
            ArrayList<Categories> categories = new ArrayList<>();
            Statement statement = StorehouseConnector.getConection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM categories");
            while (resultSet.next()) {
                categories.add(new Categories(resultSet.getInt("id_categories"),
                        resultSet.getString("categories_name")));
            }
            return categories;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<Categories>();
    }
}

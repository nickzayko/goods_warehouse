package com.teachmeskills.models;

import com.teachmeskills.database.StorehouseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Subcategories {
    private int id_subcategories;
    private String subcategories_name;
    private Categories categories;

    public Subcategories(int id_subcategories, String subcategories_name, Categories categories) {
        this.id_subcategories = id_subcategories;
        this.subcategories_name = subcategories_name;
        this.categories = categories;
    }

    public Subcategories() {}

    public int getId_subcategories() {
        return id_subcategories;
    }

    public void setId_subcategories(int id_subcategories) {
        this.id_subcategories = id_subcategories;
    }

    public String getSubcategories_name() {
        return subcategories_name;
    }

    public void setSubcategories_name(String subcategories_name) {
        this.subcategories_name = subcategories_name;
    }

    public Categories getCategory() {
        return categories;
    }

    public void setCategory(Categories categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return subcategories_name;
    }

    public static ArrayList<Subcategories> getSubcategories() {
        try {
            ArrayList<Subcategories> subcategories = new ArrayList<>();
            Statement statement = StorehouseConnector.getConection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT subcategories.*, categories.* FROM subcategories " +
                    "LEFT JOIN categories ON subcategories.categories_id = categories.id_categories");
            while (resultSet.next()) {
                Subcategories subcategory = new Subcategories();
                subcategory.setId_subcategories(resultSet.getInt("id_subcategories"));
                subcategory.setSubcategories_name(resultSet.getString("subcategories_name"));
                subcategory.categories = new Categories(resultSet.getInt("id_categories"),
                        resultSet.getString("categories_name"));
                subcategories.add(subcategory);
            }
            return subcategories;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<Subcategories>();
    }

    public void save() {
        String sql = " UPDATE subcategories SET subcategories_name = ?, categories_id = (SELECT id_categories FROM categories WHERE " +
                " categories_name = ?) WHERE id_subcategories = ? ";
        try {
            PreparedStatement preparedStatement = StorehouseConnector.getConection().prepareStatement(sql);
            preparedStatement.setString(1, subcategories_name);
            preparedStatement.setString(2, categories.getCategories_name());
            preparedStatement.setString(3, String.valueOf(id_subcategories));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

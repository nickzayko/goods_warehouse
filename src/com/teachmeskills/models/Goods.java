package com.teachmeskills.models;

import com.teachmeskills.database.StorehouseConnector;

import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Goods {
    private int id_goods;
    private String goods_name;
    private String description;
    private int goods_price;
    private int goods_count;
    private Subcategories subcategories;
    private Categories categories;

    public Goods(int id_goods, String goods_name, String description, int goods_price, int goods_count, Subcategories subcategories, Categories categories) {
        this.id_goods = id_goods;
        this.goods_name = goods_name;
        this.description = description;
        this.goods_price = goods_price;
        this.goods_count = goods_count;
        this.subcategories = subcategories;
        this.categories = categories;
    }

    public Goods() {}

    public int getId_goods() {
        return id_goods;
    }

    public void setId_goods(int id_goods) {
        this.id_goods = id_goods;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(int goods_price) {
        this.goods_price = goods_price;
    }

    public int getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(int goods_count) {
        this.goods_count = goods_count;
    }

    public Subcategories getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(Subcategories subcategories) {
        this.subcategories = subcategories;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public static ArrayList<Goods> getGoods() {
        try {
            ArrayList<Goods> goods = new ArrayList<>();
            Statement statement = StorehouseConnector.getConection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT goods.*, subcategories.*, categories.* FROM goods " +
                    "LEFT JOIN subcategories ON goods.subcategories_id = subcategories.id_subcategories " +
                    "LEFT JOIN categories ON subcategories.categories_id = categories.id_categories");
            while (resultSet.next()) {
                Goods good = new Goods();
                good.setGoods_name(resultSet.getString("goods_name"));
                good.setDescription(resultSet.getString("description"));
                good.setGoods_price(resultSet.getInt("goods_price"));
                good.setGoods_count(resultSet.getInt("goods_count"));
                good.subcategories = new Subcategories(resultSet.getInt("id_subcategories"),
                        resultSet.getString("subcategories_name"), new Categories(resultSet.getInt("id_categories"),
                        resultSet.getString("categories_name")));
                good.categories = new Categories(resultSet.getInt("id_categories"), resultSet.getString("categories_name"));
                good.setId_goods(resultSet.getInt("id_goods"));
                goods.add(good);
            }
            return goods;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<Goods>();
    }

    public static ArrayList<Goods> getGoodsBySearch(String search_word1) {
        ArrayList<Goods> goods = new ArrayList<>();
        try {
            String sql = "SELECT goods.*, subcategories.*, categories.* FROM goods " +
                    "LEFT JOIN subcategories ON goods.subcategories_id = subcategories.id_subcategories " +
                    "LEFT JOIN categories ON subcategories.categories_id = categories.id_categories WHERE goods.goods_name LIKE ? " +
                    " || subcategories.subcategories_name LIKE ? || categories.categories_name LIKE ? " +
                    " || goods.id_goods LIKE ?";
            PreparedStatement preparedStatement = StorehouseConnector.getConection().prepareStatement(sql);
            preparedStatement.setString(1, search_word1 + "%");
            preparedStatement.setString(2, search_word1 + "%");
            preparedStatement.setString(3, search_word1 + "%");
            preparedStatement.setString(4, search_word1);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Goods good = new Goods();
                good.setGoods_name(resultSet.getString("goods_name"));
                good.setDescription(resultSet.getString("description"));
                good.setGoods_price(resultSet.getInt("goods_price"));
                good.setGoods_count(resultSet.getInt("goods_count"));
                good.subcategories = new Subcategories(resultSet.getInt("id_subcategories"),
                        resultSet.getString("subcategories_name"), new Categories(resultSet.getInt("id_categories"),
                        resultSet.getString("categories_name")));
                good.categories = new Categories(resultSet.getInt("id_categories"), resultSet.getString("categories_name"));
                good.setId_goods(resultSet.getInt("id_goods"));
                goods.add(good);
            }
            return goods;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<Goods>();
    }

    // начало метода, который фильтрует товары по цене, независимо от поиска
    public static ArrayList<Goods> getGoodsByFiltration(String pricefrom, String priceto, String word_to_search) {
        ArrayList<Goods> goods = new ArrayList<>();
        try {
            if (word_to_search.length() == 0) {
                if (pricefrom.length() != 0 && priceto.length() == 0) {
                    String sql = "SELECT goods.*, subcategories.*, categories.* FROM goods " +
                            "LEFT JOIN subcategories ON goods.subcategories_id = subcategories.id_subcategories " +
                            "LEFT JOIN categories ON subcategories.categories_id = categories.id_categories WHERE goods.goods_price >= ? ";
                    PreparedStatement preparedStatement = StorehouseConnector.getConection().prepareStatement(sql);
                    preparedStatement.setString(1, pricefrom);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        Goods good = new Goods();
                        good.setGoods_name(resultSet.getString("goods_name"));
                        good.setDescription(resultSet.getString("description"));
                        good.setGoods_price(resultSet.getInt("goods_price"));
                        good.setGoods_count(resultSet.getInt("goods_count"));
                        good.subcategories = new Subcategories(resultSet.getInt("id_subcategories"),
                                resultSet.getString("subcategories_name"), new Categories(resultSet.getInt("id_categories"),
                                resultSet.getString("categories_name")));
                        good.categories = new Categories(resultSet.getInt("id_categories"), resultSet.getString("categories_name"));
                        good.setId_goods(resultSet.getInt("id_goods"));
                        goods.add(good);
                    }
                } else {
                    if (pricefrom.length() != 0 && priceto.length() != 0) {
                        String sql = "SELECT goods.*, subcategories.*, categories.* FROM goods " +
                                "LEFT JOIN subcategories ON goods.subcategories_id = subcategories.id_subcategories " +
                                "LEFT JOIN categories ON subcategories.categories_id = categories.id_categories WHERE goods.goods_price >= ? && goods.goods_price <= ?";
                        PreparedStatement preparedStatement = StorehouseConnector.getConection().prepareStatement(sql);
                        preparedStatement.setString(1, pricefrom);
                        preparedStatement.setString(2, priceto);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        while (resultSet.next()) {
                            Goods good = new Goods();
                            good.setGoods_name(resultSet.getString("goods_name"));
                            good.setDescription(resultSet.getString("description"));
                            good.setGoods_price(resultSet.getInt("goods_price"));
                            good.setGoods_count(resultSet.getInt("goods_count"));
                            good.subcategories = new Subcategories(resultSet.getInt("id_subcategories"),
                                    resultSet.getString("subcategories_name"), new Categories(resultSet.getInt("id_categories"),
                                    resultSet.getString("categories_name")));
                            good.categories = new Categories(resultSet.getInt("id_categories"), resultSet.getString("categories_name"));
                            good.setId_goods(resultSet.getInt("id_goods"));
                            goods.add(good);
                        }
                    } else {
                        String sql = "SELECT goods.*, subcategories.*, categories.* FROM goods " +
                                "LEFT JOIN subcategories ON goods.subcategories_id = subcategories.id_subcategories " +
                                "LEFT JOIN categories ON subcategories.categories_id = categories.id_categories WHERE goods.goods_price <= ? ";
                        PreparedStatement preparedStatement = StorehouseConnector.getConection().prepareStatement(sql);
                        preparedStatement.setString(1, priceto);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        while (resultSet.next()) {
                            Goods good = new Goods();
                            good.setGoods_name(resultSet.getString("goods_name"));
                            good.setDescription(resultSet.getString("description"));
                            good.setGoods_price(resultSet.getInt("goods_price"));
                            good.setGoods_count(resultSet.getInt("goods_count"));
                            good.subcategories = new Subcategories(resultSet.getInt("id_subcategories"),
                                    resultSet.getString("subcategories_name"), new Categories(resultSet.getInt("id_categories"),
                                    resultSet.getString("categories_name")));
                            good.categories = new Categories(resultSet.getInt("id_categories"), resultSet.getString("categories_name"));
                            good.setId_goods(resultSet.getInt("id_goods"));
                            goods.add(good);
                        }
                    }
                }
                return goods;
            } else {
                if (pricefrom.length() != 0 && priceto.length() == 0) {
                    String sql = "SELECT goods.*, subcategories.*, categories.* FROM goods " +
                            "LEFT JOIN subcategories ON goods.subcategories_id = subcategories.id_subcategories " +
                            "LEFT JOIN categories ON subcategories.categories_id = categories.id_categories WHERE goods.goods_price >= ?  AND " +
                            " (goods.goods_name LIKE ? || subcategories.subcategories_name LIKE ? || categories.categories_name LIKE ?) " +
                            " || (goods.id_goods LIKE ?)";
                    PreparedStatement preparedStatement = StorehouseConnector.getConection().prepareStatement(sql);
                    preparedStatement.setString(1, pricefrom);
                    preparedStatement.setString(2, word_to_search + "%");
                    preparedStatement.setString(3, word_to_search + "%");
                    preparedStatement.setString(4, word_to_search + "%");
                    preparedStatement.setString(5, word_to_search);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        Goods good = new Goods();
                        good.setGoods_name(resultSet.getString("goods_name"));
                        good.setDescription(resultSet.getString("description"));
                        good.setGoods_price(resultSet.getInt("goods_price"));
                        good.setGoods_count(resultSet.getInt("goods_count"));
                        good.subcategories = new Subcategories(resultSet.getInt("id_subcategories"),
                                resultSet.getString("subcategories_name"), new Categories(resultSet.getInt("id_categories"),
                                resultSet.getString("categories_name")));
                        good.categories = new Categories(resultSet.getInt("id_categories"), resultSet.getString("categories_name"));
                        good.setId_goods(resultSet.getInt("id_goods"));
                        goods.add(good);
                    }
                } else {
                    if (pricefrom.length() != 0 && priceto.length() != 0) {
                        String sql = "SELECT goods.*, subcategories.*, categories.* FROM goods " +
                                "LEFT JOIN subcategories ON goods.subcategories_id = subcategories.id_subcategories " +
                                "LEFT JOIN categories ON subcategories.categories_id = categories.id_categories WHERE goods.goods_price >= ? && goods.goods_price <= ? AND" +
                                " (goods.goods_name LIKE ? || subcategories.subcategories_name LIKE ? || categories.categories_name LIKE ?) " +
                                " || (goods.id_goods LIKE ?)";
                        PreparedStatement preparedStatement = StorehouseConnector.getConection().prepareStatement(sql);
                        preparedStatement.setString(1, pricefrom);
                        preparedStatement.setString(2, priceto);
                        preparedStatement.setString(3, word_to_search + "%");
                        preparedStatement.setString(4, word_to_search + "%");
                        preparedStatement.setString(5, word_to_search + "%");
                        preparedStatement.setString(6, word_to_search);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        while (resultSet.next()) {
                            Goods good = new Goods();
                            good.setGoods_name(resultSet.getString("goods_name"));
                            good.setDescription(resultSet.getString("description"));
                            good.setGoods_price(resultSet.getInt("goods_price"));
                            good.setGoods_count(resultSet.getInt("goods_count"));
                            good.subcategories = new Subcategories(resultSet.getInt("id_subcategories"),
                                    resultSet.getString("subcategories_name"), new Categories(resultSet.getInt("id_categories"),
                                    resultSet.getString("categories_name")));
                            good.categories = new Categories(resultSet.getInt("id_categories"), resultSet.getString("categories_name"));
                            good.setId_goods(resultSet.getInt("id_goods"));
                            goods.add(good);
                        }
                    } else {
                        String sql = "SELECT goods.*, subcategories.*, categories.* FROM goods " +
                                "LEFT JOIN subcategories ON goods.subcategories_id = subcategories.id_subcategories " +
                                "LEFT JOIN categories ON subcategories.categories_id = categories.id_categories WHERE goods.goods_price <= ? AND " +
                                " (goods.goods_name LIKE ? || subcategories.subcategories_name LIKE ? || categories.categories_name LIKE ?) " +
                                " || (goods.id_goods LIKE ?)";
                        PreparedStatement preparedStatement = StorehouseConnector.getConection().prepareStatement(sql);
                        preparedStatement.setString(1, priceto);
                        preparedStatement.setString(2, word_to_search + "%");
                        preparedStatement.setString(3, word_to_search + "%");
                        preparedStatement.setString(4, word_to_search + "%");
                        preparedStatement.setString(5, word_to_search);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        while (resultSet.next()) {
                            Goods good = new Goods();
                            good.setGoods_name(resultSet.getString("goods_name"));
                            good.setDescription(resultSet.getString("description"));
                            good.setGoods_price(resultSet.getInt("goods_price"));
                            good.setGoods_count(resultSet.getInt("goods_count"));
                            good.subcategories = new Subcategories(resultSet.getInt("id_subcategories"),
                                    resultSet.getString("subcategories_name"), new Categories(resultSet.getInt("id_categories"),
                                    resultSet.getString("categories_name")));
                            good.categories = new Categories(resultSet.getInt("id_categories"), resultSet.getString("categories_name"));
                            good.setId_goods(resultSet.getInt("id_goods"));
                            goods.add(good);
                        }
                    }
                }
                return goods;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<Goods>();
    }
    // окончание метода, который фильтровал товары по цене

    @Override
    public String toString() {
        return id_goods + " " + goods_name + " " + description + " " + goods_price + " " + goods_count + " " + subcategories + " " + categories;
    }
}

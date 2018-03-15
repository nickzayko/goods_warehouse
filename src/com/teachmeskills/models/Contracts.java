package com.teachmeskills.models;

import com.teachmeskills.database.StorehouseConnector;

import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Contracts {
    private int id_contracts;
    private int id_goods;
    private String goods_name;
    private String description;
    private int count_of_selled_good;
    private int contract_price;
    private String bayer_name;

    public Contracts(int id_contracts, int id_goods, String goods_name, String description, int count_of_selled_good, int contract_price, String bayer_name) {
        this.id_contracts = id_contracts;
        this.id_goods = id_goods;
        this.goods_name = goods_name;
        this.description = description;
        this.count_of_selled_good = count_of_selled_good;
        this.contract_price = contract_price;
        this.bayer_name = bayer_name;
    }

    public Contracts() {
    }

    public int getId_contracts() {
        return id_contracts;
    }

    public void setId_contracts(int id_contracts) {
        this.id_contracts = id_contracts;
    }

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

    public int getCount_of_selled_good() {
        return count_of_selled_good;
    }

    public void setCount_of_selled_good(int count_of_selled_good) {
        this.count_of_selled_good = count_of_selled_good;
    }

    public int getContract_price() {
        return contract_price;
    }

    public void setContract_price(int contract_price) {
        this.contract_price = contract_price;
    }

    public String getBayer_name() {
        return bayer_name;
    }

    public void setBayer_name(String bayer_name) {
        this.bayer_name = bayer_name;
    }

    public static ArrayList<Contracts> getContracts() {
        try {
            ArrayList<Contracts> contracts = new ArrayList<>();
            Statement statement = StorehouseConnector.getConection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT contracts.* FROM contracts ");
            while (resultSet.next()) {
                Contracts contract = new Contracts();
                contract.setId_contracts(resultSet.getInt("id_contracts"));
                contract.setId_goods(resultSet.getInt("id_goods"));
                contract.setGoods_name(resultSet.getString("goods_name"));
                contract.setDescription(resultSet.getString("goods_description"));
                contract.setCount_of_selled_good(resultSet.getInt("count_of_selled_goods"));
                contract.setContract_price(resultSet.getInt("contract_price"));
                contract.setBayer_name(resultSet.getString("bayer_name"));
                contracts.add(contract);
            }
            return contracts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<Contracts>();
    }

    public static ArrayList<Contracts> getContractsBySearch(String search_word2) {
        ArrayList<Contracts> contracts = new ArrayList<>();
        try {
            String sql = "SELECT contracts.* FROM contracts WHERE id_contracts LIKE ? " + " || id_goods LIKE ? || goods_name LIKE ? || contract_price LIKE ? " + " || bayer_name LIKE ? ";
            PreparedStatement preparedStatement = StorehouseConnector.getConection().prepareStatement(sql);
            preparedStatement.setString(1, search_word2);
            preparedStatement.setString(2, search_word2);
            preparedStatement.setString(3, search_word2 + "%");
            preparedStatement.setString(4, search_word2);
            preparedStatement.setString(5, search_word2 + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Contracts contract = new Contracts();
                contract.setId_contracts(resultSet.getInt("id_contracts"));
                contract.setId_goods(resultSet.getInt("id_goods"));
                contract.setGoods_name(resultSet.getString("goods_name"));
                contract.setDescription(resultSet.getString("goods_description"));
                contract.setCount_of_selled_good(resultSet.getInt("count_of_selled_goods"));
                contract.setContract_price(resultSet.getInt("contract_price"));
                contract.setBayer_name(resultSet.getString("bayer_name"));
                contracts.add(contract);
            }
            return contracts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<Contracts>();
    }
}

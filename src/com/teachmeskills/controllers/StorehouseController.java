package com.teachmeskills.controllers;

import com.teachmeskills.database.StorehouseConnector;
import com.teachmeskills.models.Categories;
import com.teachmeskills.models.Goods;
import com.teachmeskills.models.Subcategories;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class StorehouseController {

    @FXML
    private TableView<Goods> table;
    @FXML
    private TableColumn<Goods, String> goods_name;
    @FXML
    private TableColumn<Goods, Subcategories> subcategories_name;
    @FXML
    private TableColumn<Goods, Categories> categories_name;
    @FXML
    private TableColumn<Goods, Integer> goods_price;
    @FXML
    private TableColumn<Goods, String> description;
    @FXML
    private TableColumn<Goods, Integer> goods_count;
    @FXML
    private TableColumn<Goods, Integer> id_goods;
    @FXML
    private TextField search_word, price_from, price_to, input_goods_name, input_price, input_count;
    @FXML
    private TextArea input_description;
    @FXML
    private ComboBox<Subcategories> subcategory_combo_box;

    private static Stage new_subcategory;
    private static Stage update_good;
    private static Stage sell_good;
    private static Stage show_contracts;
    private static Goods editgood;

    Goods good = new Goods();

    public static Goods getSelectedGood() {
        return editgood;
    }

    public static Stage getUpdate_goodgood() {
        return update_good;
    }

    public static Stage sellGood () { return sell_good; }

    public void initialize() {
        subcategory_combo_box.getItems().setAll(Subcategories.getSubcategories());
        tableInit();
    }

    private void tableInit() {
        goods_name.setCellValueFactory(new PropertyValueFactory<Goods, String>("goods_name"));
        description.setCellValueFactory(new PropertyValueFactory<Goods, String>("description"));
        goods_price.setCellValueFactory(new PropertyValueFactory<Goods, Integer>("goods_price"));
        goods_count.setCellValueFactory(new PropertyValueFactory<Goods, Integer>("goods_count"));
        subcategories_name.setCellValueFactory(new PropertyValueFactory<Goods, Subcategories>("subcategories"));
        categories_name.setCellValueFactory(new PropertyValueFactory<Goods, Categories>("categories"));
        id_goods.setCellValueFactory(new PropertyValueFactory<Goods, Integer>("id_goods"));
        setTableValue();
    }

    private void setTableValue() {
        table.getItems().clear();
        table.getItems().setAll(Goods.getGoods());
    }

    private void setTableValue(ArrayList<Goods> goods) {
        table.getItems().clear();
        table.getItems().setAll(FXCollections.observableArrayList(goods));
    }

    public void search_word(KeyEvent keyEvent) {
        String search_word1 = search_word.getText();
        if (search_word1.length() == 0) {
            setTableValue();
        } else {
            setTableValue(Goods.getGoodsBySearch(search_word1));
        }
    }

    public void filtration_by_price(ActionEvent actionEvent) {
        String pricefrom = price_from.getText();
        String priceto = price_to.getText();
        String word_to_search = search_word.getText();
        if (pricefrom.length() == 0 && priceto.length() == 0 && word_to_search.length() == 0) {
            setTableValue();
        } else {
            setTableValue(Goods.getGoodsByFiltration(pricefrom, priceto, word_to_search));
        }
    }

    public void edit_subcategory(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../views/edit_subcategory.fxml"));
            new_subcategory = new Stage();
            new_subcategory.setTitle("Edit subcategory (Storehouse created by Nick) ");
            new_subcategory.setScene(new Scene(root, 600, 400));
            new_subcategory.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void add_new_good(ActionEvent actionEvent) {
        try {
            String added_name = input_goods_name.getText();
            String added_description = input_description.getText();
            int added_price = Integer.parseInt(input_price.getText());
            int added_count = Integer.parseInt(input_count.getText());
            if (added_name != null && added_description != null && added_price != 0 && added_count != 0) {
                String sql = " INSERT INTO goods VALUES (null, ?, ?, ?, ?, (SELECT id_subcategories FROM subcategories WHERE " +
                        " subcategories_name = ?)) ";
                try {
                    PreparedStatement preparedStatement = StorehouseConnector.getConection().prepareStatement(sql);
                    preparedStatement.setString(1, added_name);
                    preparedStatement.setString(2, added_description);
                    preparedStatement.setString(3, String.valueOf(added_price));
                    preparedStatement.setString(4, String.valueOf(added_count));
                    preparedStatement.setString(5, String.valueOf(subcategory_combo_box.getValue()));
                    preparedStatement.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                input_goods_name.clear();
                input_description.clear();
                input_price.clear();
                input_count.clear();
            } else {
                setTableValue();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ВНИМАНИЕ!");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка при заполнении полей товара!");
            alert.showAndWait();
            input_goods_name.clear();
            input_description.clear();
            input_price.clear();
            input_count.clear();
        }
    }

    public void refresh_window(ActionEvent actionEvent) {
        subcategory_combo_box.getItems().setAll(Subcategories.getSubcategories());
        setTableValue();
    }

    public void delete_good(ActionEvent actionEvent) {
        good = table.getSelectionModel().getSelectedItem();
        if (good != null) {
            int selected_id_goods = good.getId_goods();
            String sql = " DELETE FROM goods WHERE id_goods = ? ";
            try {
                PreparedStatement preparedStatement = StorehouseConnector.getConection().prepareStatement(sql);
                preparedStatement.setInt(1, selected_id_goods);
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ВНИМАНИЕ!");
            alert.setHeaderText(null);
            alert.setContentText("Товар, который желаете удалить, не выбран!");
            alert.showAndWait();
        }
    }

    public void update_good(ActionEvent actionEvent) {
        editgood = table.getSelectionModel().getSelectedItem();
        if (editgood != null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../views/update_good.fxml"));
                update_good = new Stage();
                update_good.setTitle("Update good (Storehouse created by Nick)");
                update_good.setScene(new Scene(root, 800, 400));
                update_good.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ВНИМАНИЕ!");
            alert.setHeaderText(null);
            alert.setContentText("Товар, который нужно редактировать, не выбран!");
            alert.showAndWait();
        }
    }

    public void sell_good(ActionEvent actionEvent) {
        editgood = table.getSelectionModel().getSelectedItem();
        if (editgood != null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../views/sell_good.fxml"));
                sell_good = new Stage();
                sell_good.setTitle("Sell good (Storehouse created by Nick)");
                sell_good.setScene(new Scene(root, 800, 400));
                sell_good.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ВНИМАНИЕ!");
            alert.setHeaderText(null);
            alert.setContentText("Товар, который нужно продать, не выбран!");
            alert.showAndWait();
        }
    }

    public void show_contracts(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../views/show_contracts.fxml"));
            show_contracts = new Stage();
            show_contracts.setTitle("Show contracts (Storehouse created by Nick)");
            show_contracts.setScene(new Scene(root,800, 400));
            show_contracts.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


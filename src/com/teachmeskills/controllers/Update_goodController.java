package com.teachmeskills.controllers;

import com.teachmeskills.database.StorehouseConnector;
import com.teachmeskills.models.Categories;
import com.teachmeskills.models.Goods;
import com.teachmeskills.models.Subcategories;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.PreparedStatement;

public class Update_goodController {

    @FXML
    private TextField edit_good_name, edit_price, edit_count, edit_description;
    @FXML
    private Label category_Label, id_goods, subcategory_Label;
    @FXML
    private TableView<Subcategories> table;
    @FXML
    private TableColumn<Subcategories, String> subcategory_column;
    @FXML
    private TableColumn<Subcategories, Categories> category_column;

    private Goods editgood;
    private Subcategories subcategory = new Subcategories();

    public void initialize() {
        subcategory_column.setCellValueFactory(new PropertyValueFactory<Subcategories, String>("subcategories_name"));
        category_column.setCellValueFactory(new PropertyValueFactory<Subcategories, Categories>("category"));
        editgood = StorehouseController.getSelectedGood();
        table.getItems().clear();
        table.getItems().setAll(Subcategories.getSubcategories());
        edit_good_name.setText(editgood.getGoods_name());
        edit_description.setText(editgood.getDescription());
        edit_price.setText(String.valueOf(editgood.getGoods_price()));
        edit_count.setText(String.valueOf(editgood.getGoods_count()));
        id_goods.setText("ID товара: " + String.valueOf(editgood.getId_goods()));
        category_Label.setText(String.valueOf(editgood.getCategories().getCategories_name()));
        subcategory_Label.setText(editgood.getSubcategories().getSubcategories_name());
    }

    public void save_changes(ActionEvent actionEvent) {
        if (edit_good_name.getText().length() != 0 && edit_description.getText().length() != 0 && edit_price.getText().length() != 0
                && edit_count.getText().length() != 0) {
            String sql = " UPDATE goods SET goods_name = ?, description = ?, goods_price = ?, goods_count = ?, subcategories_id = " +
                    " (SELECT id_subcategories FROM subcategories WHERE subcategories_name = ?) WHERE id_goods = ? ";
            try {
                PreparedStatement preparedStatement = StorehouseConnector.getConection().prepareStatement(sql);
                preparedStatement.setString(1, edit_good_name.getText());
                preparedStatement.setString(2, edit_description.getText());
                preparedStatement.setString(3, edit_price.getText());
                preparedStatement.setString(4, edit_count.getText());
                preparedStatement.setString(5, subcategory_Label.getText());
                preparedStatement.setString(6, String.valueOf(editgood.getId_goods()));
                preparedStatement.executeUpdate();
                StorehouseController.getUpdate_goodgood().close();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ВНИМАНИЕ!");
                alert.setHeaderText(null);
                alert.setContentText("Ошибка при заполнении полей (цена и/или количество)!");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ВНИМАНИЕ!");
            alert.setHeaderText(null);
            alert.setContentText("Перед сохранением необходимо заполнить все поля!");
            alert.showAndWait();
        }
    }

    public void input_new_category(ActionEvent actionEvent) {
        subcategory = table.getSelectionModel().getSelectedItem();
        if (subcategory != null) {
            subcategory_Label.setText(subcategory.getSubcategories_name());
            category_Label.setText(subcategory.getCategory().getCategories_name());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ВНИМАНИЕ!");
            alert.setHeaderText(null);
            alert.setContentText("Новая подкатегория и категория для товара не выбраны!");
            alert.showAndWait();
        }
    }
}

package com.teachmeskills.controllers;

import com.teachmeskills.database.StorehouseConnector;
import com.teachmeskills.models.Categories;
import com.teachmeskills.models.Subcategories;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditSubcategoryController {

    @FXML
    public Button edit_button;
    @FXML
    private TableColumn<Subcategories, Categories> edit_category;
    @FXML
    private TableColumn<Subcategories, String> edit_subcategory;
    @FXML
    private TableView<Subcategories> table;
    @FXML
    private TextField text_subcat;
    @FXML
    private RadioButton food_goods, non_food_goods;

    private static Subcategories subcategories;
    private static Stage updateStage;

    public static Stage getUpdateStage() {
        return updateStage;
    }

    public static Subcategories getSelectedSubcategory() {
        return subcategories;
    }

    public void initialize() {
        edit_subcategory.setCellValueFactory(new PropertyValueFactory<Subcategories, String>("subcategories_name"));
        edit_category.setCellValueFactory(new PropertyValueFactory<Subcategories, Categories>("category"));
        setTableValue();
        ToggleGroup toggleGroup = new ToggleGroup();
        food_goods.setToggleGroup(toggleGroup);
        non_food_goods.setToggleGroup(toggleGroup);
        food_goods.fire();
    }

    private void setTableValue() {
        table.getItems().clear();
        table.getItems().setAll(FXCollections.observableArrayList(Subcategories.getSubcategories()));
    }

    public void add_new_subcategory(ActionEvent actionEvent) {
        String name;
        String category_of_new_subcategori_is;
        if (food_goods.isSelected()) {
            category_of_new_subcategori_is = "продовольственные";
        } else {
            category_of_new_subcategori_is = "непродовольственные";
        }
        name = text_subcat.getText();
        if (name.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ВНИМАНИЕ!");
            alert.setHeaderText(null);
            alert.setContentText("Не введено название подкатегории!");
            alert.showAndWait();
        } else {
            String sql = "INSERT INTO subcategories VALUES (null, ?, ( SELECT id_categories FROM categories WHERE categories_name = ? ) )";
            try {
                PreparedStatement preparedStatement = StorehouseConnector.getConection().prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, category_of_new_subcategori_is);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            setTableValue();
        }
    }

    public void delete_sucategory(ActionEvent actionEvent) {
        subcategories = table.getSelectionModel().getSelectedItem();
        if (subcategories != null) {
            String subcat_name_selected;
            subcat_name_selected = subcategories.getSubcategories_name();
            String sql = " DELETE FROM subcategories WHERE subcategories_name = ? ";
            try {
                PreparedStatement preparedStatement = StorehouseConnector.getConection().prepareStatement(sql);
                preparedStatement.setString(1, subcat_name_selected);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            setTableValue();
        } else {
            setTableValue();
        }
    }

    public void update_subcategory(ActionEvent actionEvent) {
        subcategories = table.getSelectionModel().getSelectedItem();
        if (subcategories != null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../views/update_subcategory.fxml"));
                updateStage = new Stage();
                updateStage.setOnHidden(event -> setTableValue());
                updateStage.setTitle("Update subcategory (Storehouse created by Nick)");
                updateStage.setScene(new Scene(root, 400, 300));
                updateStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            setTableValue();
        }
    }
}

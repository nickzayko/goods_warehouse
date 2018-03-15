package com.teachmeskills.controllers;

import com.teachmeskills.models.Subcategories;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UpdateSubcategoryController {

    @FXML
    private TextField input_changes;
    @FXML
    private Button save_button;
    @FXML
    private RadioButton nonfood_goods_button;
    @FXML
    private RadioButton food_goods_button;

    private Subcategories subcategories;
    private String food_goods = "продовольственные";
    private String nonfood_goods = "непродовольственные";

    public void initialize() {
        subcategories = EditSubcategoryController.getSelectedSubcategory();
        input_changes.setText(subcategories.getSubcategories_name());
        ToggleGroup toggleGroup = new ToggleGroup();
        food_goods_button.setToggleGroup(toggleGroup);
        nonfood_goods_button.setToggleGroup(toggleGroup);
        if (subcategories.getCategory().getCategories_name().equals(food_goods)) {
            food_goods_button.fire();
        } else {
            nonfood_goods_button.fire();
        }
    }

    @FXML
    void save_update_subcategory(ActionEvent actionEvent) {
        String newCategoriesName;
        subcategories.setSubcategories_name(input_changes.getText());
        if (food_goods_button.isSelected()) {
            newCategoriesName = food_goods;
        } else {
            newCategoriesName = nonfood_goods;
        }
        if (input_changes.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ВНИМАНИЕ!");
            alert.setHeaderText(null);
            alert.setContentText("Введите название подкатегории!");
            alert.showAndWait();
        } else {
            subcategories.getCategory().setCategories_name(newCategoriesName);
            subcategories.save();
            EditSubcategoryController.getUpdateStage().close();
        }
    }
}

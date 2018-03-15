package com.teachmeskills.controllers;

import com.teachmeskills.database.StorehouseConnector;
import com.teachmeskills.models.Goods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.PreparedStatement;

public class SellGoodController {

    @FXML
    private Label column_category, column_description,column_count, column_name, column_price, column_id_goods,column_subcategory, count_of_selled_good, price;
    @FXML
    private Button resoult_button;
    @FXML
    private TextField input_count, input_bayer;

    @FXML
    private TextField res_contract;


    private Goods editgood;

    public void initialize (){
        editgood = StorehouseController.getSelectedGood();
        column_name.setText(editgood.getGoods_name());
        column_description.setText(editgood.getDescription());
        column_id_goods.setText(String.valueOf(editgood.getId_goods()));
        column_price.setText(String.valueOf(editgood.getGoods_price()));
        column_count.setText(String.valueOf(editgood.getGoods_count()));
        column_subcategory.setText(editgood.getSubcategories().getSubcategories_name());
        column_category.setText(editgood.getCategories().getCategories_name());
    }

    @Override
    public String toString() {
        return String.valueOf(price);
    }


    public void resoult(ActionEvent actionEvent) {
        try {
            int count = Integer.parseInt(input_count.getText());
            if (count > editgood.getGoods_count()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ВНИМАНИЕ!");
                alert.setHeaderText(null);
                alert.setContentText("Количество товара, которое имеется на складе недостаточно для осуществления запрашиваемой сделки!" +
                        " Количество товара на складе: "+editgood.getGoods_count());
                alert.showAndWait();
                res_contract.clear();
                input_count.clear();
                count_of_selled_good.setText(" ");
                price.setText(" ");
            } else {
                int raschet = count * editgood.getGoods_price();
                res_contract.setText(String.valueOf(raschet));
                count_of_selled_good.setText(String.valueOf(input_count.getText()));
                price.setText(String.valueOf(res_contract.getText()));
            }

        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ВНИМАНИЕ!");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка при вводе данных!");
            alert.showAndWait();
            res_contract.clear();
            input_count.clear();
            count_of_selled_good.setText(" ");
            price.setText(" ");
        }


    }

    public void save_contract(ActionEvent actionEvent) {
        editgood = StorehouseController.getSelectedGood();
        String bayer_name = input_bayer.getText();
        if (bayer_name.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ВНИМАНИЕ!");
            alert.setHeaderText(null);
            alert.setContentText("Необходимо ввести данные о покупателе!");
            alert.showAndWait();
        } else {
            try {
                int remain_goods = editgood.getGoods_count() - Integer.parseInt(count_of_selled_good.getText());
                String sql_to_table_goods = " UPDATE goods SET goods_count = ? WHERE id_goods = ? ";
                String sql_to_table_contracts = " INSERT INTO contracts VALUES (null, ?, ?, ?, ?, ?, ?) ";
                PreparedStatement preparedStatement = StorehouseConnector.getConection().prepareStatement(sql_to_table_goods);
                preparedStatement.setInt(1, remain_goods);
                preparedStatement.setInt(2, editgood.getId_goods());
                preparedStatement.executeUpdate();
                PreparedStatement preparedStatement1 = StorehouseConnector.getConection().prepareStatement(sql_to_table_contracts);
                preparedStatement1.setInt(1, editgood.getId_goods());
                preparedStatement1.setString(2, editgood.getGoods_name());
                preparedStatement1.setString(3, editgood.getDescription());
                preparedStatement1.setInt(4, Integer.parseInt(count_of_selled_good.getText()));
                preparedStatement1.setInt(5, Integer.parseInt(price.getText()));
                preparedStatement1.setString(6, bayer_name);
                preparedStatement1.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Congratulation!");
                alert.setHeaderText(null);
                alert.setContentText("Сделка состоялась успешно, информация внесена в базу данных!");
                alert.showAndWait();
                StorehouseController.sellGood().close();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ВНИМАНИЕ!");
                alert.setHeaderText(null);
                alert.setContentText("Ошибка при вводе данных!");
                alert.showAndWait();
                input_bayer.clear();
            }
        }
    }
}

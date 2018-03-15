package com.teachmeskills.controllers;

import com.teachmeskills.database.StorehouseConnector;
import com.teachmeskills.models.Contracts;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class ShowContractsController {
    @FXML
    private TableView<Contracts> contract_table;
    @FXML
    private TableColumn<Contracts, String> description_column;
    @FXML
    private TableColumn<Contracts, Integer> id_contract_column;
    @FXML
    private TableColumn<Contracts, String> goods_name_column;
    @FXML
    private TableColumn<Contracts, Integer> contract_price_column;
    @FXML
    private TableColumn<Contracts, Integer> count_selled_goods_column;
    @FXML
    private TableColumn<Contracts, String> bayer_column;
    @FXML
    private TableColumn<Contracts, Integer> id_goods_column;
    @FXML
    private TextField input_text;

    Contracts contract = new Contracts();

    public void initialize() {
        id_contract_column.setCellValueFactory(new PropertyValueFactory<Contracts, Integer>("id_contracts"));
        id_goods_column.setCellValueFactory(new PropertyValueFactory<Contracts, Integer>("id_goods"));
        goods_name_column.setCellValueFactory(new PropertyValueFactory<Contracts, String>("goods_name"));
        description_column.setCellValueFactory(new PropertyValueFactory<Contracts, String>("description"));
        count_selled_goods_column.setCellValueFactory(new PropertyValueFactory<Contracts, Integer>("count_of_selled_goods"));
        contract_price_column.setCellValueFactory(new PropertyValueFactory<Contracts, Integer>("contract_price"));
        bayer_column.setCellValueFactory(new PropertyValueFactory<Contracts, String>("bayer_name"));
        setTableValue();
    }

    private void setTableValue() {
        contract_table.getItems().clear();
        contract_table.getItems().setAll(Contracts.getContracts());
    }

    private void setTableValue(ArrayList<Contracts> contracts) {
        contract_table.getItems().clear();
        contract_table.getItems().setAll(FXCollections.observableArrayList(contracts));
    }

    public void search_contract(KeyEvent keyEvent) {
        String search_word2 = input_text.getText();
        if (search_word2.length() == 0) {
            setTableValue();
        } else {
            setTableValue(Contracts.getContractsBySearch(search_word2));
        }
    }

    public void delete_contract(ActionEvent actionEvent) {
        contract = contract_table.getSelectionModel().getSelectedItem();
        if (contract != null) {
            int selected_contract_ID = contract.getId_contracts();
            String sql = " DELETE FROM contracts WHERE id_contracts = ? ";
            try {
                PreparedStatement preparedStatement = StorehouseConnector.getConection().prepareStatement(sql);
                preparedStatement.setInt(1, selected_contract_ID);
                preparedStatement.executeUpdate();
                setTableValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ВНИМАНИЕ!");
            alert.setHeaderText(null);
            alert.setContentText("Сделка, которую желаете удалить, не выбрана!");
            alert.showAndWait();
        }
    }
}

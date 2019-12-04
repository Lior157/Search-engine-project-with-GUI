package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class DictionaryWindow {
    @FXML
    TableView table;


    public void initialize(){ }


    public void insertData(String path){

        TableColumn<String, DictionaryViewObject> column1 = new TableColumn<>("Term");
        column1.setCellValueFactory(new PropertyValueFactory<>("Term"));


        TableColumn<String, DictionaryViewObject> column2 = new TableColumn<>("Amount");
        column2.setCellValueFactory(new PropertyValueFactory<>("Amount"));

        table.getColumns().add(column1);
        table.getColumns().add(column2);
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(path+"/dictionaryAllCorpus.txt"));
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] divide=line.split("=");
                table.getItems().add(new DictionaryViewObject(divide[0],divide[1]));
            }
            reader.close();
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read the dictionary");
            e.printStackTrace();
        }
    }


}

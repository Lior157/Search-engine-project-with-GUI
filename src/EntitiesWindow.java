import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;

import java.util.ArrayList;


public class EntitiesWindow {
    @FXML
    TableView table;


    public void initialize(){ }


    /**
     * @param entities a list that contains the entities
     * This function gets the strongest entities and adds his contents to the new window
     */
    public void insertData(ArrayList<Pair<String,Double>> entities){

        TableColumn<String, DictionaryViewObject> column1 = new TableColumn<>("Entity");
        column1.setCellValueFactory(new PropertyValueFactory<>("Entity"));


        TableColumn<String, DictionaryViewObject> column2 = new TableColumn<>("Rank");
        column2.setCellValueFactory(new PropertyValueFactory<>("Rank"));

        table.getColumns().add(column1);
        table.getColumns().add(column2);
        for (int i = 0; i <entities.size() ; i++)
            table.getItems().add(new EntityViewObject(entities.get(i).getKey(),entities.get(i).getValue().toString()));

    }


}

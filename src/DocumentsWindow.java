import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class DocumentsWindow {

    @FXML
    TableView table;

    @FXML
    Button saveFile;

    public Searcher searcher;

    public ObservableList<QueryViewObject> docList=FXCollections.observableArrayList();

    DirectoryChooser directoryChooser;

    public void initialize(){
        this.directoryChooser = new DirectoryChooser();
        configuringDirectoryChooser(directoryChooser);
    }


    /**
     * @param docs a list that contains the documents
     * This function reads the document list and adds his contents to the new window
     */
    public void insertData(ArrayList<Map.Entry<String,Double>> docs,Searcher searcher){

        this.searcher=searcher;

        TableColumn<String, DictionaryViewObject> column1 = new TableColumn<>("Number");
        column1.setCellValueFactory(new PropertyValueFactory<>("Number"));

        TableColumn<String, DictionaryViewObject> column2 = new TableColumn<>("DocumentID");
        column2.setCellValueFactory(new PropertyValueFactory<>("DocumentID"));


        TableColumn<String, DictionaryViewObject> column3 = new TableColumn<>("Rank");
        column3.setCellValueFactory(new PropertyValueFactory<>("Rank"));

        table.getColumns().add(column1);
        table.getColumns().add(column2);
        table.getColumns().add(column3);
        for (int i = 0; i <docs.size() ; i++) {
            //table.getItems().add(new QueryViewObject(Integer.toString(i+1),docs.get(i).getKey(), docs.get(i).getValue().toString()));
            docList.add(new QueryViewObject(Integer.toString(i+1),docs.get(i).getKey(), docs.get(i).getValue().toString()));
        }
        table.setItems(docList);
    }


    public void getEntities(ActionEvent event){
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EntitiesWindow.fxml"));
            root =loader.load();
            Scene scene=new Scene(root, 450, 450);
            Stage stage = new Stage();
            stage.setTitle("Entities");
            stage.setScene(scene);
            EntitiesWindow controller=loader.getController();
            controller.insertData(searcher.getStrongestEntities(docList.get(table.getSelectionModel().getSelectedIndex()).getDocumentID()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveResults(ActionEvent event) throws IOException
    {
        File dir = directoryChooser.showDialog(saveFile.getScene().getWindow());
        if(dir!=null){
            try(FileWriter fw = new FileWriter(dir.getAbsolutePath()+"/Qres.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                for (QueryViewObject qv:docList) {
                    String line="111 0 "+qv.getDocumentID()+" 1 42.38 mt";
                    out.println(line);
                }
                openInformation("File saved!");
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }
    }


    /**
     * @param directoryChooser
     * This function configures the directory chooser used to get the paths
     */
    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        // Set title for DirectoryChooser
        directoryChooser.setTitle("Select Some Directories");

        // Set Initial Directory
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }


    /**
     * @param info A string that contains the alert information
     * This function opens an information alert with a message according the the call
     */
    public void openInformation(String info){
        Alert missing=new Alert(Alert.AlertType.INFORMATION);
        missing.setContentText(info);
        missing.showAndWait();
    }


    /**
     * @param error A string that contains the alert information
     * This function opens an error alert with a message according the the call
     */
    public void openError(String error){
        Alert missing=new Alert(Alert.AlertType.ERROR);
        missing.setContentText(error);
        missing.showAndWait();
    }
}

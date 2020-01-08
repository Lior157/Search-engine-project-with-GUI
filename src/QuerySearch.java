
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class QuerySearch {
    String posting;
    Searcher searcher;

    @FXML
    TextField query;

    @FXML
    CheckBox semantics;

    public void initialize(){ } //initializes all the variables


    public void insertData(String posting){
        this.posting=posting;
        searcher=new Searcher(Paths.get(posting+"/dictionary/withoutStemPostingFiles"),Paths.get("resources"));
    }

    public void runQuery(ActionEvent event){
        if(query.getText().equals("")) {
            openError("You didnt enter any query!");
            return;
        }
        try {
            if(semantics.isSelected())
                searcher.turnOnSemantics();
            else
                searcher.turnOffSemantics();
            ArrayList<Map.Entry<String,Double>> docs=searcher.analyzeQuery(query.getText());
            Parent root;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("DocumentsWindow.fxml"));
                root =loader.load();
                Scene scene=new Scene(root, 450, 450);
                Stage stage = new Stage();
                stage.setTitle("Documents");
                stage.setScene(scene);
                DocumentsWindow controller=loader.getController();
                controller.insertData(docs,searcher);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }


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

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Controller {
    @FXML
    Button corpusButton;

    @FXML
    Button dictionaryButton;

    @FXML
    TextField corpusPath;
    @FXML
    TextField dictionaryPath;

    @FXML
    Button OpenDictionary;

    @FXML
    Button reset;

    @FXML
    CheckBox stem;

    ArrayList<Pair<String,String>> dictionary;
    PostingBuild pb;
    boolean stemLoaded;
    boolean withoutStemLoaded;

    DirectoryChooser directoryChooser;
    boolean firstTime;

    public void initialize(){
        this.directoryChooser = new DirectoryChooser();
        configuringDirectoryChooser(directoryChooser);
        firstTime=true;
        dictionary=new ArrayList<>();
        stemLoaded=false;
        withoutStemLoaded=false;
        pb=new PostingFiles();
    }

    public void getCorpusPath(ActionEvent event) {
        File dir = directoryChooser.showDialog(corpusButton.getScene().getWindow());
        if (dir != null) {
            corpusPath.setText(dir.getAbsolutePath());
        } else {
            corpusPath.setText(null);
        }
    }

    public void getDictionaryPath(ActionEvent event) {
        File dir = directoryChooser.showDialog(dictionaryButton.getScene().getWindow());
        if (dir != null) {
            dictionaryPath.setText(dir.getAbsolutePath());
        } else {
            dictionaryPath.setText(null);
        }
    }

    public void createDictionary(ActionEvent event) {
        String path=dictionaryPath.getText();
        String dictPath=path+"/dictionary";
        if(corpusPath.getText().equals("")&&path.equals("")) {
            openError("You didnt enter the corpus path or the wanted Posting files path.");
            return;
        }
        new File(dictPath).mkdir();
        pb.startBuildingStock(Paths.get(dictPath),Paths.get(corpusPath.getText()));
        openInformation("Dictionary created successfully!");
    }

    public void loadDictionary(ActionEvent event){
        File dictionary;
        this.dictionary.clear();
        if(stem.isSelected())
            dictionary = new File(dictionaryPath.getText()+"/dictionary/withStem AllDictionary.txt");
        else
            dictionary = new File(dictionaryPath.getText()+"/dictionary/withoutStem AllDictionary.txt");
        if(dictionaryPath.getText().equals("")||!dictionary.exists()) {
            openError("You didnt enter one of the paths or didnt create the dictionary.");
            return;
        }
        try
        {
            BufferedReader reader;
            if(stem.isSelected()) {
                reader = new BufferedReader(new FileReader(dictionaryPath.getText() + "/dictionary/withStem AllDictionary.txt"));
                stemLoaded=true;
                withoutStemLoaded=false;
            }
            else {
                reader = new BufferedReader(new FileReader(dictionaryPath.getText() + "/dictionary/withoutStem AllDictionary.txt"));
                stemLoaded=false;
                withoutStemLoaded=true;
            }
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] divide=line.split("=");
                this.dictionary.add(new Pair<>(divide[0],divide[1]));
            }
            reader.close();
            openInformation("Dictionary loaded successfully!");
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read the dictionary");
            e.printStackTrace();
        }
    }




    public void openDictionary(ActionEvent event) {
        if(!stemLoaded && !withoutStemLoaded) {
            openError("You didnt load the dictionary.");
            return;
        }
        if(stem.isSelected()&&!stemLoaded){
            openError("You didnt load the dictionary.");
            return;
        }
        else if(!stem.isSelected()&&!withoutStemLoaded){
            openError("You didnt load the dictionary.");
            return;
        }

            Parent root;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("DictionaryWindow.fxml"));
                root =loader.load();
                Scene scene=new Scene(root, 450, 450);
                Stage stage = new Stage();
                stage.setTitle("Dictionary");
                stage.setScene(scene);
                DictionaryWindow controller=loader.getController();
                controller.insertData(this.dictionary);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    public void resetDictionary(ActionEvent event) {
        String path = dictionaryPath.getText();
        String dictPath = dictionaryPath.getText()+"/dictionary";
        File dictionary = new File(dictPath+"/withoutStem AllDictionary.txt");
        if(!path.equals("")&&dictionary.exists()) {
            pb.deleteFolderInformation(Paths.get(dictPath));
            new File(dictPath).delete();
            dictionaryPath.clear();
            corpusPath.clear();
            this.dictionary.clear();
            openInformation("Dictionary and Posting files successfully deleted!");
        }
        else
            openError("You didnt enter the dictionary path or the dictionary wasnt created.");
    }


    public void openInformation(String info){
            Alert missing=new Alert(Alert.AlertType.INFORMATION);
            missing.setContentText(info);
            missing.showAndWait();
        }

    public void openError(String error){
        Alert missing=new Alert(Alert.AlertType.ERROR);
        missing.setContentText(error);
        missing.showAndWait();
    }



    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        // Set title for DirectoryChooser
        directoryChooser.setTitle("Select Some Directories");

        // Set Initial Directory
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }
}

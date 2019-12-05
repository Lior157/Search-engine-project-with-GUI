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
    ArrayList<Pair<String,String>> dictionaryStem;
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

    public void resetDictionary(ActionEvent event) {
        String path = dictionaryPath.getText();
        File dictionary = new File(path+"/withoutStem AllDictionary.txt");
        if(!corpusPath.getText().equals("")&&!path.equals("")&&dictionary.exists()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++)
                files[i].delete();
            dictionaryPath.clear();
            corpusPath.clear();
        }
    }


    public void loadDictionary(ActionEvent event){
        File dictionary;
        if(stem.isSelected())
            dictionary = new File(dictionaryPath.getText()+"/withStem AllDictionary.txt");
        else
            dictionary = new File(dictionaryPath.getText()+"/withoutStem AllDictionary.txt");
        if(corpusPath.getText().equals("")||dictionaryPath.getText().equals("")||!dictionary.exists()) {
            openError();
            return;
        }
        try
        {
            BufferedReader reader;
            if(stem.isSelected()) {
                reader = new BufferedReader(new FileReader(dictionaryPath.getText() + "/withStem AllDictionary.txt"));
                stemLoaded=true;
                withoutStemLoaded=false;
            }
            else {
                reader = new BufferedReader(new FileReader(dictionaryPath.getText() + "/withoutStem AllDictionary.txt"));
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
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read the dictionary");
            e.printStackTrace();
        }
    }

        public void openDictionary(ActionEvent event) {
        if(!stemLoaded && !withoutStemLoaded) {
            openError();
            return;
        }
            String path=dictionaryPath.getText();
            File dictionary;
            if(stem.isSelected())
                dictionary = new File(path+"/withStem AllDictionary.txt");
            else
                dictionary = new File(path+"/withoutStem AllDictionary.txt");
            if(corpusPath.getText().equals("")||dictionaryPath.getText().equals("")||!dictionary.exists()) {
                openError();
                return;
            }
            else {
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
        }

        public void openError(){
            Alert missing=new Alert(Alert.AlertType.INFORMATION);
            missing.setContentText("You did not enter all the paths needed or didn't create the dictionary!");
            missing.showAndWait();
        }



    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        // Set title for DirectoryChooser
        directoryChooser.setTitle("Select Some Directories");

        // Set Initial Directory
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }
}

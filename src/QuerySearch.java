
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class QuerySearch {
    String posting;
    Searcher searcher;

    @FXML
    TextField query;

    @FXML
    CheckBox semantics;


    FileChooser chooser;

    DirectoryChooser directoryChooser;

    public void initialize(){    //initializes all the variables
        chooser=new FileChooser();
        directoryChooser=new DirectoryChooser();
    }


    public void insertData(String posting){
        this.posting=posting;
        searcher=new Searcher(Paths.get(posting+"/dictionary/withoutStemPostingFiles"),Paths.get("resources"));
    }

    public void runQuery(ActionEvent event) {
        if (query.getText().equals("")) {
            openError("You didnt enter any query!");
            return;
        }
        try {
            if (semantics.isSelected())
                searcher.turnOnSemantics();
            else
                searcher.turnOffSemantics();
            ArrayList<Map.Entry<String, Double>> docs = searcher.analyzeQuery(query.getText(),1.2,0.4);
            openDocumentsWindow(docs,query.getText());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This function reads from the query file and then open the results in new windows
     * @param event
     */
    public void browseQueryFile(ActionEvent event){
        boolean save=false;
        if(semantics.isSelected())
            searcher.turnOnSemantics();
        else
            searcher.turnOffSemantics();
        ArrayList<ArrayList<Map.Entry<String,Double>>> allDocs=new ArrayList<>();
        ArrayList<String> q=new ArrayList<>();
        File file=chooser.showOpenDialog(query.getScene().getWindow());
        File dir=null;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save to file");
        alert.setHeaderText("Do you want to save the results into a file?");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeTwo = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            dir = directoryChooser.showDialog(query.getScene().getWindow());
            save=true;
        } else if (result.get() == buttonTypeTwo) {
            save=false;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            boolean newDoc=false;
            String st,num="",query="";
            while ((st = br.readLine()) != null) {
                if(st.contains("</top>")){
                    Searcher.setMinimum(5.0);
                    ArrayList<Map.Entry<String,Double>> temp=searcher.analyzeQuery(query,0.9,0.75);
                    allDocs.add(temp);
                    q.add(query);
                    if(save){
                        saveResults(temp,num,dir);
                    }
                }
                else if(st.contains("<num>"))
                    num=st.substring(14,17);
                else if(st.contains("<title>")){
                    query=st.substring(8);
                    while(query.endsWith(" "))
                        query=query.substring(0,query.length()-1);
                }
                else if(st.contains("<desc>")){
                    while(!(st=br.readLine()).contains("<narr>"))
                        query=query+" "+st;
                }

            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        if(save)
            openInformation("File saved!");
        for (int i = 0; i <allDocs.size() ; i++) {
            openDocumentsWindow(allDocs.get(i),q.get(i));
        }
    }

    public void saveResults(ArrayList<Map.Entry<String,Double>> docList,String num,File dir) throws IOException
    {
        if(dir!=null){
            try(FileWriter fw = new FileWriter(dir.getAbsolutePath()+"/Qres.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                for (Map.Entry<String,Double> entry:docList) {
                    String line=num+" 0 "+entry.getKey()+" 1 42.38 mt";
                    out.println(line);
                }
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }
    }


    public void openDocumentsWindow(ArrayList<Map.Entry<String,Double>> docs,String query){
            Parent root;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("DocumentsWindow.fxml"));
                root =loader.load();
                Scene scene=new Scene(root, 450, 450);
                Stage stage = new Stage();
                stage.setTitle(query);
                stage.setScene(scene);
                DocumentsWindow controller=loader.getController();
                controller.insertData(docs,searcher);
                stage.show();
            } catch (IOException e) {
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

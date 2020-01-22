package DragonAnalyzer;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Screen;

import java.io.IOException;
import java.util.ArrayList;

public class DetailsScene  {
    @FXML
    AnchorPane mainPane;
    @FXML
    AnchorPane tablePane;
    @FXML
    WebView postView;

    private TableView tableView;
    private String url;
    private ArrayList<String> profile = new ArrayList<>();
    private ArrayList<String> profileLink = new ArrayList<>();
    private ArrayList<String> comment = new ArrayList<>();
    private int size;

    /*@Override implements Initializable
    public void initialize(URL location, ResourceBundle resources) {


    }
    /*@FXML
    protected void initialize() {

    }*/

    public void start(){
        //getting necessary data

        //calculating screen
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        System.out.println(primaryScreenBounds.getWidth());

        tableView = new TableView();
        tableView.setMinSize(primaryScreenBounds.getWidth() - 600, primaryScreenBounds.getHeight()-50);

        TableColumn<String, PersonComment> column1 = new TableColumn<>("Profile");
        column1.setCellValueFactory(new PropertyValueFactory<>("profile"));

        TableColumn<String, PersonComment> column2 = new TableColumn<>("Profile Link");
        column2.setCellValueFactory(new PropertyValueFactory<>("plink"));

        TableColumn<String, PersonComment> column3 = new TableColumn<>("Comment");
        column3.setCellValueFactory(new PropertyValueFactory<>("comment"));

        TableColumn<String, PersonComment> column4 = new TableColumn<>("Characters count");
        column4.setCellValueFactory(new PropertyValueFactory<>("percentage"));


        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);
        tableView.getColumns().add(column4);


        VBox vbox = new VBox(tableView);
        tableView.prefWidth(tablePane.getWidth());
        tableView.prefHeight(tablePane.getHeight());
        tablePane.getChildren().add(vbox);
        for (int i=0; i < size; i++){
            tableView.getItems().add(new PersonComment(profile.get(i),profileLink.get(i),comment.get(i),String.valueOf(comment.get(i).split("").length)));
        }

        System.out.println(url);
        //f3f3f3
        postView.getEngine().loadContent("<center><iframe style='margin:0;border:none;overflow-x:hidden;overflow-y:hidden' src='https://web.facebook.com/plugins/post.php?href="+url+"&width=500' width='500' height='550'scrolling='no' frameborder='0' allowTransparency='true' allow='encrypted-media'></iframe> <div style='position:absolute; right:0px; top:0px; width:100%; height:520px;'></div></center>", "text/html");

        PrintUsers printUsers = new PrintUsers();
        try {
            printUsers.writeCsv(profile,profileLink, url.split("/")[url.split("/").length-1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData(String url, ArrayList<String> profiles,  ArrayList<String> profileLinks,  ArrayList<String> comments, int size){
        this.url = url;
        this.profile = profiles;
        this.profileLink = profileLinks;
        this.comment = comments;
        this.size = size;
        start();
    }

    public void thredPause(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void taskKiller(){
        try {
            Runtime.getRuntime().exec("taskkill /F /IM jsloader.exe /T");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

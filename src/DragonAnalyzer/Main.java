package DragonAnalyzer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main extends Application {

    private  Alert alert;
    private double xoffset = 0;
    private double yoffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{


        //check if connection exists
        Socket socket = new Socket();
        InetSocketAddress inetSocketAddress = new InetSocketAddress("www.google.com",80);

        try{
            socket.connect(inetSocketAddress);
        }catch (Exception e){
            alert = new Alert(Alert.AlertType.ERROR,"No internet connection.", ButtonType.OK);
            Platform.exit();
            alert.showAndWait();
            System.exit(0);
        }

        //if exists launch chrome driver directory path chooser for first time launch only
        DirectoryChooser directoryChooser = new DirectoryChooser();
        if (!Files.exists(Paths.get("c:\\DragonAnalyzer"))) {
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if(selectedDirectory == null){
                //No Directory selected
                Platform.exit();
                System.exit(0);
            }else{
                //create a folder in c:\
                new File("c:\\DragonAnalyzer").mkdir();
                //check if the driver exists
                File f = new File(selectedDirectory.getAbsolutePath()+"/jsloader.exe");
                if(f.exists() && !f.isDirectory()) {
                    //save chrome driver path in a .txt file
                    try (PrintStream out = new PrintStream(new FileOutputStream("c:\\DragonAnalyzer\\driver.txt"))) {
                        out.print(selectedDirectory.getAbsolutePath());
                    }
                }else {
                    try {
                        new File("c:\\DragonAnalyzer").delete();
                    }catch (Exception e){
                        System.out.println(e);
                    }
                }
            }

        }

        //Close main loading page
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Dragon Analyzer");
        primaryStage.setScene(new Scene(root, 700, 500));
        Image image = new Image("/DragonAnalyzer/images/icon.png");
        primaryStage.getIcons().add(image);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        //drag event
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xoffset = event.getSceneX();
                yoffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xoffset);
                primaryStage.setY(event.getScreenY() - yoffset);
            }
        });
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }


}

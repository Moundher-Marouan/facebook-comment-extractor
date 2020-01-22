package DragonAnalyzer;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    @FXML
    Button exitBtn, minimizeBtn, goBtn, fdone, startBtn;
    @FXML
    Label membership, error, poper;
    @FXML
    TextField query, femail, fpass;
    @FXML
    ImageView profile;
    @FXML
    AnchorPane profileDialog, popup1, popup11;
    @FXML
    ProgressIndicator progress;

    ArrayList<String> profiles = new ArrayList<>();
    ArrayList<String> profileLinks = new ArrayList<>();
    ArrayList<String> comments = new ArrayList<>();


    ChromeDriver driver;
    String line = "";
    boolean isDone =false;
    WebElement popup;
    private int firstLogAttempt = 0;

    private double xoffset = 0;
    private double yoffset = 0;

    private ObservableList<String> profileName = FXCollections.observableArrayList();
    private ObservableList<String> profileLink = FXCollections.observableArrayList();
    private ObservableList<String> comment = FXCollections.observableArrayList();
    @FXML
    protected void initialize() {

        //getting driver path
        try {
            File file = new File("c:\\DragonAnalyzer\\driver.txt");
            File ef = new File("c:\\DragonAnalyzer\\email.txt");
            line = new Scanner(file).useDelimiter("\\Z").next();
            String emailLine = new Scanner(ef).useDelimiter("\\Z").next();
            femail.setText(emailLine);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //kill jsloader process if running
        try {
            Runtime.getRuntime().exec("taskkill /F /IM jsloader.exe /T");
        } catch (IOException e) {
            e.printStackTrace();
        }

        profile.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                openProfiler(popup1);
            }
        });

        fdone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup1.setVisible(false);
                profileDialog.setVisible(false);
                //put fb email if existed
                if (!Files.exists(Paths.get("c:\\DragonAnalyzer\\email.txt"))) {
                    try {
                        PrintStream out = new PrintStream(new FileOutputStream("c:\\DragonAnalyzer\\email.txt"));
                        out.print(femail.getText());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                isDone = true;
            }
        });
    }

    public void sniffer(){
        if (!query.getText().equals("") ) {
            openProfiler(popup11);
            prepareSlideMenuAnimation(popup11);

            new Thread(() -> {
                //killing chrome driver proccess if existed.
                taskKiller();
                //Creating new webdriver
                System.setProperty("webdriver.chrome.driver", line + "\\jsloader.exe");
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless");
                driver = new ChromeDriver(chromeOptions);
                driver.manage().window().maximize();
                JavascriptExecutor js = (JavascriptExecutor) driver;
                driver.get(query.getText());

                //wait for post
                js.executeScript("window.scrollTo(0, document.body.scrollHeight/4)");
                WebElement wait1 = (new WebDriverWait(driver, 60))
                        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class='_62uh']")));
                //removing the box
                thredPause(2000);
                js.executeScript("return document.getElementsByClassName('_62uh')[0].remove();");
                js.executeScript("return document.getElementsByClassName('_5hn6')[0].remove();");


                WebElement NumberOfComments = driver.findElement(By.cssSelector("div[class='_5pcr userContentWrapper']"));//span[class='_1whp _4vn2'] > a"
                System.out.println("comments: "+NumberOfComments.findElement(By.cssSelector("span[class='_1whp _4vn2'] > a")).getText());

                driver.findElement(By.cssSelector("div[class='_5pcr userContentWrapper']")).findElement(By.cssSelector("span[class='_1whp _4vn2'] > a")).click();

                WebElement wait2 = (new WebDriverWait(driver, 60))
                        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[class='_7a99 _21q1 _p']")));
                NumberOfComments.findElement(By.cssSelector("a[class='_7a99 _21q1 _p']")).click();

                WebElement wait3 = (new WebDriverWait(driver, 60))
                        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class='_54ng']")));

                WebElement wait4 = (new WebDriverWait(driver, 60))
                        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class='_54nq _57di _21ii _558b _2n_z'")));//js_1pj
                thredPause(500);
                driver.findElement(By.cssSelector("ul[class='_54nf'] > li:nth-child(3)")).click();

                thredPause(2000);
                while (reachCount() < 1){
                    try {
                        driver.findElement(By.cssSelector("span[class=' _4ssp']")).click();
                        thredPause(3000);
                    }catch(Exception ep){
                        break;
                    }
                }

                WebElement box = driver.findElement(By.cssSelector("div[class='_5pcr userContentWrapper']"));//_72vr
                List<WebElement> users = box.findElements(By.cssSelector("div[class='_72vr']"));//_4eek _7a9b _7a9c clearfix clearfix

                List<WebElement> accounts = box.findElements(By.cssSelector("div[class='_ohe lfloat'] > a"));
                //System.out.println(user.findElement(By.cssSelector("div[class='_ohe lfloat'] > a")).getAttribute("data-hovercard"));
                for (WebElement user : users){

                        System.out.println("[");
                        System.out.println("Profile: " + user.findElement(By.className("_6qw4")).getText());
                        profiles.add(user.findElement(By.className("_6qw4")).getText());
                    try {
                        System.out.println("Comment: " + user.findElement(By.className("_3l3x")).getText());
                        comments.add(user.findElement(By.className("_3l3x")).getText());
                    }catch (Exception ee){
                        System.out.println("Comment: User commented with Image/gif.");
                        comments.add("User commented with Image/gif.");
                    }

                    System.out.println("]");
                }

                for (WebElement account : accounts){
                    System.out.println("https://web.facebook.com/"+account.getAttribute("data-hovercard").split("\\?")[1].split("=")[1]);
                    profileLinks.add("https://web.facebook.com/"+account.getAttribute("data-hovercard").split("\\?")[1].split("=")[1]);
                }
                System.out.println(users.size());
                System.out.println(accounts.size());

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        poper.setText("Process attachment was successful!");
                        poper.setStyle("-fx-text-fill: green;");
                        progress.setVisible(false);
                        startBtn.setVisible(true);
                        startBtn.setDisable(false);
                        startBtn.setOnMouseClicked((event) -> {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader();
                                fxmlLoader.setLocation(getClass().getResource("details_scene.fxml"));
                                Parent root = (Parent)fxmlLoader.load();

                                /*
                                 * if "fx:controller" is not set in fxml
                                 * fxmlLoader.setController(NewWindowController);
                                 */

                                //passing data to tableView
                                DetailsScene detailsScene = fxmlLoader.<DetailsScene>getController();
                                detailsScene.loadData(driver.getCurrentUrl(), profiles, profileLinks, comments,users.size());

                                Scene scene = new Scene(root, 600, 400);

                                Stage stage = new Stage();
                                stage.setTitle("Dragon Analyser - "+driver.getTitle());
                                Image image = new Image("/DragonAnalyzer/images/icon.png");
                                stage.getIcons().add(image);
                                stage.setMaximized(true);
                                stage.setScene(scene);
                                stage.show();

                                final Node source = (Node) event.getSource();
                                final Stage stagew = (Stage) source.getScene().getWindow();
                                stagew.close();

                                driver.quit();

                            } catch (IOException e) {
                                Logger logger = Logger.getLogger(getClass().getName());
                                logger.log(Level.SEVERE, "Failed to create new Window.", e);
                            }
                        });
                    }
                });

            }).start();
        }else{
            error.setText("Make sure to fill your facebook email, password and post url!");
        }
    }

    public double reachCount(){
        double fullReach = 0;
        try {
            WebElement totalReach = driver.findElementByCssSelector("span[class='_3bu3 _7a93']");

            String totalR = totalReach.getText().split(" of ")[totalReach.getText().split("of").length - 1];
            String liveR = totalReach.getText().split(" of ")[0];
            int totalRInt = Integer.parseInt(totalR);
            int liveRInt = Integer.parseInt(liveR);
            fullReach = liveRInt / totalRInt;
        }catch (Exception exc){}
        return fullReach;
    }
    public void thredPause(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void exit(ActionEvent event){
        Platform.exit();
    }

    public void minimize(ActionEvent event){
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.setIconified(true);
    }

    public void openWebsite(ActionEvent event){
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("https://www.garventool.com"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public void openProfiler(AnchorPane view){
        profileDialog.setVisible(true);
        prepareSlideMenuAnimation(view);
    }
    private void prepareSlideMenuAnimation(AnchorPane p) {
        FadeTransition ft = new FadeTransition(Duration.millis(600), p);
        ft.setFromValue(0.0);
        p.setVisible(true);
        ft.setToValue(1.0);
        ft.play();
    }

    private void taskKiller(){
        try {
            Runtime.getRuntime().exec("taskkill /F /IM jsloader.exe /T");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String urlToMobile(String webLink){
        return "mobile"+webLink.split("web")[1];
    }


}

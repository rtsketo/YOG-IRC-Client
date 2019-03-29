package ygolauncher;


import java.awt.Desktop;
import java.awt.MouseInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class IrcChatController implements Initializable {

    String regex = "(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\00"
            + "0-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\""
            + ".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"("
            + "?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:"
            + "\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\["
            + "\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r"
            + "\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*"
            + "(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])"
            + "+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|"
            + "\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:"
            + "[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|"
            + "\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\."
            + ")*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-"
            + "\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\["
            + "\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:"
            + "\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:"
            + "\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\["
            + "\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:"
            + "\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\"."
            + "\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@"
            + ",;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)"
            + "?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\["
            + "\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:"
            + "\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?"
            + "[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] "
            + "\\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:"
            + "\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ "
            + "\\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] "
            + "\\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:"
            + "\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t])"
            + ")*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>"
            + "@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?="
            + "[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:"
            + "\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t"
            + "])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ "
            + "\\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]"
            + "|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?"
            + ":[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|"
            + "\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\."
            + ")*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)|(?:[^()"
            + "<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|("
            + "?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?"
            + ":\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*:(?:(?:\\r\\n)?[ \\t"
            + "])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n"
            + ")?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r"
            + "\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:"
            + "\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]"
            + "+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))"
            + "|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n"
            + ")?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] "
            + "\\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:"
            + "\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ "
            + "\\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] "
            + "\\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:"
            + "\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ "
            + "\\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r"
            + "\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r"
            + "\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<"
            + "(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031"
            + "]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]])"
            + ")|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?"
            + ":(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:("
            + "?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[("
            + "[^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:"
            + "\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:"
            + "\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\["
            + "\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)"
            + "?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n"
            + ")?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r"
            + "\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t]"
            + ")*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ "
            + "\\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]"
            + "|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:"
            + "(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?"
            + ":(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:"
            + "[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t"
            + "])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\0"
            + "31]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]"
            + "]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\."
            + "(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?"
            + ":(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\["
            + "([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r"
            + "\\n)?[ \\t])*)(?:,\\s*(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]"
            + "+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))"
            + "|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n"
            + ")?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] "
            + "\\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:"
            + "\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t])"
            + ")*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;"
            + ":\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\["
            + "\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:"
            + "\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\"."
            + "\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@"
            + ",;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)"
            + "?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r"
            + "\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r"
            + "\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<"
            + "(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031"
            + "]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]])"
            + ")|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?"
            + ":(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:("
            + "?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[("
            + "[^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r"
            + "\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r"
            + "\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]"
            + "\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ "
            + "\\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ "
            + "\\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]"
            + "|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?("
            + "?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+"
            + "|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\."
            + "|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r"
            + "\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r"
            + "\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r"
            + "\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@("
            + "?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:"
            + "(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\["
            + "([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r"
            + "\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r"
            + "\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r"
            + "\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t]"
            + ")*))*)?;\\s*)";
    
    @FXML private WebView web;
    @FXML private AnchorPane chat;
    @FXML private AnchorPane login;
    @FXML private TextFlow chatBox;
    @FXML private TextFlow userBox;
    @FXML private TextField textBox;
    @FXML private ScrollPane scroll;
//    @FXML private ScrollPane userScroll;
    @FXML private TextField signNick;
    @FXML private TextField signPass;
    @FXML private TextField signMail;
    @FXML private TextField logNick;
    @FXML private TextField logPass;
    @FXML private AnchorPane window;
    @FXML private AnchorPane logPane;
    @FXML private AnchorPane signPane;
    @FXML private Hyperlink tours;
    @FXML private ImageView slif;
    @FXML private ImageView wira;
    @FXML private ImageView obsi;
    @FXML private ImageView pyra;
    @FXML private ImageView ex;
    @FXML private ImageView min;
    @FXML private ImageView sign;
    @FXML private ImageView back;
    @FXML private ImageView wait;
    @FXML private ImageView bgc;
    @FXML private ImageView bg;
    @FXML private ImageView fb;
    @FXML private ImageView yt;
    @FXML private ImageView yg;
    @FXML private ImageView pp;
    @FXML private Pane battle;
//    @FXML private Pane donate;
//    @FXML private Pane events;
//    @FXML private Pane ranks;
    @FXML private Label info;
    @FXML private Label ver;
    @FXML private Label bp;
    
    int mouseX = 0;
    int mouseY = 0;
    private final AlertType[] types = {
        AlertType.CONFIRMATION,
        AlertType.ERROR,
        AlertType.INFORMATION,
        AlertType.NONE,
        AlertType.WARNING
    };
    
    ygoIRC irc;
    String path;
    Bloom efe;
    MediaPlayer mp;
    MediaPlayer bs;
    ArrayList users = new ArrayList();
    ArrayList oppers = new ArrayList();
    ArrayList voicers = new ArrayList();
    Image butNorm = new Image("/ygolauncher/images/buttonNorm.png");
    Image butHigh = new Image("/ygolauncher/images/buttonHigh.png");
    Image butPres = new Image("/ygolauncher/images/buttonPress.png");
    Image minNorm = new Image("/ygolauncher/images/minNorm.png");
    Image minHigh = new Image("/ygolauncher/images/minHigh.png");
    Image minPres = new Image("/ygolauncher/images/minPress.png");
    Image signHigh = new Image("/ygolauncher/images/signHigh.png");
    Image signNorm = new Image("/ygolauncher/images/signNorm.png");
    Image signPres = new Image("/ygolauncher/images/signPress.png");
    Image exHigh = new Image("/ygolauncher/images/exHigh.png");
    Image exNorm = new Image("/ygolauncher/images/exNorm.png");
    Image exPres = new Image("/ygolauncher/images/exPress.png");
    Image backNorm = new Image("/ygolauncher/images/backNorm.png");
    Image backHigh = new Image("/ygolauncher/images/backHigh.png");
    Image backPres = new Image("/ygolauncher/images/backPress.png");
    
    double xOffset;
    double yOffset;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        irc = new ygoIRC();
        textBox.setFont(Font.font("Calibri", FontWeight.SEMI_BOLD, 18));
        
        login.setVisible(true);
        chat.setVisible(false);
        
        slowInit();
        irc.connect(this);
        
        Thread bpreq = new Thread() {
            @Override public void run() {
                while(true) try {
                    if ( chat.isVisible() && (
                            mouseX != MouseInfo.getPointerInfo().getLocation().x ||
                            mouseY != MouseInfo.getPointerInfo().getLocation().y ))
                        try (BufferedReader bpr = new BufferedReader(
                            new InputStreamReader((
                                new URL("http://185.206.146.113:9777/userbp.php?" +
                                    "nick="+irc.nick.toLowerCase()+"&bp=2&csen=" +
                                        irc.nick)).openStream()))) {
                            String bpt = bpr.readLine();
                            Platform.runLater(()->{
                                bp.setText("BP: " + Math.round(
                                    ((float)Integer.parseInt(bpt))/100)); });
                            mouseX = MouseInfo.getPointerInfo().getLocation().x;
                            mouseY = MouseInfo.getPointerInfo().getLocation().y;
                            sleep(60000); } 
                        catch (Exception e) {}
                    sleep(500); }
                catch (Exception e) {}}};
        bpreq.setPriority(Thread.MIN_PRIORITY);
        bpreq.start();
    }    
    
    @FXML
    private void clickTour(ActionEvent event) throws MalformedURLException {
        openWebpage(new URL("http://yogduel.com/tournaments/"));
    }
    
    @FXML
    private void enter(KeyEvent event) throws IOException {
        if (event.getCode() == ENTER) {
            String line = textBox.getText();
            if (!line.equals("")) {
                if (line.toLowerCase().contains("msg nickserv verify register"))
                    irc.send("PRIVMSG NICKSERV :" + line.substring(
                            line.toLowerCase().indexOf("verify")) + "\r\n");
                else if (line.toLowerCase().startsWith("/w ")) {
                    String nick = line.split(" ")[1];
                    String msg = line.substring(line.indexOf(line.split(" ")[2]));
                    irc.send("PRIVMSG " + nick + " :" + msg + "\r\n");
                    printChat("Εσύ προς " + nick, msg);
                }
                else {
                    if (line.startsWith("!tour") && (oppers.contains(irc.nick) || voicers.contains(irc.nick))) {
                        Media media = new Media(YgoLauncher.class.getResource("music/tour.mp3").toString());
                        bs = new MediaPlayer(media);
                        bs.play(); }
                    irc.send("PRIVMSG " + irc.chan + " :" + line + "\r\n");
                    printChat(irc.nick, line); }
                textBox.setText(""); }}
    }
    
    @FXML
    private void mouseEnter(MouseEvent event) {
        if (event.getSource() instanceof Pane)
            ((Pane)event.getSource()).getChildren().forEach((ob)->{
                if (ob instanceof ImageView) 
                    ((ImageView)ob).setImage(butHigh);
            });     
        else if (event.getSource() instanceof ImageView) {
            ImageView image = (ImageView)event.getSource();
            if (image == sign)
                image.setImage(signHigh);
            else if (image == back)
                image.setImage(backHigh);
            else if (image == min)
                image.setImage(minHigh);
            else if (image == ex)
                image.setImage(exHigh);
            else {
                image.setEffect(new DropShadow());
                if (image == slif || image == wira
                 || image == obsi || image == pyra) {
                    image.setScaleX(1.2);
                    image.setScaleY(1.2);
                    image.toFront();}}
        }
    }
    
    @FXML
    private void mouseExit(MouseEvent event) {
        if (event.getSource() instanceof Pane)
            ((Pane)event.getSource()).getChildren().forEach((ob)->{
                if (ob instanceof ImageView) 
                    ((ImageView)ob).setImage(butNorm);
            });     
        else if (event.getSource() instanceof ImageView) {
            ImageView image = (ImageView)event.getSource();
            if (image == sign)
                image.setImage(signNorm);
            else if (image == back)
                image.setImage(backNorm);
            else if (image == min)
                image.setImage(minNorm);
            else if (image == ex)
                image.setImage(exNorm);
            else {
                image.setEffect(null);
                image.setScaleX(1);
                image.setScaleY(1);
                if (image == slif || image == wira
                 || image == obsi || image == pyra)
                    image.toBack(); }}    
    }

    @FXML
    private void mousePress(MouseEvent event) {
        if (event.getSource() instanceof Pane)
            ((Pane)event.getSource()).getChildren().forEach((ob)->{
                if (ob instanceof ImageView) 
                    ((ImageView)ob).setImage(butPres);
                else if (ob instanceof Label)
                    ((Label)ob).setTextFill(new Color(0.8,0.8,0.8,1.0));
        });
        else if (event.getSource() instanceof ImageView) {
            ImageView image = (ImageView)event.getSource();
            if (image == sign)
                image.setImage(signPres);
            else if (image == back)
                image.setImage(backPres);
            else if (image == min)
                image.setImage(minPres);
            else if (image == ex)
                image.setImage(exPres);
            else {
                image.setEffect(new Bloom());
                image.setScaleX(.8);
                image.setScaleY(.8);
            }
        }
    }

    
    @FXML
    private void enterLS(KeyEvent event) {
            if (event.getCode() == ENTER)
                logsign();
    }
    
    void logsign() {
        loading(true);
        if (signPane.isVisible()) {
            String nick = signNick.getText().replaceAll(" ", "");
            String pass = signPass.getText();
            String mail = signMail.getText();
            if (nick.isEmpty() || pass.isEmpty() || mail.isEmpty()) 
                alert("Τα ψευδώνυμο, ο κωδικός ή το email δεν μπορούν να είναι κενά!", 4, 300);
            else if (!((Pattern.compile(regex)).matcher(mail).matches())) 
                alert("Το εισαχθέν e-mail δεν είναι έγγυρο!", 4, 250);
            else if (nick.matches("^\\d.+"))
                alert("Το ψευδώνυμο δεν μπορεί να αρχίζει από αριθμούς!", 4, 350);
            else if (!nick.equals(nick.replaceAll("[^A-Za-z0-9]", "")))
                alert("Το ψευδώνυμο χρησιμοποιεί απαγορευμένους χαρακτήρες."
                        + "\nΠαρακαλώ χρησιμοποιείστε μόνο γράμματα του αγγλικού"
                        + " αλφαβήτου ή αριθμούς (ένας αριθμός δεν μπορεί να είναι"
                        + " στην αρχή του ψευδώνυμου).", 4, 500);
            else {
                irc.signup(nick, pass, mail);
                logNick.setText(nick);
                logPass.setText("");
                signNick.setText("");
                signPass.setText("");
            }
        } else {
            String nick = logNick.getText().replaceAll(" ", "");
            String pass = logPass.getText();
            if (nick.isEmpty() || pass.isEmpty())
                alert("Τα ψευδώνυμο ή ο κωδικός δεν μπορούν να είναι κενά!", 4, 350);
            else if (nick.matches("^\\d.+"))
                alert("Το ψευδώνυμο δεν μπορεί να αρχίζει από αριθμούς!", 4, 350);
            else if (!nick.equals(nick.replaceAll("[^A-Za-z0-9]", "")))
                alert("Το ψευδώνυμο χρησιμοποιεί απαγορευμένους χαρακτήρες."
                        + "\nΠαρακαλώ χρησιμοποιείστε μόνο γράμματα του αγγλικού"
                        + " αλφαβήτου ή αριθμούς (ένας αριθμός δεν μπορεί να είναι"
                        + " στην αρχή του ψευδώνυμου).", 4, 500);
            else irc.login(nick, pass); 
        }   
    }
    
    @FXML
    private void mouseRele(MouseEvent event) throws MalformedURLException {
        if (event.getSource() instanceof Pane) {
            Pane pane = (Pane)event.getSource();
            pane.getChildren().forEach((ob)->{
                if (ob instanceof ImageView)
                    ((ImageView)ob).setImage(butHigh);
                else if (ob instanceof Label)
                ((Label)ob).setTextFill(new Color(1,1,1,1.0));});
            if (pane == battle) {
                try {
                    ProcessBuilder pb = new ProcessBuilder(path+"bin/exterm.exe",
                            "rtsketo", irc.nick, "ygoGame");
                    pb.directory(new File(path+"bin/"));
                    pb.start();
                } catch (IOException ex) {
                    printChat("",ex.toString());
                }}
            else logsign();
        }  
        else if (event.getSource() instanceof ImageView) {
            ImageView image = (ImageView)event.getSource();
            if (image == sign) {
                image.setImage(signHigh);
                switchPane(); }
            else if (image == back) {
                image.setImage(backHigh);
                switchPane(); }
            else if (image == min) {
                image.setImage(minHigh);
                YgoLauncher.stage.setIconified(true); }
            else if (image == ex) {
                image.setImage(exHigh);
                System.exit(0); }
            else {
                image.setScaleX(1);
                image.setScaleY(1);
                image.setEffect(new DropShadow());
                if (image == yt)
                    openWebpage(new URL("https://www.youtube.com/c/Yugioh-onlineGr"));
                else if (image == yg)
                    openWebpage(new URL("http://yogduel.com/"));
                else if (image == fb)
                    openWebpage(new URL("https://www.facebook.com/Yugioh-Online-Greek-FanPage-759570624099454"));
                else if (image == pp)
                    openWebpage(new URL("http://yogduel.com/donate-paypal/"));
                else {
                    image.setScaleX(1.2);
                    image.setScaleY(1.2);
                    if (image == slif)
                        openWebpage(new URL("http://yogduel.com/slifer-duelists/"));
                    else if (image == wira)
                        openWebpage(new URL("http://yogduel.com/ra-duelists/"));
                    else if (image == obsi)
                        openWebpage(new URL("http://yogduel.com/obelisk-duelists/"));
                    else if (image == pyra)
                        openWebpage(new URL("http://yogduel.com/god-duelists/")); }}}
    }
    
    public void switchPane() {
        if (signPane.isVisible()) {
            signPane.setVisible(false);
            logPane.setVisible(true);
        } else {
            signPane.setVisible(true);
            logPane.setVisible(false);
        }
    }
    
    public void alert(String what, int type, double width) {
        Platform.runLater(() -> {
            Alert alert = new Alert(types[type]);
            alert.setHeaderText(null);
            alert.setContentText(what);
            alert.getDialogPane().setMaxSize(width, 200);
            Stage stalert = ((Stage)alert.getDialogPane().getScene().getWindow());
            stalert.initStyle(StageStyle.UNDECORATED);
            stalert.getScene().getRoot().setStyle(
                    "-fx-background-image: url('/ygolauncher/images/alertbg.jpg');" +
                    "-fx-background-color: rgba(1,1,1,0);" +
                    "-fx-background-position: center center;" +
                    "-fx-background-size: stretch;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 1.5em;" +
                    "-fx-font-family: 'Calibri';");
            alert.showAndWait();
            if (width == 301) System.exit(0);
        });
    } 

    void switchLayout(boolean pew) {
        Platform.runLater(()->{
            YgoLauncher.stage.setResizable(true);
            YgoLauncher.stage.setResizable(false);
            login.setVisible(!pew);
            chat.setVisible(pew);
            
            
            stopMusic();
            clear();
            loading(!pew);
            irc.join();
        });
    }
    
    public void user(String list) {
        Platform.runLater(()->{
            ArrayList userKO = new ArrayList();
            String[] names = list.split(" ");
            Arrays.sort(names);
            oppers.clear();
            voicers.clear();
            userBox.getChildren().clear();
            for (String user : names) 
                if (user.startsWith("@") && !user.equals("@EsperBot")) {
                    user = user.substring(1);
                    oppers.add(user.toLowerCase());
                    userKO.add(user);
                    addUser(user);
                }
            for (String user : names) 
                if (user.startsWith("+")) {
                    user = user.substring(1);
                    voicers.add(user.toLowerCase());
                    userKO.add(user);
                    addUser(user);
                }
            for (String user : names) 
                if (!user.startsWith("@") && !user.startsWith("+"))
                    for (Object uselvl : users)
                        if (((String[])uselvl)[0].equals(user))
                            if ("1".equals(((String[])uselvl)[1])) {
                                userKO.add(user);
                                addUser(user);
                            }
            for (String user : names) 
                if (!user.startsWith("@") && !user.startsWith("+"))
                    for (Object uselvl : users)
                        if (((String[])uselvl)[0].equals(user))
                            if ("2".equals(((String[])uselvl)[1])) {
                                userKO.add(user);
                                addUser(user);
                            }
            for (String user : names) 
                if (!user.startsWith("@") && !user.startsWith("+"))
                    for (Object uselvl : users)
                        if (((String[])uselvl)[0].equals(user)) 
                            if ("3".equals(((String[])uselvl)[1])) {
                                userKO.add(user);
                                addUser(user);
                            }
            for (String user : names) 
                if (!user.startsWith("@") && !user.startsWith("+"))
                    if (!userKO.contains(user))
                        addUser(user);
        });
    }
    
    public void removeUser(String user){
        Platform.runLater(()->{
            for (Node node : userBox.getChildren()) {
                Text child = ((Text)node);
                if (child.getText().replace("\r\n", "").equals(user))
                    Platform.runLater(()->{
                        userBox.getChildren().remove(child);});
            }
        });
    }
    
    public void strikeUser(String user){
        removeUser(user);
        addUser(user, false, true);
    }
        
    public void addUser(String user) {
        addUser(user, false, false);
    }
    
    void addUser(String user, boolean bold, boolean strike) {
//        Platform.runLater(()->{ 
            Text text;
            if (userBox.getChildren().isEmpty())
                text = new Text(user);
            else text = new Text("\r\n" + user);
            
            text.setStrikethrough(strike);
            text.setFill(colorUser(text));
            text.setFont(Font.font("Calibri", 18));
            
            if (bold || strike) {
                if (bold) text.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
                reorderUser(text); }
            else {
                setClick(text);
                userBox.getChildren().add(text);
            }
//        });
    }
    
    void setClick(Text text) {
        text.setOnMouseClicked((e)->{
            Platform.runLater(()->{
                String nick = text.getText();
                nick = nick.replace("\r\n","");
                String curtext = "";
                if (!textBox.getText().toLowerCase().contains("/w "))
                    curtext = textBox.getText();
                else if (textBox.getText().split(" ").length > 2)
                    curtext = textBox.getText().substring(
                            textBox.getText().indexOf(
                            textBox.getText().split(" ")[2]));
                textBox.requestFocus();
                textBox.setText("/w " + nick + " " + curtext);
                textBox.positionCaret(textBox.getText().length()); });});
    }
    
    void reorderUser(Text user) {
        Platform.runLater(()->{
            ArrayList<Text> list = new ArrayList<Text>();
            for (Node node : userBox.getChildren())
                list.add((Text)node);
            userBox.getChildren().clear();
            if (list.size() != 0) info.setText("Online Χρήστες: "+(list.size()+1));
            
            for (Text curuser : list)
                if (oppers.contains(curuser.getText().replace("\r\n", "").toLowerCase()) || 
                        voicers.contains(curuser.getText().replace("\r\n", "").toLowerCase())) {
                    if (userBox.getChildren().isEmpty())
                        curuser.setText(curuser.getText().replace("\r\n", ""));
                    setClick(curuser);
                    userBox.getChildren().add(curuser);
                }
            
            if (userBox.getChildren().isEmpty())
                        user.setText(user.getText().replace("\r\n", ""));
            setClick(user);
            userBox.getChildren().add(user);
            
            for (Text curuser : list)
                if (!oppers.contains(curuser.getText().replace("\r\n", "").toLowerCase()) &&
                        !voicers.contains(curuser.getText().replace("\r\n", "").toLowerCase())) {
                    if (userBox.getChildren().isEmpty())
                        curuser.setText(curuser.getText().replace("\r\n", ""));
                    setClick(curuser);
                    userBox.getChildren().add(curuser);
                }
            
            (new Timer()).schedule(new TimerTask(){
                @Override public void run() {
                    if (user.isStrikethrough()) removeUser(user.getText().replace("\r\n", ""));
                    else user.setFont(Font.font("Calibri", 18)); 
                    irc.readUsers(); }}, 10000); });
    }
    
    public void newUser(String user) {
        removeUser(user);
        addUser(user, true, false);
    }

    void clear() {
        Platform.runLater(()->{
            chatBox.getChildren().clear();
            userBox.getChildren().clear();
        });
    }
    
    void readRanks() {
        Thread slow = new Thread(){
            @Override
            public void run() {
                users = new ArrayList();
                try (BufferedReader ini = new BufferedReader(new InputStreamReader((
                        new URL("http://yogduel.com/ranks.ini")).openStream()))) {
                    int i = 0;
                    String line;
                    while ((line = ini.readLine()) != null) {
                        System.out.println(line);
                        String[] usetable = new String[2];
                        usetable[0] = line.split(" ")[0];
                        usetable[1] = line.split(" ")[1];
                        users.add(usetable);
                    }
                    (new Timer()).schedule(new TimerTask() {
                        @Override public void run() { readRanks();}}, 1000*60*30);
                
                } catch (IOException e) {
                    System.out.println("Error: Can't read user ranks!");
                    (new Timer()).schedule(new TimerTask() {
                        @Override public void run() { readRanks(); }}, 10000);}
                    System.out.println(users.toString());}};
        
        slow.setPriority(Thread.MIN_PRIORITY);
        slow.start();
    }
    
    void readSomethingElse() {
        try (BufferedReader ini = new BufferedReader(new InputStreamReader((
                new URL("http://rtsketo.duckdns.org/ygo/byebye.ini")).openStream()))) {
            String line;
            if ((line = ini.readLine()) != null) 
                alert("Τερματισμός λειτουργίας λόγω κακόβουλης χρήσης...", 1, 301); }
        catch (IOException e) {
            (new Timer()).schedule(new TimerTask() {
            @Override public void run() { readSomethingElse();}}, 30*60000);}
    }
    
    void slowInit() {
        Thread slow  = new Thread(){
            @Override
            public void run() {
                Platform.runLater(()->{
                    web.getEngine().load("http://yogduel.com/update");
                });
                
                window.setOnMousePressed((MouseEvent event) -> {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                });
                window.setOnMouseDragged((MouseEvent event) -> {
                    YgoLauncher.stage.setX(event.getScreenX() - xOffset);
                    YgoLauncher.stage.setY(event.getScreenY() - yOffset);
                });
                
                Media media = new Media(YgoLauncher.class.getResource("music/music.mp3").toString());
                mp = new MediaPlayer(media);
                mp.setOnEndOfMedia(()->{
                    mp = new MediaPlayer(media);
                    mp.play();});
                mp.play();
                        
                try {
                    path = IrcChatController.class.getProtectionDomain()
                            .getCodeSource().getLocation().toString()
                            .replace("yog.exe","")
                            .replace("file:/","");
                    path = URLDecoder.decode(path, "UTF-8");
                    
                    BufferedReader br = new BufferedReader(new FileReader(path+"bin/system.conf"));
                    String line;
                    while ((line = br.readLine()) != null)
                        if (line.startsWith("nickname ="))
                            logNick.setText(line.substring("nickname = ".length())); }
                
                catch (FileNotFoundException ex) { System.out.println(ex); }
                catch (IOException ex) { System.out.println("Error!!! " + ex); }
                
                efe = new Bloom();
                efe.setThreshold(1);
                bg.setEffect(efe);
                bgc.setEffect(efe);
                
                Thread efe = new Thread() { 
                    @Override
                    public void run() {
                        double b = 1;
                        int mult = -1;
                        
                        while (true) {
                            try {
                                if (mult > 0) b = Math.min(0.91,b+0.011);
                                else b = Math.max(0.8, b-0.01);
                                
                                if (Math.random() < 0.1) mult *= -1;
                                ((Bloom)bg.getEffect()).setThreshold(b);

                                sleep(120);} 
                            catch (InterruptedException ex) {}}}};
                efe.setPriority(MIN_PRIORITY);
                efe.start();
                             
                printChat("",path);
                
                String version = YgoLauncher.locaVer;
                int length = version.length();
                String rev = version.substring(length-3);
                String sub = version.substring(length-5, length-3);
                version = version.substring(0, length-5);
                length = Integer.parseInt(rev);
                ver.setText(version+"."+sub+"."+length);
                
                readRanks(); 
                readSomethingElse();
            }};
        slow.setPriority(Thread.MIN_PRIORITY);
        slow.start();
    }
    
    Paint colorUser(Text text) {
        Color color = Color.DIMGRAY;
        String user = text.getText().replace("\r\n", "").replace(": ","");
        if (oppers.contains(user.toLowerCase()))
            color = Color.web("B90989");
        else if (voicers.contains(user.toLowerCase()))
            color = Color.web("27AD09");
        else for (Object uselvl : users)
                if (((String[])uselvl)[0].equalsIgnoreCase(user))
                    if ("1".equals(((String[])uselvl)[1]))
                        color = Color.web("F37F19");
                    else if ("2".equals(((String[])uselvl)[1]))
                        color = Color.web("192DF3");
                    else if ("3".equals(((String[])uselvl)[1]))
                        color = Color.web("AFA206");
        return color;
    }

    void beep() {
        if (!YgoLauncher.stage.isFocused()) {
            Media media = new Media(YgoLauncher.class.getResource("music/notify.mp3").toString());
            bs = new MediaPlayer(media);
            bs.play(); }
    }
    
    void printChat(String nickname, String message) {
        Platform.runLater(()->{
            String nick = nickname;
            String msg = message;
            String msglc = msg.toLowerCase();
            String nicklc = "";
            if (irc.nick != null)
                nicklc = irc.nick.toLowerCase();
            
            if (!"".equals(nick)) {
                Text name = new Text("\r\n" + nick + ": ");
                name.setFont(Font.font("Calibri", 18));
                if (nick.contains("προς")) {
                    name.setFill(Color.DEEPPINK);
                    beep(); }
                else name.setFill(colorUser(name));
                chatBox.getChildren().add(name); }
            
            while (msglc.contains("http://") || msglc.contains("https://")) {
                String url = msg.substring(msglc.indexOf("http")).split(" ")[0];
                Hyperlink hlink = new Hyperlink("link");
                hlink.setOnAction((e)->{
                    try { openWebpage(new URL(url));
                    } catch (MalformedURLException ex) {}});
                hlink.setFont(Font.font("Calibri", 16));
                hlink.setTooltip(new Tooltip(url));
                chatBox.getChildren().add(hlink);
                msg = msg.replace(" "+url, "");
                msg = msg.replace(url+" ", "");
                msg = msg.replace(url, "");
                msglc = msg.toLowerCase();
            }
            
            if (msglc.contains(nicklc)) {           
                String stringBefore = msg.substring(0,msglc.indexOf(nicklc));
                String stringAfter = msg.substring(msglc.indexOf(nicklc)+nicklc.length());
                Text textBefore = new Text(stringBefore);
                Text textAfter = new Text(stringAfter+" ");
                Text textNick = new Text(irc.nick);
                textBefore.setFont(Font.font("Calibri", 18));    
                textAfter.setFont(Font.font("Calibri", 18));
                textNick.setFont(Font.font("Calibri", 18));
                textBefore.setFill(Color.DIMGREY);
                textAfter.setFill(Color.DIMGREY);
                textNick.setFill(Color.BLUEVIOLET);
                chatBox.getChildren().add(textBefore);
                chatBox.getChildren().add(textNick);
                chatBox.getChildren().add(textAfter);
                beep(); }
            else {
                Text text = new Text(msg+" ");
                text.setFont(Font.font("Calibri", 18));
                text.setFill(Color.DIMGREY);
                chatBox.getChildren().add(text); }
            
            scroll.layout();
            scroll.setVvalue(scroll.getVmax()); });
    }
    
    void loading(boolean is) {
        wait.setVisible(is);
        logPane.setDisable(is);
        signPane.setDisable(is);
        
        if (is) {
            Media media = new Media(YgoLauncher.class.getResource("music/lockb.mp3").toString());
            bs = new MediaPlayer(media);
            bs.play();
            
            (new Timer()).schedule(new TimerTask(){
            @Override
            public void run() {
                loading(false);
            }}, 10000);
        }
    }
    
    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (IOException e) {
            }
        }
    }

    public static void openWebpage(URL url) {
        try {
            openWebpage(url.toURI());
        } catch (URISyntaxException e) {
        }
    }

    private void stopMusic() {
        mp.setVolume(mp.getVolume()-0.01);
        if (mp.getVolume() > 0)
            (new Timer()).schedule(new TimerTask(){
                @Override public void run() { stopMusic();}}, (long)(5/Math.max(0.01,mp.getVolume())));
        else mp.stop();
    }
}
    
    
    
    
    
    
    
 
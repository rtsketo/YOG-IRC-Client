package ygolauncher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


public class YgoLauncher extends Application {
    
    public static Stage stage;
    public static String chan = "";
    public static String locaVer = "100000";
    
    @Override
    public void start(Stage stage) throws Exception {
        Font.loadFont(getClass().getResourceAsStream("/ygolauncher/Yog Online/FRAHV.TTF"), 14);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ircChat.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);       
        scene.setFill(Color.TRANSPARENT);
        root.setStyle("-fx-background-color: transparent;");
        stage.setScene(scene);
        stage.setOnHiding((WindowEvent event) -> {
            Platform.runLater(() -> {
                System.exit(0); }); });
        stage.initStyle(StageStyle.TRANSPARENT);
        this.stage = stage;
        stage.setTitle("YOG");
        stage.getIcons().add(new Image(getClass().getResource("images/yugioh.png").toExternalForm()));
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("file.encoding","UTF-8");
        Field charset = Charset.class.getDeclaredField("defaultCharset");
        charset.setAccessible(true);
        charset.set(null,null);
        update();
        if (args.length > 0) chan = "#"+args[0];
        launch(args);
    }   
    
    static void update() {
        try (BufferedReader ini = new BufferedReader(new InputStreamReader((
                new URL("http://yogduel.com/updates.ini")).openStream()))) {
            
            try (BufferedReader ani = new BufferedReader(new FileReader("./bin/version"))) {
                locaVer = ani.readLine(); }
            catch (IOException e) {}
            
            String servVer;
            while((servVer = ini.readLine()) != null) {
                ini.readLine();
                if (Integer.parseInt(servVer) > Integer.parseInt(locaVer)) {
                    String path = YgoLauncher.class.getProtectionDomain()
                            .getCodeSource().getLocation().toString()
                            .replace("yog.exe","")
                            .replace("file:/","");
                    path = URLDecoder.decode(path, "UTF-8");
                    System.out.println(path);
                    
                    ProcessBuilder pb = new ProcessBuilder("cmd.exe","/c","start","ygu.exe");
                    pb.directory(new File(path));
                    pb.start();
                    
                    System.exit(0);
                }
            }
        } catch (IOException e) {
                    System.out.println("Error: Can't read update file!");
        }
    }
}

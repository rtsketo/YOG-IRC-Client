package ygolauncher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.codec.binary.Base64;

public class ygoIRC {
    
    boolean disconnect = false;
    boolean read = true;
    
    BufferedReader reader;
    BufferedWriter writer;
    IrcChatController icc;
    ircReader feed;
    Timer timer;
    Timer names;
    String nick;
    String pass;
    String mail;
    
    String sp = " ";
    String ln = "\r\n";
    String server = "irc.esper.net";
    String login = "nomac";
    String chan = "#ygochatgr";
    String key = "tsoupa";
    
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
        
    public void send(String line) {
        try {
            writer.write(line);
            writer.flush();
        } catch (Exception ex) { }
    }
               
    public void connect(IrcChatController icc) {
        Socket socket;
        this.icc = icc;
        if (!"".equals(YgoLauncher.chan)) chan = YgoLauncher.chan;
        try {
            login = new Base64().encodeAsString(hexStringToByteArray(GetNetworkAddress.GetAddress("mac").replace("-", "")));
            login = login.replaceAll("[^A-Za-z0-9]", "");
        } catch (Exception e) { }
        System.out.println(login);
        
        try {
            socket = new Socket(server, 6667);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            disconnect = false;
        } catch (IOException ex) {
            if (!disconnect) {
                icc.alert("Δεν υπάρχει ενεργή σύνδεση στο διαδύκτιο!", 1, 350);
                disconnect = true;
                icc.clear();
            }
        }

        int randomNum = ThreadLocalRandom.current().nextInt(1000000000, 2147483647);
        send(
            "NICK yogUser" + randomNum + ln +
            "USER " + login + " pew pew YOGChat " + ln);
        
        timer = new Timer();
        names = new Timer();
        feed = new ircReader(icc, this);
        timer.scheduleAtFixedRate(feed, 50, 50);
        names.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {

            }
        }, 60000, 3*60000);
    }

    public void readUsers() {
        if (read) {
            read = false;
            feed.users = "";
            send("NAMES " + chan + ln);
            
            (new Timer()).schedule(new TimerTask() {
                @Override public void run() { read = true; }}, 5*60000); }
    }
    
    void login(String nick, String pass) {
        this.nick = nick;
        this.pass = pass;

        System.out.println(icc.path+"bin/system.conf");
        try (BufferedReader br = new BufferedReader(new FileReader(icc.path+"bin/system.conf"))) {
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("nickname ="))
                    line = "nickname = " + nick; 
                lines.add(line); }
            br.close();
            
            PrintWriter wrtr = new PrintWriter(icc.path+"bin/system.conf", "UTF-8");
            lines.forEach((rline) -> {
                wrtr.println(rline);
            });
            wrtr.close(); }
        catch (FileNotFoundException ex) { System.out.println(ex); }
        catch (IOException ex) { System.out.println("Error!!! " + ex); }
        
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                send("PRIVMSG NICKSERV :ghost " + nick + sp + pass + ln);
            }
        }, 2500);
        
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                send("NICK " + nick + ln);
                send("PRIVMSG NICKSERV :identify " + pass + ln);
            }
        }, 4000);
    }

    void signup(String nick, String pass, String mail) {
        this.nick = nick;
        this.pass = pass;
        this.mail = mail;
        
        int randomNum = ThreadLocalRandom.current().nextInt(1000000000, 2147483647);
        send("NICK yogUser" + randomNum + ln);
        send("NICK " + nick + ln);
        feed.answer();
    }
    
    void signupContinue() {
        send("PRIVMSG NICKSERV :register " + pass + sp + mail + ln);
        send("PRIVMSG NICKSERV :set hidemail off" + ln);
    }
        
    void join() {
        send("JOIN " + chan + sp + key + ln);
    }
    
    public void stop() {
        timer.cancel();
        names.cancel();
    }
}

package ygolauncher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ircReader extends TimerTask implements Runnable {

    Socket socket;
    ygoIRC irc;
    String sp = " ";
    String ln = "\r\n";
    int answer = 0;
    BufferedReader reader;
    BufferedWriter writer;
    IrcChatController icc;
    public String users = "";
      
    ircReader(IrcChatController icc, ygoIRC irc) {
        this.reader = irc.reader;
        this.writer = irc.writer;
        this.icc = icc;
        this.irc = irc;
    }
    
    @Override
    public void run() {
        try {
            String line = reader.readLine();
            if (line != null) System.out.println(line);
            if (line != null) {
                String linelc = line.toLowerCase();
                
                if (answer > 0) {
                    if (linelc.contains("esper.net 433")) {
                        icc.alert("Το ψευδώνυμο χρησιμοποιείται ήδη!", 1, 250);
                        answer = 0; }
                    else if (linelc.contains("nick :")) {
                        if (answer == 1) answer = 2;
                        else { irc.signupContinue();
                               answer = 0; }}}
                else if (linelc.startsWith("ping")) {
                    writer.write("PONG " + line.substring(6) + ln);
                    writer.flush(); } 
                else if (!line.contains("esper.net")) {
                    if (linelc.contains("privmsg")) {
                        String nick = line.substring(1, line.indexOf("!"));
                        String after = line.substring(linelc.indexOf("privmsg"));
                        String msg = after.substring(after.indexOf(after.split(" ")[2])+1);
                        if (msg.startsWith("!tour") && (icc.oppers.contains(nick.toLowerCase()) || icc.voicers.contains(nick.toLowerCase()))) {
                            Media media = new Media(YgoLauncher.class.getResource("music/tour.mp3").toString());
                            icc.bs = new MediaPlayer(media); icc.bs.play(); }
                        if (after.split(" ")[1].toLowerCase().equals(irc.nick.toLowerCase()))
                            icc.printChat(nick + " προς εσένα", msg);
                        else icc.printChat(nick, msg); }
                    else if (linelc.contains(" nick ")) {
                        String nick = line.substring(1, line.indexOf("!"));
                        String newnick = line.substring(linelc.indexOf("nick :")+6);
                        icc.printChat(nick, "--> " + newnick); }
                    else if (linelc.contains(" kick ")) {
                        icc.strikeUser(line.substring(1, line.indexOf("!")));
                        if (irc.nick.equals(line.split(" ")[3])) 
                            icc.alert("O χρήστης " + line.substring(1, line.indexOf("!"))
                                    + " σας αφαίρεσε από την συνομιλία με την αιτιολογία "
                                    + "\""+ line.substring(line.indexOf(":", 1)+1) + "\"...", 2, 301); }
                    else if (linelc.contains("part") || linelc.contains("quit"))
                        icc.strikeUser(line.substring(1, line.indexOf("!")));
                    else if (linelc.contains("join"))
                        icc.newUser(line.substring(1, line.indexOf("!"))); }
                else if (linelc.contains("esper.net 332"))
                    icc.printChat("",line.split(":")[2]);
                else if (linelc.contains("esper.net 433"))
                    irc.send("NICK " + irc.nick + "z" + ln);
                else if (linelc.contains("esper.net 404"))
                    icc.printChat("", "Σας έχει αφαιρεθεί ο λόγος.");
                else if (linelc.contains("esper.net 353"))
                    users += line.substring(line.indexOf(":", 1)+1) + " ";
                else if (linelc.contains("esper.net 366"))
                    icc.user(users);
                else if (linelc.contains("esper.net 474"))
                    icc.alert("Έχετε αποκλειστεί από την συνομιλία. Η εφαρμογή τώρα θα κλείσει. (UserID: "+ irc.login +")", 2, 301);
                else if (linelc.startsWith(":nickserv!")) {
                    if (linelc.contains("you are now identified") || linelc.contains("instructions to complete your registration"))
                        icc.switchLayout(true);
                    else if (linelc.contains("invalid password")) {
                        icc.alert("Ο κωδικός είναι λανθασμένος!", 1, 250);
                        icc.loading(false); }
                    else if (linelc.contains("has now been verified"))
                        icc.alert("Η ενεργοποίηση ολοκληρώθηκε επιτυχώς!", 2, 250);
                    else if (linelc.contains("is already registered")) {
                        icc.alert("Το ψευδώνυμο αυτό υπάρχει ήδη!", 1, 250);
                        icc.loading(false); }
                    else if (linelc.contains("is not a registered nickname")) {
                        icc.alert("Δεν υπάρχει αυτός ο λογαριασμός!", 1, 200);
                        icc.loading(false); }
                    else if (linelc.contains("cannot use your nickname as a password")) {
                        icc.alert("Δεν γίνεται ο Κωδικός και το Όνομα να είναι ίδια!", 1, 250);
                        irc.send("NICK " + irc.nick + "z" + ln);
                        icc.loading(false); }
                    else if (linelc.contains("your nickname will expire")) {
                        icc.alert("Έχει αποσταλεί e-mail στην διεύθυνση " + irc.mail + 
                                " με οδηγίες για την ενεργοποίηση του λογαριασμού." +
                                " Ο λογαριασμός θα λήξει αν δεν γίνει η ενεργοποίηση μέχρι αύριο.", 2, 450);
                        irc.pass = "";
                        icc.switchPane();
                        icc.loading(false); }}}
        } catch (Exception ex) {
            System.out.println("Reader: "+ex);
            irc.stop();
            icc.switchLayout(false);
            new Timer().schedule(new TimerTask(){
                @Override public void run() {
                    irc.connect(icc);
                    icc.loading(false); }}, 10000); }            
    }

    void answer() {
        answer = 1;
    }
}

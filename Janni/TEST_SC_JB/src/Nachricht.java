import java.time.LocalDateTime;

public class Nachricht {
    private String user;
    private LocalDateTime zeitstempel;
    private String nachricht;

    public Nachricht(){
    }

    public Nachricht(String user, LocalDateTime zeitstempel, String nachricht) {
        this.user = user;
        this.nachricht = nachricht;
        this.zeitstempel = zeitstempel;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public LocalDateTime getTimestamp() {
        return zeitstempel;
    }
    public void setTimestamp(LocalDateTime zeitstempel){
        this.zeitstempel = zeitstempel;
    }
    public String getMessage(){
        return nachricht;
    }
    public void setMessage(String nachricht) {
        this.nachricht = nachricht;
    }
    @Override
    public String toString() {
        return "User: " + user + " " + "Zeitstempel: " + zeitstempel + " " + "Nachricht: " + nachricht;
    }
}

package tk.only5.ptsmoodle;

/**
 * Created by DHRUMESH on 8/2/2016.
 */
public class Notification {
    String title, datetime, message;

    public Notification(String title, String datetime, String message) {
        this.title = title;
        this.datetime = datetime;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return datetime;
    }

    public void setDateTime(String time) {
        this.datetime = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

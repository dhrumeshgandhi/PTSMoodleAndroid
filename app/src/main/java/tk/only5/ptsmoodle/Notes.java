package tk.only5.ptsmoodle;

/**
 * Created by DHRUMESH on 9/4/2016.
 */
public class Notes {
    private String name, uploaded_by, upload_date, subject;

    public Notes(String name, String uploaded_by, String upload_date, String subject) {
        this.name = name;
        this.uploaded_by = uploaded_by;
        this.upload_date = upload_date;
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUploaded_by() {
        return uploaded_by;
    }

    public void setUploaded_by(String uploaded_by) {
        this.uploaded_by = uploaded_by;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

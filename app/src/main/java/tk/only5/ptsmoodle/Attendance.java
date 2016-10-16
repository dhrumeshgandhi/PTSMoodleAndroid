package tk.only5.ptsmoodle;

import com.parse.ParseUser;

import java.text.DecimalFormat;

/**
 * Created by DHRUMESH on 10/16/2016.
 */

public class Attendance {
    private String subject;
    private int totalLectures, attendedLectures;
    private String attendancePercentage;
    private ParseUser teacherUser;
    private String teacherName;
    private DecimalFormat decimalFormat = new DecimalFormat("##.#");

    public Attendance(String subject, int totalLectures, int attendedLectures,
                      ParseUser teacherUser) {
        this.subject = subject;
        this.totalLectures = totalLectures;
        this.attendedLectures = attendedLectures;
        this.attendancePercentage = decimalFormat.format(((double) attendedLectures / totalLectures) * 100);
        this.teacherUser = teacherUser;
        this.teacherName = teacherUser.getString("FIRST_NAME")
                + " " + teacherUser.getString("LAST_NAME");
    }

    public ParseUser getTeacherUser() {
        return teacherUser;
    }

    public void setTeacherUser(ParseUser teacherUser) {
        this.teacherUser = teacherUser;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getTotalLectures() {
        return totalLectures;
    }

    public void setTotalLectures(int totalLectures) {
        this.totalLectures = totalLectures;
    }

    public int getAttendedLectures() {
        return attendedLectures;
    }

    public void setAttendedLectures(int attendedLectures) {
        this.attendedLectures = attendedLectures;
    }

    public String getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(String attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }


}

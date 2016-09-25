package tk.only5.ptsmoodle;

import java.io.Serializable;

/**
 * Created by DHRUMESH on 02-03-2016.
 */
public class Quiz implements Serializable {
    private String name, subject, no_of_ques, positive_marks, negative_marks, time_limit, teacher_id, branch, sem, upload_date;

    public Quiz(String name, String subject, String no_of_ques, String positive_marks, String negative_marks, String time_limit, String teacher_id, String branch, String sem, String upload_date) {
        this.name = name;
        this.subject = subject;
        this.no_of_ques = no_of_ques;
        this.positive_marks = positive_marks;
        this.negative_marks = negative_marks;
        this.time_limit = time_limit;
        this.teacher_id = teacher_id;
        this.branch = branch;
        this.sem = sem;
        this.upload_date = upload_date;

    }

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNo_of_ques() {
        return no_of_ques;
    }

    public void setNo_of_ques(String no_of_ques) {
        this.no_of_ques = no_of_ques;
    }

    public String getPositive_marks() {
        return positive_marks;
    }

    public void setPositive_marks(String positive_marks) {
        this.positive_marks = positive_marks;
    }

    public String getNegative_marks() {
        return negative_marks;
    }

    public void setNegative_marks(String negative_marks) {
        this.negative_marks = negative_marks;
    }

    public String getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }
}

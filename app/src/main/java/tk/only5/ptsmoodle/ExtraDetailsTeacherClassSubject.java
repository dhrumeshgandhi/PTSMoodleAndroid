package tk.only5.ptsmoodle;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by DHRUMESH on 8/2/2016.
 */
public class ExtraDetailsTeacherClassSubject {
    String classItem, subjectItem;

    public ExtraDetailsTeacherClassSubject(String classItem, String subjectItem) {
        this.classItem = classItem;
        this.subjectItem = subjectItem;
    }

    public String getClassItem() {
        return classItem;
    }

    public void setClassItem(String classItem) {
        this.classItem = classItem;
    }

    public String getSubjectItem() {
        return subjectItem;
    }

    public void setSubjectItem(String subjectItem) {
        this.subjectItem = subjectItem;
    }

    public JSONObject toJSONObject() {
        try {
            return (new JSONObject()).put("CLASS", classItem).put("SUBJECT", subjectItem);
        } catch (Exception e) {
            Log.e(InitClass.TAG, "ERROR", e);
            return new JSONObject();
        }
    }
}

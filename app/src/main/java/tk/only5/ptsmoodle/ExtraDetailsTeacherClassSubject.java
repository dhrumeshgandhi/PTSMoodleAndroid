package tk.only5.ptsmoodle;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by DHRUMESH on 8/2/2016.
 */
public class ExtraDetailsTeacherClassSubject {
    String branchItem, subjectItem, semItem;

    public ExtraDetailsTeacherClassSubject(String semItem, String branchItem, String subjectItem) {
        this.branchItem = branchItem;
        this.subjectItem = subjectItem;
        this.semItem = semItem;
    }

    public String getBranchItem() {
        return branchItem;
    }

    public void setBranchItem(String branchItem) {
        this.branchItem = branchItem;
    }

    public String getSubjectItem() {
        return subjectItem;
    }

    public void setSubjectItem(String subjectItem) {
        this.subjectItem = subjectItem;
    }

    public JSONObject toJSONObject() {
        try {
            return (new JSONObject()).put("BRANCH", branchItem).put("SUBJECT", subjectItem).put("SEM", semItem);
        } catch (Exception e) {
            Log.e(InitClass.TAG, "ERROR", e);
            return new JSONObject();
        }
    }

    public String getSemItem() {
        return semItem;
    }

    public void setSemItem(String semItem) {
        this.semItem = semItem;
    }

    public String getClassItem() {
        return semItem + " " + branchItem;
    }
}

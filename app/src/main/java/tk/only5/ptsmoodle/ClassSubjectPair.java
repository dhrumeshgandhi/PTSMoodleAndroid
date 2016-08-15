package tk.only5.ptsmoodle;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * Created by DHRUMESH on 8/9/2016.
 */
public class ClassSubjectPair<Class, Subject> implements Serializable {
    private Class c;
    private Subject s;

    public ClassSubjectPair(Class c, Subject s) {
        this.c = c;
        this.s = s;
    }

    public Class getC() {
        return c;
    }

    public void setC(Class c) {
        this.c = c;
    }

    public Subject getS() {
        return s;
    }

    public void setS(Subject s) {
        this.s = s;
    }

    public JSONArray toJsonPair() {
        return new JSONArray().put(c).put(s);
    }
}

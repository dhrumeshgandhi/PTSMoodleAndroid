package tk.only5.ptsmoodle;

import java.io.Serializable;

/**
 * Created by DHRUMESH on 02-03-2016.
 */
public class Question implements Serializable {
    private String que = "", ans = "", opt1 = "", opt2 = "", opt3 = "", id = "";

    public Question(String id) {
        this.id = id;
    }

    public Question(String que, String ans, String opt1, String opt2, String opt3, String id) {
        this.que = que;
        this.ans = ans;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQue() {
        return que;
    }

    public void setQue(String que) {
        this.que = que;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getOpt1() {
        return opt1;
    }

    public void setOpt1(String opt1) {
        this.opt1 = opt1;
    }

    public String getOpt2() {
        return opt2;
    }

    public void setOpt2(String opt2) {
        this.opt2 = opt2;
    }

    public String getOpt3() {
        return opt3;
    }

    public void setOpt3(String opt3) {
        this.opt3 = opt3;
    }

    public String[] getAll(boolean idFirst) {
        String[] allData;
        if (idFirst)
            allData = new String[]{id, que, ans, opt1, opt2, opt3};
        else
            allData = new String[]{que, ans, opt1, opt2, opt3, id};
        return allData;
    }
}
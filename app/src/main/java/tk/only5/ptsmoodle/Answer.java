package tk.only5.ptsmoodle;

/**
 * Created by DHRUMESH on 10/2/2016.
 */

public class Answer {
    String que, ans, id;

    public Answer(String que, String ans, String id) {

        this.que = que;
        this.ans = ans;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


package tk.only5.ptsmoodle;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DHRUMESH on 10/2/2016.
 */

public class GivingQuizListAdapter extends BaseAdapter {
    ViewHolder holder = null;
    private LayoutInflater li;
    private ArrayList<Question> questions;
    private int id = 0;
    private ArrayList<Answer> answers;
    private Question question;
    private OnQuestionAnsweredListener onQuestionAnsweredListener;

    public GivingQuizListAdapter(Activity activity, final ArrayList<Question> questions, int id, OnQuestionAnsweredListener onQuestionAnsweredListener) {
        this.li = LayoutInflater.from(activity);
        this.questions = questions;
        this.id = id;
        this.onQuestionAnsweredListener = onQuestionAnsweredListener;
        answers = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            question = questions.get(i);
            answers.add(new Answer(question.getQue(), "", question.getId()));
        }
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Question getItem(int position) {
        return questions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = li.inflate(R.layout.list_row_giving_quiz, null);
            holder.tvQue = (TextView) convertView.findViewById(R.id.tvQue);
            holder.rbOpt1 = (RadioButton) convertView.findViewById(R.id.rbOpt1);
            holder.rbOpt2 = (RadioButton) convertView.findViewById(R.id.rbOpt2);
            holder.rbOpt3 = (RadioButton) convertView.findViewById(R.id.rbOpt3);
            holder.rbOpt4 = (RadioButton) convertView.findViewById(R.id.rbOpt4);
            //   holder.tvNumber = (TextView) convertView.findViewById(R.id.tvNumber);
            holder.rbOpts = (RadioGroup) convertView.findViewById(R.id.rbOpts);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        // holder.tvNumber.setText("Q" + questions.get(position).getId() + ")");
        holder.tvQue.setText(questions.get(position).getQue());
        holder.rbOpt1.setText(questions.get(position).getAns());
        holder.rbOpt2.setText(questions.get(position).getOpt1());
        holder.rbOpt3.setText(questions.get(position).getOpt2());
        holder.rbOpt4.setText(questions.get(position).getOpt3());
        holder.rbOpts.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checked = (RadioButton) group.findViewById(checkedId);
                if (answers.get(position).getAns() == "")
                    onQuestionAnsweredListener.onQuestionAnswered();
                answers.get(position).setAns(checked.getText().toString());
                Log.d(InitClass.TAG, "Que:" + answers.get(position).getQue() + " Ans:" + answers.get(position).getAns());
            }
        });
        return convertView;
    }

    protected ArrayList<Answer> getAnswers() {
      /*  for(int i=0;i<answers.size();i++){
            if(answers.get(i).getAns()=="") return new ArrayList<Answer>();
        }*/
        return answers;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnQuestionAnsweredListener {
        void onQuestionAnswered();
    }

    public class ViewHolder {
        TextView tvQue, tvNumber;
        RadioButton rbOpt1, rbOpt2, rbOpt3, rbOpt4;
        RadioGroup rbOpts;
    }
}

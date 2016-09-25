package tk.only5.ptsmoodle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DHRUMESH on 9/4/2016.
 */
public class QuizListAdapter extends BaseAdapter {
    ViewHolder holder = null;
    private LayoutInflater li;
    private ArrayList<Quiz> quizList;

    public QuizListAdapter(Context context, ArrayList<Quiz> quizList) {
        this.li = LayoutInflater.from(context);
        this.quizList = quizList;
    }

    @Override
    public int getCount() {
        return quizList.size();
    }

    @Override
    public Quiz getItem(int position) {
        return quizList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = li.inflate(R.layout.list_row_notes, null);
            holder.tvSubject = (TextView) convertView.findViewById(R.id.tvGiveQuizSubject);
            holder.tvDateTime = (TextView) convertView.findViewById(R.id.tvGiveQuizDateTime);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvGiveQuizName);
            holder.tvTeacher = (TextView) convertView.findViewById(R.id.tvGiveQuizTeacher);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.tvSubject.setText(quizList.get(position).getSubject());
        holder.tvDateTime.setText(quizList.get(position).getUpload_date());
        holder.tvName.setText(quizList.get(position).getName());
        holder.tvTeacher.setText(quizList.get(position).getTeacher_id());
        return convertView;
    }

    public class ViewHolder {
        TextView tvSubject, tvDateTime, tvName, tvTeacher;
    }

}

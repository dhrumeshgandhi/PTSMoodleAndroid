package tk.only5.ptsmoodle;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by DHRUMESH on 8/2/2016.
 */
public class AddClassSubjectListAdapter extends BaseAdapter {
    ViewHolder holder = null;
    private LayoutInflater li;
    private ArrayList<ExtraDetailsTeacherClassSubject> classSubjectList;

    public AddClassSubjectListAdapter(Context context,
                                      ArrayList<ExtraDetailsTeacherClassSubject> classSubjectList) {
        this.li = LayoutInflater.from(context);
        this.classSubjectList = classSubjectList;
    }

    @Override
    public int getCount() {
        return classSubjectList.size();
    }

    @Override
    public ExtraDetailsTeacherClassSubject getItem(int position) {
        return classSubjectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = li.inflate(R.layout.list_row_extra_details_teacher_class_subjects, null);
            holder.tvClass = (TextView) convertView.findViewById(R.id.tvRowClass);
            holder.tvSubject = (TextView) convertView.findViewById(R.id.tvRowSubject);
            holder.tvDelete = (TextView) convertView.findViewById(R.id.tvRowDelete);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.tvClass.setText(classSubjectList.get(position).getClassItem());
        holder.tvSubject.setText(classSubjectList.get(position).getSubjectItem());
        holder.tvDelete.setText("X");
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classSubjectList.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    protected JSONArray getClassSubjectPairs() {
        try {
            JSONArray classSubjectArray = new JSONArray();
            ExtraDetailsTeacherClassSubject classSubject;
            for (int i = 0; i < classSubjectList.size(); i++) {
                classSubject = classSubjectList.get(i);
                classSubjectArray.put(classSubject.toJSONObject());
            }
            return classSubjectArray;
        } catch (Exception e) {
            Log.e(InitClass.TAG, "ERROR", e);
            return new JSONArray();
        }
    }

    public class ViewHolder {
        TextView tvClass, tvSubject, tvDelete;
    }

}

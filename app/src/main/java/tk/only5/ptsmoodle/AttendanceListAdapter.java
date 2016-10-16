package tk.only5.ptsmoodle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DHRUMESH on 8/2/2016.
 */
public class AttendanceListAdapter extends BaseAdapter {
    ViewHolder holder = null;
    private LayoutInflater li;
    private ArrayList<Attendance> attendanceList;

    public AttendanceListAdapter(Context context, ArrayList<Attendance> attendanceList) {
        this.li = LayoutInflater.from(context);
        this.attendanceList = attendanceList;
    }

    @Override
    public int getCount() {
        return attendanceList.size();
    }

    @Override
    public Attendance getItem(int position) {
        return attendanceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = li.inflate(R.layout.list_row_attendance, null);
            holder.tvSubject = (TextView) convertView.findViewById(R.id.tvSubject);
            holder.tvAttendedLectures = (TextView) convertView.findViewById(R.id.tvAttendedLectures);
            holder.tvAttendancePercentage = (TextView) convertView.findViewById(R.id.tvAttendancePercentage);
            holder.tvTotalLectures = (TextView) convertView.findViewById(R.id.tvTotalLectures);
            holder.tvTeacherName = (TextView) convertView.findViewById(R.id.tvTeacher);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.tvSubject.setText(attendanceList.get(position).getSubject());
        holder.tvAttendedLectures.setText(attendanceList.get(position).getAttendedLectures() + "");
        holder.tvTotalLectures.setText(attendanceList.get(position).getTotalLectures() + "");
        holder.tvAttendancePercentage.setText(attendanceList.get(position).getAttendancePercentage() + "%");
        holder.tvTeacherName.setText(attendanceList.get(position).getTeacherName());
        return convertView;
    }

    public class ViewHolder {
        TextView tvSubject, tvAttendedLectures, tvTotalLectures, tvAttendancePercentage, tvTeacherName;
    }

}
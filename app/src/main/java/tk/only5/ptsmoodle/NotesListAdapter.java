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
public class NotesListAdapter extends BaseAdapter {
    ViewHolder holder = null;
    private LayoutInflater li;
    private ArrayList<Notes> notesList;

    public NotesListAdapter(Context context, ArrayList<Notes> notesList) {
        this.li = LayoutInflater.from(context);
        this.notesList = notesList;
    }

    @Override
    public int getCount() {
        return notesList.size();
    }

    @Override
    public Notes getItem(int position) {
        return notesList.get(position);
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
            holder.tvSubject = (TextView) convertView.findViewById(R.id.tvNotesSubject);
            holder.tvDateTime = (TextView) convertView.findViewById(R.id.tvNotesDateTime);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvNotesName);
            holder.tvTeacher = (TextView) convertView.findViewById(R.id.tvNotesTeacher);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.tvSubject.setText(notesList.get(position).getSubject());
        holder.tvDateTime.setText(notesList.get(position).getUpload_date());
        holder.tvName.setText(notesList.get(position).getName());
        holder.tvTeacher.setText(notesList.get(position).getUploaded_by());
        return convertView;
    }

    public class ViewHolder {
        TextView tvSubject, tvDateTime, tvName, tvTeacher;
    }

}

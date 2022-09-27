package com.ulan.timetable.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ulan.timetable.R;
import com.ulan.timetable.model.Week;
import com.ulan.timetable.utils.AlertDialogsHelper;
import com.ulan.timetable.utils.DbHelper;

import java.util.ArrayList;
import java.util.Objects;


/**
 * Created by Ulan on 08.09.2018.
 */
public class WeekAdapter extends ArrayAdapter<Week> {

    private Activity mActivity;
    private int mResource;
    private ArrayList<Week> weeklist;
    private Week week;
    private ListView mListView;

    private static class ViewHolder {
        TextView subject;
        TextView time;
        ImageView popup;
        TextView mode;
        CardView cardView;
    }

    public WeekAdapter(Activity activity, ListView listView, int resource, ArrayList<Week> objects) {
        super(activity, resource, objects);
        mActivity = activity;
        mResource = resource;
        weeklist = objects;
        mListView = listView;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        String subject = Objects.requireNonNull(getItem(position)).getSubject();
        String time_from = Objects.requireNonNull(getItem(position)).getFromTime();
        String time_to = Objects.requireNonNull(getItem(position)).getToTime();
        String mode = Objects.requireNonNull(getItem(position)).getMode();
        int fromtime_h = Objects.requireNonNull(getItem(position)).getFromtime_h();
        int fromtime_m = Objects.requireNonNull(getItem(position)).getFromtime_m();
        int totime_h = Objects.requireNonNull(getItem(position)).getTotime_h();
        int totime_m = Objects.requireNonNull(getItem(position)).getTotime_m();

        int color = getItem(position).getColor();

        week = new Week(subject, time_from, time_to, color, mode, fromtime_h, fromtime_m, totime_h, totime_m);
        final ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.subject = convertView.findViewById(R.id.subject);
            holder.time = convertView.findViewById(R.id.time);
            holder.popup = convertView.findViewById(R.id.popupbtn);
            holder.mode = convertView.findViewById(R.id.weekmode);
            holder.cardView = convertView.findViewById(R.id.week_cardview);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.subject.setText(week.getSubject());
        holder.time.setText(week.getFromTime() + " - " + week.getToTime());
        holder.cardView.setCardBackgroundColor(week.getColor());
        holder.mode.setText(week.getMode());
        holder.popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(mActivity, holder.popup);
                final DbHelper db = new DbHelper(mActivity);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_popup:
                                db.deleteWeekById(getItem(position));
                                db.updateWeek(getItem(position));
                                weeklist.remove(position);
                                notifyDataSetChanged();
                                return true;

                            case R.id.edit_popup:
                                final View alertLayout = mActivity.getLayoutInflater().inflate(R.layout.dialog_add_subject, null);
                                AlertDialogsHelper.getEditSubjectDialog(mActivity, alertLayout, weeklist, mListView, position);
                                notifyDataSetChanged();
                                return true;
                            default:
                                return onMenuItemClick(item);
                        }
                    }
                });
                popup.show();
            }
        });

        hidePopUpMenu(holder);

        return convertView;
    }

    public ArrayList<Week> getWeekList() {
        return weeklist;
    }

    public Week getWeek() {
        return week;
    }

    private void hidePopUpMenu(ViewHolder holder) {
        SparseBooleanArray checkedItems = mListView.getCheckedItemPositions();
        if (checkedItems.size() > 0) {
            for (int i = 0; i < checkedItems.size(); i++) {
                int key = checkedItems.keyAt(i);
                if (checkedItems.get(key)) {
                    holder.popup.setVisibility(View.INVISIBLE);
                    }
            }
        } else {
            holder.popup.setVisibility(View.VISIBLE);
        }
    }

}

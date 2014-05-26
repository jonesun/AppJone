package com.jone.app.ui.test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jone.app.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jone_admin on 14-2-10.
 */
public class TestAdapter extends ArrayAdapter<String> {
    private int resource;
    private LayoutInflater inflater;
    private boolean[] checks; //用于保存checkBox的选择状态

    public TestAdapter(Context context, int resource, List<String> list) {
        super(context, resource, list);
        checks = new boolean[list.size()];
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void selectAll(){
        for(int i = 0; i < checks.length; i++){
            checks[i] = true;
        }
        notifyDataSetChanged();
    }

    public void delete(boolean itemStates[]){
        List<String> tmp = new ArrayList<>();
        for(int i = 0; i < itemStates.length; i++){
            if(!itemStates[i]){
                tmp.add(getItem(i));
            }
        }
        clear();
        addAll(tmp);
        notifyDataSetChanged();
    }

    public void unselectAll(){
        for(int i = 0; i < checks.length; i++){
            checks[i] = false;
        }
        notifyDataSetChanged();
    }

    public void select(int position){
        checks[position] = true;
        notifyDataSetChanged();
    }

    public void unSelect(int position){
        checks[position] = false;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(getItem(position));
        final int pos  = position; //pos必须声明为final
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                checks[pos] = isChecked;
            }});
        holder.checkBox.setChecked(checks[pos]);
        return convertView;
    }
    static class ViewHolder {
        TextView title;
        CheckBox checkBox;
    }
}

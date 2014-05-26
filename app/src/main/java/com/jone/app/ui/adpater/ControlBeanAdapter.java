package com.jone.app.ui.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jone.app.R;
import com.jone.bean.ControlBean;

import java.util.List;

/**
 * Created by jone_admin on 2014/4/23.
 */
public class ControlBeanAdapter extends ArrayAdapter<ControlBean> {
    private LayoutInflater inflater;
    private int resource;
    public ControlBeanAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
    }

    public void setData(List list){
        clear();
        addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.imBtnControl = (ImageButton) convertView.findViewById(R.id.imBtnControl);
            holder.txtControl = (TextView) convertView.findViewById(R.id.txtControl);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ControlBean controlBean = getItem(position);
        if(controlBean.getImageSrcResource() != null){
            holder.imBtnControl.setImageResource(controlBean.getImageSrcResource());
        }
        if(controlBean.getBackgroundResource() != null){
            holder.imBtnControl.setBackgroundResource(controlBean.getBackgroundResource());
        }
        holder.imBtnControl.setOnClickListener(controlBean.getOnClickListener());
//            holder.imBtnControl.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    Intent intent = new Intent(activity, SelectPicPopupWindowActivity.class);
//                    intent.putExtra("isCut", true);
//                    activity.startActivityForResult(intent, PhotoUtils.GET_PHOTO_CODE);
//                    return true;
//                }
//            });
        holder.txtControl.setText(controlBean.getName());
        return convertView;
    }
    static class ViewHolder{
        ImageButton imBtnControl;
        TextView txtControl;
    }
}

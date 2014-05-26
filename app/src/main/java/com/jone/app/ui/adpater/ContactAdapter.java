package com.jone.app.ui.adpater;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jone.app.R;
import com.jone.app.entities.ContactBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jone_admin on 14-2-17.
 */
public class ContactAdapter extends ArrayAdapter<ContactBean> {
    private LayoutInflater inflater;
    private int resource;
    private List<ContactBean> contactBeanList = null; //所有数据
    private List<ContactBean> objects;

    public ContactAdapter(Context context, int resource, List<ContactBean> objects) {
        super(context, resource, objects);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
//                System.out.println("ContactAdapter performFiltering charSequence: " + charSequence + ", objects: " + objects.size());
                FilterResults results = new FilterResults();
                if(contactBeanList == null){
                    contactBeanList = new ArrayList<>(objects);
                }
                if (charSequence == null || charSequence.length() == 0) {
                    results.count = contactBeanList.size();
                    results.values = contactBeanList;
                } else {
                    List<ContactBean> list = new ArrayList<>();
                    charSequence = charSequence.toString().toLowerCase();
                    for (int i = 0; i < contactBeanList.size(); i++) {
                        ContactBean data = contactBeanList.get(i);
                        if (data.getName().toLowerCase().startsWith(charSequence.toString())) {
                            list.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = list.size();
                    results.values = list;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                System.out.println("ContactAdapter publishResults charSequence: " + charSequence);
                setData((List<ContactBean>) filterResults.values);
            }
        };
//        return super.getFilter();
        return filter;
    }

    public void initDate(List dataList){
        contactBeanList = null;
        setData(dataList);
    }

    public void setData(List dataList){
        this.clear();
        this.addAll(dataList);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.imPhoto = (ImageView) convertView.findViewById(R.id.imPhoto);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtNumber = (TextView) convertView.findViewById(R.id.txtNumber);
            holder.imBtnSendSMS = (ImageButton) convertView.findViewById(R.id.imBtnSendSMS);
            holder.imBtnCall = (ImageButton) convertView.findViewById(R.id.imBtnCall);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ContactBean contactBean = getItem(position);
        if(contactBean.getPhoto() == null){
            holder.imPhoto.setImageResource(R.drawable.icon);
        }else {
            holder.imPhoto.setImageBitmap(contactBean.getPhoto());
        }
        holder.txtName.setText(contactBean.getName());
        holder.txtNumber.setText(contactBean.getNumber());
        holder.imBtnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + contactBean.getNumber())));
            }
        });
        holder.imBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactBean.getNumber())));
            }
        });
        return convertView;
    }


    static class ViewHolder {
        ImageView imPhoto;
        TextView txtName;
        TextView txtNumber;
        ImageButton imBtnSendSMS;
        ImageButton imBtnCall;
    }
}

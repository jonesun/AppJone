package com.jone.app.ui.test;

import android.app.ListFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jone.app.R;

import java.util.ArrayList;
import java.util.List;

import core.common.tuple.Tuple;
import core.common.tuple.Tuple2;

/**
 * Created by jone_admin on 14-2-10.
 */
public class TestListFragment extends ListFragment {
    ViewGroup rootView;
    TestAdapter adapter;
    List<String> dataList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.list_fragment_test, container, false);
        dataList = getData();
        adapter = new TestAdapter(getActivity(), R.layout.item_list_fragment_test, getData());
        setListAdapter(adapter);


        actionss();

        return rootView;
    }

    private ListView listView;

    private boolean itemStates[];
    private int count;
    private void actionss(){
        listView = (ListView) rootView.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                // Here you can do something when items are selected/de-selected,
                // such as update the title in the CAB
                itemStates[position] = checked;
                if(checked){
                    count++;
                    adapter.select(position);
                }else {
                    count--;
                    adapter.unSelect(position);
                }
                if(count > 0){
                    mode.setTitle("选中了" + count + "项");
                }
                System.out.println("onItemCheckedStateChanged: " + position + " id: " + id + " check: " + checked);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.menu_delete:
//                        deleteSelectedItems();
                        System.out.println("删除");
                        adapter.delete(itemStates);
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    case R.id.menu_select_all:
                        adapter.selectAll();
                        return true;
                    case R.id.menu_unselect_call:
                        adapter.unselectAll();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
                itemStates = new boolean[adapter.getCount()];
                count = 0;
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
                adapter.unselectAll();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                System.out.println("onPrepareActionMode");
                return false;
            }
        });
    }

    private List getData() {
        List list = new ArrayList();
        for(int i = 0; i < 30; i++){
            list.add("手机" + i);
        }
        return list;
    }
}

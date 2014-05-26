package com.jone.app.ui.activities;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.jone.app.R;
import com.jone.app.asyncTaskLoader.CustomListAsyncTaskLoader;
import com.jone.app.callbacks.CommonListener;
import com.jone.app.entities.ContactBean;
import com.jone.app.ui.adpater.ContactAdapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends ListActivity implements SearchView.OnQueryTextListener {
    private ListView listView;
    private ContactAdapter contactAdapter;
    private List<ContactBean> contactBeanList = new ArrayList<>();

    private static final String[] CONTACTS_QUERY = {Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID};
    /**联系人显示名称**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**电话号码**/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**头像ID**/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /**联系人的ID**/
    private static final int PHONES_CONTACT_ID_INDEX = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        initViews();
    }

    private int firstVisiblePosition;
    @Override
    protected void onRestart() {
        super.onRestart();
        listView.setSelectionFromTop(firstVisiblePosition, listView.getScrollY()); //todo
//        listView.setSelection(firstVisiblePosition);
        System.out.println("onRestart firstVisiblePosition: " + firstVisiblePosition);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firstVisiblePosition = listView.getFirstVisiblePosition();
        System.out.println("onStop firstVisiblePosition: " + firstVisiblePosition);
    }

    private void initViews(){
        contactAdapter = new ContactAdapter(this, R.layout.layout_item_contact, contactBeanList);
        setListAdapter(contactAdapter);
        getLoaderManager().initLoader(0, null, contactLoaderCallbacks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SearchView searchView = new SearchView(ContactActivity.this);
        setupListView();
        setupSearchView(searchView);

        menu.add(0, 1, 1, "Search")
                .setIcon(android.R.drawable.ic_menu_search)
                .setActionView(searchView)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        return super.onCreateOptionsMenu(menu);
    }

    private void setupListView(){
        listView = getListView();
        listView.setTextFilterEnabled(true);
        listView.setFastScrollEnabled(true);//快速滑动效果
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContactBean contactBean = (ContactBean) adapterView.getItemAtPosition(i);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.contacts/contacts/" + contactBean.getId())));
            }
        });
    }

    private void setupSearchView(SearchView searchView) {
        //设置该SearchView默认是否自动缩小为图标
        searchView.setIconifiedByDefault(true);
        //为该SearchView组件设置事件监听器
        searchView.setOnQueryTextListener(this);
        //设置该SearchView显示搜索按钮
        searchView.setSubmitButtonEnabled(true);

        //设置该SearchView内默认显示的提示文本
        searchView.setQueryHint("查找联系人");
    }

    @Override//单机搜索按钮时
    public boolean onQueryTextSubmit(String s) {
        //实际应用中应该在该方法内执行实际查询
        System.out.println("您选择的是: " + s);
        //隐藏软键盘

        return true;
    }

    @Override//用户输入字符时
    public boolean onQueryTextChange(String s) {
        if(TextUtils.isEmpty(s)){
            listView.clearTextFilter();
        }else {
            listView.setFilterText(s);
        }
        return true;
    }

    /**
     * 获取手机联系人列表
     */
    private List<ContactBean> getPhoneContacts(){
        List<ContactBean> contactBeanList = new ArrayList<>();
        ContentResolver resolver = getContentResolver();
        Cursor contactCursor = resolver.query(Phone.CONTENT_URI, CONTACTS_QUERY, null, null, null);

        if(contactCursor != null){
            while(contactCursor.moveToNext()){
                String contact_number = contactCursor.getString(PHONES_NUMBER_INDEX);
                if(TextUtils.isEmpty(contact_number)){
                    continue;
                }

                String contact_name = contactCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                long contact_id = contactCursor.getLong(PHONES_CONTACT_ID_INDEX);
                long contact_pic_id = contactCursor.getLong(PHONES_PHOTO_ID_INDEX);

                Bitmap contact_photo = null;

                if(contact_pic_id > 0){
                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contact_id);
                    InputStream is = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                    contact_photo = BitmapFactory.decodeStream(is);
                }
                contactBeanList.add(new ContactBean(contact_id, contact_name, contact_number, contact_photo));
            }
            contactCursor.close();
        }
        return contactBeanList;
    }

    /**得到手机SIM卡联系人人信息**/
    private List<ContactBean> getSIMContacts() {
        List<ContactBean> contactBeanList = new ArrayList<>();
        ContentResolver resolver = getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, CONTACTS_QUERY, null, null,
                null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                long contact_id = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                //Sim卡中没有联系人头像
                contactBeanList.add(new ContactBean(contact_id, contactName, phoneNumber, null));
            }

            phoneCursor.close();
        }
        return contactBeanList;
    }

    private LoaderManager.LoaderCallbacks<List> contactLoaderCallbacks = new LoaderManager.LoaderCallbacks<List>() {
        @Override
        public Loader<List> onCreateLoader(int i, Bundle bundle) {
            return new CustomListAsyncTaskLoader(new CommonListener() {
                @Override
                public Object onExecute(Object o) {
                    contactBeanList = getPhoneContacts();
                    contactBeanList.addAll(getSIMContacts());
                    return contactBeanList;
                }
            });
        }

        @Override
        public void onLoadFinished(Loader<List> listLoader, List list) {
            contactAdapter.initDate(list);
        }

        @Override
        public void onLoaderReset(Loader<List> listLoader) {
            contactAdapter.clear();
        }
    };
}

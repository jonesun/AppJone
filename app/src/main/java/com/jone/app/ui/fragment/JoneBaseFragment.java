package com.jone.app.ui.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Created by jone_admin on 2014/3/21.
 */
public abstract class JoneBaseFragment extends Fragment{
    public void onShow(){}
    public void onHide(){}

    public static void addFragment(FragmentManager fragmentManager, int attachResource, JoneBaseFragment fragment, String fragmentTag){
        if(fragmentManager != null){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(attachResource, fragment, fragmentTag).commit();
        }
    }

    public static void switchFragment(FragmentManager fragmentManager, int attachResource, JoneBaseFragment fromFragment, JoneBaseFragment toFragment, String toFragmentTag){
        if(fragmentManager != null){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if(fromFragment == null){
                transaction.add(attachResource, toFragment, toFragmentTag).commit();
                return;
            }
            if(toFragment.isAdded()){
                transaction.hide(fromFragment).show(toFragment).commit();
                toFragment.onShow();
            }else {
                transaction.hide(fromFragment).add(attachResource, toFragment, toFragmentTag).commit();
            }
            fromFragment.onHide();
        }
    }
}

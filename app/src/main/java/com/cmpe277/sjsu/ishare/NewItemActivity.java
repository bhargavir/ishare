package com.cmpe277.sjsu.ishare;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/*
 * NewMealActivity contains two fragments that handle
 * data entry and capturing a photo of a given meal.
 * The Activity manages the overall meal data.
 */
public class NewItemActivity extends Activity {

    private IShareItem item;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        item = new IShareItem();
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

      
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar mActionBar =  getActionBar();
        //  System.out.println("@@@@ mActionBar "+mActionBar);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomViewNew = mInflater.inflate(R.layout.customlayoutactionnewitem, null);

        mActionBar.setCustomView(mCustomViewNew);
        mActionBar.setDisplayShowCustomEnabled(true);

        // Begin with main data entry view,

        // NewItemFragment
        setContentView(R.layout.activity_new_item);

        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new NewItemFragment();
            manager.beginTransaction().add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    public IShareItem getCurrentItem() {
        return item;
    }

}


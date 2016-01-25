package com.cmpe277.sjsu.ishare;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


public class RequestedList extends ListActivity {

    //private ParseQueryAdapter<IShareItem> mainAdapter;
    //not using this one for now
    //private FavoriteMealAdapter favoritesAdapter;
    public ItemAdapterRequested mainAdapterReq;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        ActionBar mActionBar =  getActionBar();
        //  System.out.println("@@@@ mActionBar "+mActionBar);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.customlayoutaction_requestlist, null);

        // this is icon click
        ImageView imageview = (ImageView) mCustomView.findViewById(R.id.ivIconBackPerson_req);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Icon Clicked!",Toast.LENGTH_LONG).show();
                //finish();
            }
        });

        // Add new Item is clicked
        ImageView imageViewCamera = (ImageView) mCustomView.findViewById(R.id.imageButtonCamera_req);
        imageViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getApplicationContext(), "Camera Clicked!",Toast.LENGTH_LONG).show();
                newItem();
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);


        //old code starts from here -- santanu
        // need to modify the listview adapter as per need
        //santanu changed
        //getListView().setClickable(false);
        getListView().setClickable(true);

        //mainAdapter = new ParseQueryAdapter<IShareItem>(this, IShareItem.class);
        //mainAdapter = new ItemAdapter(this,IShareItem.class);

        mainAdapterReq = new ItemAdapterRequested(this);
        mainAdapterReq.setTextKey("title");
        mainAdapterReq.setImageKey("photo");

        // Subclass of ParseQueryAdapter
        //commented
        //favoritesAdapter = new FavoriteMealAdapter(this);

        // Default view is all meals
        setListAdapter(mainAdapterReq);

        //    Toast.makeText(getApplicationContext(), "@@@ at ItemListActivity"+mainAdapter, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
        //Toast.makeText(this, "Clicked row " + position, Toast.LENGTH_SHORT).show();

        String titleRow = mainAdapterReq.getItem(position).getTitle();
        String descRow = mainAdapterReq.getItem(position).getDescription();

        //       String category = mainAdapter.getItem(position).getCategory();

        //lets implement this later
        //ParseFile imagRow = mainAdapter.getItem(position).getPhotoFile();

        //Toast.makeText(getApplicationContext(), "@@@ titleRow "+titleRow+ "  descRow "+descRow, Toast.LENGTH_LONG).show();
        Intent rowInfo = new Intent(RequestedList.this, RowInfoDisplay.class);
        rowInfo.putExtra("titleRow",titleRow);
        rowInfo.putExtra("descRow",descRow);
        //     rowInfo.putExtra("category",category);
        rowInfo.putExtra("rowId",position);
        startActivity(rowInfo);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //commenting for now
        getMenuInflater().inflate(R.menu.activity_item_list, menu);
        return true;
    }*/

    /*
     * Posting meals and refreshing the list will be controlled from the Action
     * Bar.
     */
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_refresh: {
             
               // updateItemList();
                break;
            }

            *//*case R.id.action_snap: {
                showFavorites();
                break;
            }*//*

            case R.id.action_snap: {
                newMeal();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }*/

    private void updateMealList() {
        mainAdapterReq.loadObjects();
        setListAdapter(mainAdapterReq);
    }

    private void showFavorites() {
     
        // favoritesAdapter.loadObjects();
        // setListAdapter(favoritesAdapter);
    }

    /*private void newMeal() {
        Intent i = new Intent(this, NewItemActivity.class);
        startActivityForResult(i, 0);
    }*/

    private void newItem() {
        Intent i = new Intent(this, NewItemActivity.class);
        startActivityForResult(i, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // If a new post has been added, update
            // the list of posts
            updateMealList();
        }
    }
}

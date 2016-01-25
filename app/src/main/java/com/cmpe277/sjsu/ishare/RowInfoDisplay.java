package com.cmpe277.sjsu.ishare;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ui.ParseLoginBuilder;

import java.util.List;

public class RowInfoDisplay extends Activity{

    private String titleRow, descRow, categoryRow;
    private TextView titleDisplay, descDisplay, catDisplay;
    private ImageView imageRow;

    private Button requestItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActionBar mActionBar =  getActionBar();
        //  System.out.println("@@@@ mActionBar "+mActionBar);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.customlayoutactionnewitem, null);

        // Requested is clicked
        ImageView imageViewRequestedList = (ImageView) mCustomView.findViewById(R.id.imageButtonRequestedList);
        imageViewRequestedList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               // Toast.makeText(getApplicationContext(), "Requested List from row screen!", Toast.LENGTH_LONG).show();
                // add implementation for requested item
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        setContentView(R.layout.activity_rowinfo_display);

        titleDisplay = (TextView)findViewById(R.id.tvTitleRow);
        descDisplay = (TextView)findViewById(R.id.tvDescRow);
        catDisplay = (TextView)findViewById(R.id.tvCategoryRow);

        titleRow = getIntent().getStringExtra("titleRow");
        descRow = getIntent().getStringExtra("descRow");
        //categoryRow = getIntent().getStringExtra("category");
        final int rowId = getIntent().getIntExtra("rowId",0);

        titleDisplay.setText(titleRow);
        descDisplay.setText(descRow);
        //catDisplay.setText(categoryRow);

        //imageRow = (ParseImageView)findViewById(R.id.ivRowPictureBig);
       // ParseFile image = ItemListActivity.mainAdapter.getItem(rowId).getPhotoFile();
        //imageRow.setBackgroundResource(image);


        ParseImageView imageRow = (ParseImageView)findViewById(R.id.ivRowPictureBig);
        ParseFile photoFile = ItemListActivity.mainAdapter.getItem(rowId).getPhotoFile();
        if (photoFile != null) {
            imageRow.setParseFile(photoFile);
            imageRow.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    // nothing to do
                }
            });
        }

        requestItem = (Button)findViewById(R.id.btRequestItem);
        IShareItem item;
        requestItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                ParseQuery<IShareItem> reqQuery = ParseQuery.getQuery(IShareItem.class);
                reqQuery.whereEqualTo("title",titleRow);
                reqQuery.whereEqualTo("description",descRow);

                reqQuery.findInBackground(new FindCallback<IShareItem>() {
                    @Override
                    public void done(List<IShareItem> iShareItems, ParseException e) {
                        IShareItem item = iShareItems.get(0);
                        item.setRequested("true");

                        // Saveing the item and return
                        item.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    RowInfoDisplay.this.setResult(Activity.RESULT_OK);
                                    RowInfoDisplay.this.finish();
                                } else {
                                     Toast.makeText(
                                            RowInfoDisplay.this.getApplicationContext(),
                                            "Error saving: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                        });

                    }


                });



                // finishing the activity
                finish();
            }
        });
    }// end of onCreate

}

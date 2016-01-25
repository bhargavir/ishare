package com.cmpe277.sjsu.ishare;

import java.util.Arrays;
import android.content.Context;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import com.parse.GetDataCallback;
        import com.parse.ParseException;
        import com.parse.ParseFile;
        import com.parse.ParseImageView;
        import com.parse.ParseQuery;
        import com.parse.ParseQueryAdapter;

/*
 * The FavoriteMealAdapter is an extension of ParseQueryAdapter
 * that has a custom layout for favorite meals, including a
 * bigger preview image, the meal's rating, and a "favorite"
 * star.
 */

public class ItemAdapter extends ParseQueryAdapter<IShareItem> {

    public ItemAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<IShareItem>() {
            public ParseQuery<IShareItem> create() {
                // Here we can configure a ParseQuery to display
                // only top-rated meals.
                ParseQuery query = new ParseQuery("IshareItem");
                query.orderByDescending("title");
                query.whereContains("requested","false");
                try {
                    System.out.println("@@@@@@ query " + query.count());

                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                    return query;
            }
        });
    }

    @Override
    public View getItemView(IShareItem item, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_list_custom, null);
        }
        System.out.println("@@@@@@@@@@@@@ parent "+parent+" v "+v+" item "+item);

      //  super.getItemView(item, v, parent);

        ParseImageView mealImage = (ParseImageView) v.findViewById(R.id.icon);
        ParseFile photoFile = item.getParseFile("photo");
        if (photoFile != null) {
            mealImage.setParseFile(photoFile);
            mealImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    // nothing to do
                }
            });
        }

        TextView titleTextView = (TextView) v.findViewById(R.id.title);
        titleTextView.setText(item.getTitle());
       // TextView ratingTextView = (TextView) v.findViewById(R.id.descListview);
        //ratingTextView.setText(item.getCategory());         //.getRating());
        return v;
    }

}

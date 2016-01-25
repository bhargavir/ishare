package com.cmpe277.sjsu.ishare;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.net.URL;

/*
 * This fragment manages the data entry for a
 * new Meal object. It lets the user input a
 * meal name, give it a rating, and take a
 * photo. If there is already a photo associated
 * with this meal, it will be displayed in the
 * preview at the bottom, which is a standalone
 * ParseImageView.
 */
public class NewItemFragment extends Fragment {

   /* private ImageButton photoButton;
    private Button saveButton;
    private Button cancelButton;
    private TextView mealName;
    private Spinner mealRating;
    private ParseImageView mealPreview;*/

//    private ImageView photoImageView;
    private  EditText title, desc;
    private Spinner catSpinner;
    private Button saveBtn;
    private Button cancelBtn;

    private ParseImageView itemPreview;
    private TextView itemName;
    private LocationManager locationManager;
    private Location mLocation;

  //  private ParseImageView itemPreview;
    private ParseImageView photoImageView;
  //  private TextView itemName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle SavedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_new_item_details, parent, false);

        //mealName = ((EditText) v.findViewById(R.id.meal_name));
        //itemName = ((TextView) v.findViewById(R.id.tvTitle));

        title = (EditText)v.findViewById(R.id.etItemTitle);
        desc = (EditText)v.findViewById(R.id.etItemDesc);
        catSpinner = (Spinner)v.findViewById(R.id.category_spinner);
        //photoImageView = (ImageView)v.findViewById(R.id.ivItemImage);
        photoImageView = (ParseImageView)v.findViewById(R.id.ivItemImage);

        // The mealRating spinner lets people assign favorites of meals they've eaten.
        // Meals with 4 or 5 ratings will appear in the Favorites view.

        /*mealRating = ((Spinner) v.findViewById(R.id.rating_spinner));
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.ratings_array,
                        android.R.layout.simple_spinner_dropdown_item);
        mealRating.setAdapter(spinnerAdapter);*/

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.category_array,
                        android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(spinnerAdapter);

        //photoButton = ((ImageButton) v.findViewById(R.id.photo_button));
        photoImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
                startCamera();
            }
        });

        saveBtn = ((Button) v.findViewById(R.id.save_button));
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                IShareItem item = ((NewItemActivity) getActivity()).getCurrentItem();

                // When the user clicks "Save," upload the meal to Parse
                // Add data to the item object:
                // Associate the item with the current user
                item.setAuthor(ParseUser.getCurrentUser());
                item.setTitle(title.getText().toString());
                item.setDescription(desc.getText().toString());
                item.setCategory(catSpinner.getSelectedItem().toString());
                item.setRequested("false");

                // Add the rating
               // meal.setRating(mealRating.getSelectedItem().toString());

                // If the user added a photo, that data will be
                // added in the CameraFragment


                // Add location to  Parse
                item.setLocation(new ParseGeoPoint(mLocation.getLatitude(), mLocation.getLongitude()));

                // Save the meal and return

                item.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            getActivity().setResult(Activity.RESULT_OK);
                            getActivity().finish();
                        } else {
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    "Error saving: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });

            }
        });

        cancelBtn = ((Button) v.findViewById(R.id.cancel_button));
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });

        // Until the user has taken a photo, hide the preview
        //santanu changing
       // itemPreview = (ParseImageView) v.findViewById(R.id.meal_preview_image);
       // itemPreview.setVisibility(View.INVISIBLE);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Do a fast get
        Criteria criteria = new Criteria();
        String bestProvider = /*LocationManager.GPS_PROVIDER;*/  locationManager.getBestProvider(criteria, true);
        mLocation = locationManager.getLastKnownLocation(bestProvider);
        new GetImageTask((ImageView) v.findViewById(R.id.imageMap)).execute("https://maps.googleapis.com/maps/api/staticmap?size=720x405&maptype=roadmap" +
                "&markers=color:red%7C" + mLocation.getLatitude() + "," + mLocation.getLongitude());

        // Do a slow update
        locationManager.requestSingleUpdate(bestProvider, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // TextView latLongText = (TextView) v.findViewById(R.id.latLong);
                // latLongText.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
                mLocation = location;
                new GetImageTask((ImageView) v.findViewById(R.id.imageMap)).execute("https://maps.googleapis.com/maps/api/staticmap?size=720x405&maptype=roadmap" +
                        "&markers=color:red%7C" + location.getLatitude() + "," + location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        }, null);


        return v;
    }

    /*
     * All data entry about a Meal object is managed from the NewMealActivity.
     * When the user wants to add a photo, we'll start up a custom
     * CameraFragment that will let them take the photo and save it to the Meal
     * object owned by the NewMealActivity. Create a new CameraFragment, swap
     * the contents of the fragmentContainer (see activity_new_meal.xml), then
     * add the NewMealFragment to the back stack so we can return to it when the
     * camera is finished.
     */
    public void startCamera() {
        Fragment cameraFragment = new CameraFragment();
        FragmentTransaction transaction = getActivity().getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragmentContainer, cameraFragment);
        transaction.addToBackStack("NewItemFragment");
        transaction.commit();
    }

    /*
     * On resume, check and see if a meal photo has been set from the
     * CameraFragment. If it has, load the image in this fragment and make the
     * preview image visible.
     */
    @Override
    public void onResume() {
        super.onResume();
        ParseFile photoFile = ((NewItemActivity) getActivity())
                .getCurrentItem().getPhotoFile();
        if (photoFile != null) {
           // itemPreview.setParseFile(photoFile);
            photoImageView.setParseFile(photoFile);
            photoImageView.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) photoImageView.getLayoutParams();
                   // params.width = 120;
                    params.height = 260;
                    // existing height is ok as is, no need to edit it
                    photoImageView.setLayoutParams(params);
                    //photoImageView.getLayoutParams().height = 150;
                    photoImageView.setVisibility(View.VISIBLE);

                }
            });
        }
    }

    private class GetImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView image;

        public GetImageTask(ImageView image) {
            this.image = image;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap bmp = null;
            try {
                URL url = new URL(urls[0]);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                Log.e("Error", urls[0]);
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }

        protected void onPostExecute(Bitmap result) {
            image.setImageBitmap(result);
        }
    }

}

package com.example.fshen4.CS442_Places_Near_Me;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private GMapV2Direction md;
    private EditText address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.fshen4.CS442_Places_Near_Me.R.layout.activity_maps);
        address = (EditText)findViewById(R.id.tAddress);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(com.example.fshen4.CS442_Places_Near_Me.R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        setUpMapIfNeeded();
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
        }



    }

    private void setUpMapIfNeeded() {
        return;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng stuartBuilding = new LatLng(41.839118, -87.627351);
        //mMap.addMarker(new MarkerOptions().position(stuartBuilding).title("Marker in Stuart Building"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(stuartBuilding));
        // 刪除原來預設的內容
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        // 建立位置的座標物件
        LatLng place = new LatLng(41.838598, -87.627383);

        // 移動地圖
        moveMap(place);
        addMarker(place, "Hello!", "Stuart Building");
        mMap.setMyLocationEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.fshen4.Places_Near_Me/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.fshen4.Places_Near_Me/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    // 移動地圖到參數指定的位置
    private void moveMap(LatLng place) {
        // 建立地圖攝影機的位置物件
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(13)
                        .build();

        // 使用動畫的效果移動地圖
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    // 在地圖加入指定位置與標題的標記
    private void addMarker(LatLng place, String title, String snippet) {
        BitmapDescriptor icon =
                com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource(com.example.fshen4.CS442_Places_Near_Me.R.mipmap.ic_map_marker);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place)
                .title(title)
                .snippet(snippet)
                .icon(icon);

        mMap.addMarker(markerOptions);
    }
    public boolean onSearchRequesting(View view) {
        //tracking user and calory calculation
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        return true;
    }

    public boolean onSearchRequested(View view) {
        EditText tLocation = (EditText)findViewById(com.example.fshen4.CS442_Places_Near_Me.R.id.tAddress);
        String sLocation = tLocation.getText().toString();
        List<Address> addressList = null;
        mMap.clear();
        if (sLocation != null || !sLocation.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(sLocation, 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(int i = 0; i<addressList.size(); i++) {
                Log.d(String.valueOf(addressList.get(i).getAdminArea()), "AdminArea");
                Log.d(String.valueOf(addressList.get(i).getLatitude()), "Latitude");
                Log.d(String.valueOf(addressList.get(i).getLongitude()), "Longitude");
                Log.d(String.valueOf(addressList.get(i).getPhone()), "Phone");
                Log.d(String.valueOf(addressList.get(i).getPostalCode()), "PostalCode");
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(com.example.fshen4.CS442_Places_Near_Me.R.id.map)).getMap();
            LatLng sourcePosition = new LatLng(41.843730, -87.621782);
            LatLng destPosition = new LatLng(41.864909, -87.666747);
            md = new GMapV2Direction(sourcePosition, destPosition, GMapV2Direction.MODE_WALKING);
            md.execute();
            Document doc = null;
            try {
                doc = md.get();

                ArrayList<LatLng> directionPoint = md.getDirection(doc);
                PolylineOptions rectLine = new PolylineOptions().width(8)
                        .color(Color.BLUE);

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                Polyline polylin = mMap.addPolyline(rectLine);
            } catch (Exception e) {
                //nothing to do for now
            }

            new GetPlaces(this, "ATM".toLowerCase().replace("-", "_").replace(" ", "_"), 5000).execute();
        }

        // 除了输入查询的值，还可额外绑定一些数据
        //Bundle appSearchData = new Bundle();
        //appSearchData.putString("KEY", "text");

        //startSearch(null, false, appSearchData, false);
        // 必须返回true。否则绑定的数据作废
        return true;
    }

    public void onZoom(View view) {
        if(view.getId() == com.example.fshen4.CS442_Places_Near_Me.R.id.Zoomin) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if(view.getId() == com.example.fshen4.CS442_Places_Near_Me.R.id.Zoomout) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    private class GetPlaces extends AsyncTask<Void, Void, ArrayList<Place>> {

        private ProgressDialog dialog;
        private String places;
        private double radius;

        public GetPlaces(MapsActivity mapsActivity, String places, double radius) {
            this.places = places;
            this.radius = radius;
        }

        @Override
        protected void onPostExecute(ArrayList<Place> result) {
            super.onPostExecute(result);
            //if (dialog.isShowing()) {
            //    dialog.dismiss();
            //}
            LatLng cll = new LatLng(41.843730, -87.621782);
            // see more on Marker: https://developers.google.com/maps/documentation/android/reference/com/google/android/gms/maps/model/Marker
            mMap.addMarker(new MarkerOptions().position(cll).title("Current Location"));
            for (int i = 0; i < result.size(); i++) {
                mMap.addMarker(new MarkerOptions()
                        .title(result.get(i).getName())
                        .position(
                                new LatLng(result.get(i).getLatitude(), result
                                        .get(i).getLongitude()))
                        .icon(BitmapDescriptorFactory
                                .fromResource(com.example.fshen4.CS442_Places_Near_Me.R.drawable.pin))
                        .snippet(result.get(i).getVicinity()));
                LatLng tempLoc = new LatLng(result.get(i).getLatitude(), result.get(i).getLongitude());
                md = new GMapV2Direction(cll, tempLoc, GMapV2Direction.MODE_WALKING);
                md.execute();
                Document doc = null;
                // Document doc = md.getDocument(cll, zll,
                // GMapV2Direction.MODE_DRIVING);
                try {
                    // get the result from the asynctask returned by Google,
                    // wait if necessary
                    doc = md.get();

                    // now process/parse the results from Google
                    ArrayList<LatLng> directionPoint = md.getDirection(doc);

                    // here, draw the lines based on the direction points
                    PolylineOptions rectLine = new PolylineOptions().width(8)
                            .color(Color.BLUE);

                    for (int j = 0; j < directionPoint.size(); j++) {
                        rectLine.add(directionPoint.get(j));
                    }
                    Polyline polylin = mMap.addPolyline(rectLine);

                } catch (Exception e) {
                    // just ignore here, possible exceptions thrown by the
                    // md.get() call:
                    // see
                    // http://developer.android.com/reference/android/os/AsyncTask.html#get()
                }
            }
            /*
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(result.get(0).getLatitude(), result
                            .get(0).getLongitude())) // Sets the center of the map to
                            // Mountain View
                    .zoom(13) // Sets the zoom
                    .tilt(30) // Sets the tilt of the camera to 30 degrees
                    .build(); // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            */
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog = new ProgressDialog(getApplicationContext());
            //dialog.setCancelable(false);
            //dialog.setMessage("Loading..");
            //dialog.isIndeterminate();
            //dialog.show();
        }

        @Override
        protected ArrayList<Place> doInBackground(Void... arg0) {
            PlacesService service = new PlacesService("AIzaSyC0eOv4pn7xPPauCtkVE960iG3UM_O-ZSc");
            ArrayList<Place> findPlaces = service.findPlaces(41.843730, -87.621782, places, radius);

            for (int i = 0; i < findPlaces.size(); i++) {

                Place placeDetail = findPlaces.get(i);
                Log.e("MAPS", "places : " + placeDetail.getName());
            }
            return findPlaces;
        }

    }
}

package cj.patience.tentsizer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Order extends AppCompatActivity {

    public LocationManager locationManager;
    public String svcName = Context.LOCATION_SERVICE;
    public TextView autoUpdate;
    public TextView firstCorner;
    public TextView secondCorner;
    public TextView thirdCorner;
    public TextView tentSizeTV;
    public String latLongString;
    public double[] firstCornerLatLng;
    public double[] secondCornerLatLng;
    public double[] thirdCornerLatLng;
    public static final String FIRSTCORNER = "first_corner";
    public static final String SECONDCORNER = "second_corner";
    public static final String THIRDCORNER = "third_corner";
    public static final String TENTSIZE = "tent_size";

    public String[] tentSizeList = new String[]{"20x20", "20x40", "20x60", "40x40", "40x60", "40x80", "40x100", "60x60", "60x80", "60x100"};
    int CornerLat;
    int CornerLong;
    double lat;
    double lng;
    public int indexOfTentSize;
    public int tentSizeMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Button x = (Button) findViewById(R.id.button2);
        Button y = (Button) findViewById(R.id.delete_corner);
        locationManager = (LocationManager) getSystemService(svcName);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        String provider = locationManager.getBestProvider(criteria, true);
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
        Location first = locationManager.getLastKnownLocation(provider);
        updateLocation(first);
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thirdCorner != null){
                    thirdCorner = null;
                    return;
                } else if (secondCorner != null){
                    secondCorner = null;
                    return;
                }else if (firstCorner != null){
                    firstCorner = null;
                }
            }
        });
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstCorner = (TextView) findViewById(R.id.first_Corner);
                secondCorner = (TextView) findViewById(R.id.second_Corner);
                thirdCorner = (TextView) findViewById(R.id.third_Corner);
                if (firstCorner == null) {
                    firstCorner.setText("First Corner is:\n" + latLongString);
                    CornerLat = (int) lat;
                    CornerLong = (int) lng;
                    firstCornerLatLng = new double[]{lat, lng};
                    return;
                }
                if (secondCorner == null) {
                    secondCorner.setText("Second Corner is:\n" + latLongString);
                    CornerLat = (int) lat;
                    CornerLong = (int) lng;
                    secondCornerLatLng = new double[]{lat, lng};
                    tentSizeMarker = indexOfTentSize;
                    return;
                }
                if (thirdCorner == null) {
                    thirdCornerLatLng = new double[]{lat, lng};
                    thirdCorner.setText("Third Corner is:\n" + latLongString);
                    return;
                }
            }});
          Button z = (Button) findViewById(R.id.start_placing_order);
          z.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  startPlaceOrder();
              }
          });

        locationManager.requestLocationUpdates(provider, 2000, 10, mLocationListener);
    }

    public void startPlaceOrder(){
        Intent x = new Intent(this, PlaceOrder.class);
        Bundle y = new Bundle();
        y.putString(TENTSIZE, (String) tentSizeTV.getText());
        y.putDoubleArray(FIRSTCORNER, firstCornerLatLng);
        y.putDoubleArray(SECONDCORNER, secondCornerLatLng);
        y.putDoubleArray(THIRDCORNER, thirdCornerLatLng);
        x.putExtras(y);
        startActivity(x);
    }

    private void updateLocation(Location location){
        autoUpdate = (TextView) findViewById(R.id.autoupdate);
        latLongString = "No location found";
        if (location != null){
            lat = location.getLatitude();
            lng = location.getLongitude();
            latLongString = "Lat:" + lat + "\nLong:" + lng;
            if(firstCorner == null){
                autoUpdate.setText(latLongString);
            }
            else{
                autoUpdate.setText("Feet from Corner:" + (gpsDistance(lat, lng)));
                tentSize(gpsDistance(lat, lng));
            }
        }
    }

    public void tentSize(int x){
        tentSizeTV = (TextView) findViewById(R.id.tent_size);
        if(secondCorner != null){
            tentSizeTV.setText(tentSizeList[(x/20) + indexOfTentSize]);
            return;
        }
        indexOfTentSize = x/20;
        tentSizeTV.setText(tentSizeList[x/20]);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateLocation(location);
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
    };

    public int dToR(int degrees) {
        return (int) (degrees * Math.PI / 180);
    }

    public int gpsDistance(double lat, double lng){
        int measurementft = 20903520;
        int mLat = dToR((int) (CornerLat - lat));
        int mLon = dToR((int) (CornerLong - lng));
        lat = dToR((int)lat);
        int lat2 = dToR((int) CornerLat);
        double x = Math.sin(mLat/2) * Math.sin(mLat/2) +
                Math.sin(mLon/2) * Math.sin(mLon/2) * Math.cos(lat) * Math.cos(lat2);
        double y = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(1 - x));
        return (int) (measurementft * y);

        /** Alternative Method faster?
        double p = 0.5 - Math.cos((firstCornerLat - lat) * PI) / 2 +
                Math.cos(lat * PI) * Math.cos(firstCornerLat * PI) *
                        ( 1 - Math.cos((firstCornerLong - lng) * PI)) / 2;
        return 12742 * Math.asin(Math.sqrt(p)) // 2 * R; R = 6371 km
        */
    }
}

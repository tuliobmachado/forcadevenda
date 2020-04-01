package br.com.informsistemas.forcadevenda.model.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.activity.MainActivity;
import br.com.informsistemas.forcadevenda.fragments.MovimentoFragment;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.helper.Misc;

public class LocalizacaoTask extends AsyncTask<String, Integer, Integer> {

    private Context context;
    private ProgressDialog dialog;
    private boolean pedido;
    private LocationManager locationManager;
    private Location location;
    private boolean GPS;
    private double lati = 0.0;
    private double longi = 0.0;

    public LocalizacaoTask(Context context, boolean pedido) {
        this.context = context;
        this.pedido = pedido;

        dialog = new ProgressDialog(context, R.style.DialogDefault);
        dialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Obtendo localização...");
        dialog.show();
    }

    @Override
    protected Integer doInBackground(String... strings) {
        GPS = Misc.onGPSActive((MainActivity) context);

        if (GPS) {
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }

            while (this.lati == 0.0) {
                location = getLocation();
                if (location != null) {
                    lati = location.getLatitude();
                    longi = location.getLongitude();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (!GPS) {
            dialog.dismiss();
            Misc.onOpenSettingsGPS((MainActivity) context);
        } else {
            Constants.LOCATION.LATITUDE = lati;
            Constants.LOCATION.LONGITUDE = longi;
            dialog.dismiss();

            if (pedido) {
                ((MainActivity) context).onPedido();
            } else {
                ((MovimentoFragment) ((MainActivity) context).movimentoFragment).onPedido();
            }
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lati = location.getLatitude();
            longi = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            location = null;
        }
    };

    private Location getLocation() {
        int MIN_TIME_BW_UPDATES = 10000;
        int MIN_DISTANCE_CHANGE_FOR_UPDATES = 10000;
        try {
            locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isPassiveEnabled = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
            boolean isNetWorkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled || isNetWorkEnabled || isNetWorkEnabled) {
                if (isGPSEnabled && location == null) {
                    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                    Log.d("GPS", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }

                }

                if (isPassiveEnabled && location == null) {
                    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                    Log.d("Provider", "Provider Enabled");
                    if (locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    }
                }

                if (isNetWorkEnabled && location == null){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                    Log.d("Network", "Network Enabled");
                    if (locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
            }else{
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

}

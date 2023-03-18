package com.example.cityevents;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cityevents.databinding.FragmentSecondBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SecondFragment extends Fragment implements OnMapReadyCallback{

    private FragmentSecondBinding binding;
    private MapView mapView;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private final int REQUEST_CODE_ACCESS_FINE_LOCATION = 1;
    private final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 2;
    String weatherAPI = "633a5e7b43ec4e90ba222914231603";
    String url;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(callback);
        return binding.getRoot();
    }

    private final OnMapReadyCallback callback = googleMap -> {
        Bundle bundle = getArguments();
        int eventId = bundle.getInt("event_id");
        Cursor cursor = mDb.rawQuery("SELECT * FROM Events WHERE id = ?", new String[] {Integer.toString(eventId)});
        cursor.moveToFirst();

        LatLng krasnoyarsk = new LatLng(Double.parseDouble(cursor.getString(7)), Double.parseDouble(cursor.getString(6)));
        googleMap.addMarker(new MarkerOptions().position(krasnoyarsk).title(cursor.getString(1)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(krasnoyarsk, 12));

        cursor.close();
    };

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
                return;
        }
        map.setMyLocationEnabled(true);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        int eventId;
        if (bundle != null) {
            eventId = bundle.getInt("event_id");

            mDBHelper = new DatabaseHelper(getActivity().getApplicationContext());

            try {
                mDBHelper.updateDataBase();
            } catch (IOException mIOException) {
                Toast.makeText(getActivity(), "Ошибка обновления базы данных",
                        Toast.LENGTH_LONG).show();
            }

            try {
                mDb = mDBHelper.getWritableDatabase();
            } catch (SQLException mSQLException) {
                Toast.makeText(getActivity(), "Ошибка обновления базы данных",
                        Toast.LENGTH_LONG).show();
            }
            Cursor cursor = mDb.rawQuery("SELECT * FROM Events WHERE id = ?", new String[] {Integer.toString(eventId)});
            cursor.moveToFirst();
            Event event = new Event(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), Double.parseDouble(cursor.getString(4)), cursor.getString(5), Integer.parseInt(cursor.getString(8)));
            cursor.close();;
            String month_str = event.getDate().substring(3, 5);
            int month = "0".equals(month_str.substring(0, 1)) ? Integer.parseInt(month_str.substring(1)) : Integer.parseInt(month_str);
            int year = 2023;
            if (month <= 3)
                month += 8;
            String date = event.getDate().substring(0, 2) + "";
            String zero = month / 10 == 0 ? "0" : "";
            url = "https://api.weatherapi.com/v1/future.json?key=" + weatherAPI + "&q=" + "Krasnoyarsk" + "&dt=" + year + "-" + zero + month + "-" + date.substring(0, 2);
            binding.textView5.setText(event.getName());
            binding.textView6.setText(event.getDate());
            binding.textView7.setText(event.getTime());
            if (event.getCost() != 0)
                binding.textView4.setText(event.getCost() + "₽");
            else
                binding.textView4.setText("Бесплатно");
            binding.textView8.setText(event.getLength());
        }

        binding.buttonSecond.setOnClickListener(view1 -> {
            Toolbar toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.events_list);
            Fragment fragment = new FirstFragment();
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment_content_main, fragment);
            ft.commit();
        });
        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE_ACCESS_FINE_LOCATION);
        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_CODE_ACCESS_COARSE_LOCATION);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                    try {
                        binding.textView2.setText(((JSONObject) response.getJSONObject("forecast")
                                .getJSONArray("forecastday").get(0)).getJSONObject("day").getString("avgtemp_c") + "° C");
                        String res = ((JSONObject) response.getJSONObject("forecast").getJSONArray("forecastday")
                                .get(0)).getJSONObject("day").getJSONObject("condition").getString("icon");
                        System.out.println(res);
                        URL aURL = new URL("https:" + res + "?key=" + weatherAPI);
                        URLConnection conn = aURL.openConnection();
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        BufferedInputStream bis = new BufferedInputStream(is);
                        Bitmap bmp = BitmapFactory.decodeStream(bis);
                        binding.imageView.setImageBitmap(bmp);
                        bis.close();
                        is.close();
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Ошибка получения данных",
                                Toast.LENGTH_LONG).show();
                    } catch (MalformedURLException e) {
                        Toast.makeText(getActivity(), "Ошибка получения данных",
                                Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(getActivity(), "Ошибка получения данных",
                                Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), "Ошибка получения данных",
                                Toast.LENGTH_LONG).show();
                    }
                }, error -> {
                    Toast.makeText(getActivity(), "Ошибка:" + error.toString(), Toast.LENGTH_SHORT).show();
                });
                requestQueue.add(request);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Ошибка:" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
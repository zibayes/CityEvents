package com.example.cityevents;

import android.Manifest;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class MapsFragment extends Fragment {

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private final int REQUEST_CODE_ACCESS_FINE_LOCATION = 1;
    private final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 2;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mDBHelper = new DatabaseHelper(getActivity().getApplicationContext());

            try {
                mDBHelper.updateDataBase();
            } catch (IOException mIOException) {
                throw new Error("UnableToUpdateDatabase");
            }

            try {
                mDb = mDBHelper.getWritableDatabase();
            } catch (SQLException mSQLException) {
                throw mSQLException;
            }
            Cursor cursor = mDb.rawQuery("SELECT * FROM Events", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                LatLng event = new LatLng(Double.parseDouble(cursor.getString(7)), Double.parseDouble(cursor.getString(6)));
                googleMap.addMarker(new MarkerOptions().position(event).title(cursor.getString(1)));
                cursor.moveToNext();
            }
            cursor.close();
            LatLng krasnoyarsk = new LatLng(56, 92.84);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(krasnoyarsk, 11));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        ActivityCompat.requestPermissions(getActivity(), new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE_ACCESS_FINE_LOCATION);
        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_CODE_ACCESS_COARSE_LOCATION);
    }
}
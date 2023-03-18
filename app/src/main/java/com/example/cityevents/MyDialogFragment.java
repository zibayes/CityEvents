package com.example.cityevents;

import android.app.Dialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;

public class MyDialogFragment extends DialogFragment {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Создание мероприятия")
                .setNegativeButton("Отмена", (dialog, id) -> dialog.cancel());

        final LinearLayout layout = new LinearLayout(getActivity());
        final ScrollView scrollView = new ScrollView(getActivity());

        final EditText input_name = new EditText(getContext());
        input_name.setHint("Название");
        input_name.setInputType(InputType.TYPE_CLASS_TEXT);

        final EditText input_date = new EditText(getContext());
        input_date.setHint("Дата (ДД.ММ.ГГ)");
        input_date.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);

        final EditText input_time = new EditText(getContext());
        input_time.setHint("Время (ЧЧ:ММ)");
        input_time.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);

        final EditText input_cost = new EditText(getContext());
        input_cost.setHint("Стоимость");
        input_cost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        final EditText input_length = new EditText(getContext());
        input_length.setHint("Продолжительность");
        input_length.setInputType(InputType.TYPE_CLASS_TEXT);

        final EditText input_latitude = new EditText(getContext());
        input_latitude.setHint("Широта");
        input_latitude.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        final EditText input_longtitude = new EditText(getContext());
        input_longtitude.setHint("Долгота");
        input_longtitude.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        /*
        input_name.setText("Тест");
        input_date.setText("18.06.23");
        input_time.setText("15:30");
        input_cost.setText("300");
        input_length.setText("90 мин.");
        input_latitude.setText("56.03");
        input_longtitude.setText("92.79");
        */


        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(input_name);
        layout.addView(input_date);
        layout.addView(input_time);
        layout.addView(input_cost);
        layout.addView(input_length);
        layout.addView(input_latitude);
        layout.addView(input_longtitude);
        scrollView.addView(layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(lp);
        builder.setIcon(R.drawable.event).setView(scrollView);

        builder.setPositiveButton("Создать", (dialog, which) -> {
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

            String name = input_name.getText().toString();
            if(name.length() == 0){
                Toast.makeText(getActivity(), "Поле с названием мероприятия не должно быть пустым",
                        Toast.LENGTH_LONG).show();
                return;
            }

            String data = input_date.getText().toString();
            if(data.length() != 8){
                Toast.makeText(getActivity(), "Введена дата неверного формата (ДД.ММ.ГГ)",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (!isInteger(data.substring(0,2)) || !isInteger(data.substring(3,5)) || !isInteger(data.substring(6))){
                Toast.makeText(getActivity(), "Введена дата неверного формата (ДД.ММ.ГГ)",
                        Toast.LENGTH_LONG).show();
                return;
            }

            String time = input_time.getText().toString();
            if(time.length() != 5){
                Toast.makeText(getActivity(), "Введено время неверного формата (ЧЧ:ММ)",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (!isInteger(time.substring(0,2)) || !isInteger(time.substring(3,5))){
                Toast.makeText(getActivity(), "Введено время неверного формата (ЧЧ:ММ)",
                        Toast.LENGTH_LONG).show();
                return;
            }

            String сost = input_cost.getText().toString();
            if (!isDouble(сost)){
                Toast.makeText(getActivity(), "Стоимость введена некорректно",
                        Toast.LENGTH_LONG).show();
                return;
            }

            String length = input_length.getText().toString();
            if(length.length() == 0){
                Toast.makeText(getActivity(), "Поле с продолжительностью мероприятия не должно быть пустым",
                        Toast.LENGTH_LONG).show();
                return;
            }

            String latitude = input_latitude.getText().toString();
            if (!isDouble(latitude)){
                Toast.makeText(getActivity(), "Широта введена некорректно",
                        Toast.LENGTH_LONG).show();
                return;
            }

            String longtitude = input_longtitude.getText().toString();
            if (!isDouble(longtitude)) {
                Toast.makeText(getActivity(), "Долгота введена некорректно",
                        Toast.LENGTH_LONG).show();
                return;
            }

            mDb.execSQL("INSERT INTO Events (Name, Date, Time, Cost, Length, latitude, longtitude, User) VALUES ('"+name+"','" +
                    ""+data+"','"+time+"',"+сost+",'"+input_length.getText().toString()+"'," +
                    ""+latitude+","+longtitude+",1)");
            Fragment fragment = new FirstFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment_content_main, fragment);
            ft.commit();
            dialog.cancel();
        });

        return builder.create();
    }
}
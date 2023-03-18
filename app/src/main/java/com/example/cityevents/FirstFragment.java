package com.example.cityevents;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cityevents.databinding.FragmentFirstBinding;

import java.io.IOException;
import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private final ArrayList<Event> events = new ArrayList<Event>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
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
        Cursor cursor = mDb.rawQuery("SELECT * FROM Events", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = new Event(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), Double.parseDouble(cursor.getString(4)), cursor.getString(5), Integer.parseInt(cursor.getString(8)));
            events.add(event);
            cursor.moveToNext();
        }
        cursor.close();

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        binding.floatingActionButton3.setOnClickListener(view1 -> {
            MyDialogFragment myDialogFragment = new MyDialogFragment();
            FragmentManager manager = getParentFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            myDialogFragment.show(transaction, "dialog");
        });

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (Event event : events) {
            LinearLayout newEvent = new LinearLayout(getActivity().getApplicationContext());
            RelativeLayout container = new RelativeLayout(getActivity().getApplicationContext());

            newEvent.setOrientation(LinearLayout.HORIZONTAL);
            newEvent.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_corners));
            newEvent.setBackgroundColor(Color.parseColor("#BBDDFF"));
            newEvent.setPadding(5, 0, 5, 0);
            newEvent.setGravity(Gravity.CENTER_VERTICAL);

            TextView name = (TextView) getLayoutInflater().inflate(R.layout.text_view_template, null);
            name.setText(event.getName());
            name.setGravity(Gravity.CENTER_VERTICAL);
            name.setLayoutParams(new TableLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.1f));
            newEvent.addView(name);

            TextView date = (TextView) getLayoutInflater().inflate(R.layout.text_view_template, null);
            date.setText(event.getDate());
            date.setGravity(Gravity.CENTER_VERTICAL);
            date.setLayoutParams(new TableLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.15f));
            newEvent.addView(date);

            TextView time = (TextView) getLayoutInflater().inflate(R.layout.text_view_template, null);
            time.setText(event.getTime());
            time.setGravity(Gravity.CENTER_VERTICAL);
            time.setLayoutParams(new TableLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.15f));
            newEvent.addView(time);

            newEvent.setOnClickListener(view1 -> {
                Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
                toolbar.setTitle(R.string.event);
                Fragment fragment = new SecondFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("event_id", event.getId());
                fragment.setArguments(bundle);
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment_content_main, fragment);
                ft.commit();
            });

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
            container.addView(newEvent, lp);
            binding.eventsList.addView(container, lp);
            Space space = new Space(getActivity().getApplicationContext());

            if (event.getUser() == 1) {
                ImageView delete = new ImageView(getContext());
                delete.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.delete));
                ConstraintLayout.LayoutParams clp = new ConstraintLayout.LayoutParams(55,55);
                clp.setMargins(615,22,0,0);
                delete.setLayoutParams(clp);
                delete.setOnClickListener(view1 -> new AlertDialog.Builder(getContext())
                        .setTitle("Удалить мероприятие")
                        .setMessage("Вы уверены, что хотите удалить мероприятие " + event.getName() + "?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            binding.eventsList.removeView(container);
                            binding.eventsList.removeView(space);
                            mDb.execSQL("DELETE FROM Events WHERE ID = " + event.getId());
                            Fragment fragment = new FirstFragment();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.nav_host_fragment_content_main, fragment);
                            ft.commit();
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show());
                container.addView(delete);
            }


            space.setMinimumHeight(25);
            binding.eventsList.addView(space);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
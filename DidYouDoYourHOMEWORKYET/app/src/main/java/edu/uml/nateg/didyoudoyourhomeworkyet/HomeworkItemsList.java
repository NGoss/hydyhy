package edu.uml.nateg.didyoudoyourhomeworkyet;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;


public class HomeworkItemsList extends ListActivity {

    public int ADD_TODO_ITEM_REQUEST = 0;
    HomeworkListAdapter mAdapter;
    private static final String FILE_NAME = "HYDYHYData.txt";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new HomeworkListAdapter(getApplicationContext());

        getListView().setFooterDividersEnabled(true);
        getListView().setHeaderDividersEnabled(true);

        TextView footerView = (TextView) getLayoutInflater().inflate(R.layout.homework_items_list, getListView(), false);
        getListView().addFooterView(footerView);

        ImageView headerView = (ImageView) getLayoutInflater().inflate(R.layout.header_view, getListView(), false);
        getListView().addHeaderView(headerView);

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddHomeworkItem.class);
                startActivityForResult(intent, ADD_TODO_ITEM_REQUEST);
            }
        });

        setListAdapter(mAdapter);

        loadItems();
    }

    private void loadItems() {
        BufferedReader reader = null;
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(fis));

            String title = null;
            String description = null;
            String priority = null;
            int percentDone = 0;
            Date date = null;

            while (null != (title = reader.readLine())) {
                description = reader.readLine();
                priority = reader.readLine();
                date = HomeworkItem.FORMAT.parse(reader.readLine());
                mAdapter.add(new HomeworkItem(title, description, HomeworkItem.Priority.valueOf(priority),
                        date, percentDone));
                percentDone = Integer.parseInt(reader.readLine());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Save ToDoItems to file
    private void saveItems() {
        PrintWriter writer = null;
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    fos)));

            for (int idx = 0; idx < mAdapter.getCount(); idx++) {

                writer.println(mAdapter.getItem(idx));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        log("Back in items list");
        if (requestCode == ADD_TODO_ITEM_REQUEST) {
            switch (resultCode) {
                case RESULT_OK:
                    HomeworkItem newHWItem = new HomeworkItem(data);
                    mAdapter.add(newHWItem);
                    mAdapter.notifyDataSetChanged();
                    break;
                case RESULT_CANCELED:
                    break;
            }
        }

    }
    private void log(String msg) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveItems();
    }

}

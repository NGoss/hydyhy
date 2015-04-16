package edu.uml.nateg.didyoudoyourhomeworkyet;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.uml.nateg.didyoudoyourhomeworkyet.HomeworkItem.Priority;



public class MainActivity extends ListActivity {

    // IDs for menu items
    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_DUMP = Menu.FIRST + 1;
    private static final String FILE_NAME = "HYDYHYData.txt";
    private int mDoneCounter;
    private int mToDoCounter;
    private enum GRADE {
        A, B, C, D, F
    }

    private GRADE mGrade = GRADE.A;
    private int mIntGrade = 100;

    ArrayList<HomeworkItem> itemsList = new ArrayList<HomeworkItem>();
    HomeworkListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        mAdapter = new HomeworkListAdapter(getApplicationContext());

        getListView().setFooterDividersEnabled(true);
        getListView().setHeaderDividersEnabled(true);

        RelativeLayout footerView = (RelativeLayout) getLayoutInflater().inflate(R.layout.main_footer, getListView(), false);
        getListView().addFooterView(footerView);

        LinearLayout headerView = (LinearLayout) getLayoutInflater().inflate(R.layout.main_header, getListView(), false);
        getListView().addHeaderView(headerView);

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeworkItemsList.class);
                startActivity(intent);
            }
        });

        setListAdapter(mAdapter);
        log("Completed onCreate");


        //Calculate three most urgent items
        mAdapter.clear();
        HomeworkItem hw1 = new HomeworkItem("placeholder", "placeholder", Priority.LOW, new Date(), 0);
        HomeworkItem hw2 = new HomeworkItem("placeholder", "placeholder", Priority.LOW, new Date(), 0);
        HomeworkItem hw3 = new HomeworkItem("placeholder", "placeholder", Priority.LOW, new Date(), 0);
        double score1 = 0;
        double score2 = 0;
        double score3 = 0;

        loadItems();
        double scoren = 0;
        for(HomeworkItem item : itemsList){
            log("Parsing item: " + item.toString());
            scoren = calculateUrgencyScore(item);
            if (scoren > score1){
                hw3 = hw2;
                hw2 = hw1;
                hw1 = item;
            } else if (scoren > score2) {
                hw3 = hw2;
                hw2 = item;
            } else if (scoren > score3) {
                hw3 = item;
            }
        }
        mAdapter.add(hw1);
        mAdapter.add(hw2);
        mAdapter.add(hw3);
    }

    double calculateUrgencyScore(HomeworkItem item) {
        double score = 0;

        //Algorithm v1.0: Time left until due date * priority multiplier
        //Priority Multiplier:
        // Low: 0.5
        // Med: 1
        // High:1.5
        double priorityMult = 0;
        if (item.getPriority() == Priority.HIGH){
            priorityMult = 1.5;
        } else if (item.getPriority() == Priority.MED){
            priorityMult = 1;
        } else if (item.getPriority() == Priority.LOW) {
            priorityMult = 0.5;
        }

        //calculate time left
        Date currentDate = new Date();
        double diffMilli = item.getDate().getTime() - currentDate.getTime();
        score = (diffMilli / 60000) * priorityMult;

        return score;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
        menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DELETE:
                mAdapter.clear();
                return true;
            case MENU_DUMP:
                dump();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dump() {

        for (int i = 0; i < mAdapter.getCount(); i++) {
            String data = ((HomeworkItem) mAdapter.getItem(i)).toLog();
            log("Item " + i + ": " + data.replace(HomeworkItem.ITEM_SEP, ","));
        }

    }

    private void log(String msg) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("HYDYHY", msg);
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
                itemsList.add(new HomeworkItem(title, description, HomeworkItem.Priority.valueOf(priority),
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
}
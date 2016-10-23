package com.example.mathurs.testshivammathur;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.helpshift.support.Support;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private LinearLayout downloadData;
    public static final String TAG = "TESTING APP";
    private RecyclerView scoreRecycler;
    private BufferedReader reader;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        downloadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAsyncTaskLoadData().execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.chat:
                Support.showConversation(MainActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        context = MainActivity.this;
        downloadData = (LinearLayout) findViewById(R.id.downloadData);
        scoreRecycler = (RecyclerView) findViewById(R.id.scoreRecycler);
    }


    public class MyAsyncTaskLoadData extends AsyncTask<String, Void, String> {
        String response = "";
        Dialog downloadDialog = new Dialog(context);
        Dialog downloadCompleteDialog = new Dialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            downloadDialog.setContentView(R.layout.downloaddialog);
            downloadDialog.setCancelable(false);
            downloadDialog.show();

            downloadCompleteDialog.setContentView(R.layout.downloadcompletedialog);
            downloadCompleteDialog.setCancelable(false);

        }


        @Override
        protected String doInBackground(String... params) {
            String line = null;
            String urlLink = "https://xobin.com/static/xobin_playground/enparadigm/FootballScoresData.json";
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(urlLink);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                response = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jObject = new JSONObject(s);
                JSONArray array = jObject.names();
                ArrayList<Structure> struct = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    int win = 0;
                    int loose = 0;
                    int tie = 0;
                    int totalMyGoals = 0;
                    int totalThereGoals = 0;
                    JSONObject json_date = (JSONObject) jObject.get(array.getString(i));
                    for (int j = 0; j < json_date.length(); j++) {
                        int matchCount = j + 1;
                        String matchScore = json_date.getString("match_" + matchCount);
                        String[] parts = matchScore.split("-");
                        int myScore = Integer.parseInt(parts[0]);
                        int otherScore = Integer.parseInt(parts[1]);

                        totalMyGoals = totalMyGoals + myScore;
                        totalThereGoals = totalThereGoals + otherScore;

                        if (myScore > otherScore) {
                            win++;
                        } else if (myScore < otherScore) {
                            loose++;
                        } else {
                            tie++;
                        }
                    }
                    int winPoints = win * 3;
                    int drawPoints = tie;
                    int totalPoint = winPoints + drawPoints;
                    int totalPlayed = win + loose + tie;
                    int goalDifference = totalMyGoals - totalThereGoals;

//                    Log.i(TAG, "onPostExecute: TEAM NAME: " + array.get(i));
//                    Log.i(TAG, "onPostExecute: Total My Goals: " + totalMyGoals);
//                    Log.i(TAG, "onPostExecute: Total There Goals: " + totalThereGoals);
//                    Log.i(TAG, "onPostExecute: Total My Won: " + win);
//                    Log.i(TAG, "onPostExecute: Total My Loose: " + loose);
//                    Log.i(TAG, "onPostExecute: Total My Tie: " + tie);
//                    Log.i(TAG, "onPostExecute: Total Points: " + totalPoint);
//                    Log.i(TAG, "onPostExecute: Total Played: " + totalPlayed);
//                    Log.i(TAG, "onPostExecute: Goal Difference: " + goalDifference);
//                    Log.i(TAG, "onPostExecute: ");
//                    Log.i(TAG, "onPostExecute: ");

                    Structure structure = new Structure();
                    structure.teamName = array.getString(i);
                    structure.played = totalPlayed;
                    structure.won = win;
                    structure.lost = loose;
                    structure.drawn = tie;
                    structure.totalPoint = totalPoint;
                    structure.gd = goalDifference;

                    Log.i(TAG, "onPostExecute: BEFORE TEAM: " + array.getString(i) + " Total:" + totalPoint + " GD:" + goalDifference);
                    struct.add(structure);
                }
                Collections.sort(struct, new Compare());
                for (Structure str : struct) {
                    Log.i(TAG, "onPostExecute: AFTER TEAM: " + str.teamName + " Total:" + str.totalPoint + " GD:" + str.gd);
                }
                downloadDialog.dismiss();
                downloadCompleteDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        downloadCompleteDialog.dismiss();
                    }
                }, 2000);
                ScoreRecycler adapter = new ScoreRecycler(context, struct);
                LinearLayoutManager llm = new LinearLayoutManager(context);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                scoreRecycler.setLayoutManager(llm);
                scoreRecycler.setAdapter(adapter);
                downloadData.setVisibility(View.GONE);
                scoreRecycler.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

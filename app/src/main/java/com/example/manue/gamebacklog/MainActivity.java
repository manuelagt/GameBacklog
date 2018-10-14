package com.example.manue.gamebacklog;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GameAdapter.GameClickListener {

    private List<Game> mGames;
    private GameAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public static final String EXTRA_GAME = "Game";
    public static final int REQUESTCODE = 1234;
    private int mModifyPosition;

    public final static int TASK_GET_ALL_GAMES = 0;
    public final static int TASK_DELETE_GAME = 1;
    public final static int TASK_UPDATE_GAME = 2;
    public final static int TASK_INSERT_GAME = 3;

    static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize the local variables

        db = AppDatabase.getInstance(this);

        mGames = new ArrayList<>();
        new GameAsyncTask(TASK_GET_ALL_GAMES).execute();

        mRecyclerView = findViewById(R.id.recyclerView);
        updateUI();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));



        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());
                        new GameAsyncTask(TASK_DELETE_GAME).execute(mGames.get(position));

                    }


                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToAddPage(View view) {
        startActivityForResult(new Intent(MainActivity.this, AddGame.class), REQUESTCODE);

    }

    void onGameDbUpdated(List list) {
        mGames = list;
        updateUI();

    }

    private void updateUI() {
//        mGames = db.reminderDao().getAllGames();
        if (mAdapter == null) {
            mAdapter = new GameAdapter(mGames, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapList(mGames);
        }

    }

    @Override
    public void reminderOnClick(int i) {
        Intent intent = new Intent(MainActivity.this, EditGame.class);
        mModifyPosition = i;

        intent.putExtra(EXTRA_GAME, mGames.get(i));
        startActivityForResult(intent, REQUESTCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                Game updatedGame = data.getParcelableExtra(MainActivity.EXTRA_GAME);
                // New timestamp: timestamp of update
                //      mReminders.set(mModifyPosition, updatedReminder);
                new GameAsyncTask(TASK_UPDATE_GAME).execute(updatedGame);
                updateUI();
            }
        }
    }


    public class GameAsyncTask extends AsyncTask<Game, Void, List> {

        private int taskCode;

        public GameAsyncTask(int taskCode) {
            this.taskCode = taskCode;
        }


        @Override
        protected List doInBackground(Game... games) {
            switch (taskCode) {
                case TASK_DELETE_GAME:
                    db.gameDao().deleteGames(games[0]);
                    break;

                case TASK_UPDATE_GAME:
                    db.gameDao().updateGames(games[0]);
                    break;

                case TASK_INSERT_GAME:
                    db.gameDao().insertGames(games[0]);
                    break;
            }


            //To return a new list with the updated data, we get all the data from the database again.
            return db.gameDao().getAllGames();
        }


        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            onGameDbUpdated(list);
        }

    }
}

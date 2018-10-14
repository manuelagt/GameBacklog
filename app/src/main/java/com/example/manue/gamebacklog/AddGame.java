package com.example.manue.gamebacklog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class AddGame extends AppCompatActivity {

    EditText mTitle;
    EditText mPlatform;
    EditText mNotes;
    Spinner mStatus;

    private List<Game> mGames;
    private GameAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public final static int TASK_GET_ALL_GAMES = 0;
    public final static int TASK_DELETE_GAME = 1;
    public final static int TASK_UPDATE_GAME = 2;
    public final static int TASK_INSERT_GAME = 3;

    static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = findViewById(R.id.add_title);
        mPlatform = findViewById(R.id.add_platform);
        mNotes = findViewById(R.id.add_notes);
        mStatus = findViewById(R.id.add_spinner);

        //Initialize the local variables
        db = AppDatabase.getInstance(this);

        ArrayAdapter<CharSequence> myStatusAdapter = ArrayAdapter.createFromResource(this,
                R.array.statuses, android.R.layout.simple_spinner_item);

        myStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStatus.setAdapter(myStatusAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_save_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitle.getText().toString();
                String platform = mPlatform.getText().toString();
                String notes = mNotes.getText().toString();
                String status = mStatus.getSelectedItem().toString();

                Game newGame = new Game(title,platform,notes,status);

                if (!TextUtils.isEmpty(title)) {

                    new GameAsyncTask(TASK_INSERT_GAME).execute(newGame);

                    finish();

                } else {
                    Snackbar.make(view, "Please, insert a title for the Game", Snackbar.LENGTH_LONG);
                }
            }
        });
    }

    public class GameAsyncTask extends AsyncTask<Game, Void, List> {

        private int taskCode;

        public GameAsyncTask(int taskCode) {
            this.taskCode = taskCode;
        }


        @Override
        protected List doInBackground(Game... games) {
            switch (taskCode) {


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
            //onReminderDbUpdated(list);
        }

    }

}

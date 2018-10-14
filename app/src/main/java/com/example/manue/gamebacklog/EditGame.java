package com.example.manue.gamebacklog;

import android.app.Activity;
import android.content.Intent;
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
import java.util.logging.Logger;


public class EditGame extends AppCompatActivity {

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
        setContentView(R.layout.activity_edit_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = findViewById(R.id.edit_title);
        mPlatform = findViewById(R.id.edit_platform);
        mNotes = findViewById(R.id.edit_notes);
        mStatus = findViewById(R.id.edit_spinner);

        //Initialize the local variables
        db = AppDatabase.getInstance(this);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditGame.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.statuses));

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStatus.setAdapter(dataAdapter);

        final Game gameEdit = getIntent().getParcelableExtra(MainActivity.EXTRA_GAME);
        mTitle.setText(gameEdit.getGameTitle());
        mPlatform.setText(gameEdit.getGamePlatform());
        mNotes.setText(gameEdit.getGameNotes());
        mStatus.setSelection(0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.edit_save_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitle.getText().toString();
                String platform = mPlatform.getText().toString();
                String notes = mNotes.getText().toString();
                String status = mStatus.getSelectedItem().toString();

                Game newGame = new Game(title,platform,notes,status);

                if (!TextUtils.isEmpty(title)) {

                    gameEdit.setGameTitle(title);
                    gameEdit.setGamePlatform(platform);
                    gameEdit.setGameNotes(notes);
                    gameEdit.setGameStatus(status);

                    //Prepare the return parameter and return
                    Intent resultIntent = new Intent(EditGame.this,MainActivity.class);
                    resultIntent.putExtra(MainActivity.EXTRA_GAME, gameEdit);
                    setResult(Activity.RESULT_OK, resultIntent);

                    finish();

                } else {
                    Snackbar.make(view, "Please, insert a title for the Game", Snackbar.LENGTH_LONG);
                }
            }
        });
    }

}

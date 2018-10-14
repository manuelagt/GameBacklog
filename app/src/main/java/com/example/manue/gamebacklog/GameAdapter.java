package com.example.manue.gamebacklog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<Game> mGames;
    final private GameClickListener mGameClickListener;
    private Context mContext;

    public interface GameClickListener{
        void reminderOnClick(int i);
    }

    public GameAdapter(List<Game> mGames, GameClickListener mGameClickListener) {
        this.mGames = mGames;
        this.mGameClickListener = mGameClickListener;
    }

    @NonNull
    @Override
    public GameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.game_post, parent, false);

        // Return a new holder instance
        GameAdapter.ViewHolder viewHolder = new GameAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull GameAdapter.ViewHolder holder, int position) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy / MM / dd ");
        String currentDate = "Current Date : " + format.format(calendar.getTime());
        Game game = mGames.get(position);
        holder.mGameTitle.setText(game.getGameTitle());
        holder.mGamePlatform.setText(game.getGamePlatform());
        holder.mGameStatus.setText(game.mGameStatus);
        holder.mDate.setText(currentDate);
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mGameTitle;
        public TextView mGamePlatform;
        public TextView mGameStatus;
        public TextView mDate;

        public ViewHolder(View itemView) {

            super(itemView);
            mGameTitle = itemView.findViewById(R.id.game_name);
            mGamePlatform = itemView.findViewById(R.id.console_type);
            mGameStatus = itemView.findViewById(R.id.game_status);
            mDate = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mGameClickListener.reminderOnClick(clickedPosition);
        }

    }

    public void swapList (List<Game> newList) {


        mGames = newList;

        if (newList != null) {

            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();

        }

    }


}

package com.example.mathurs.testshivammathur;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mathurs on 23-10-2016.
 */

public class ScoreRecycler extends RecyclerView.Adapter<ScoreRecycler.MyViewHolder> {
    private List<Structure> structure;
    private LayoutInflater inflater;

    public ScoreRecycler(Context context, List<Structure> structures) {
        inflater = LayoutInflater.from(context);
        this.structure = structures;
    }


    @Override
    public ScoreRecycler.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.recyclermodel, parent, false));
    }

    @Override
    public void onBindViewHolder(ScoreRecycler.MyViewHolder holder, int position) {
        int rank = position + 1;
        holder.rank.setText("Rank " + rank + ": Team " + structure.get(position).teamName);
        holder.played.setText("Played: " + structure.get(position).played);
        holder.won.setText("Won: " + structure.get(position).won);
        holder.lose.setText("Lost: " + structure.get(position).lost);
        holder.drawn.setText("Drawn: " + structure.get(position).drawn);
        String gd = "" + structure.get(position).gd;
        if (structure.get(position).gd > 0)
            gd = "+" + structure.get(position).gd;
        holder.gd.setText("GD: " + gd);
    }

    @Override
    public int getItemCount() {
        return structure.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rank, played, won, lose, drawn, gd;

        MyViewHolder(View itemView) {
            super(itemView);
            rank = (TextView) itemView.findViewById(R.id.rankTextView);
            played = (TextView) itemView.findViewById(R.id.playedTextView);
            won = (TextView) itemView.findViewById(R.id.wonTextView);
            lose = (TextView) itemView.findViewById(R.id.lostTextView);
            drawn = (TextView) itemView.findViewById(R.id.drawnTextView);
            gd = (TextView) itemView.findViewById(R.id.gdTextView);
        }
    }
}


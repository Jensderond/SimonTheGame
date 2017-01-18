package com.example.jensderond.simongame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import io.realm.Realm;

/**
 * Created by jensderond on 17/01/2017.
 */

public class HighscoreListAdapter extends ArrayAdapter<Highscore> {

    private ArrayList<Highscore> highscoresArrayList = new ArrayList<>();
    private static LayoutInflater inflater = null;
    private Realm realm;

    /***
     * Constructor of the ArrayOverviewAdapter
     *
     * @param context
     * @param highscores
     */
    public HighscoreListAdapter(Context context, ArrayList<Highscore> highscores) {
        super(context, R.layout.listview_row_highscore, highscores);
        this.highscoresArrayList = highscores;
        realm = Realm.getDefaultInstance();
    }

    /**
     * returns the size of the arraylist
     *
     * @return
     */
    public int getCount() {
        return highscoresArrayList.size();
    }

    /**
     * This function binds the information to the view and returns the view requested
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Highscore highscore = getItem(position);

        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_row_highscore, parent, false);
            viewHolder.display_image = (ImageView) convertView.findViewById(R.id.display_image);
            viewHolder.display_name = (TextView) convertView.findViewById(R.id.display_name);
            viewHolder.display_score = (TextView) convertView.findViewById(R.id.display_score);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = new ViewHolder();
            viewHolder.display_image = (ImageView) convertView.findViewById(R.id.display_image);
            viewHolder.display_name = (TextView) convertView.findViewById(R.id.display_name);
            viewHolder.display_score = (TextView) convertView.findViewById(R.id.display_score);
        }


        Player result = realm.where(Player.class).equalTo("name", highscore.getPlayer()).findFirst();

        // Populate the data into the template view using the data object
        viewHolder.display_image.setImageResource(result.getImage());
        viewHolder.display_name.setText(highscore.getPlayer());
        viewHolder.display_score.setText(String.valueOf(highscore.getScore()));

//         Return the completed view to render on screen
        return convertView;
    }

    /**
     * class of the viewholder
     */
    public static class ViewHolder {
        public ImageView display_image;
        public TextView display_name;
        public TextView display_score;
    }
}
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

public class UsersListAdapter extends ArrayAdapter<Player> {

    private ArrayList<Player> usersArrayList = new ArrayList<>();
    private static LayoutInflater inflater = null;
    private Realm realm;

    /***
     * Constructor of the UsersListAdapter
     *
     * @param context
     * @param players
     */
    public UsersListAdapter(Context context, ArrayList<Player> players) {
        super(context, R.layout.listview_row_highscore, players);
        this.usersArrayList = players;
        realm = Realm.getDefaultInstance();
    }

    /**
     * returns the size of the arraylist
     *
     * @return
     */
    public int getCount() {
        return usersArrayList.size();
    }

    /**
     * This function binds the information to the view and returns the view requested
     *
     * @param position
     * @param convertView
     * @param parent
     * @return convertView
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Player player = getItem(position);

        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_row_users, parent, false);
            viewHolder.display_image = (ImageView) convertView.findViewById(R.id.display_user_image);
            viewHolder.display_name = (TextView) convertView.findViewById(R.id.display_user_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = new ViewHolder();
            viewHolder.display_image = (ImageView) convertView.findViewById(R.id.display_user_image);
            viewHolder.display_name = (TextView) convertView.findViewById(R.id.display_user_name);
        }

        // Populate the data into the template view using the data object
        viewHolder.display_image.setImageResource(player.getImage());
        viewHolder.display_name.setText(player.getName());

//         Return the completed view to render on screen
        return convertView;
    }

    /**
     * class of the viewholder
     */
    public static class ViewHolder {
        public ImageView display_image;
        public TextView display_name;
    }
}
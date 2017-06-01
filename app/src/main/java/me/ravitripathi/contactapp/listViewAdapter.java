package me.ravitripathi.contactapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ravi on 31-05-2017.
 */

public class listViewAdapter extends ArrayAdapter<detListItem> {
    private ArrayList<detListItem> arrayList;

    public listViewAdapter(Context context, int textViewResourceId, ArrayList<detListItem> objects) {
        super(context, textViewResourceId, objects);
        arrayList = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.det_list_item, null);
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        detListItem i = arrayList.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView head = (TextView) v.findViewById(R.id.head);
            TextView details = (TextView) v.findViewById(R.id.det);

            if (head != null)
                head.setText(i.getTitle());

            if (details != null)
                details.setText(i.getDetails());
        }

        return v;
    }
}

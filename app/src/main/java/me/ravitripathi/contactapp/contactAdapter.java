package me.ravitripathi.contactapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ravi on 30-05-2017.
 */

public class contactAdapter extends RecyclerView.Adapter<contactAdapter.arcVH> {

    private List<contactDet> contactList;

    public contactAdapter(List<contactDet> contactList) {
        this.contactList = contactList;
    }


    public class arcVH extends RecyclerView.ViewHolder {
        public TextView name, phone;
        public ImageView image;

        public arcVH(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            phone = (TextView) view.findViewById(R.id.num);
            image = (ImageView) view.findViewById(R.id.profile_image);
        }
    }


    @Override
    public arcVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new arcVH(itemView);
    }

    @Override
    public void onBindViewHolder(arcVH holder, int position) {
        contactDet contact = contactList.get(position);
        holder.name.setText(contact.getName());
        holder.name.setSelected(true);
        holder.phone.setText(contact.getPhone());
        try {
            holder.image.setImageResource(R.drawable.ic_bot);
            if (contact.getThumbnail() != null) {
                holder.image.setImageURI(contact.getThumbnail());
            } else {
                holder.image.setImageResource(R.drawable.ic_bot);
            }

            if(holder.image.getDrawable() == null) holder.image.setImageResource(R.drawable.ic_bot);
            // Seting round image
        } catch (Exception e) {
            // Add default picture
            holder.image.setImageResource(R.drawable.ic_bot);
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}

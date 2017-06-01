package me.ravitripathi.contactapp;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Ravi on 30-05-2017.
 */

public class contactDet {

    public Uri thumbnail;
    public String name, phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String id;

    public contactDet(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Uri getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Uri thumbnail) {
        this.thumbnail = thumbnail;
    }

}

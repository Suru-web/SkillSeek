package com.helping.skillseek;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class profile_picture_zoom_view {
    public profile_picture_zoom_view(ImageView imageView, String Url, Context context){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.profile_pic_view);
        ImageView dialogImageView = dialog.findViewById(R.id.viewProfilePhoto);
        Picasso.get()
                .load(Url)
                .placeholder(R.drawable.profilepicture)
                .error(R.drawable.profilepicture)
                .into(dialogImageView);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }
}

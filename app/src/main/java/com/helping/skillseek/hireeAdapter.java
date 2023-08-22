package com.helping.skillseek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class hireeAdapter extends RecyclerView.Adapter<hireeAdapter.MyViewHolder> {

    Context context;
    ArrayList<hireeDetailsForFB> list;

    public hireeAdapter(Context context, ArrayList<hireeDetailsForFB> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.hiree_list,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        hireeDetailsForFB user = list.get(position);
        Picasso.get()
                .load(user.getImageURL())
                .placeholder(R.drawable.profilepicture)
                .error(R.drawable.profilepicture)
                .into(holder.profileImage);
        holder.name.setText(user.getName());
        holder.skill.setText(user.getSkill());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profileImage;
        TextView name,skill;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profilepictureofhiree);
            name = itemView.findViewById(R.id.nameofhiree);
            skill = itemView.findViewById(R.id.skillofhiree);
        }
    }

}

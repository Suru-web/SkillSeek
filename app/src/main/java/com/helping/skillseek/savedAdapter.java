package com.helping.skillseek;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class savedAdapter extends RecyclerView.Adapter<savedAdapter.MyViewHolder> {

    Context context;
    static ArrayList<hireeDetailsForFB> list;

    public savedAdapter(Context context, ArrayList<hireeDetailsForFB> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.hiree_list,parent,false);
        return new MyViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        hireeDetailsForFB user = list.get(position);
        Picasso.get()
                .load(user.getdownloadUrl())
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
        LinearLayout cardView;

        public MyViewHolder(@NonNull View itemView, final Context context) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profilepictureofhiree);
            name = itemView.findViewById(R.id.nameofhiree);
            skill = itemView.findViewById(R.id.skillofhiree);
            cardView = itemView.findViewById(R.id.cardviewClickable);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        hireeDetailsForFB item = list.get(position);
                        String id = item.getId();
                        Intent intent = new Intent(context, hireeInfoShow.class);
                        intent.putExtra("hireeListId",id);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

}

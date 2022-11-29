package com.alexmumo.xpressway.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexmumo.xpressway.R;
import com.alexmumo.xpressway.models.Complains;

import java.util.List;

public class ComplainsAdapter extends RecyclerView.Adapter<ComplainsAdapter.ComplainsViewHolder>{

    List<Complains> complainsList;
    Context context;
    public ComplainsAdapter(Context context, List<Complains> complainsList) {
        this.context = context;
        this.complainsList = complainsList;
    }

    @NonNull
    @Override
    public ComplainsAdapter.ComplainsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.complains_item, parent, false);
        return new ComplainsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplainsAdapter.ComplainsViewHolder holder, int position) {

        holder.message.setText(complainsList.get(position).getMessage());
        holder.email.setText(complainsList.get(position).getName());
        holder.type.setText(complainsList.get(position).getType());

    }

    @Override
    public int getItemCount() {
        return complainsList.size();
    }

    public class ComplainsViewHolder extends RecyclerView.ViewHolder {

        private TextView type, email, message;
        public ComplainsViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.typeTv);
            email = itemView.findViewById(R.id.emailTv);
            message = itemView.findViewById(R.id.messageTv);
        }
    }
}

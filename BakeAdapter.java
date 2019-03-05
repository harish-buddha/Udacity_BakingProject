package com.example.harish.lets_bake;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.harish.lets_bake.BakeModel.BakeRecipe;

import java.io.Serializable;
import java.util.List;

public class BakeAdapter extends RecyclerView.Adapter<BakeAdapter.ViewHolder> {

    private List<BakeRecipe> bakelist;
    private Context context;

    public BakeAdapter(List<BakeRecipe> bakelist, MainActivity mainActivity) {
        this.bakelist = bakelist;
        this.context = mainActivity;

    }


    @NonNull
    @Override
    public BakeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BakeAdapter.ViewHolder viewHolder, int i) {
        viewHolder.textView.setText("" + bakelist.get(i).getName());

    }

    @Override
    public int getItemCount() {
        return bakelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.bake_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(context, ItemListActivity.class);
                    intent.putExtra("stepslist", (Serializable) bakelist.get(pos).getSteps());
                    intent.putExtra("ingredients", (Serializable) bakelist.get(pos).getIngredients());
                    context.startActivity(intent);
                }
            });

        }
    }


}

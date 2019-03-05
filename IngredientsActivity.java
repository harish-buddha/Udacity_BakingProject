package com.example.harish.lets_bake;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.harish.lets_bake.BakeModel.Ingredient;

import java.util.ArrayList;

public class IngredientsActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ArrayList<Ingredient> ingred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        recyclerView = findViewById(R.id.ingredientsRec);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        ingred = (ArrayList<Ingredient>) b.getSerializable("key_data");


        IngredentsAdapter ingredentsAdapter = new IngredentsAdapter(this, ingred);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ingredentsAdapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public class IngredentsAdapter extends RecyclerView.Adapter<IngredentsAdapter.ViewHolder> {
        Context context;
        ArrayList<Ingredient> ingredients;


        IngredentsAdapter(IngredientsActivity ingredientsActivity, ArrayList<Ingredient> ingredients) {
            this.context = ingredientsActivity;
            this.ingredients = ingredients;
        }

        @NonNull
        @Override
        public IngredentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.ingredientsrow, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull IngredentsAdapter.ViewHolder holder, int position) {
            holder.tv1.setText(ingredients.get(position).getQuantity());
            holder.tv2.setText(ingredients.get(position).getMeasure());
            holder.tv3.setText(ingredients.get(position).getIngredient());

        }

        @Override
        public int getItemCount() {
            return ingredients.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv1, tv2, tv3;

            ViewHolder(View itemView) {
                super(itemView);
                tv1 = itemView.findViewById(R.id.tv_ing1);
                tv2 = itemView.findViewById(R.id.tv_ing2);
                tv3 = itemView.findViewById(R.id.tv_ing3);
            }
        }
    }
}

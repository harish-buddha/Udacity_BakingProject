package com.example.harish.lets_bake;


import android.app.ActionBar;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;
import com.example.harish.lets_bake.BakeModel.Ingredient;
import com.example.harish.lets_bake.BakeModel.Step;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ItemListActivity extends Activity {

    Toolbar toolbar;
    private boolean mTwoPane;
    private ArrayList<Step> stepslist;
    private ArrayList<Ingredient> ingredientsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        stepslist = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        ingredientsList = new ArrayList<>();
        ActionBar actionBar=getActionBar();
        if(actionBar!=null)
        {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ingredientsList = (ArrayList<Ingredient>) getIntent().getSerializableExtra("ingredients");
        stepslist = (ArrayList<Step>) getIntent().getSerializableExtra("stepslist");
        if (findViewById(R.id.item_detail_container) != null) {

            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, ingredientsList, stepslist, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<Step> steps;
        private final List<Ingredient> ingredients;
        private final boolean mTwoPane;


        SimpleItemRecyclerViewAdapter(ItemListActivity itemListActivity, ArrayList<Ingredient> ingredientsList, ArrayList<Step> stepslist, boolean mTwoPane) {
            this.steps = stepslist;
            mParentActivity = itemListActivity;
            this.mTwoPane = mTwoPane;
            ingredients = ingredientsList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            if (position == 0) {
                holder.mIdView.setText("Ingredients");

            } else if (position == 1) {
                holder.mIdView.setText(steps.get(position - 1).getShortDescription());
            } else {
                holder.mIdView.setText(steps.get(position - 1).getShortDescription());
            }

        }

        @Override
        public int getItemCount() {
            return steps.size() + 1;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            final TextView mIdView;
            final TextView mContentView;
            CardView cardView;

            ViewHolder(View view) {
                super(view);
                cardView = itemView.findViewById(R.id.card17);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
                cardView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int p = getAdapterPosition();


                if (p == 0) {
                    Intent i = new Intent(mParentActivity, IngredientsActivity.class);
                    SharedPreferences sharedPreferences = mParentActivity.getSharedPreferences("shared", MODE_PRIVATE);
                    StringBuilder builder = new StringBuilder();

                    for (int i1 = 0; i1 < ingredients.size(); i1++) {
                        String q = ingredients.get(i1).getQuantity();
                        String m = ingredients.get(i1).getMeasure();
                        builder.append(q + ":" + m + "\n");
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.putString("quantity", builder.toString());
                    editor.apply();

                    Intent intent = new Intent(mParentActivity, IngredientWidget.class);
                    intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                    int widget[] = AppWidgetManager.getInstance(mParentActivity.getApplication()).getAppWidgetIds(new ComponentName(mParentActivity.getApplication(), IngredientWidget.class));
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widget);
                    mParentActivity.sendBroadcast(intent);

                    i.putExtra("key_data", (Serializable) ingredients);
                    mParentActivity.startActivity(i);
                } else {

                    if (mTwoPane) {

                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailActivity.Thumbnail, steps.get(p - 1).getThumbnailURL());
                        arguments.putString(ItemDetailActivity.VideoUrl, steps.get(p - 1).getVideoURL());
                        arguments.putString(ItemDetailActivity.Short_Description, steps.get(p - 1).getShortDescription());
                        arguments.putString(ItemDetailActivity.Description, steps.get(p - 1).getDescription());
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        mParentActivity.getFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();

                    } else {

                        Intent intent = new Intent(mParentActivity, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailActivity.VideoUrl, steps.get(p - 1).getVideoURL());
                        intent.putExtra(ItemDetailActivity.Thumbnail, steps.get(p - 1).getThumbnailURL());
                        intent.putExtra(ItemDetailActivity.Description, steps.get(p - 1).getDescription());
                        intent.putExtra("desList", (Serializable) steps);
                        intent.putExtra(ItemDetailActivity.Short_Description, steps.get(p - 1).getShortDescription());
                        intent.putExtra("pos", getAdapterPosition() - 1);
                        mParentActivity.startActivity(intent);
                    }


                }


            }
        }

    }
}

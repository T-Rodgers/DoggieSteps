package com.tdr.app.doggiesteps.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.database.DogDatabase;
import com.tdr.app.doggiesteps.model.Dog;

import java.util.List;


public class DogListAdapter extends RecyclerView.Adapter<DogListAdapter.DogViewHolder> {

    private final LayoutInflater inflater;
    private List<Dog> dogList;
    private Context context;
    private DogDatabase dogDatabase;

    public DogListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;

    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.linear_list_item, parent, false);


        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {

        if (dogList != null) {
            Dog currentDog = dogList.get(position);
            holder.dogNameView.setText(currentDog.getDogName());
            holder.breedView.setText(currentDog.getBreed());
            holder.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(context, holder.options);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.menu_list_item);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.delete_list_item) {
                                dogDatabase.dogDao().delete(currentDog.getPetId());
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();

                }
            });

        } else {
            // Covers the case of data not being ready yet
            holder.dogNameView.setText(R.string.no_dog);
            holder.breedView.setText(R.string.not_applicable);
        }
    }

    void setDogList(List<Dog> dogs) {
        dogList = dogs;
        notifyDataSetChanged();

        Log.d("Total Items ", String.valueOf(dogList.size()));

    }

    @Override
    public int getItemCount() {
        if (dogList != null) {
            return dogList.size();
        } else {
            return 0;
        }
    }

    class DogViewHolder extends RecyclerView.ViewHolder {

        private final TextView dogNameView;
        private final TextView breedView;
        private final ImageView options;

        private DogViewHolder(View itemView) {
            super(itemView);
            dogNameView = itemView.findViewById(R.id.dog_name_text_view);
            breedView = itemView.findViewById(R.id.dog_breed_text_view);
            options = itemView.findViewById(R.id.list_item_options);

        }
    }

}

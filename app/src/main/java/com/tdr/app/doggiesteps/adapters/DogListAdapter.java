package com.tdr.app.doggiesteps.adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.database.DogDatabase;
import com.tdr.app.doggiesteps.model.Dog;
import com.tdr.app.doggiesteps.utils.AppExecutors;
import com.tdr.app.doggiesteps.utils.DialogUtils;

import java.util.List;


public class DogListAdapter extends RecyclerView.Adapter<DogListAdapter.DogViewHolder> {

    private final LayoutInflater inflater;
    private List<Dog> dogList;
    private final Context context;
    private final DogListAdapterClickHandler mClickHandler;

    public interface DogListAdapterClickHandler {
        void onClick(Dog dogData);
    }

    public DogListAdapter(Context context, DogListAdapterClickHandler handler) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        mClickHandler = handler;
    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.pet_list_item, parent, false);

        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {

        Dog currentDog = dogList.get(position);
        holder.dogNameView.setText(currentDog.getPetName());
        holder.breedView.setText(currentDog.getBreed());
        holder.petPhotoImageView.setContentDescription(context.getString(
                R.string.content_description_favorite_photo, currentDog.getPetName()));
        String photoPath = currentDog.getPhotoPath();
        Glide.with(context)
                .load(photoPath)
                .error(R.drawable.ic_action_pet_favorites)
                .fallback(R.drawable.ic_action_pet_favorites)
                .into(holder.petPhotoImageView);
        holder.options.setOnClickListener(view -> {

            //creating a popup menu
            PopupMenu popup = new PopupMenu(context, holder.options);
            //inflating menu from xml resource
            popup.inflate(R.menu.menu_list_item);
            //adding click listener
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.delete_list_item) {
                    DialogUtils.showDeleteDialog(context, currentDog.getPetName(), currentDog);
                }
                return false;
            });
            //displaying the popup
            popup.show();
        });
    }

    public void setDogList(List<Dog> dogs) {
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

    public class DogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView dogNameView;
        private final TextView breedView;
        private final ImageView options;
        private final ImageView petPhotoImageView;

        private DogViewHolder(View itemView) {
            super(itemView);
            dogNameView = itemView.findViewById(R.id.dog_name_text_view);
            breedView = itemView.findViewById(R.id.dog_breed_text_view);
            options = itemView.findViewById(R.id.list_item_options);
            petPhotoImageView = itemView.findViewById(R.id.list_pet_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Dog dog = dogList.get(position);
            mClickHandler.onClick(dog);
        }
    }
}

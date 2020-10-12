package com.tdr.app.doggiesteps.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.model.Favorite;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private final LayoutInflater inflater;
    private List<Favorite> favoriteList;
    private Context context;
    private FavoritesAdapter.FavoritesClickHandler mclickHandler;

    public FavoritesAdapter(Context context, FavoritesAdapter.FavoritesClickHandler handler) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        mclickHandler = handler;

    }

    public interface FavoritesClickHandler{
        void onClick(Favorite favoriteData);
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.favorite_list_item, parent, false);

        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {

        Favorite currentFavorite = favoriteList.get(position);
        String favoriteImagePath = currentFavorite.getPhotoPath();

        holder.petId.setText(String.valueOf(currentFavorite.getId()));
        Glide.with(context)
                .load(favoriteImagePath)
                .error(R.drawable.ic_action_pet_favorites)
                .into(holder.favoritePetImage);
    }

    public void setFavoriteList(List<Favorite> favorites) {
        favoriteList = favorites;
        notifyDataSetChanged();

        Log.d("Total Items ", String.valueOf(favoriteList.size()));

    }

    @Override
    public int getItemCount() {
        if (favoriteList != null) {
            return favoriteList.size();
        } else {
            return 0;
        }
    }



    public class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView petId;
        private final ImageView favoritePetImage;

        private FavoriteViewHolder(View itemView) {
            super(itemView);
            petId = itemView.findViewById(R.id.favorite_petid);
            favoritePetImage = itemView.findViewById(R.id.favorite_pet_photo);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Favorite favorite = favoriteList.get(position);
            mclickHandler.onClick(favorite);
        }
    }
}

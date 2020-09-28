package com.tdr.app.doggiesteps.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.database.DogListViewModel;
import com.tdr.app.doggiesteps.model.Dog;

import java.util.List;


public class DogListAdapter extends RecyclerView.Adapter<DogListAdapter.DogViewHolder> {

    private final LayoutInflater inflater;
    private List<Dog> dogList;
    private Context context;
    private DogListAdapterClickHandler mclickhandler;

    public interface DogListAdapterClickHandler{
        void onClick(Dog dogData);
    }

    public DogListAdapter(Context context, DogListAdapterClickHandler handler) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        mclickhandler = handler;

    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.linear_list_item, parent, false);

        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {


            Dog currentDog = dogList.get(position);
            holder.dogNameView.setText(currentDog.getPetName());
            holder.breedView.setText(currentDog.getBreed());
            holder.options.setOnClickListener(view -> {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.options);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_list_item);
                //adding click listener
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.delete_list_item) {
                        DogListViewModel dogListViewModel =
                                new ViewModelProvider((FragmentActivity)context)
                                .get(DogListViewModel.class);
                        dogListViewModel.delete(currentDog);
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

        private DogViewHolder(View itemView) {
            super(itemView);
            dogNameView = itemView.findViewById(R.id.dog_name_text_view);
            breedView = itemView.findViewById(R.id.dog_breed_text_view);
            options = itemView.findViewById(R.id.list_item_options);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Dog dog = dogList.get(position);
            mclickhandler.onClick(dog);
        }
    }

}

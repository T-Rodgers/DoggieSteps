package com.tdr.app.doggiesteps.utils;




import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.tdr.app.doggiesteps.R;
import com.tdr.app.doggiesteps.database.DogDatabase;
import com.tdr.app.doggiesteps.model.Dog;

public class DialogUtils {

    public static void showDeleteDialog(Context context, String petName, Dog dog) {
        String dialogMessage = context.getResources().getString(R.string.delete_pet_message, petName);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Custom_Dialog);
        builder.setTitle(R.string.action_delete);
        builder.setMessage(dialogMessage);
        builder.setIcon(R.mipmap.ds_icon_round);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        DogDatabase database = DogDatabase.getInstance(context.getApplicationContext());
                        database.dogDao().delete(dog);
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();

    }
}

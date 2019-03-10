package ca.engrLabs_390.engrlabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class LabSortFragment extends DialogFragment {

    private static final String TAG = "InsertCourseDialog";

    Button saveButton;
    List<ImageView> images = new ArrayList<>();
    int chosenOption = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflator, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflator.inflate(R.layout.fragment_lab_sort, container, false);

        images.add((ImageView) view.findViewById(R.id.image1));
        images.get(0).setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_black_24dp));

        images.add((ImageView) view.findViewById(R.id.image2));
        images.get(1).setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_unchecked_black_24dp));

        images.add((ImageView) view.findViewById(R.id.image3));
        images.get(2).setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_unchecked_black_24dp));

        images.add((ImageView) view.findViewById(R.id.image4));
        images.get(3).setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_unchecked_black_24dp));

        images.add((ImageView) view.findViewById(R.id.image5));
        images.get(4).setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_unchecked_black_24dp));

        images.get(0).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeOption(0);
                // your code here
            }
        });
        images.get(1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeOption(1);
                // your code here
            }
        });
        images.get(2).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeOption(2);
                // your code here
            }
        });
        images.get(3).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeOption(3);
                // your code here
            }
        });
        images.get(4).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeOption(4);
                // your code here
            }
        });

        saveButton = view.findViewById(R.id.saveButton);
        saveButton.setText("Filter");
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getDialog().dismiss();
                // your code here
            }
        });

        return view;
    }

    void changeOption(int option){
        for(int i=0;i<images.size();i++){
            if (option == i){
                images.get(i).setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_checked_black_24dp));
            }
            else {
                images.get(i).setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_button_unchecked_black_24dp));
            }
        }
    }
}

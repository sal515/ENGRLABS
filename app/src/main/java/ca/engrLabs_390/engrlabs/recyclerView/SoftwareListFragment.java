package ca.engrLabs_390.engrlabs.recyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import ca.engrLabs_390.engrlabs.R;
import ca.engrLabs_390.engrlabs.dataModels.SIngleton2ShareData;

public class SoftwareListFragment extends DialogFragment {

    private static final String TAG = "InsertCourseDialog";

    Button closeButton;
    ListView list;
    ArrayList<String> softwareList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflator, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflator.inflate(R.layout.fragment_lab_software, container, false);

        int floor = getArguments().getInt("Floor");
        int room = getArguments().getInt("Room");
        char building = getArguments().getChar("Building");
        closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        list = view.findViewById(R.id.softwareList);
        //softwareList.add(Integer.toString(floor));
        //softwareList.add(Integer.toString(room));
        //softwareList.add(String.valueOf(building));

        List<String> masterList= SIngleton2ShareData.getSoftwareList();
        for (int i = 0;i< masterList.size();i++){
            softwareList.add(masterList.get(i));
        }

        ArrayAdapter arrayAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,softwareList);
        list.setAdapter(arrayAdapter);
        return view;
    }
}

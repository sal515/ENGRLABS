package ca.engrLabs_390.engrlabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class LabSortFragment extends DialogFragment {

    private static final String TAG = "InsertCourseDialog";
    EditText titleEditText;
    EditText codeEditText;
    Button cancelButton;
    Button saveButton;
    //DBHelperCourse dbHandler = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflator, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflator.inflate(R.layout.fragment_lab_sort, container, false);

        /*
        titleEditText = view.findViewById(R.id.titleEditText);
        codeEditText = view.findViewById(R.id.gradeEditText);
        saveButton = view.findViewById(R.id.saveButton);
        cancelButton = view.findViewById(R.id.cancelButton);


        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onclick: cancel button");
                getDialog().dismiss();
            }
        });


        saveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String code = codeEditText.getText().toString();
                if(title.equals("")) {
                    Toast.makeText(getActivity(), "No Title", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(code.equals("")) {
                    Toast.makeText(getActivity(), "No Course Code", Toast.LENGTH_SHORT).show();
                    return;
                }

                getDialog().dismiss();

            }
        });
        */
        return view;
    }

}

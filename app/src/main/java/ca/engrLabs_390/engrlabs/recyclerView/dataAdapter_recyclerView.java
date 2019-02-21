package ca.engrLabs_390.engrlabs.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

// For more details of recycler or recycler adapter follow the link below:
// https://guides.codepath.com/android/using-the-recyclerview

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import ca.engrLabs_390.engrlabs.R;
import ca.engrLabs_390.engrlabs.database_files.recyclerViewData;


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class dataAdapter_recyclerView extends ListAdapter<recyclerViewData, dataAdapter_recyclerView.ViewHolder> {


    //==================== Fill Adapter with Data Model =============================


    public dataAdapter_recyclerView() {
        super(DIFF_CALLBACK);
    }

    // Store a member variable for the data
    private List<recyclerViewData> dataArr;

    // Pass in the contact array into the constructor
    // public dataAdapter_recyclerView(ArrayList<recyclerViewData> dataArr) {
    //    this.dataArr = dataArr;
    // }

    public static final DiffUtil.ItemCallback<recyclerViewData> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<recyclerViewData>() {
                @Override
                public boolean areItemsTheSame(recyclerViewData oldItem, recyclerViewData newItem) {
                    return oldItem.getmId() == newItem.getmId();
                }

                @Override
                public boolean areContentsTheSame(recyclerViewData oldItem, recyclerViewData newItem) {
                    return (oldItem.getName() == newItem.getName() && oldItem.getOnline() == newItem.getOnline());
                }
            };

    //==================== ============================= =============================


    //==================== Define View Holder =============================
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        public TextView textViewData1;
        public TextView textViewData2;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            textViewData1 = itemView.findViewById(R.id.rowTextView1);
            textViewData2 = itemView.findViewById(R.id.rowTextView2);
        }
    }


    //================================================================


    //==================== 3 Primary Methods of RecyclerView =============================

    // onCreateViewHolder to inflate the item layout and create the holder
    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom Layout for each row and return as variable
        View dataOrRowView = inflater.inflate(R.layout.layout_custom_row_expandable_recycler, parent, false);

        // return the new holder instance
        ViewHolder viewHolder = new ViewHolder(dataOrRowView);
        return viewHolder;
    }

    // onBindViewHolder to set the view attributes based on the data
    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(dataAdapter_recyclerView.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        // recyclerViewData data = dataArr.get(position);

        recyclerViewData data = getItem(position);

        // set item views based on your views and data model
        TextView textView1 = viewHolder.textViewData1;
        textView1.setText(data.getName());

        TextView textView2 = viewHolder.textViewData2;
        textView2.setText(data.getOnline());

    }

    // getItemCount to determine the number of items
    // @Override
    // public int getItemCount() {
    //        return dataArr.size();
    // }

    public void addMoreContacts(List<recyclerViewData> newdata) {
        dataArr.addAll(newdata);
        submitList(dataArr); // DiffUtil takes care of the check
    }

    //==================== ============================= =============================

}


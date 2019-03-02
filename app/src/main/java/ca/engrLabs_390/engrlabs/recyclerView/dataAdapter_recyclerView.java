package ca.engrLabs_390.engrlabs.recyclerView;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

// For more details of recycler or recycler adapter follow the link below:
// https://guides.codepath.com/android/using-the-recyclerview

import java.util.List;
import java.util.Queue;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import ca.engrLabs_390.engrlabs.LabInfo;
import ca.engrLabs_390.engrlabs.R;
import ca.engrLabs_390.engrlabs.database_files.recyclerViewData;


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class dataAdapter_recyclerView extends ListAdapter<recyclerViewData, dataAdapter_recyclerView.ViewHolder> {

    // member array to keep track of the state of the rows : https://android.jlelse.eu/android-handling-checkbox-state-in-recycler-views-71b03f237022
    private static SparseBooleanArray hiddenStateArray = new SparseBooleanArray();


    private Context context;
    private ViewHolder viewHolder;
    private Queue<Integer> openedQueue;
    RecyclerView.LayoutManager layoutManager;
    List<LabInfo> info;

    //==================== Fill Adapter with Data Model =============================

    public dataAdapter_recyclerView(List<LabInfo> inputInfo) {
        super(DIFF_CALLBACK);
        this.info = inputInfo;
    }

    // Store a member variable for the data
    private List<recyclerViewData> dataArr;

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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView roomNumberEdit;
        private TextView availabilityEdit;
        private TextView temperatureTitle;
        private TextView temperatureEdit;

        // Use groups for hide and visible
        private ViewGroup headerGroup;
        private ViewGroup expandingGroup;
        private ViewGroup rowContainer;

        private TextView numOfStudentsRoomEdit;
        private TextView roomCapacityEdit;
        private TextView upcomingClassEdit;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);


            // initialize the context member variable to the context of the activity
            context = itemView.getContext();

            roomNumberEdit = itemView.findViewById(R.id.roomNumberEdit);
            availabilityEdit = itemView.findViewById(R.id.availabilityEdit);
            temperatureTitle = itemView.findViewById(R.id.temperatureTitle);
            temperatureEdit = itemView.findViewById(R.id.temperatureEdit);


            headerGroup = itemView.findViewById(R.id.headingSection_constraintLayout);
            expandingGroup = itemView.findViewById(R.id.expandingSection_constraintLayout);
            rowContainer = itemView.findViewById(R.id.recycler_row_constrainet_layout);

            numOfStudentsRoomEdit = itemView.findViewById(R.id.numOfStudentsEdit);
            roomCapacityEdit = itemView.findViewById(R.id.roomCapacityEdit);
            upcomingClassEdit = itemView.findViewById(R.id.upcomingClassEdit);

            //Start with all the expandable sections closed
            expandingGroup.setVisibility(View.GONE);

            headerGroup.setOnClickListener(headerSectionListener);
            expandingGroup.setOnClickListener(expandingSectionListener);
//            rowContainer.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            // Just here coz it needs to be overridden due to inheritance; we are using the bottom two
        }

        private View.OnClickListener headerSectionListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = getAdapterPosition();
                if (!(hiddenStateArray.get(adapterPosition, false)) && v.getId() == R.id.headingSection_constraintLayout) {
                    if (expandingGroup.getVisibility() == View.GONE) {
                        hiddenStateArray.put(adapterPosition, true);
                        expandingGroup.setVisibility(View.VISIBLE);
                    }

                    Toast.makeText(context, "Visible", Toast.LENGTH_SHORT).show();

                } else {
                    if (expandingGroup.getVisibility() == View.VISIBLE) {
                        hiddenStateArray.put(adapterPosition, false);
                        expandingGroup.setVisibility(View.GONE);

                        Toast.makeText(context, "Invisible", Toast.LENGTH_SHORT).show();

                    }
                }
            }

        };

        private View.OnClickListener expandingSectionListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = getAdapterPosition();
                if ((hiddenStateArray.get(adapterPosition, false)) && v.getId() == R.id.expandingSection_constraintLayout) {
                    if (expandingGroup.getVisibility() == View.VISIBLE) {
                        hiddenStateArray.put(adapterPosition, false);
                        expandingGroup.setVisibility(View.GONE);
                    }
                    Toast.makeText(context, "Invisible", Toast.LENGTH_SHORT).show();

                } else {
                    if (expandingGroup.getVisibility() == View.VISIBLE) {
                        hiddenStateArray.put(adapterPosition, false);
                        expandingGroup.setVisibility(View.GONE);
                    }
                    Toast.makeText(context, "Invisible", Toast.LENGTH_SHORT).show();

                }

            }
        };


        // implementation found from here : https://github.com/Oziomajnr/RecyclerViewCheckBoxExample2/blob/with-sparse-boolean-array/app/src/main/java/ogbe/ozioma/com/recyclerviewcheckboxexample/Adapter.java
        void onRecycleHideExpandedSections(int position) {
            if (hiddenStateArray.get(position, false)) {
                if (expandingGroup.getVisibility() == View.VISIBLE) {
                    hiddenStateArray.put(position, false);
                    expandingGroup.setVisibility(View.GONE);
                }
                Toast.makeText(context, "Invisible", Toast.LENGTH_SHORT).show();
            }

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
        viewHolder = new ViewHolder(dataOrRowView);


        return viewHolder;
    }

    // onBindViewHolder to set the view attributes based on the data
    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(dataAdapter_recyclerView.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        // recyclerViewData data = dataArr.get(position);

        // getting the data at the position from the array
        recyclerViewData data = getItem(position);


        // set item views based on your views and data model
        TextView textView1 = viewHolder.roomNumberEdit;
        textView1.setText("Room: " + Integer.toString(info.get(position).floor) + Integer.toString(info.get(position).room));
        //textView1.setText(data.getName());

        TextView textView2 = viewHolder.availabilityEdit;
        textView2.setText(data.getOnline());


        TextView textView3 = viewHolder.numOfStudentsRoomEdit;
        textView3.setText(Integer.toString(info.get(position).numberOfStudents));

        TextView textView4 = viewHolder.roomCapacityEdit;
        textView4.setText(Integer.toString(info.get(position).roomCapacity));

        TextView textView5 = viewHolder.upcomingClassEdit;
        textView5.setText(Integer.toString(info.get(position).upcomingClass));

        /*(
        numOfStudentsRoomEdit = itemView.findViewById(R.id.numOfStudentsEdit);
        roomCapacityEdit = itemView.findViewById(R.id.roomCapacityEdit);
        upcomingClassEdit = itemView.findViewById(R.id.upcomingClassEdit);
        */

    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        // it closes the recycled rows
        holder.onRecycleHideExpandedSections(holder.getAdapterPosition());
    }


    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        // its works on the view that you are about to see - works well for my needs here
        // holder.expandingGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        // One time thing - once you see it its gone
        // holder.expandingGroup.setVisibility(View.GONE);
    }

    public void addMoreContacts(List<recyclerViewData> newdata) {
        dataArr.addAll(newdata);
        submitList(dataArr); // DiffUtil takes care of the check
    }


    //==================== ============================= =============================


}


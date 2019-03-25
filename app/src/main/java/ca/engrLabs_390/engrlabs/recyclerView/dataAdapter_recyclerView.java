package ca.engrLabs_390.engrlabs.recyclerView;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import ca.engrLabs_390.engrlabs.database_files.LabDataModel;
import ca.engrLabs_390.engrlabs.R;


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class dataAdapter_recyclerView extends ListAdapter<LabDataModel, dataAdapter_recyclerView.ViewHolder> {

    // member array to keep track of the state of the rows : https://android.jlelse.eu/android-handling-checkbox-state-in-recycler-views-71b03f237022
    private static SparseBooleanArray hiddenStateSparseBoolArray = new SparseBooleanArray();


    private Context context;
    private ViewHolder viewHolder;
    private Queue<Integer> openedQueue;
    RecyclerView.LayoutManager layoutManager;

    // Array list of the DataModel
    List<LabDataModel> labDataModel;

    //==================== Fill Adapter with Data Model =============================

    //    public dataAdapter_recyclerView(List<LabDataModel> inputInfo) {
    public dataAdapter_recyclerView() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<LabDataModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<LabDataModel>() {
                @Override
                public boolean areItemsTheSame(LabDataModel oldItem, LabDataModel newItem) {
                    return ((oldItem.getFloor() == newItem.getFloor()) &&
                            (oldItem.getRoom() == newItem.getRoom()));
//                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(LabDataModel oldItem, LabDataModel newItem) {
                    return ((oldItem.getRoomCapacity() == newItem.getRoomCapacity()) &&
                            (oldItem.getNumberOfStudents() == newItem.getNumberOfStudents()) &&
                            oldItem.getTemperature() == newItem.getTemperature());
                }
            };

    //==================== ============================= =============================


    //==================== Define View Holder =============================
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView roomNumberEdit;
        private TextView availabilityEdit;
        private TextView temperatureEdit;

        // Use groups for hide and visible
        private ViewGroup headerGroup;
        private ViewGroup expandingGroup;
        private ViewGroup expandingInfo;
        private ViewGroup rowContainer;

        private TextView numOfStudentsRoomEdit;
        private TextView roomCapacityEdit;
        private TextView upcomingClassEdit;

        private ImageView favourite;

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
            temperatureEdit = itemView.findViewById(R.id.temperatureEdit);

            favourite = itemView.findViewById(R.id.favouriteStar);
            favourite.setImageResource(R.drawable.ic_star_border_black_24dp);

            headerGroup = itemView.findViewById(R.id.headingSection_constraintLayout);
            expandingGroup = itemView.findViewById(R.id.expandingSection_relativeLayout);
            expandingInfo = itemView.findViewById(R.id.expandingInformationBlock);
            rowContainer = itemView.findViewById(R.id.recycler_row_constrainet_layout);

            numOfStudentsRoomEdit = itemView.findViewById(R.id.numOfStudentsEdit);
            roomCapacityEdit = itemView.findViewById(R.id.roomCapacityEdit);
            upcomingClassEdit = itemView.findViewById(R.id.upcomingClassEdit);

            // setting the layout listeners
            headerGroup.setOnClickListener(headerSectionListener);
            expandingInfo.setOnClickListener(expandingSectionListener);
//            favourite.setOnClickListener(favouriteStarListener);

        }

        @Override
        public void onClick(View v) {
            // Just here coz it needs to be overridden due to inheritance; we are using the following ones:
        }

        private View.OnClickListener headerSectionListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = getAdapterPosition();
                if (!(hiddenStateSparseBoolArray.get(adapterPosition, false)) && v.getId() == R.id.headingSection_constraintLayout) {
                    if (expandingGroup.getVisibility() == View.GONE) {
                        hiddenStateSparseBoolArray.put(adapterPosition, true);
                        expandingGroup.setVisibility(View.VISIBLE);
                    }

                    //Toast.makeText(context, "Visible", Toast.LENGTH_SHORT).show();

                } else {
                    if (expandingGroup.getVisibility() == View.VISIBLE) {
                        hiddenStateSparseBoolArray.put(adapterPosition, false);
                        expandingGroup.setVisibility(View.GONE);

                        //Toast.makeText(context, "Invisible", Toast.LENGTH_SHORT).show();

                    }
                }
            }

        };

        private View.OnClickListener expandingSectionListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = getAdapterPosition();
                if ((hiddenStateSparseBoolArray.get(adapterPosition, false)) && v.getId() == R.id.expandingSection_relativeLayout) {
                    if (expandingGroup.getVisibility() == View.VISIBLE) {
                        hiddenStateSparseBoolArray.put(adapterPosition, false);
                        expandingGroup.setVisibility(View.GONE);
                    }
                    //Toast.makeText(context, "Invisible", Toast.LENGTH_SHORT).show();

                } else {
                    if (expandingGroup.getVisibility() == View.VISIBLE) {
                        hiddenStateSparseBoolArray.put(adapterPosition, false);
                        expandingGroup.setVisibility(View.GONE);
                    }
                    //Toast.makeText(context, "Invisible", Toast.LENGTH_SHORT).show();

                }

            }
        };

        // FIXME: Figure out the Logic later on --- Commented for now
//        private View.OnClickListener favouriteStarListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int adapterPosition = getAdapterPosition();
//                if (labDataModel.get(getAdapterPosition()).favourite == false) {
//                    labDataModel.get(getAdapterPosition()).favourite = true;
//                    Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
//                } else {
//                    labDataModel.get(getAdapterPosition()).favourite = false;
//                    Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show();
//                }
//
//                //FIXME: The getAdapgerPosition() should be used to identify the row where the data is changed
//                // then update that specific row rather than the whole dataset change
//                notifyDataSetChanged();
//                //notifyItemChanged(getAdapterPosition());
//            }
//        };

        // implementation found from here : https://github.com/Oziomajnr/RecyclerViewCheckBoxExample2/blob/with-sparse-boolean-array/app/src/main/java/ogbe/ozioma/com/recyclerviewcheckboxexample/Adapter.java
        void onRecycleHideExpandedSections(int position) {
            if (hiddenStateSparseBoolArray.get(position, false)) {
                if (expandingGroup.getVisibility() == View.VISIBLE) {
                    hiddenStateSparseBoolArray.put(position, false);
                    expandingGroup.setVisibility(View.GONE);
                }
                //Toast.makeText(context, "Invisible", Toast.LENGTH_SHORT).show();
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

        // getting the data at the position from the array
        LabDataModel data = getItem(position);


        // set item views based on your views and data model
        TextView roomNumberTextView = viewHolder.roomNumberEdit;
        roomNumberTextView.setText("Room: " +  Integer.toString(data.getRoom()));
        //roomNumberTextView.setText(data.getName());

        TextView availabilityTextView = viewHolder.availabilityEdit;
        availabilityTextView.setText(data.getAvailability());

        TextView numberOfStudentsTextView = viewHolder.numOfStudentsRoomEdit;
        numberOfStudentsTextView.setText(data.getNumberOfStudents());

        TextView roomCapacityTextView = viewHolder.roomCapacityEdit;
        roomCapacityTextView.setText(Integer.toString(data.getRoomCapacity()));

        TextView upcomingClassTextView = viewHolder.upcomingClassEdit;
        // FIXME: Set up the map properly and then set the value
        //        upcomingClassTextView.setText(Integer.toString(data.getUpcomingClass().));

        TextView temperatureTextView = viewHolder.temperatureEdit;
        temperatureTextView.setText(data.getTemperature() + "°C");

        if (!hiddenStateSparseBoolArray.get(position)) {
            //Start with all the expandable sections closed
            viewHolder.expandingGroup.setVisibility(View.GONE);
        } else {
            viewHolder.expandingGroup.setVisibility(View.VISIBLE);
        }

        if (!data.isFavourite()) {
            viewHolder.favourite.setImageResource(R.drawable.ic_star_border_black_24dp);
        } else {
            viewHolder.favourite.setImageResource(R.drawable.ic_star_border_yellow_24dp);
        }
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

    public void addMoreContacts(List<LabDataModel> newLabDataModel) {
        labDataModel.addAll(newLabDataModel);
        submitList(labDataModel); // DiffUtil takes care of the check
    }


    //==================== ============================= =============================


}


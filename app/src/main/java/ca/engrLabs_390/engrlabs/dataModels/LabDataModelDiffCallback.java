package ca.engrLabs_390.engrlabs.dataModels;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class LabDataModelDiffCallback extends DiffUtil.Callback {

    private final List<LabDataModel> oldLabDataModelArrayList;
    private final List<LabDataModel> newLabDataModelArrayList;

    public LabDataModelDiffCallback(List<LabDataModel> oldLabDataModelArrayList, List<LabDataModel> newLabDataModelArrayList) {
        this.oldLabDataModelArrayList = oldLabDataModelArrayList;
        this.newLabDataModelArrayList = newLabDataModelArrayList;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // needed for Item animator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }

    @Override
    public int getOldListSize() {
        return oldLabDataModelArrayList.size();
    }

    @Override
    public int getNewListSize() {
        return newLabDataModelArrayList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        try {
            return oldLabDataModelArrayList.get(oldItemPosition).getRoom() == newLabDataModelArrayList.get(newItemPosition).getRoom();
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        try {
            return this.oldLabDataModelArrayList.get(oldItemPosition).equals(this.newLabDataModelArrayList.get(newItemPosition));
        } catch (Exception e) {
            return false;
        }
    }
}

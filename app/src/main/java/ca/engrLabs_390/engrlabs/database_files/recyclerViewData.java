package ca.engrLabs_390.engrlabs.database_files;

import java.util.ArrayList;
import java.util.List;

// just a static class with static functions to get the data as necessary
public class recyclerViewData {
    private String mName;
    private String mOnline;
    private int mId;

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public recyclerViewData(String name, String online, int id) {
        mName = name;
        mOnline = online;
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public String getOnline() {
        return mOnline;
    }

    private static int lastContactId = 0;

    public static List<recyclerViewData> createContactsList(int numContacts) {
        List<recyclerViewData> data = new ArrayList<recyclerViewData>();

        for (int i = 1; i <= numContacts; i++) {
            data.add(new recyclerViewData("Data " + ++lastContactId, "yes", i));
        }

        return data;
    }

}

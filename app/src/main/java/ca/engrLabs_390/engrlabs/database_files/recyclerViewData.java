package ca.engrLabs_390.engrlabs.database_files;

import java.util.ArrayList;

// just a static class with static functions to get the data as necessary
public class recyclerViewData {
    private String mName;
    private String mOnline;

    public recyclerViewData(String name, String online) {
        mName = name;
        mOnline = online;
    }

    public String getName() {
        return mName;
    }

    public String getOnline() {
        return mOnline;
    }

    private static int lastContactId = 0;

    public static ArrayList<recyclerViewData> createContactsList(int numContacts) {
        ArrayList<recyclerViewData> data = new ArrayList<recyclerViewData>();

        for (int i = 1; i <= numContacts; i++) {
            data.add(new recyclerViewData("Data " + ++lastContactId, "yes"));
        }

        return data;
    }

}

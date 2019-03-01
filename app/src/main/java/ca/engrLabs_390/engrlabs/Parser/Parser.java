package ca.engrLabs_390.engrlabs.Parser;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class Parser {
    private static final String TAG = "test";
    private Context context;

    public Vector<Software> parse(Context current){
        context = current;
        //FileReader input = new FileReader("partially_parsed.txt");
        //BufferedReader bufRead = new BufferedReader(input);

        AssetManager assetManager = current.getResources().getAssets();
        Vector<Software> software = new Vector<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("software.txt")));
            String line;
            while ((line = reader.readLine()) != null) {

                Software temp;
                String tempName = line;
                System.out.println(" ");
                System.out.println("Software: " + tempName);
                line = reader.readLine();
                line+=","; //add a comma at the end to make parsing easier
                int timer =0;
                boolean flagTest = false;
                while((!line.isEmpty())&& timer<1000){
                    System.out.println(line);
                    timer++;
                    //Log.i(TAG,line);
                    String building=line.substring(0,1);
                    int comma =  line.indexOf(',');
                    //System.out.println("comma: " + comma);
                    //String replacement = line.substring(comma+1);
                    //System.out.println("replacement: " + replacement);
                    //line = replacement;

                    if(!building.equals("H")){
                        String replacement = line.substring(comma+1);
                        line = replacement;
                        continue;
                    }
                    else{
                        String substring = line.substring(0,comma);
                        System.out.println("substring: " + substring);
                        String floor = substring.substring(1,3);
                        String room = substring.substring(3,5);
                        if (!((floor.matches("[0-9]+"))&&(room.matches("[0-9]+")))){
                            if(flagTest == false){
                                flagTest = true;
                                line = line.substring(0,1) + "0" + line.substring(1);
                                System.out.println("ERRORRRRRRRRR: " + substring + " TRYING TO ADD 0");
                                continue;
                            }
                            else{
                                System.out.println("ERRORRRRRRRRR: " + substring + "IGNORING");
                                String replacement = line.substring(comma+1);
                                line = replacement;
                                flagTest = false;
                                continue;
                            }
                        }
                        if (flagTest == true){
                            System.out.println("ERRORRRRRRRRR: " + substring + "FIXED");
                        }
                        flagTest = false;
                        int floorInt = Integer.parseInt(floor);
                        int roomInt = Integer.parseInt(room);
                        System.out.println("floor: " + floorInt + " room: " + roomInt);

                        Classroom tempClass = new Classroom();
                        tempClass.floor = floorInt;
                        tempClass.room = roomInt;
                        tempClass.building = building;
                        software.add(new Software(tempName,tempClass));
                        System.out.println("added");
                        String replacement = line.substring(comma+1);
                        line = replacement;
                    }

                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {

        }

        return software;
    }
}

package ca.engrLabs_390.engrlabs.Parser;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Parser {
    private static final String TAG = "test";
    private Context context;

    public Vector<Software> parse(Context current) {
        context = current;
        //FileReader input = new FileReader("partially_parsed.txt");
        //BufferedReader bufRead = new BufferedReader(input);


        AssetManager assetManager1 = current.getResources().getAssets();
        Vector<Software> software1 = new Vector<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("input.txt")));
            String line;
            String inputString = "";

                //Concatenate the entire file
            while ((line = reader.readLine()) != null) {
                inputString += line;
            }

            System.out.println(inputString);
            System.out.println("step 1-----------------------------------------------");




            int limitter = 0;
            List<String> tr = new ArrayList<>();
            inputString = inputString.substring(inputString.indexOf("<tr>",0));
            while ((inputString.indexOf("<tr>",0) != -1)|| limitter >200)
            {
                int beginning = (inputString.indexOf("<tr>",0));
                int end = (inputString.indexOf("</tr>",0)) + 5;
                System.out.println("start: " + beginning + "   end: " + end);
                if (end<beginning){
                    break;
                }
                String temp = inputString.substring(beginning,end-beginning);
                limitter++;
                inputString = inputString.substring(end);
                tr.add(temp);
            }

            for (int i = 0;i<tr.size();i++) {
                //std::cout << info[i].software << "\n" << info[i].classes << "\n";
                System.out.println(tr.get(i));
            }
            System.out.println("step 2-----------------------------------------------");





            List<SoftwareTemp> info = new ArrayList<>();
            for(int j = 0;j<tr.size();j++) {
                SoftwareTemp temp = new SoftwareTemp();
                for (int i = 0; i < 2; i++) {
                    int beginning = (tr.get(j).indexOf("<td>", 0));
                    int end = (tr.get(j).indexOf("</td>", 0));

                    if (i == 0) {
                        temp.software = tr.get(j).substring(beginning+4,end-beginning+4);
                        System.out.println("start: " + beginning + "   end: " + end);
                        System.out.println("software: " + temp.software);
                        String tempString = tr.get(j).substring(end+1);
                        tr.set(j,tempString);
                    }
                    else {
                        temp.classes = tr.get(j).substring(beginning+4,end-beginning+4);
                        System.out.println("class: " + temp.classes);
                    }

                }
                info.add(temp);
            }

            System.out.println("step 3-----------------------------------------------");


            for (int i = 0;i<info.size();i++)
            {
                if(info.get(i).classes.equals("")){
                    System.out.println("deleted: " + info.get(i).software);
                    info.remove(i);
                    i--;
                }
            }

            System.out.println("step 4-----------------------------------------------");

            String output = "";

            for (int i = 0;i<info.size();i++) {
                //std::cout << info[i].software << "\n" << info[i].classes << "\n";
                output += info.get(i).software + "\n" + info.get(i).classes + "\n";
            }
            System.out.println(output);


            System.out.println("step 5-----------------------------------------------\n-\n/\n-\n/\n-\n/\n-\n/\n-\n/\n-\n/\n-\n/\n-\n/\n-\n/\n-\n/\n-\n/\n-\n/\n-\n/\n-\n/\n-\n/\n-\n/\n-");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }





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

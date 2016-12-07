package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MyCsv {

	public static HashMap<String, ArrayList<String>> readAndMap(String filePath,int keyTitleIndex){
		BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        HashMap<String, ArrayList<String>> map=new HashMap<>();
        
        try {

            br = new BufferedReader(new FileReader(filePath));
            br.readLine(); //drop Header
            while ((line = br.readLine()) != null) {

                String[] columns = line.split(cvsSplitBy);
                ArrayList<String> list;
                if(map.containsKey(columns[keyTitleIndex])){
                	list=map.get(columns[keyTitleIndex]);
                	
                }else{
                	list=new ArrayList<>();
                }
                list.add(line);
            	map.put(columns[keyTitleIndex],list );
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return map;
	}
}

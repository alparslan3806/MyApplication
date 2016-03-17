package alp.myapplication;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Created by Toshıba on 15.3.2016.
 */
public class Area
{
    private Context context;
    private Vector<DistanceViewModel> model;
    private Map<String, Integer> areaList = new HashMap<>();
    DatabaseHelper db;

    public Area(Context context)
    {
        this.context = context;
        db = new DatabaseHelper(context);
    }

    public String findZone(TreeMap<Double, String> scanResultsMap)
    {
        String area = null;

        Iterator<Map.Entry<Double, String>> scanEntries = scanResultsMap.entrySet().iterator();
        while (scanEntries.hasNext()) {   /** Here entry is scanned results object. */

            Map.Entry<Double, String> entry = scanEntries.next();
            model = db.getDistanceFromBSSID(entry.getValue());
            String modelString;
            for (int counter = 0; counter < model.size(); counter++)
            {
                modelString = model.get(counter).zone;
                /** for(int i = 0; i < entityBaseVector.size(); i++) Below if controls the distance. */
                if (entry.getKey() <= model.get(counter).farthest && entry.getKey() >= model.get(counter).shortest)
                {
                    if(areaList.isEmpty() || !areaList.containsKey(model.get(counter).zone))
                    {
                        areaList.put(modelString, 1);
                    } else if(areaList.containsKey(model.get(counter).zone))
                    {
                        areaList.put(modelString, areaList.get(modelString) + 1);
                    }

                }
            }
        }
        int maxFrequently = 1;
        for(String mostFrequentlyArea : areaList.keySet())
        {
            // What if same number of Frequently occurs ?
            if(areaList.get(mostFrequentlyArea) > maxFrequently)
            {
                maxFrequently = areaList.get(mostFrequentlyArea);
            }
        }
        int number;
        for(String findedArea : areaList.keySet())
        {
            number = areaList.get(findedArea);
            if(number >= maxFrequently)
            {
                area = findedArea;
            }
        }


        return area;
    }
}



/**
 Iterator<Map.Entry<Double, String>> scanEntries = scanResultsMap.entrySet().iterator();
 while (scanEntries.hasNext())
 {
 Map.Entry<Double, String> entry = scanEntries.next();
 for (DistanceViewModel dataEntities : entityBaseVector)
 //for(int i = 0; i < entityBaseVector.size(); i++)
 if(entry.getValue().equals(dataEntities.bssid))
 {
 if(entry.getKey() <= dataEntities.farthest && entry.getKey() >= dataEntities.shortest)
 {
 area = dataEntities.zone;
 }
 }
 }
 */
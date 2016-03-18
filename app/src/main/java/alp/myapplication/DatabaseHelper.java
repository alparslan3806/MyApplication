package alp.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Alparslan Selçuk Develioğlu on 11.3.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "AVM.db";
    public static final String TABLESTORENAME = "StoreNames_Table";
    public static final String TABLEDISTANCES = "Distances_Table";

    public static final String ID = "ID";
    public static final String STORENAMES = "StoreNames";
    public static final String ZONE = "Zone";
    public static final String SHORTESTDISTANCE = "ShortestDistance";
    public static final String FARTHESTDISTANCE = "FarthestDistance";
    public static final String NEARZONES = "NearZones";
    public static final String BSSID = "BSSID";

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLESTORENAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "StoreNames TEXT, Zone TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLEDISTANCES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Zone TEXT, BSSID TEXT, NearZones TEXT, FarthestDistance INTEGER, ShortestDistance INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLEDISTANCES);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLESTORENAME);
        onCreate(db);
    }

    public Vector<DistanceViewModel> getDistanceFromBSSID(String sendedBSSID)
    {
        String[] columns = {ID, ZONE, BSSID, NEARZONES, FARTHESTDISTANCE, SHORTESTDISTANCE };
        DistanceViewModel entity;
        SQLiteDatabase dbReadable = this.getReadableDatabase();
        Cursor result = dbReadable.query(TABLEDISTANCES, columns, "BSSID=?", new String[] {sendedBSSID},null,null,null,null);
        Vector<DistanceViewModel> records = new Vector<>(result.getCount());
        if(result.moveToNext())
        {// zone bssid nearzone farhtest shortest

            while(result.moveToNext())
            {
                entity = new DistanceViewModel(result.getString(1), result.getString(2), result.getString(3),
                                               result.getInt(4), result.getInt(5));
                records.add(entity);
            }
        }

        return records;
    }


    public Cursor getDistanceInfo()
    {
        Cursor result = db.rawQuery("SELECT * FROM " + TABLEDISTANCES, null);
        return result;
    }

    public boolean insertStoreNameData(String storeName, String zone)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put( STORENAMES, storeName);
        contentValues.put( ZONE, zone);

        long result = db.insert(TABLESTORENAME, null, contentValues);
        if(result == -1)
        {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertDistancesData(String zone, String bssid, String nearZones, String Farthest, String Shortest)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put( ZONE, zone);
        contentValues.put( BSSID, bssid);
        contentValues.put( NEARZONES, nearZones);
        contentValues.put( FARTHESTDISTANCE, Farthest);
        contentValues.put( SHORTESTDISTANCE, Shortest);
        db = this.getWritableDatabase();
        long result = db.insert(TABLEDISTANCES, null, contentValues);
        if(result == -1)
        {
            return false;
        } else {
            return true;
        }
    }
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);
            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {
                alc.set(0,c);
                c.moveToFirst();
                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}

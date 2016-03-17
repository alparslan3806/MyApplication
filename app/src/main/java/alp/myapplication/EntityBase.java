package alp.myapplication;

import java.util.Map;

/**
 * Created by Toshıba on 15.3.2016.
 */
public class EntityBase {
    private int ID;
    public int getID()
    {
        return ID;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    public EntityBase()
    {
    }

    public EntityBase(int id)
    {
        this();
        ID = id;
    }

}

package alp.myapplication;
/**
 * Created by Alparslan Selçuk Develioğlu on 13.3.2016.
 */
public class DistanceViewModel extends EntityBase
{
    public String bssid = null;
    public String zone = null;
    public String nearZone = null;
    public Integer farthest = null;
    public Integer shortest = null;

    public DistanceViewModel(String Zone, String Bssid, String Nearzone, int Farthest, int Shortest)
    {
        super();
        bssid = Bssid;
        zone = Zone;
        nearZone = Nearzone;
        farthest = Farthest;
        shortest = Shortest;
    }
    public DistanceViewModel()
    {
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public Integer getFarthest() {
        return farthest;
    }

    public void setFarthest(Integer farthest) {
        this.farthest = farthest;
    }

    public String getNearZone() {
        return nearZone;
    }

    public void setNearZone(String nearZone) {
        this.nearZone = nearZone;
    }

    public Integer getShortest() {
        return shortest;
    }

    public void setShortest(Integer shortest) {
        this.shortest = shortest;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }


}

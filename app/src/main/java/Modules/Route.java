package Modules;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/** Represents a Route Object*/
public class Route {

    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}

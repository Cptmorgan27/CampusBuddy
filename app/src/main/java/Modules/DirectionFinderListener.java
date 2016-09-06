package Modules;

import java.util.List;

/** Defines a DirectionFinderListener */
public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}

package TrafficFlow.main;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import drawing.KeyInterceptor.KeyHook;
import TrafficFlow.mapFramework.MapFrame;
import TrafficFlow.mapFramework.MapImage;

public class Program {

    private static MapImage _mapImage;
    private static MapFrame _mapFrame;
    private static Map<Character, Set<String>> _locationsMap; // Map to store routes based on their location of origin

    private static String _savedRoute; // New variable to store the saved route
    private static Set<String> _collisions; // New variable to store colliding routes

    private static Graph<String> _graph; // New graph instance
    private static int _maxColors; // Maximum number of colors used
    private static int _currentColor; // Current color being shown

    /**
     * Lambda method which is called each time the user presses keys {A, B, C, D}
     * 
     * @param keyEvent -> Details about the key which was pressed
     */
    private static KeyHook _hook = (KeyEvent keyEvent) -> {
        String statusText = "";
        // takes the key that was pressed and if not uppercase turns it into one
        char key = Character.toTitleCase(keyEvent.getKeyChar());
        // if the the key is in the location set
        if (_locationsMap.containsKey(key)) {
            // the routes are painted
            processKeyPress(key);
            // the status message is printed
            statusText = "Key: '" + key + "': Routes: " + _locationsMap.get(key);
            _mapFrame.setStatusMessage(statusText);
        }
    };

    /**
     * Initializes _locationsMap with routes grouped by their origin character.
     */
    public static void buildLoctionMap() {
        _locationsMap = new HashMap<>();
        Set<String> routes = _mapImage.getRoutes();
        for (String route : routes) {
            char origin = route.charAt(0);
            // checks if the key exists in the map
            if (!_locationsMap.containsKey(origin)) {
                // doesn't exist -> creatte a new HashSet and add it to the map
                _locationsMap.put(origin, new HashSet<>());
            }
            // add the routes to the HashSet (keys)
            _locationsMap.get(origin).add(route);
        }
    }

    /**
     * * Processes a key press event, overlaying associated routes on the map.
     *
     * @param key the pressed key
     */
    private static void processKeyPress(char key) {
        // Clear all overlays & Clear the status bar text
        _mapFrame.repaint();
        // Check if locationsMap contains the key that was typed
        if (_locationsMap.containsKey(key)) {
            // Get the set of routes associated with the key
            Set<String> routes = _locationsMap.get(key);
            // Overlay the set of routes on the map
            _mapImage.setOverlays(routes);
        }
        // Reflect changes by repainting the map
        _mapFrame.repaint();
    }

    /**
     * Lambda method which is called each time the user presses key 'X'
     */
    private static KeyHook _hookX = (KeyEvent keyEvent) -> {
        Set<String> overlays = _mapImage.getOverlays();
        if (overlays.size() == 1) {
            // Save the single route in _savedRoute
            _savedRoute = overlays.iterator().next();

            // Find all collisions for the saved route
            _collisions = new HashSet<>();
            for (String route : _mapImage.getRoutes()) {
                if (_mapImage.collide(_savedRoute, route)) {
                    _collisions.add(route);
                }
            }

            // Add the original route to the collisions set
            _collisions.add(_savedRoute);

            // Overlay the collisions and the original route
            _mapImage.setOverlays(_collisions);
            _mapFrame.repaint();
            _mapFrame.setStatusMessage("Showing collisions for route: " + _savedRoute);
        } else if (_savedRoute != null) {
            // If a route was previously saved, overlay only that route
            _mapImage.setOverlays(Set.of(_savedRoute));
            _mapFrame.repaint();
            _mapFrame.setStatusMessage("Showing route: " + _savedRoute);

            // // Clear the saved route and collisions
            _savedRoute = null;
            _collisions = null;
        }else{
            // Increment the current color
            _currentColor = (_currentColor + 1) % (_maxColors + 1);
            if (_currentColor == 0) {
                _currentColor = 1; // Skip color 0 (uncolored)
            // Get the list of nodes with the current color
            }

            List<String> coloredRoutes = _graph.getColor(_currentColor);
            // Overlay the colored routes
            _mapImage.setOverlays(new HashSet<>(coloredRoutes));
            
            _mapFrame.repaint();
            _mapFrame.setStatusMessage("Showing routes with color: " + _currentColor);

        }
    };

    public static void main(String[] args) throws IOException, InterruptedException {
        // loads an intersection image file and displays it in a map frame.
        _mapImage = MapImage.load("TrafficFlow/maps/Loyal.jpg");
        _mapFrame = new MapFrame(_mapImage);
        buildLoctionMap();

        // Get all the routes from the map image
        List<String> routes = new ArrayList<>(_mapImage.getRoutes());

        // // Create a new graph
        _graph = new Graph<>();

        // Add all the routes as nodes in the graph
        for (String route : routes) {
            _graph.addNode(route);
        }

        // Build the collision list and add edges between colliding routes
        for (String a : routes) {
            for (String b : routes) {
                if (_mapImage.collide(a, b)) {
                    _graph.addEdge(a, b);
                }
            }
        }
      

        // Perform greedy coloring and save the maximum number of colors
        _maxColors = _graph.greedyColoring();
        _currentColor = 0; // Start with color 0 (uncolored)

        // Set up a key typed hook for 'X'
        _mapFrame.setKeyTypedHook('X', _hookX);

        // opens the GUI window
        _mapFrame.open();

        // stops, waiting for user action
        _mapFrame.stop();

        // close the window and terminate the program
        _mapFrame.close();
    }

}

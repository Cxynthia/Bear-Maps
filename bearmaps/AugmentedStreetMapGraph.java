package bearmaps;

import bearmaps.utils.graph.streetmap.StreetMapGraph;

import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.ArrayList;
import bearmaps.utils.ps.WeirdPointSet;
import bearmaps.utils.ps.Point;
import bearmaps.utils.graph.streetmap.Node;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private WeirdPointSet pointSet;
    private HashMap<Point, Node> pointToNode = new HashMap<>();
    private MyTrieSet trieSet = new MyTrieSet();
    private HashMap<String, List<Node>> names = new HashMap<>();

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        List<Node> nodes = this.getNodes();
        List<Point> points = new ArrayList<>();

        for (Node n : nodes) {
            if (n.name() == null) {
                Point p = new Point(n.lon(), n.lat());
                pointToNode.put(p, n);
                points.add(p);
            } else {
                String cleaned = cleanString(n.name());
                if (names.containsKey(cleaned)) {
                    names.get(cleaned).add(n);
                } else {
                    ArrayList<Node> newL = new ArrayList<>();
                    newL.add(n);
                    names.put(cleaned, newL);
                }
                trieSet.add(cleaned);
            }
        }
        pointSet = new WeirdPointSet(points);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Node n = pointToNode.get(pointSet.nearest(lon, lat));
        return n.id();
    }


    /**
     * For Project Part III (extra credit)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> cleaned = trieSet.keysWithPrefix(cleanString(prefix));
        List<String> results = new LinkedList<>();
        for (String s : cleaned) {
            for (Node n : names.get(s)) {
                if (!results.contains(n.name()) || n.name().equals(prefix)) {
                    results.add(n.name());
                }
            }
        }
        return results;
    }

    /**
     * For Project Part III (extra credit)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        String cleaned = cleanString(locationName);
        List<Map<String, Object>> resInfo = new LinkedList<>();
        if (names.containsKey(cleaned)) {
            for (Node n : names.get(cleaned)) {
                Map<String, Object> info = new HashMap<>();
                info.put("lat", n.lat());
                info.put("lon", n.lon());
                info.put("name", n.name());
                info.put("id", n.id());
                resInfo.add(info);
            }
        }
        return resInfo;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
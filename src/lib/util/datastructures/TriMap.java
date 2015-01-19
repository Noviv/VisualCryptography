package lib.util.datastructures;

import java.util.HashMap;

public class TriMap {

    private HashMap<String, Double> map;

    public TriMap() {
        map = new HashMap<>();
    }

    public int getMinValX() {
        String minVal = null;
        for (String val : map.keySet()) {
            if (minVal == null) {
                minVal = val;
            }
            if (map.get(minVal) < map.get(val)) {
                val = minVal;
            }
        }
        return (minVal == null ? -1 : Integer.parseInt(minVal.substring(0, minVal.indexOf(", "))));
    }

    public int getMinValY() {
        String minVal = null;
        for (String val : map.keySet()) {
            if (minVal == null) {
                minVal = val;
            }
            if (map.get(minVal) < map.get(val)) {
                val = minVal;
            }
        }
        return (minVal == null ? -1 : Integer.parseInt(minVal.substring(minVal.indexOf(", ") + 2)));
    }

    public double getAverage() {
        double average = 0.0;
        for (double d : map.values()) {
            average += d;
        }
        return average / map.values().size();
    }

    public int size() {
        return map.size();
    }

    public void put(int x, int y, double val) {
        map.put("" + x + ", " + y, val);
    }

    public double get(int x, int y) {
        return map.get("" + x + ", " + y);
    }

    public Double[] getVals() {
        return (Double[]) map.values().toArray();
    }

    public void clear() {
        map.clear();
    }

    public void print() {
        for (String key : map.keySet()) {
            System.out.println(key + " -> " + map.get(key));
        }
    }
}

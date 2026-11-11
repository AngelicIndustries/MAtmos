package eu.ha3.matmos.core.sheet;

import eu.ha3.matmos.util.math.Numbers;

public class DeltaSheet extends GenericSheet implements VirtualSheet {
    private final DataPackage data;

    private final String actualSheet;
    private final String deltaSheet;

    public DeltaSheet(DataPackage data, String actualSheet, String deltaSheet) {
        this.data = data;
        this.actualSheet = actualSheet;
        this.deltaSheet = deltaSheet;
    }

    @Override
    public void apply() {
        Sheet actual = data.getSheet(actualSheet);
        Sheet delta = data.getSheet(deltaSheet);

        for (String key : values.keySet()) {
            String newValue = values.get(key);
            // A value seen for the first time is where it starts, not a change from 0: joining a world with full
            // air otherwise reads as breathing again, and soundpacks play the out-of-water splash.
            String previousValue = actual.exists(key) ? actual.get(key) : newValue;

            actual.set(key, newValue);
            delta.set(key, computeDelta(previousValue, newValue));
        }

        clear();
    }

    protected String computeDelta(String previousValue, String newValue) {
        Long newLong = Numbers.toLong(newValue);
        
        if(newLong != null) {
            if(newValue.equals(previousValue)) {
                return "0";
            } else {
                Long previousLong = Numbers.toLong(previousValue);

                if (previousLong != null) {
                    return Long.toString(newLong - previousLong);
                }
            }
        }

        return newValue.equals(previousValue) ? "NOT_MODIFIED" : "MODIFIED";
    }

}

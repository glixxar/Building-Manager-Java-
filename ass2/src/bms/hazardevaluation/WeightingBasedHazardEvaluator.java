package bms.hazardevaluation;

import bms.sensors.HazardSensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Evaluates the hazard level of a location
 * using weightings for the sensor values.
 *
 * The sum of the weightings of all sensors
 * must equal 100.
 *
 * @ass2
 */
public class WeightingBasedHazardEvaluator
        implements HazardEvaluator {

    /**
     * Mapping of sensors to their respective weightings
     */
    private Map<HazardSensor, Integer> sensorWeights;

    /**
     * Creates a new weighting-based hazard evaluator
     * with the given sensors and weightings.
     *
     * Each weighting must be between 0 and 100 inclusive,
     * and the total sum of all weightings must equal 100.
     *
     * @param sensors mapping of sensors to their respective weighting
     * @throws IllegalArgumentException weighted average of
     * current sensor hazard levels
     * @ass2
     */
    public WeightingBasedHazardEvaluator(Map<HazardSensor, Integer> sensors)
            throws IllegalArgumentException {
        int totalWeight = 0;
        for (Map.Entry<HazardSensor, Integer> entry : sensors.entrySet()) {
            if (entry.getValue() < 0 || entry.getValue() > 100 ) {
                throw new IllegalArgumentException();
            }
            totalWeight += entry.getValue();
        }
        if (totalWeight > 100 || totalWeight < 100) {
            throw new IllegalArgumentException();
        }
        this.sensorWeights = sensors;

    }

    /**
     * Returns the weighted average of the current hazard
     * levels of all sensors in the map passed to the constructor.
     *
     * The weightings given in the constructor should be used.
     * The final evaluated hazard level should be rounded to
     * the nearest integer between 0 and 100.
     *
     * For example, given the following sensors and weightings, this method should return a value of 28.
     *
     * @return weighted average of current sensor hazard levels
     * @ass2
     */
    public int evaluateHazardLevel() {
        double totalWeight = 0;
        for (Map.Entry<HazardSensor, Integer> entry :
                this.sensorWeights.entrySet()) {
            totalWeight += (entry.getKey().getHazardLevel() * entry.getValue())
                    / 100.0;
        }
        return (int) Math.min(Math.floor(totalWeight /
                this.sensorWeights.size()), 100);
    }

    /**
     * Returns a list containing the weightings associated
     * with all of the sensors monitored by this hazard evaluator.
     *
     * @return weightings
     * @ass2
     */
    public List<Integer> getWeightings() {
        List<Integer> weightList = new ArrayList<>();
        for (Integer i :
                this.sensorWeights.values()) {
            weightList.add(i);
        }
        return weightList;
    }

    /**
     * Returns the string representation of this hazard evaluator.
     *
     * The format of the string to return is simply
     * "WeightingBased" without the double quotes.
     *
     * See the demo save file for an example (uqstlucia.txt).
     *
     * @return string representation of this hazard evaluator
     * @ass2
     */
    @Override
    public String toString() {
        return "WeightingBased";
    }

}

package bms.hazardevaluation;

import bms.sensors.HazardSensor;
import bms.sensors.OccupancySensor;

import java.util.List;

/**
 * Evaluates the hazard level of a
 * location using a rule based system.
 *
 * @ass2
 */
public class RuleBasedHazardEvaluator
        implements HazardEvaluator {

    /**
     * List of Hazard Sensors for RuleBasedHazardEvaluator
     */
    private List<HazardSensor> sensorList;

    /**
     * Creates a new rule-based hazard evaluator with the given list of sensors.
     *
     * @param sensors sensors to be used in the hazard level calculation.
     * @ass2
     */
    public RuleBasedHazardEvaluator(List<HazardSensor> sensors) {
        this.sensorList = sensors;
    }

    /**
     * Returns a calculated hazard level based on applying a set of rules
     * to the list of sensors passed to the constructor.
     * The rules to be applied are as follows.
     * Note that square brackets [] have been used to
     * indicate mathematical grouping.
     *
     * If there are no sensors, return 0.
     *
     * If there is only one sensor, return that sensor's
     * current hazard level as per HazardSensor.getHazardLevel().
     *
     * If there is more than one sensor:
     *
     * If any sensor that is not an OccupancySensor
     * has a hazard level of 100, return 100.
     *
     * Calculate the average hazard level of all sensors that
     * are not an OccupancySensor.
     * Floating point division should be used when finding the average.
     * If there is an OccupancySensor in the list, multiply the average
     * calculated in the previous step by [the occupancy sensor's
     * current hazard level divided by 100, using floating point division].
     * Return the final average rounded to the nearest integer between 0 and 100.
     * You can assume that there is no more than one
     * OccupancySensor in the list passed to the constructor.
     *
     * @return calculated hazard level according to a set of rules
     * @ass2
     */
    public int evaluateHazardLevel() {
        // Checks to see if list of sensors is empty or
        // only contains one hazard sensor
        if (sensorList.isEmpty()) {
            return 0;
        } else if (sensorList.size() == 1) {
            return sensorList.get(0).getHazardLevel();
        }
        double totalHazardLevel = 0;
        int numSensor = 0;
        int sensorPos = 0;
        boolean occSensor = false;
        // Evaluates hazard level according to rules given
        for (HazardSensor sensor : sensorList) {
            if ((sensor.getHazardLevel() >= 100) &&
                    (!(sensor instanceof OccupancySensor)) ) {
                return 100;
            }
            if (sensor instanceof OccupancySensor) {
                occSensor = true;
                sensorPos = sensorList.indexOf(sensor);
            }
            if (!(sensor instanceof OccupancySensor)) {
                totalHazardLevel += sensor.getHazardLevel();
                numSensor += 1;
            }
        }
        totalHazardLevel = totalHazardLevel / numSensor;
        if (occSensor) {
            totalHazardLevel = totalHazardLevel *
                    ((sensorList.get(sensorPos).getHazardLevel())/100.0);
        }
        return (int) Math.min(Math.floor(totalHazardLevel), 100);

    }

    /**
     * Returns the string representation of this
     * hazard evaluator.
     * The format of the string to return is
     * simply "RuleBased" without double quotes.
     *
     * See the demo save file for an example (uqstlucia.txt).
     *
     * @return string representation of this room
     * @ass2
     */
    @Override
    public String toString() {
        return "RuleBased";
    }
}

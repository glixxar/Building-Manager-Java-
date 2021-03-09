package bms.sensors;

/**
 * A sensor that measures levels of carbon dioxide (CO2) in the air, in parts
 * per million (ppm).
 * @ass1
 */
public class CarbonDioxideSensor extends TimedSensor implements HazardSensor {

    /**
     * The ideal value for this sensor, where the comfort level is highest.
     */
    private int idealValue;

    /**
     * The maximum variation that is allowed from the ideal value. The comfort
     * level will be 0 when the value is this far (or further) away from the
     * ideal value.
     */
    private int variationLimit;

    /**
     * Creates a new carbon dioxide sensor with the given sensor readings,
     * update frequency, ideal CO2 value and acceptable variation limit.
     * <p>
     * Different rooms and environments may naturally have different "normal"
     * CO2 concentrations, for example, a large room with many windows may
     * have lower typical CO2 concentrations than a small room with poor
     * airflow.
     * <p>
     * To allow for these discrepancies, each CO2 sensor has an "ideal" CO2
     * concentration and a maximum acceptable variation from this value.
     * Both the ideal value and variation limit must be greater than zero.
     * These two values must be such that (idealValue - variationLimit) &gt;= 0.
     *
     * @param sensorReadings array of CO2 sensor readings <b>in ppm</b>
     * @param updateFrequency indicates how often the sensor readings update,
     *                        in minutes
     * @param idealValue ideal CO2 value in ppm
     * @param variationLimit acceptable range above and below ideal value in ppm
     * @throws IllegalArgumentException if idealValue &lt;= 0;
     * or if variationLimit &lt;= 0; or if (idealValue - variationLimit) &lt; 0
     * @ass1
     */
    public CarbonDioxideSensor(int[] sensorReadings, int updateFrequency,
                               int idealValue, int variationLimit)
            throws IllegalArgumentException {
        super(sensorReadings, updateFrequency);

        if (idealValue <= 0) {
            throw new IllegalArgumentException("Ideal CO2 value must be > 0");
        }
        if (variationLimit <= 0) {
            throw new IllegalArgumentException(
                    "CO2 variation limit must be > 0");
        }

        if (idealValue - variationLimit < 0) {
            throw new IllegalArgumentException("Ideal CO2 value - variation "
                    + "limit must be >= 0");
        }

        this.idealValue = idealValue;
        this.variationLimit = variationLimit;
    }

    /**
     * Returns the sensor's CO2 variation limit.
     *
     * @return variation limit in ppm
     * @ass1
     */
    public int getVariationLimit() {
        return variationLimit;
    }

    /**
     * Returns the sensor's ideal CO2 value.
     *
     * @return ideal value in ppm
     * @ass1
     */
    public int getIdealValue() {
        return idealValue;
    }

    /**
     * Returns the hazard level as detected by this sensor.
     * <p>
     * The returned hazard level is determined by the following table, and is
     * based on the current sensor reading.
     * <table border="1">
     * <caption>CO2 hazard level table</caption>
     * <tr>
     * <th>Current sensor reading</th>
     * <th>Hazard level</th>
     * <th>Associated effect</th>
     * </tr>
     * <tr><td>0-999</td><td>0</td><td>No effects</td></tr>
     * <tr><td>1000-1999</td><td>25</td><td>Drowsiness</td></tr>
     * <tr><td>2000-4999</td><td>50</td>
     * <td>Headaches, sleepiness, loss of concentration</td></tr>
     * <tr><td>5000+</td><td>100</td><td>Oxygen deprivation</td></tr>
     * </table>
     *
     * @return the current hazard level as an integer between 0 and 100
     * @ass1
     */
    @Override
    public int getHazardLevel() {
        final int currentReading = this.getCurrentReading();
        if (currentReading < 1000) {
            return 0;
        }
        if (currentReading < 2000) {
            return 25;
        }
        if (currentReading < 5000) {
            return 50;
        }
        return 100;
    }

    /**
     * Returns the comfort level as detected by this sensor.
     *
     * The comfort level is calculated as the complement of
     * the percentage given by: the (absolute) difference
     * between the ideal CO2 value and the current sensor
     * reading, all divided by getVariationLimit().
     *
     * When the absolute difference between the ideal and the
     * current values is equal to or more than the variation limit,
     * the comfort is equal to zero percent (0).
     *
     * For example:
     *
     * a sensor with an ideal CO2 value of 600ppm,
     * a variation limit of 200ppm, and
     * a current sensor reading of 640ppm would have a comfort level of 80
     *
     * a sensor with an ideal CO2 value of 500ppm,
     * a variation limit of 100ppm, and
     * a current sensor reading of 420ppm would have a comfort level of 20
     *
     * a sensor with an ideal CO2 value of 800ppm,
     * a variation limit of 100ppm, and
     * a current sensor reading of 1000ppm would have a comfort level of 0
     *
     * Floating point division should be used when performing the calculation,
     * however the resulting floating point number should be rounded
     * to the nearest integer before being returned.
     *
     * @return the current comfort level as an integer between 0 and 100
     * @ass2
     */
    public int getComfortLevel() {
        double diffReading = Math.abs(this.idealValue - getCurrentReading());
        double varLimit = getVariationLimit();
        if (diffReading >= varLimit) {
            return 0;
        }
        double pctReading = (diffReading / varLimit) * 100;
        return (int) Math.round(100 - pctReading);
    }

    /**
     * Returns true if and only if this CO2 sensor
     * is equal to the other given sensor.
     *
     * For two CO2 sensors to be equal,
     * they must have the same:
     *
     * update frequency
     * sensor readings array (in the same order)
     * ideal CO2 value
     * CO2 variation limit
     *
     * @param obj other object to compare equality
     * @return true if equal, false otherwise
     * @ass2
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } if (!(obj instanceof CarbonDioxideSensor)) {
            return false;
        }
        CarbonDioxideSensor carbonDioxideSensor = (CarbonDioxideSensor) obj;
        return super.equals(obj) &&
                carbonDioxideSensor.getIdealValue() == this.idealValue &&
                carbonDioxideSensor.getVariationLimit() == this.variationLimit;

    }

    /**
     * Returns the hash code of this CO2 sensor.
     *
     * Two CO2 sensors that are equal according
     * to equals(Object) should have the same hash code.
     *
     * @return hash code of this sensor
     * @ass2
     */
    @Override
    public int hashCode() {
        return super.hashCode() * idealValue * variationLimit;
    }

    /**
     * Returns the human-readable string representation of this CO2 sensor.
     * <p>
     * The format of the string to return is
     * "TimedSensor: freq='updateFrequency', readings='sensorReadings',
     * type=CarbonDioxideSensor, idealPPM='idealValue',
     * varLimit='variationLimit'"
     * without the single quotes, where 'updateFrequency' is this sensor's
     * update frequency (in minutes), 'sensorReadings' is a comma-separated
     * list of this sensor's readings, 'idealValue' is this sensor's ideal CO2
     * concentration, and 'variationLimit' is this sensor's variation limit.
     * <p>
     * For example: "TimedSensor: freq=5, readings=702,694,655,680,711,
     * type=CarbonDioxideSensor, idealPPM=600, varLimit=250"
     *
     * @return string representation of this sensor
     * @ass1
     */
    @Override
    public String toString() {
        return String.format(
                "%s, type=CarbonDioxideSensor, idealPPM=%d, varLimit=%d",
                super.toString(),
                this.idealValue,
                this.variationLimit);
    }

    /**
     * Returns the machine-readable string
     * representation of this carbon dioxide sensor.
     *
     * The format of the string to return is:
     *
     *  CarbonDioxideSensor:sensorReading1,
     *  sensorReading2,...,sensorReadingN:frequency:idealValue:varLimit
     *
     * where 'sensorReadingX' is the Xth sensor reading in
     * this sensor's list of readings, from 1 to N where
     * N is the number of readings, 'frequency' is this
     * sensor's update frequency (in minutes), 'idealValue'
     * is this sensor's ideal CO2 concentration, and 'varLimit'
     * is this sensor's variation limit.
     *
     * There should be no newline at the end of the string.
     *
     * See the demo save file for an example (uqstlucia.txt).
     *
     * @return encoded string representation of this carbon dioxide sensor
     * @ass2
     */
    public String encode() {
        return String.format("CarbonDioxideSensor:%s:%d:%d:%d",
                super.encode(),
                this.getUpdateFrequency(),
                this.idealValue,
                this.variationLimit);
    }
}

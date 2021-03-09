package bms.sensors;

/**
 * A sensor that measures the noise levels in a room.
 * @ass1
 */
public class NoiseSensor extends TimedSensor implements HazardSensor, ComfortSensor {

    /**
     * Threshold sound level for evaluating hazard and comfort levels.
     * <p>
     * Approximately represents the noise level generated by loud conversation.
     */
    private static final int referenceDB = 70;

    /**
     * Creates a new noise sensor with the given sensor readings and update
     * frequency.
     *
     * @param sensorReadings array of noise sensor readings <b>in decibels</b>
     * @param updateFrequency indicates how often the sensor readings update,
     *                        in minutes
     * @ass1
     */
    public NoiseSensor(int[] sensorReadings, int updateFrequency) {
        super(sensorReadings, updateFrequency);
    }

    /**
     * Calculates the relative loudness level compared to a reference of 70.0
     * decibels.
     * <p>
     * The loudness of sounds in comparison to 70.0 decibels is given by the
     * formula:
     * <p>
     * 2^((measured volume - 70.0)/10.0)
     * <p>
     * For example, a sound reading of 67 decibels would have a relative
     * loudness of 0.8123. A Sound reading of 82 decibels would have a relative
     * loudness of 2.2974.
     * <p>
     * Refer to:
     * http://www.sengpielaudio.com/calculator-levelchange.htm
     * https://www.iacacoustics.com/blog-full/comparative-examples-of-noise-levels.html
     * https://www.safeworkaustralia.gov.au/noise
     *
     * @return relative loudness of current reading to 70dB
     * @ass1
     */
    public double calculateRelativeLoudness() {
        return Math.pow(2, (this.getCurrentReading() - referenceDB) / 10.0);
    }

    /**
     * Returns the current hazard level observed by the sensor, based on the
     * current loudness reading.
     * <p>
     * Retrieves the relative loudness using
     * {@link #calculateRelativeLoudness()}, multiplies the result by 100
     * (floating point multiplication should be used), then rounds <b>down</b>
     * to the largest integer that is less than or equal to the calculated
     * value.
     * <p>
     * If the result is &gt; 100, 100 is returned.
     * Otherwise, the result is returned.
     * <p>
     * For example, if {@link #calculateRelativeLoudness()} returns 0.8968 then
     * 89 must be returned. If {@link #calculateRelativeLoudness()} returns
     * 1.7646 then 100 must be returned.
     * @ass1
     */
    @Override
    public int getHazardLevel() {
        double relativeLoudnessMult100 = this.calculateRelativeLoudness() * 100;
        return (int) Math.min(Math.floor(relativeLoudnessMult100), 100);
    }

    /**
     * Returns the current comfort level observed by the sensor,
     * based on the current loudness reading.
     *
     * Subtracts the relative loudness returned by
     * calculateRelativeLoudness() from 1, multiplies the
     * result by 100 (floating point multiplication should
     * be used), then rounds down to the largest integer
     * that is less than or equal to the calculated value.
     *
     * If the result is < 0, 0 is returned. Otherwise, the result is returned.
     *
     * For example, if calculateRelativeLoudness() returns 0.8968 then
     * 10 must be returned. If calculateRelativeLoudness() returns 1.7646
     * then 0 must be returned.
     *
     * @return level of comfort at sensor location, 0 to 100
     * @ass2
     */
    public int getComfortLevel() {
        double comfortLevel = (1 - this.calculateRelativeLoudness());
        if (comfortLevel < 0) {
            return 0;
        } else {
            return (int) Math.min(Math.floor(comfortLevel * 100), 100);
        }
    }


    /**
     * Returns the human-readable string representation of this noise
     * sensor.
     * <p>
     * The format of the string to return is
     * "TimedSensor: freq='updateFrequency', readings='sensorReadings',
     * type=NoiseSensor"
     * without the single quotes, where 'updateFrequency' is this sensor's
     * update frequency (in minutes) and 'sensorReadings' is a comma-separated
     * list of this sensor's readings.
     * <p>
     * For example: "TimedSensor: freq=3, readings=55,57,50,52,61,64,58,
     * type=NoiseSensor"
     *
     * @return string representation of this sensor
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s, type=NoiseSensor", super.toString());
    }

    /**
     * Returns the machine-readable string representation of this noise sensor.
     * The format of the string to return is:
     *
     *  NoiseSensor:sensorReading1,sensorReading2,...,sensorReadingN:frequency
     *
     * where 'sensorReadingX' is the Xth sensor reading in this sensor's list of
     * readings, from 1 to N where N is the number of readings, and
     * 'frequency' is this sensor's update frequency (in minutes).
     *
     * There should be no newline at the end of the string.
     *
     * See the demo save file for an example (uqstlucia.txt).
     *
     * @return encoded string representation of this noise sensor
     * @ass2
     */
    public String encode() {
        return String.format("NoiseSensor:%s    :%d",
                super.encode(),
                this.getUpdateFrequency());
    }
}
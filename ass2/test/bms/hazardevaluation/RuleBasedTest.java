package bms.hazardevaluation;

import bms.sensors.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RuleBasedTest {

    private List<HazardSensor> sensorList;

    @Test
    public void evalHazard100() {
        sensorList = new ArrayList<>();
        HazardSensor noiseSensor = new NoiseSensor(new int[]{52,42,53,56},2);
        HazardSensor tempSensor = new TemperatureSensor(new int[]{152,142,153,156});
        HazardSensor occuSensor = new OccupancySensor(new int[]{10,20},1,30);
        HazardSensor carbonSensor = new CarbonDioxideSensor(new int[]{690,740}, 5, 700,150);
        sensorList.add(noiseSensor);
        sensorList.add(tempSensor);
        sensorList.add(occuSensor);
        sensorList.add(carbonSensor);
        HazardEvaluator hh = new RuleBasedHazardEvaluator(sensorList);

        int hazardLevel = (int) Math.min(Math.floor(((noiseSensor.getHazardLevel() +
                tempSensor.getHazardLevel() +
                carbonSensor.getHazardLevel())/3.0) * occuSensor.getHazardLevel()/100.0), 100);
        Assert.assertEquals(hh.evaluateHazardLevel(), 100);

    }

    @Test
    public void evalHazardOneOnly() {
        sensorList = new ArrayList<>();
        HazardSensor noiseSensor = new NoiseSensor(new int[]{52,42,53,56},2);
        HazardSensor tempSensor = new TemperatureSensor(new int[]{152,142,153,156});
        HazardSensor occuSensor = new OccupancySensor(new int[]{10,20},1,30);
        HazardSensor carbonSensor = new CarbonDioxideSensor(new int[]{690,740}, 5, 700,150);
        sensorList.add(noiseSensor);
        HazardEvaluator hh = new RuleBasedHazardEvaluator(sensorList);

        int hazardLevel = (int) Math.min(Math.floor(((noiseSensor.getHazardLevel() +
                tempSensor.getHazardLevel() +
                carbonSensor.getHazardLevel())/3.0) * occuSensor.getHazardLevel()/100.0), 100);
        Assert.assertEquals(hh.evaluateHazardLevel(), noiseSensor.getHazardLevel());

    }

    @Test
    public void evalHazardNone() {
        sensorList = new ArrayList<>();
        HazardSensor noiseSensor = new NoiseSensor(new int[]{52,42,53,56},2);
        HazardSensor tempSensor = new TemperatureSensor(new int[]{152,142,153,156});
        HazardSensor occuSensor = new OccupancySensor(new int[]{10,20},1,30);
        HazardSensor carbonSensor = new CarbonDioxideSensor(new int[]{690,740}, 5, 700,150);
        HazardEvaluator hh = new RuleBasedHazardEvaluator(sensorList);

        int hazardLevel = (int) Math.min(Math.floor(((noiseSensor.getHazardLevel() +
                tempSensor.getHazardLevel() +
                carbonSensor.getHazardLevel())/3.0) * occuSensor.getHazardLevel()/100.0), 100);
        Assert.assertEquals(hh.evaluateHazardLevel(), 0);

    }

    @Test
    public void evalHazardOccuSensor() {
        sensorList = new ArrayList<>();
        HazardSensor noiseSensor = new NoiseSensor(new int[]{52,42,53,56},2);
        HazardSensor tempSensor = new TemperatureSensor(new int[]{52,42,53,56});
        HazardSensor occuSensor = new OccupancySensor(new int[]{10,20},1,30);
        HazardSensor carbonSensor = new CarbonDioxideSensor(new int[]{690,740}, 5, 700,150);
        sensorList.add(noiseSensor);
        sensorList.add(tempSensor);
        sensorList.add(occuSensor);
        sensorList.add(carbonSensor);
        HazardEvaluator hh = new RuleBasedHazardEvaluator(sensorList);
        int hazardLevel = (int) Math.min(Math.floor(((noiseSensor.getHazardLevel() +
                tempSensor.getHazardLevel() +
                carbonSensor.getHazardLevel())/3.0) * occuSensor.getHazardLevel()/100.0), 100);
        Assert.assertEquals(hh.evaluateHazardLevel(), hazardLevel);

    }

    @Test
    public void evalHazardNoOccuSensor() {
        sensorList = new ArrayList<>();
        HazardSensor noiseSensor = new NoiseSensor(new int[]{52,42,53,56},2);
        HazardSensor tempSensor = new TemperatureSensor(new int[]{52,42,53,56});
        HazardSensor occuSensor = new OccupancySensor(new int[]{10,20},1,30);
        HazardSensor carbonSensor = new CarbonDioxideSensor(new int[]{690,740}, 5, 700,150);
        sensorList.add(noiseSensor);
        sensorList.add(tempSensor);
        sensorList.add(carbonSensor);
        HazardEvaluator hh = new RuleBasedHazardEvaluator(sensorList);
        int hazardLevel = (int) Math.min(Math.floor(((noiseSensor.getHazardLevel() +
                tempSensor.getHazardLevel() +
                carbonSensor.getHazardLevel())/3.0)), 100);
        Assert.assertEquals(hh.evaluateHazardLevel(), hazardLevel);

    }
}

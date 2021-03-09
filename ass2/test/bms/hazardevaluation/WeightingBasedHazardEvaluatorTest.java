package bms.hazardevaluation;

import bms.exceptions.FileFormatException;
import bms.sensors.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class WeightingBasedHazardEvaluatorTest {

    Map<HazardSensor, Integer> map;

    @Test
    public void correctWeights() throws IOException, FileFormatException {
        map = new HashMap<>();
        HazardSensor noiseSensor = new NoiseSensor(new int[]{52,42,53,56},2);
        HazardSensor tempSensor = new TemperatureSensor(new int[]{52,42,53,56});
        map.put(tempSensor, 25);
        map.put(noiseSensor,75);
        HazardEvaluator hh = new WeightingBasedHazardEvaluator(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void incorrectWeightsMoreThan100Weight() throws IOException, FileFormatException {
        map = new HashMap<>();
        HazardSensor noiseSensor = new NoiseSensor(new int[]{52,42,53,56},2);
        HazardSensor tempSensor = new TemperatureSensor(new int[]{52,42,53,56});
        map.put(tempSensor, 45);
        map.put(noiseSensor,75);
        HazardEvaluator hh = new WeightingBasedHazardEvaluator(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void incorrectWeightsLessThan100Weight() throws IOException, FileFormatException {
        map = new HashMap<>();
        HazardSensor noiseSensor = new NoiseSensor(new int[]{52,42,53,56},2);
        HazardSensor tempSensor = new TemperatureSensor(new int[]{52,42,53,56});
        map.put(tempSensor, 15);
        map.put(noiseSensor,75);
        HazardEvaluator hh = new WeightingBasedHazardEvaluator(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void incorrectWeightsKeyLessThan0() throws IOException, FileFormatException {
        map = new HashMap<>();
        HazardSensor noiseSensor = new NoiseSensor(new int[]{52,42,53,56},2);
        HazardSensor tempSensor = new TemperatureSensor(new int[]{52,42,53,56});
        map.put(tempSensor, -5);
        map.put(noiseSensor,75);
        HazardEvaluator hh = new WeightingBasedHazardEvaluator(map);
    }

    @Test
    public void getWeights() throws IOException, FileFormatException {
        map = new LinkedHashMap<>();
        HazardSensor noiseSensor = new NoiseSensor(new int[]{52,42,53,56},2);
        HazardSensor tempSensor = new TemperatureSensor(new int[]{52,42,53,56});
        HazardSensor occuSensor = new OccupancySensor(new int[]{10,20},1,30);
        map.put(noiseSensor,75);
        map.put(tempSensor, 15);
        map.put(occuSensor, 10);
        HazardEvaluator hh = new WeightingBasedHazardEvaluator(map);
        List<Integer> testWeight = new ArrayList<>();
        testWeight.add(75);
        testWeight.add(15);
        testWeight.add(10);
        Assert.assertEquals(((WeightingBasedHazardEvaluator) hh).getWeightings(), testWeight);
    }

    @Test
    public void evalHazard() {
        map = new LinkedHashMap<>();
        HazardSensor noiseSensor = new NoiseSensor(new int[]{52,42,53,56},2);
        HazardSensor tempSensor = new TemperatureSensor(new int[]{52,42,53,56});
        HazardSensor occuSensor = new OccupancySensor(new int[]{10,20},1,30);
        map.put(noiseSensor, 20);
        map.put(tempSensor, 50);
        map.put(occuSensor, 30);
        HazardEvaluator hh = new WeightingBasedHazardEvaluator(map);
        int hazardLevel = (int) Math.min(Math.floor((noiseSensor.getHazardLevel() * 0.2) +
                (tempSensor.getHazardLevel() * 0.5) +
                (occuSensor.getHazardLevel() * 0.3))/3, 100);
        Assert.assertEquals(((WeightingBasedHazardEvaluator) hh).evaluateHazardLevel(), hazardLevel);
    }

    @Test
    public void evalHazard2() {
        map = new LinkedHashMap<>();
        HazardSensor noiseSensor = new NoiseSensor(new int[]{52,42,53,56},2);
        HazardSensor tempSensor = new TemperatureSensor(new int[]{52,42,53,56});
        HazardSensor occuSensor = new OccupancySensor(new int[]{10,20},1,30);
        HazardSensor carbonSensor = new CarbonDioxideSensor(new int[]{690,740}, 5, 700,150);
        map.put(noiseSensor, 20);
        map.put(tempSensor, 50);
        map.put(occuSensor, 20);
        map.put(carbonSensor, 10);
        HazardEvaluator hh = new WeightingBasedHazardEvaluator(map);
        int hazardLevel = (int) Math.min(Math.floor((noiseSensor.getHazardLevel() * 0.2) +
                (tempSensor.getHazardLevel() * 0.5) +
                (occuSensor.getHazardLevel() * 0.2) +
                (tempSensor.getHazardLevel() * 0.1))/4, 100);
        Assert.assertEquals(((WeightingBasedHazardEvaluator) hh).evaluateHazardLevel(), hazardLevel);
    }

}

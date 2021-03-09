package bms.building;

import bms.exceptions.FileFormatException;
import bms.floor.MaintenanceSchedule;
import bms.hazardevaluation.WeightingBasedHazardEvaluator;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.CarbonDioxideSensor;
import bms.sensors.NoiseSensor;
import bms.sensors.OccupancySensor;
import bms.sensors.TemperatureSensor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuildingInitialiserTest {

    List<Building> buildingList;
    List<Building> testBuildings;
    Building general;
    Building forgan;
    Building andrew;

    @Before
    public void runTestFile() throws IOException, FileFormatException {
        buildingList = BuildingInitialiser.loadBuildings("saves/uqstlucia.txt");
        general = buildingList.get(0);
        forgan = buildingList.get(1);
        andrew = buildingList.get(2);
    }

    @Test
    public void testGeneralFloor1() throws IOException, FileFormatException {
        Assert.assertEquals(general.getName(), "General Purpose South");
        Assert.assertEquals(general.getFloors().size(), 5);
        Assert.assertEquals(general.getFloorByNumber(1).getLength(), 10, 0.1);
        Assert.assertEquals(general.getFloorByNumber(1).getWidth(), 10,0.1);
        Assert.assertEquals(general.getFloorByNumber(1).getRooms().size(), 4);

        Assert.assertEquals(general.getFloorByNumber(1).getRoomByNumber(101).getArea() ,20,0.1);
        Assert.assertEquals(general.getFloorByNumber(1).getRoomByNumber(101).getType() , RoomType.STUDY);
        Assert.assertEquals(general.getFloorByNumber(1).getRoomByNumber(101).getSensors().size() , 0);

        Assert.assertEquals(general.getFloorByNumber(1).getRoomByNumber(102).getArea() ,20,0.1);
        Assert.assertEquals(general.getFloorByNumber(1).getRoomByNumber(102).getType() , RoomType.STUDY);
        Assert.assertEquals(general.getFloorByNumber(1).getRoomByNumber(102).getSensors().size() , 1);
        Assert.assertTrue(general.getFloorByNumber
                (1).getRoomByNumber(102).getSensor
                ("OccupancySensor").equals(new OccupancySensor(new int[]{13,24,28,15,6},4,30)));

        Assert.assertEquals(general.getFloorByNumber(1).getRoomByNumber(103).getArea() ,15,0.1);
        Assert.assertEquals(general.getFloorByNumber(1).getRoomByNumber(103).getType() , RoomType.STUDY);
        Assert.assertEquals(general.getFloorByNumber(1).getRoomByNumber(103).getSensors().size() , 0);

        Assert.assertEquals(general.getFloorByNumber(1).getRoomByNumber(104).getArea() ,45,0.1);
        Assert.assertEquals(general.getFloorByNumber(1).getRoomByNumber(104).getType() , RoomType.LABORATORY);
        Assert.assertEquals(general.getFloorByNumber(1).getRoomByNumber(104).getSensors().size() , 1);
        Assert.assertTrue(general.getFloorByNumber
                (1).getRoomByNumber(104).getSensor
                ("CarbonDioxideSensor").equals(new CarbonDioxideSensor(new int[]{690,740},5,700,150)));

        List<Room> mainRoom = new ArrayList<>();
        mainRoom.add(general.getFloorByNumber(1).getRoomByNumber(101));
        mainRoom.add(general.getFloorByNumber(1).getRoomByNumber(103));
        mainRoom.add(general.getFloorByNumber(1).getRoomByNumber(102));
        mainRoom.add(general.getFloorByNumber(1).getRoomByNumber(104));
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(mainRoom);
        Assert.assertEquals(general.getFloorByNumber(1).getMaintenanceSchedule().encode(),  maintenanceSchedule.encode());
    }

    @Test
    public void testGeneralFloor2() {
        Assert.assertEquals(general.getFloorByNumber(2).getLength(), 10, 0.1);
        Assert.assertEquals(general.getFloorByNumber(2).getWidth(), 10,0.1);
        Assert.assertEquals(general.getFloorByNumber(2).getRooms().size(), 3);

        Assert.assertEquals(general.getFloorByNumber(2).getRoomByNumber(201).getArea() ,50,0.1);
        Assert.assertEquals(general.getFloorByNumber(2).getRoomByNumber(201).getType() , RoomType.OFFICE);
        Assert.assertEquals(general.getFloorByNumber(2).getRoomByNumber(201).getSensors().size() , 2);

        Assert.assertTrue(general.getFloorByNumber
                (2).getRoomByNumber(201).getSensor
                ("NoiseSensor").equals(new NoiseSensor(new int[]{55,62,69,63},3)));

        Assert.assertTrue(general.getFloorByNumber
                (2).getRoomByNumber(201).getSensor
                ("OccupancySensor").equals(new OccupancySensor(new int[]{32,35,26,4,3,2,6,16,17,22,28,29},2,40)));
        Assert.assertEquals(general.getFloorByNumber(2).getRoomByNumber(201).getHazardEvaluator().toString(), "RuleBased");

        Assert.assertEquals(general.getFloorByNumber(2).getRoomByNumber(202).getArea() ,30,0.1);
        Assert.assertEquals(general.getFloorByNumber(2).getRoomByNumber(202).getType() , RoomType.OFFICE);
        Assert.assertEquals(general.getFloorByNumber(2).getRoomByNumber(202).getSensors().size() , 0);

        Assert.assertEquals(general.getFloorByNumber(2).getRoomByNumber(203).getArea() ,10,0.1);
        Assert.assertEquals(general.getFloorByNumber(2).getRoomByNumber(203).getType() , RoomType.STUDY);
        Assert.assertEquals(general.getFloorByNumber(2).getRoomByNumber(203).getSensors().size() , 1);
        Assert.assertTrue(general.getFloorByNumber
                (2).getRoomByNumber(203).getSensor
                ("TemperatureSensor").equals(new TemperatureSensor(new int[]{28,29,26,24,25,26})));


    }

    @Test
    public void testFloor3() {
        Assert.assertEquals(general.getFloorByNumber(3).getLength(), 8, 0.1);
        Assert.assertEquals(general.getFloorByNumber(3).getWidth(), 10,0.1);
        Assert.assertEquals(general.getFloorByNumber(3).getRooms().size(), 3);

        Assert.assertEquals(general.getFloorByNumber(3).getRoomByNumber(301).getArea() ,30,0.1);
        Assert.assertEquals(general.getFloorByNumber(3).getRoomByNumber(301).getType() , RoomType.STUDY);
        Assert.assertEquals(general.getFloorByNumber(3).getRoomByNumber(301).getSensors().size() , 1);

        Assert.assertTrue(general.getFloorByNumber
                (3).getRoomByNumber(301).getSensor
                ("OccupancySensor").equals(new OccupancySensor(new int[]{15,17,12,8,11},4,30)));

        Assert.assertEquals(general.getFloorByNumber(3).getRoomByNumber(302).getArea() ,25,0.1);
        Assert.assertEquals(general.getFloorByNumber(3).getRoomByNumber(302).getType() , RoomType.LABORATORY);
        Assert.assertEquals(general.getFloorByNumber(3).getRoomByNumber(302).getSensors().size() , 1);

        Assert.assertTrue(general.getFloorByNumber
                (3).getRoomByNumber(302).getSensor
                ("TemperatureSensor").equals(new TemperatureSensor(new int[]{25,26,24})));
        Assert.assertEquals(general.getFloorByNumber(3).getRoomByNumber(302).getHazardEvaluator().toString(), "RuleBased");


        Assert.assertEquals(general.getFloorByNumber(3).getRoomByNumber(303).getArea() ,25,0.1);
        Assert.assertEquals(general.getFloorByNumber(3).getRoomByNumber(303).getType() , RoomType.LABORATORY);
        Assert.assertEquals(general.getFloorByNumber(3).getRoomByNumber(303).getSensors().size() , 1);

        Assert.assertTrue(general.getFloorByNumber
                (3).getRoomByNumber(303).getSensor
                ("TemperatureSensor").equals(new TemperatureSensor(new int[]{24,21})));

    }

    @Test
    public void testFloor4() {
        Assert.assertEquals(general.getFloorByNumber(4).getLength(), 5, 0.1);
        Assert.assertEquals(general.getFloorByNumber(4).getWidth(), 10,0.1);
        Assert.assertEquals(general.getFloorByNumber(4).getRooms().size(), 3);

        List<Room> mainRoom = new ArrayList<>();
        mainRoom.add(general.getFloorByNumber(4).getRoomByNumber(403));
        mainRoom.add(general.getFloorByNumber(4).getRoomByNumber(402));
        mainRoom.add(general.getFloorByNumber(4).getRoomByNumber(401));
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(mainRoom);
        Assert.assertEquals(general.getFloorByNumber(4).getMaintenanceSchedule().encode(),  maintenanceSchedule.encode());

        Assert.assertEquals(general.getFloorByNumber(4).getRoomByNumber(401).getArea() ,20,0.1);
        Assert.assertEquals(general.getFloorByNumber(4).getRoomByNumber(401).getType() , RoomType.OFFICE);
        Assert.assertEquals(general.getFloorByNumber(4).getRoomByNumber(401).getSensors().size() , 0);

        Assert.assertEquals(general.getFloorByNumber(4).getRoomByNumber(402).getArea() ,10,0.1);
        Assert.assertEquals(general.getFloorByNumber(4).getRoomByNumber(402).getType() , RoomType.OFFICE);
        Assert.assertEquals(general.getFloorByNumber(4).getRoomByNumber(402).getSensors().size() , 0);

        Assert.assertEquals(general.getFloorByNumber(4).getRoomByNumber(403).getArea() ,10,0.1);
        Assert.assertEquals(general.getFloorByNumber(4).getRoomByNumber(403).getType() , RoomType.OFFICE);
        Assert.assertEquals(general.getFloorByNumber(4).getRoomByNumber(403).getSensors().size() , 0);
    }

    @Test
    public void testFloor5() {
        Assert.assertEquals(general.getFloorByNumber(5).getLength(), 5, 0.1);
        Assert.assertEquals(general.getFloorByNumber(5).getWidth(), 8,0.1);
        Assert.assertEquals(general.getFloorByNumber(5).getRooms().size(), 1);

        Assert.assertEquals(general.getFloorByNumber(5).getRoomByNumber(501).getArea() ,30,0.1);
        Assert.assertEquals(general.getFloorByNumber(5).getRoomByNumber(501).getType() , RoomType.LABORATORY);
        Assert.assertEquals(general.getFloorByNumber(5).getRoomByNumber(501).getSensors().size() , 2);

        Assert.assertTrue(general.getFloorByNumber
                (5).getRoomByNumber(501).getSensor
                ("TemperatureSensor").equals(new TemperatureSensor(new int[]{25,34,61,85})));

        Assert.assertTrue(general.getFloorByNumber
                (5).getRoomByNumber(501).getSensor
                ("OccupancySensor").equals(new OccupancySensor(new int[]{15,12,2,0},1,20)));

        List<Integer> weightList = new ArrayList<>();
        weightList.add(25);
        weightList.add(75);
        Assert.assertTrue(((WeightingBasedHazardEvaluator) general.getFloorByNumber
                (5).getRoomByNumber(501).getHazardEvaluator()).getWeightings().equals(weightList) );
        Assert.assertEquals(((WeightingBasedHazardEvaluator) general.getFloorByNumber
                (5).getRoomByNumber(501).getHazardEvaluator()).evaluateHazardLevel(), 9);
        Assert.assertEquals(((WeightingBasedHazardEvaluator) general.getFloorByNumber
                (5).getRoomByNumber(501).getHazardEvaluator()).toString(), "WeightingBased");
    }

    @Test
    public void testForgan() {
        Assert.assertEquals(forgan.getName(), "Forgan Smith Building");
        Assert.assertEquals(forgan.getFloors().size(), 1);

        Assert.assertEquals(forgan.getFloorByNumber(1).getLength(), 40, 0.1);
        Assert.assertEquals(forgan.getFloorByNumber(1).getWidth(), 8.5,0.1);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRooms().size(), 10);

        List<Room> mainRoom = new ArrayList<>();
        mainRoom.add(forgan.getFloorByNumber(1).getRoomByNumber(104));
        mainRoom.add(forgan.getFloorByNumber(1).getRoomByNumber(107));
        mainRoom.add(forgan.getFloorByNumber(1).getRoomByNumber(109));
        mainRoom.add(forgan.getFloorByNumber(1).getRoomByNumber(107));
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(mainRoom);
        Assert.assertEquals(forgan.getFloorByNumber(1).getMaintenanceSchedule().encode(),  maintenanceSchedule.encode());

        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(101).getArea() ,23.8,0.1);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(101).getType() , RoomType.STUDY);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(101).getSensors().size() , 0);


        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(102).getArea() ,20,0.1);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(102).getType() , RoomType.STUDY);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(102).getSensors().size() , 0);

        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(103).getArea() ,28.5,0.1);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(103).getType() , RoomType.STUDY);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(103).getSensors().size() , 1);

        Assert.assertTrue(forgan.getFloorByNumber
                (1).getRoomByNumber(103).getSensor
                ("NoiseSensor").equals(new NoiseSensor(new int[]{52,42,53,56},2)));

        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(104).getArea() ,35,0.1);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(104).getType() , RoomType.OFFICE);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(104).getSensors().size() , 0);

        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(105).getArea() ,20,0.1);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(105).getType() , RoomType.STUDY);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(105).getSensors().size() , 0);

        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(106).getArea() ,25.5,0.1);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(106).getType() , RoomType.STUDY);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(106).getSensors().size() , 0);

        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(107).getArea() ,40,0.1);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(107).getType() , RoomType.OFFICE);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(107).getSensors().size() , 2);

        Assert.assertTrue(forgan.getFloorByNumber
                (1).getRoomByNumber(107).getSensor
                ("CarbonDioxideSensor").equals(new CarbonDioxideSensor(new int[]{745,1320,2782,3216,5043,3528,1970},3,700,300)));

        Assert.assertTrue(forgan.getFloorByNumber
                (1).getRoomByNumber(107).getSensor
                ("OccupancySensor").equals(new OccupancySensor(new int[]{11,13,13,13,10},3,20)));
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(107).getHazardEvaluator().toString(), "RuleBased");

        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(108).getArea() ,20,0.1);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(108).getType() , RoomType.STUDY);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(108).getSensors().size() , 0);

        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(109).getArea() ,21.2,0.1);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(109).getType() , RoomType.STUDY);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(109).getSensors().size() , 0);

        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(110).getArea() ,20,0.1);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(110).getType() , RoomType.STUDY);
        Assert.assertEquals(forgan.getFloorByNumber(1).getRoomByNumber(110).getSensors().size() , 0);
    }

    @Test
    public void testAndrew() {
        Assert.assertEquals(andrew.getName(), "Andrew N. Liveris Building");
        Assert.assertEquals(andrew.getFloors().size(), 1);

        Assert.assertEquals(andrew.getFloorByNumber(1).getLength(), 30, 0.1);
        Assert.assertEquals(andrew.getFloorByNumber(1).getWidth(), 15,0.1);
        Assert.assertEquals(andrew.getFloorByNumber(1).getRooms().size(), 0);
    }

    @Test(expected = FileFormatException.class)
    public void testFloorNotEqualRead() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/FloorNotEqual.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testRoomFormatTooFewSemiColon() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/WrongRoomFormatTooFew.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testRoomFormatTooManySemiColon() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/WrongRoomFormatTooMany.txt");
    }


    @Test(expected = FileFormatException.class)
    public void testRoomNotEqualRead() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/RoomNotEqual.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testSensorNotEqualRead() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/SensorNotEqual.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testWrongMaintenanceScheduleRoom() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/WrongMaintRoom.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testWrongMaintenanceScheduleNull() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/WrongMaintRoom.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testWrongMaintenanceScheduleOnlyOne() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/WrongMaintLessOne.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testWrongMaintenanceScheduleConsecutive() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/WrongMaintRoomConsecutive.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testWrongMaintenanceScheduleConsecutiveEnd() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/WrongMaintRoomConsecutiveEnd.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testWrongMaintenanceFormat() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/WrongMaintRoomFormat.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testWrongRoomFormatTooFew() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/WrongRoomFormatTooFew.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testWrongRoomFormatTooMany() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/WrongRoomFormatTooMany.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testDuplicateFloorNumber() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/DuplicateFloor.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testFloorLessThanMinimumWidth() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/FloorSmallerThanWidth.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testFloorLessThanMinimumLength() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/FloorSmallerThanLength.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testNoFloorBelow() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/NoFloorBelow.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testFloorTooBig() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/FloorTooBig.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testDuplicateRoom() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/DuplicateRoom.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testInsufficientRoom() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/InsufficientRoom.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testWrongRoomType() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/WrongRoomType.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testRoomAreaLessThan() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/RoomAreaLessThan.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testRoomInvalidHazardEvaluator() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/RoomInvalidHazardEvaluator.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testRoomInvalidWeightEvaluatorWeights() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/RoomInvalidWeightEvaluatorWeights.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testRoomDuplicateSensors() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/RoomDuplicateSensors.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testRoomWrongSensorType() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/RoomWrongSensorType.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testRoomWrongUpdateFrequency() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/RoomWrongUpdateFrequency.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testCarbonDioxideWrongVariationLimit() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/CarbonDioxideWrongVariationLimit.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testLessThanZeroFloor() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/LessThanZeroFloor.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testLessThanZeroRooms() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/testLessThanZeroRooms.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testLessThanZeroSensors() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/testLessThanZeroSensors.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testNegativeSensorReadings() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/NegativeSensorReadings.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testNegativeTemperatureReadings() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/NegativeTemperatureReadings.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testNegativeCarbonDioxideIdeal() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/NegativeCarbonDioxideIdeal.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testNegativeCarbonDioxideVariation() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/NegativeCarbonDioxideVariation.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testMissingLines1() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/MissingLines1.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testMissingLines2() throws IOException, FileFormatException {
        testBuildings = BuildingInitialiser.loadBuildings("saves/MissingLines2.txt");
    }

}

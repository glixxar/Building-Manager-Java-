package bms.floor;

import bms.exceptions.DuplicateSensorException;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.TemperatureSensor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceScheduleTest {

    Room room1;
    Room room2;
    Room room3;
    Room room4;
    Room room5;
    Room room6;
    List<Room> testRoomList;

    @Before
    public void createRooms() {
        room1 = new Room(101, RoomType.STUDY, 5);
        room2 = new Room(102, RoomType.OFFICE, 5);
        room3 = new Room(103, RoomType.LABORATORY, 5);
        room4 = new Room(104, RoomType.STUDY, 10);
        room5 = new Room(105, RoomType.OFFICE, 10);
        room6 = new Room(106, RoomType.LABORATORY, 10);

        testRoomList = new ArrayList<>();
    }

    @Test
    public void createProperSchedule() {
        testRoomList.add(room1);
        testRoomList.add(room2);
        testRoomList.add(room3);
        testRoomList.add(room4);
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(testRoomList);
    }

    @Test
    public void createProperScheduleFirstRoomFalse() {
        room1.setMaintenance(false);
        testRoomList.add(room1);
        testRoomList.add(room2);
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(testRoomList);
        Assert.assertTrue(maintenanceSchedule.getCurrentRoom().maintenanceOngoing());
    }


    @Test
    public void maintenanceTimeMinArea() {
        testRoomList.add(room1);
        testRoomList.add(room2);
        testRoomList.add(room3);
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(testRoomList);
        Assert.assertEquals(maintenanceSchedule.getMaintenanceTime(room1), (int) Math.round(5 * 1.0));
        Assert.assertEquals(maintenanceSchedule.getMaintenanceTime(room2), (int) Math.round(5 * 1.5));
        Assert.assertEquals(maintenanceSchedule.getMaintenanceTime(room3), (int) Math.round(5 * 2.0));
    }

    @Test
    public void maintenanceTimeExtraArea() {
        testRoomList.add(room4);
        testRoomList.add(room5);
        testRoomList.add(room6);
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(testRoomList);
        Assert.assertEquals(maintenanceSchedule.getMaintenanceTime(room4), (int) Math.round((5 + ((room4.getArea() - room4.getMinArea()) * 0.2)) * 1.0));
        Assert.assertEquals(maintenanceSchedule.getMaintenanceTime(room5), (int) Math.round((5 + ((room5.getArea() - room5.getMinArea()) * 0.2)) * 1.5));
        Assert.assertEquals(maintenanceSchedule.getMaintenanceTime(room6), (int) Math.round((5 + ((room6.getArea() - room6.getMinArea()) * 0.2)) * 2.0));
    }

    @Test
    public void createProperScheduleStartZeroTime() {
        testRoomList.add(room1);
        testRoomList.add(room2);
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(testRoomList);
        Assert.assertEquals(maintenanceSchedule.getTimeElapsedCurrentRoom(), 0);
    }

    @Test
    public void testElapsedTime() {
        testRoomList.add(room1);
        testRoomList.add(room2);
        testRoomList.add(room3);
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(testRoomList);
        maintenanceSchedule.elapseOneMinute();
        Assert.assertEquals(maintenanceSchedule.getTimeElapsedCurrentRoom(), 1);
        maintenanceSchedule.elapseOneMinute();
        Assert.assertEquals(maintenanceSchedule.getTimeElapsedCurrentRoom(), 2);
        maintenanceSchedule.elapseOneMinute();
        Assert.assertEquals(maintenanceSchedule.getTimeElapsedCurrentRoom(), 3);
        maintenanceSchedule.elapseOneMinute();
        Assert.assertEquals(maintenanceSchedule.getTimeElapsedCurrentRoom(), 4);
    }

    @Test
    public void testEvacuateRoom() throws DuplicateSensorException {
        room1.setFireDrill(true);
        testRoomList.add(room1);
        testRoomList.add(room2);
        room2.addSensor(new TemperatureSensor(new int[]{100,122,245}));
        testRoomList.add(room3);
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(testRoomList);
        maintenanceSchedule.elapseOneMinute();
        Assert.assertEquals(maintenanceSchedule.getTimeElapsedCurrentRoom(), 0);
    }

    @Test
    public void testEvacuateRoomTemperature() throws DuplicateSensorException {
        testRoomList.add(room2);
        room2.addSensor(new TemperatureSensor(new int[]{100,122,245}));
        testRoomList.add(room3);
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(testRoomList);
        maintenanceSchedule.elapseOneMinute();
        maintenanceSchedule.elapseOneMinute();
        maintenanceSchedule.elapseOneMinute();
        Assert.assertEquals(maintenanceSchedule.getTimeElapsedCurrentRoom(), 0);
    }

    @Test
    public void moveToNextRoom() throws DuplicateSensorException {
        testRoomList.add(room1);
        testRoomList.add(room2);
        testRoomList.add(room3);
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(testRoomList);
        int maintTime = maintenanceSchedule.getMaintenanceTime(maintenanceSchedule.getCurrentRoom());
        Assert.assertTrue(room1.maintenanceOngoing());
        for (int i = 0; i < maintTime; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        Assert.assertFalse(room1.maintenanceOngoing());
        Assert.assertEquals(maintenanceSchedule.getCurrentRoom().getRoomNumber(), 102);
    }

    @Test
    public void moveToNextRoomLoopBack() throws DuplicateSensorException {
        testRoomList.add(room1);
        testRoomList.add(room2);
        testRoomList.add(room3);
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(testRoomList);
        Assert.assertEquals(maintenanceSchedule.getCurrentRoom().getRoomNumber(), 101);
        int maintTime = maintenanceSchedule.getMaintenanceTime(maintenanceSchedule.getCurrentRoom());
        for (int i = 0; i < maintTime; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        Assert.assertEquals(maintenanceSchedule.getCurrentRoom().getRoomNumber(), 102);
        maintTime = maintenanceSchedule.getMaintenanceTime(maintenanceSchedule.getCurrentRoom());
        for (int i = 0; i < maintTime; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        Assert.assertEquals(maintenanceSchedule.getCurrentRoom().getRoomNumber(), 103);
        maintTime = maintenanceSchedule.getMaintenanceTime(maintenanceSchedule.getCurrentRoom());
        for (int i = 0; i < maintTime; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        Assert.assertEquals(maintenanceSchedule.getCurrentRoom().getRoomNumber(), 101);
    }

    @Test
    public void moveToNextRoomZeroTime() throws DuplicateSensorException {
        testRoomList.add(room1);
        testRoomList.add(room2);
        testRoomList.add(room3);
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(testRoomList);
        int maintTime = maintenanceSchedule.getMaintenanceTime(maintenanceSchedule.getCurrentRoom());
        for (int i = 0; i < maintTime; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        Assert.assertEquals(maintenanceSchedule.getCurrentRoom().getRoomNumber(), 102);
        Assert.assertEquals(maintenanceSchedule.getTimeElapsedCurrentRoom(), 0);
    }

    @Test
    public void skipCurrentTest() {
        testRoomList.add(room1);
        testRoomList.add(room2);
        testRoomList.add(room3);
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(testRoomList);
        Assert.assertEquals(maintenanceSchedule.getCurrentRoom().getRoomNumber(), 101);
        maintenanceSchedule.skipCurrentMaintenance();
        Assert.assertEquals(maintenanceSchedule.getCurrentRoom().getRoomNumber(), 102);
        maintenanceSchedule.skipCurrentMaintenance();
        Assert.assertEquals(maintenanceSchedule.getCurrentRoom().getRoomNumber(), 103);
        maintenanceSchedule.skipCurrentMaintenance();
        Assert.assertEquals(maintenanceSchedule.getCurrentRoom().getRoomNumber(), 101);
        maintenanceSchedule.skipCurrentMaintenance();
        Assert.assertEquals(maintenanceSchedule.getCurrentRoom().getRoomNumber(), 102);
        maintenanceSchedule.skipCurrentMaintenance();
        Assert.assertEquals(maintenanceSchedule.getCurrentRoom().getRoomNumber(), 103);

    }

    @Test
    public void toStringTest() {
        testRoomList.add(room1);
        testRoomList.add(room2);
        testRoomList.add(room3);
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(testRoomList);
        Assert.assertEquals(maintenanceSchedule.toString(), "MaintenanceSchedule: currentRoom=#101, currentElapsed=0");
        int maintTime = maintenanceSchedule.getMaintenanceTime(maintenanceSchedule.getCurrentRoom());
        for (int i = 0; i < maintTime; i++) {
            maintenanceSchedule.elapseOneMinute();
        }
        Assert.assertEquals(maintenanceSchedule.toString(), "MaintenanceSchedule: currentRoom=#102, currentElapsed=0");
        maintenanceSchedule.elapseOneMinute();
        Assert.assertEquals(maintenanceSchedule.toString(), "MaintenanceSchedule: currentRoom=#102, currentElapsed=1");
        maintenanceSchedule.elapseOneMinute();
        maintenanceSchedule.elapseOneMinute();
        maintenanceSchedule.elapseOneMinute();
        Assert.assertEquals(maintenanceSchedule.toString(), "MaintenanceSchedule: currentRoom=#102, currentElapsed=4");
    }

    @Test
    public void encodeTest1() {
        testRoomList.add(room1);
        testRoomList.add(room2);
        testRoomList.add(room3);
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(testRoomList);
        Assert.assertEquals(maintenanceSchedule.encode(), "101,102,103");
    }

    @Test
    public void encodeTest2() {
        testRoomList.add(room4);
        testRoomList.add(room5);
        testRoomList.add(room6);
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(testRoomList);
        Assert.assertEquals(maintenanceSchedule.encode(), "104,105,106");
    }

    @Test
    public void encodeTest3() {
        testRoomList.add(room1);
        testRoomList.add(room2);
        testRoomList.add(room3);
        testRoomList.add(room4);
        testRoomList.add(room5);
        testRoomList.add(room6);
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(testRoomList);
        Assert.assertEquals(maintenanceSchedule.encode(), "101,102,103,104,105,106");
    }
}

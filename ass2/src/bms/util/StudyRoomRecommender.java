package bms.util;

import bms.building.Building;
import bms.floor.Floor;
import bms.room.Room;
import bms.room.RoomState;
import bms.room.RoomType;
import bms.sensors.ComfortSensor;
import bms.sensors.Sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class that provides a
 * recommendation for a study room in a building.
 *
 * @ass2
 */
public class StudyRoomRecommender {

    /**
     * Returns a room in the given building
     * that is most suitable for study purposes.
     *
     * Any given room's suitability for study is
     * based on several criteria, including:
     *
     * the room's type - it must be a study room (see RoomType)
     *
     * the room's status - it must be open, not being evacuated
     * or in maintenance (see Room.evaluateRoomState())
     *
     * the room's comfort level based on its available sensors
     * (see ComfortSensor.getComfortLevel())
     *
     * which floor the room is on - rooms on lower floors are better
     *
     * Since travelling up the floors of a building often requires
     * walking up stairs, the process for choosing a study room
     * begins by looking for rooms on the first floor, and
     * only considers higher floors if doing so would improve the
     * comfort level of the room chosen. Similarly, once on a floor,
     * walking back down more than one floor to a
     * previously considered study room is also not optimal.
     * If there are no rooms on the first floor of a building
     * that meet the basic criteria, then the algorithm should
     * recommend that the building be avoided entirely,
     * even if there are suitable rooms on higher floors.
     *
     * Based on these requirements, the algorithm for
     * determining the most suitable study room is as follows:
     *
     * 1. If there are no rooms in the building, return null.
     *
     * 2. Consider only rooms on the first floor.
     *
     * 3. Eliminate any rooms that are not study rooms or are not open.
     * If there are no remaining candidate rooms, return the room with the
     * highest comfort level on the previous floor, or null if there is
     * no previous floor.
     *
     * 4. Calculate the comfort level of all remaining rooms on this floor,
     * using the average of the comfort levels of each room's available
     * comfort sensors. If a room has no comfort sensors,
     * its comfort level should be treated as 0.
     *
     * 5. Keep a reference to the room with the highest comfort level
     * on this floor based on the calculation in the previous step.
     * If there is a tie between two or more rooms,
     * any of these may be chosen.
     *
     * 6. If the highest comfort level of any room on this floor
     * is less than or equal to the highest comfort level of
     * any room on the previous floor, return the room on
     * the previous floor with the highest comfort level.
     *
     * 7. If this is the top floor of the building,
     * return the room found in step 5. Otherwise,
     * repeat steps 2-7 for the next floor up.
     *
     *
     * @param building building in which to search for a study room
     * @return the most suitable study room in the building; null if there are none
     */
    public static Room recommendStudyRoom(Building building) {
        Room recommendedRoom = null;
        int numFloors = building.getFloors().size();
        // Returns null if no study rooms are found
        if (!checkTotalNumRoom(building, numFloors)) {
            return null;
        }
        // Returns the first study room with the highest comfort
        // level found on first floor if second floor has no
        // study rooms
        if (numFloors > 1
                && findStudyRoom(building.getFloorByNumber(2)) == null
                && findStudyRoom(building.getFloorByNumber(1)) != null) {
            return bestComfort(findStudyRoom(building.getFloorByNumber(1)));
        } else {
            for (int i = 1; i <= numFloors; i++) {
                if (findStudyRoom(building.getFloorByNumber(1)) != null && i == 1) {
                    recommendedRoom = bestComfort(findStudyRoom(building.getFloorByNumber(1)));
                }
                if (i != 1) {
                    if (findStudyRoom(building.getFloorByNumber(i)) == null) {
                        return recommendedRoom;
                    }
                    if (findStudyRoom(building.getFloorByNumber(i)) != null) {
                        if (averageComfort(recommendedRoom)  <
                                averageComfort(bestComfort(findStudyRoom(
                                        building.getFloorByNumber(i))))) {
                            recommendedRoom = bestComfort(findStudyRoom(
                                    building.getFloorByNumber(i)));
                        }
                    }
                }
            }
        }
        return recommendedRoom;
    }

    /**
     * Private helper method for determining how many
     * study rooms are in a building
     *
     * @param building building to be searched
     * @param numFloors number of floors in the building
     * @return true if at least one study room is found
     * and false if no study rooms are found
     * @ass2
     */
    private static boolean checkTotalNumRoom(Building building, int numFloors) {
        int numRoom = 0;
        for (int i = 1; i < numFloors + 1; i++) {
            numRoom += building.getFloorByNumber(i).getRooms().size();
        }
        if (numRoom == 0) {
            return false;
        }
        return true;
    }

    /**
     * Private helper method for the determining the
     * average comfort level of a room based on its
     * number of sensors
     *
     * @param room room which average comfort is to be calculated
     * @return the average comfort level of the room
     * @ass2
     */
    private static double averageComfort(Room room) {
        double numSensors = room.getSensors().size();
        double totalComfort = 0;
        if (numSensors == 0) {
            return 0;
        }
        for (Sensor sensor : room.getSensors()) {
            totalComfort += ((ComfortSensor) sensor).getComfortLevel();
        }
        return totalComfort / numSensors;
    }

    /**
     * Private helper method for determining the best comfort level
     * within a list of study rooms
     *
     * @param roomList list of study rooms
     * @return room with the highest comfort level
     * @ass2
     */
    private static Room bestComfort(List<Room> roomList) {
        List<Double> comfortLevel = new ArrayList<>();
        if (roomList.size() == 1) {
            return roomList.get(0);
        } else {
            for (Room room : roomList) {
                comfortLevel.add(averageComfort(room));
            }

            double totalComfort = 0;
            for (int i = 0; i < comfortLevel.size(); i++) {
                totalComfort += comfortLevel.get(i);
            }
            // Returns the room with the highest comfort level
            // or returns the first room if list only contains one
            // room.
            if (totalComfort == 0) {
                return roomList.get(0);
            } else {
                int maxPos = comfortLevel.indexOf(Collections.max(comfortLevel));
                return roomList.get(maxPos);
            }
        }
    }

    /**
     * Private helper method for finding open study rooms in a list
     * of rooms
     *
     * @param floor floor to be searched
     * @return list of open study rooms
     */
    private static List<Room> findStudyRoom(Floor floor) {
        List<Room> roomList = new ArrayList<>();
        for (Room room : floor.getRooms()) {
            if (room.getType() == RoomType.STUDY &&
                    room.evaluateRoomState() == RoomState.OPEN) {
                roomList.add(room);
            }
        }
        if (roomList.size() == 0) {
            return null;
        } else {
            return roomList;
        }
    }

}

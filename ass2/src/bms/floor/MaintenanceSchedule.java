package bms.floor;

import bms.room.Room;
import bms.room.RoomState;
import bms.room.RoomType;
import bms.util.Encodable;
import bms.util.TimedItem;
import bms.util.TimedItemManager;

import java.util.List;

/**
 * Carries out maintenance on a list of rooms in a given floor.
 *
 * The maintenance time for each room depends on the type of the
 * room and its area. Maintenance cannot progress whilst
 * an evacuation is in progress.
 *
 * @ass2
 */
public class MaintenanceSchedule
        implements TimedItem, Encodable {

    /**
     * List of rooms tracked by the Maintenance Schedule
     */
    private List<Room> roomOrder;

    /**
     * The amount of time that have elapsed while maintaining the current room
     */
    private int timeElapsed;

    /**
     * Creates a new maintenance schedule for a floor's list of rooms.
     * In this constructor, the new maintenance schedule should be
     * registered as a timed item with the timed item manager.
     *
     * The first room in the given order should be set to
     * "in maintenance", see Room.setMaintenance(boolean).
     *
     * @param roomOrder list of rooms on which to perform maintenance, in order
     * @ass2
     */
    public MaintenanceSchedule (List<Room> roomOrder) {
        this.roomOrder = roomOrder;
        this.roomOrder.get(0).setMaintenance(true);
        this.timeElapsed = 0;
        TimedItemManager.getInstance().registerTimedItem(this);
    }

    /**
     * Returns the time taken to perform maintenance on the given room,
     * in minutes. The maintenance time for a given room depends on
     * its size (larger rooms take longer to maintain) and its room
     * type (rooms with more furniture and equipment
     * take take longer to maintain).
     *
     * The formula for maintenance time is calculated as the room's
     * base maintenance time multiplied by its room type multiplier,
     * and finally rounded to the nearest integer. Floating point
     * operations should be used during all steps of the calculation,
     * until the final rounding to integer.
     *
     * Rooms with an area of Room.getMinArea() have a base maintenance
     * time of 5.0 minutes.
     *
     * Rooms with areas greater than Room.getMinArea() have a base
     * maintenance time of 5.0 minutes, plus 0.2 minutes for every
     * square metre the room's area is over Room.getMinArea().
     *
     * A room's room type multiplier is given in the table below.
     *
     * @param room room on which to perform maintenance
     * @return room's maintenance time in minutes
     * @ass2
     */
    public int getMaintenanceTime(Room room) {
        double roomArea = 0;
        if (room.getArea() > room.getMinArea()) {
            roomArea = 5.0 + ((room.getArea() - room.getMinArea()) * 0.2);
        } else {
            roomArea = 5.0;
        }

        if (room.getType() == RoomType.STUDY) {
            roomArea = roomArea * 1.0;
        } else if (room.getType() == RoomType.OFFICE) {
            roomArea = roomArea * 1.5;
        } else if (room.getType() == RoomType.LABORATORY) {
            roomArea = roomArea * 2.0;
        }
        return (int) Math.round(roomArea);
    }

    /**
     * Returns the room which is currently in the process of being maintained.
     *
     * @return room currently in maintenance
     * @ass2
     */
    public Room getCurrentRoom() {
        for (Room room : this.roomOrder) {
            if (room.maintenanceOngoing()) {
                return room;
            }
        }
        return this.roomOrder.get(0);
    }

    /**
     * Returns the number of minutes that have
     * elapsed while maintaining the current room (getCurrentRoom()).
     *
     * @return time elapsed maintaining current room
     * @ass2
     */
    public int getTimeElapsedCurrentRoom() {
        return this.timeElapsed;
    }

    /**
     * Progresses the maintenance schedule by one minute.
     * If the room currently being maintained has a room
     * state of EVACUATE, then no action should occur.
     *
     * If enough time has elapsed such that the room
     * currently being maintained has completed its
     * maintenance (according to getMaintenanceTime(Room)), then:
     *
     * the current room should have its maintenance
     * status set to false ( see Room.setMaintenance(boolean))
     * the next room in the list passed to the constructor
     * should be set as the new current room.
     * If the end of the list has been reached, the new current
     * room should "wrap around" to the first room in the list.
     * the new current room should have its
     * maintenance status set to true
     *
     * @ass2
     */
    public void elapseOneMinute() {
        if (getCurrentRoom().evaluateRoomState() != RoomState.EVACUATE) {
            this.timeElapsed++;
            int currentPointer = this.roomOrder.indexOf(getCurrentRoom());
            if (getMaintenanceTime(getCurrentRoom()) == this.timeElapsed) {
                this.timeElapsed = 0;
                getCurrentRoom().setMaintenance(false);
                if (currentPointer == this.roomOrder.size() - 1) {
                    this.roomOrder.get(0).setMaintenance(true);
                } else {
                    this.roomOrder.get(currentPointer + 1).setMaintenance(true);
                }
            }
        }
    }

    /**
     * Stops the in-progress maintenance of the
     * current room and progresses to the next room.
     * The same steps should be undertaken as
     * described in the dot point list in elapseOneMinute().
     *
     * @ass2
     */
    public void skipCurrentMaintenance() {
        int currentPointer = this.roomOrder.indexOf(getCurrentRoom());
        getCurrentRoom().setMaintenance(false);
        if (currentPointer == this.roomOrder.size() - 1) {
            this.roomOrder.get(0).setMaintenance(true);
        } else {
            this.roomOrder.get(currentPointer + 1).setMaintenance(true);
        }
    }

    /**
     * Returns the human-readable string representation
     * of this maintenance schedule.
     * The format of the string to return is
     *
     * MaintenanceSchedule: currentRoom=#currentRoomNumber, currentElapsed=elapsed
     * where 'currentRoomNumber' is the room number
     * of the room currently being maintained, and
     * 'elapsed' is the number of minutes that have elapsed
     * while maintaining the current room.
     *
     * For example:
     *
     * MaintenanceSchedule: currentRoom=#108, currentElapsed=3
     *
     * @return string representation of this maintenance schedule
     * @ass2
     */
    @Override
    public String toString() {
        return String.format("MaintenanceSchedule: currentRoom=#%d, currentElapsed=%d",
                getCurrentRoom().getRoomNumber(),this.timeElapsed);
    }

    /**
     * Returns the machine-readable string
     * representation of this maintenance schedule.
     * The format of the string to return is:
     *
     *  roomNumber1,roomNumber2,...,roomNumberN
     *
     * where 'roomNumberX' is the room number of the
     * Xth room in this maintenance schedule's room order,
     * from 1 to N where N is the number of rooms in the maintenance order.
     * There should be no newline at the end of the string.
     *
     * See the demo save file for an example (uqstlucia.txt).
     *
     * @return encoded string representation of this maintenance schedule
     * @ass2
     */
    public String encode() {
        String outputString = "";
        for (Room room : this.roomOrder) {
            outputString += room.getRoomNumber() + ",";
        }
        return outputString.substring(0, outputString.length() - 1);
    }
}

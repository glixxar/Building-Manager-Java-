package bms.building;


import bms.exceptions.*;
import bms.floor.Floor;
import bms.hazardevaluation.RuleBasedHazardEvaluator;
import bms.hazardevaluation.WeightingBasedHazardEvaluator;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Class which manages the initialisation and
 * saving of buildings by reading and writing data to a file.
 *
 * Note: the Javadoc includes the default constructor,
 * BuildingInitialiser(), however you do not need to write
 * this method in your assignment as it will be automatically generated.
 *
 * @ass2
 */
public class BuildingInitialiser {
    /**
     * Loads a list of buildings from a save file with the given filename.
     *
     * Save files have the following structure. Square brackets indicate
     * that the data inside them is optional.
     *
     * See the demo save file for an example (uqstlucia.txt).
     *
     *  buildingName
     *  numFloors
     *  floorNumber:floorWidth:floorLength:numRooms[:rooms,in,maintenance,schedule]
     *  roomNumber:ROOM_TYPE:roomArea:numSensors[:hazardEvalType]
     *  sensorType:list,of,sensor,readings[:sensorAttributes...][@weighting]
     *  ...       (more sensors)
     *  ...     (more rooms)
     *  ...   (more floors)
     *  ... (more buildings)
     *
     * A save file is invalid if any of the following conditions are true:
     *
     * The number of floors specified for a building is not equal to the actual
     * number of floors read from the file for that building.
     *
     * The number of rooms specified for a floor is not equal to the actual
     * number of rooms read from the file for that floor.
     *
     * The number of sensors specified for a room is not equal to the number
     * of sensors read from the file for that room.
     *
     * A floor's maintenance schedule contains a room number that does
     * not correspond to a room with the same number on that floor.
     *
     * A floor's maintenance schedule is invalid according to
     * Floor.createMaintenanceSchedule(List).
     *
     * A building has two floors with the same floor number (a duplicate floor).
     *
     * A floor's length or width is less than the minimum
     * length or width, respectively, for a floor.
     *
     * A floor has no floor below to support the floor.
     *
     * A floor is too large to fit on top of the floor below.
     *
     * A floor has two rooms with the same room
     * number (a duplicate room).
     *
     * A room cannot be added to its floor because there is
     * insufficient unoccupied space on the floor.
     *
     * A room's type is not one of the types listed in RoomType.
     * Room types are case-sensitive.
     *
     * A room's area is less than the minimum area for a room.
     *
     * A room's hazard evaluator type is invalid.
     *
     * A room's weighting-based hazard evaluator weightings
     * are invalid according to WeightingBasedHazardEvaluator(Map).
     *
     * A room has two sensors of the same type (a duplicate sensor).
     *
     * A sensor's type does not match one of the concrete sensor
     * types (e.g. NoiseSensor, OccupancySensor, ...).
     *
     * A sensor's update frequency does not meet the restrictions outlined in TimedSensor(int[], int).
     *
     * A carbon dioxide sensor's variation limit is greater than its ideal CO2 value.
     *
     * Any numeric value that should be non-negative is less than zero. This includes:
     * the number of floors in a building
     * the number of rooms on a floor
     * the number of sensors in room
     * sensor readings
     * occupancy sensor capacity
     * carbon dioxide sensor ideal CO2 level
     * carbon dioxide sensor variation limit
     *
     * Any numeric value that should be positive is less than or equal to zero. This includes:
     * floor numbers
     *
     * The colon-delimited format is violated, i.e. there are more/fewer colons than expected.
     *
     * Any numeric value fails to be parsed.
     *
     * An empty line occurs where a non-empty line is expected.
     *
     *
     * @param filename
     * @return
     * @throws IOException
     * @throws FileFormatException
     * @ass2
     */
    public static List<Building> loadBuildings(String filename) throws IOException,
            FileFormatException {
        List<Building> buildings= new ArrayList<>();
        String buildingName;
        int floorNum;
        Building newBuilding;

        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(filename));

            // Reads file while lines are not empty
            while ((buildingName = reader.readLine()) != null) {
                if (buildingName.contains(":")) {
                    throw new FileFormatException();
                }
                try {
                    floorNum = Integer.parseInt(reader.readLine());
                    if (floorNum < 0) {
                        throw new FileFormatException();
                    }
                } catch (NumberFormatException e) {
                    throw new FileFormatException();
                }
                try {
                    newBuilding = new Building(buildingName);
                } catch (IllegalArgumentException e) {
                    throw new FileFormatException();
                }

                for (int i = 0; i < floorNum; ++i) {
                    try {
                        newBuilding.addFloor(readFloor(reader));
                    } catch (IllegalArgumentException
                            | DuplicateFloorException
                            | NoFloorBelowException
                            | FloorTooSmallException e) {
                        throw new FileFormatException();
                    }
                }
                buildings.add(newBuilding);
            }
            reader.close();
        } catch (IOException e) {
            throw new IOException();
        }
        return buildings;
    }

    /**
     * Private helper function for reading a floor line that is
     * passed to it from the reader.
     *
     * @param reader bufferedreader for the file
     * @return floor created from the reader
     * @throws IOException if an IOException is encountered when
     * calling any IO methods
     * @throws FileFormatException if the file format of the given file
     * is invalid according to the rules above
     * @ass2
     */
    private static Floor readFloor(BufferedReader reader)
            throws IOException, FileFormatException {
        String line = reader.readLine();
        if (line == null) {
            throw new FileFormatException();
        }
        long numSemicolon = line.chars().filter(num -> num == ':').count();
        if (numSemicolon > 4 || numSemicolon < 3) {
            throw new FileFormatException();
        }

        String[] lineParts = line.split(":");

        int floorNumber;
        double width;
        double length;
        int numRooms;
        Floor floors;

        try {
            try {
                floorNumber = Integer.parseInt(lineParts[0]);
                width = Double.parseDouble(lineParts[1]);
                length = Double.parseDouble(lineParts[2]);
                numRooms = Integer.parseInt(lineParts[3]);
            } catch (NumberFormatException e) {
                throw new FileFormatException();
            }

            if (floorNumber <= 0 || width < 0 || length < 0 || numRooms < 0) {
                throw new FileFormatException();
            }

            try {
                floors = new Floor(floorNumber, width, length);
            } catch (IllegalArgumentException e) {
                throw new FileFormatException();
            }

            for (int i = 0; i < numRooms; ++i)  {
                try {
                    floors.addRoom(readRoom(reader));
                } catch (DuplicateRoomException
                        | InsufficientSpaceException
                        | IllegalArgumentException
                        | IndexOutOfBoundsException e) {
                    throw new FileFormatException();
                }
            }

            // Creates a maintenance schedule if the split strings
            // has 5 parts
            if (lineParts.length == 5) {
                List<Room> roomList = new ArrayList<>();
                String[] maintList = lineParts[4].split(",");
                long numComma = lineParts[4].chars().filter(num -> num == ',').count();
                if (numComma > maintList.length) {
                    throw new FileFormatException();
                }
                for (int i = 0; i < maintList.length; i++) {
                    try {
                        if (floors.getRoomByNumber(Integer.parseInt(maintList[i])) != null) {
                            roomList.add(floors.getRoomByNumber(Integer.parseInt(maintList[i])));
                        } else {
                            throw new FileFormatException();
                        }
                    } catch (NumberFormatException e) {
                        throw new FileFormatException();
                    }
                }

                // Try to add maintenance schedule to floor
                try {
                    floors.createMaintenanceSchedule(roomList);
                } catch (IllegalArgumentException e) {
                    throw new FileFormatException();
                }

            }
        } catch (NumberFormatException e) {
            throw new FileFormatException();
        }
        return floors;
    }

    /**
     * Private helper method for reading a room passed
     * from the buffered reader.
     *
     * @param reader bufferedreader for the file
     * @return room created from the reader
     * @throws IOException if an IOException is encountered when
     * calling any IO methods
     * @throws FileFormatException if the file format of the given file
     * is invalid according to the rules above
     * @ass2
     */
    private static Room readRoom(BufferedReader reader)
            throws IOException, FileFormatException {
        String line = reader.readLine();
        if (line == null) {
            throw new FileFormatException();
        }

        long numSemicolon = line.chars().filter(num -> num == ':').count();
        if (numSemicolon > 4 || numSemicolon < 3) {
            throw new FileFormatException(line);
        }

        String[] lineParts = line.split(":");

        int roomNumber;
        int numSensors;
        RoomType roomOfType;
        double area;
        Room room;
        String readSensorString;
        String[] storeSensorInfo;

        boolean weightingBased = false;
        boolean ruleBased = false;

        Map<HazardSensor, Integer> sensorWeights = new HashMap<>();
        List<HazardSensor> hazardSensorList = new ArrayList<>();

        try {
            if (lineParts.length == 5) {
                if (lineParts[4].equals("WeightingBased")) {
                    weightingBased = true;
                } else if (lineParts[4].equals("RuleBased")) {
                    ruleBased = true;
                } else {
                    throw new FileFormatException();
                }
            }

            try {
                roomNumber = Integer.parseInt(lineParts[0]);
                roomOfType = RoomType.valueOf(lineParts[1]);
                area = Double.parseDouble(lineParts[2]);
                numSensors = Integer.parseInt(lineParts[3]);
            } catch (NumberFormatException e) {
                throw new FileFormatException();
            }

            if (roomNumber < 0 || area < 0 || numSensors < 0) {
                throw new FileFormatException();
            }

            try {
                room = new Room(roomNumber, roomOfType, area);
            } catch (IllegalArgumentException e) {
                throw new FileFormatException();
            }

            for (int i = 0; i < numSensors; ++i) {
                readSensorString = reader.readLine();
                if (readSensorString == null) {
                    throw new FileFormatException();
                }
                if (weightingBased) {
                    storeSensorInfo = readSensorString.split(":|\\@");
                } else {
                    storeSensorInfo = readSensorString.split(":");
                }

                Sensor tempSensor;
                try {
                    // Creates sensor according to string read
                    // at the start of the line
                    if (storeSensorInfo[0].equals("OccupancySensor")) {
                        tempSensor = readOccupant(storeSensorInfo[1], storeSensorInfo[2], storeSensorInfo[3]);
                        if (weightingBased) {
                            sensorWeights.put((HazardSensor) tempSensor, Integer.parseInt(storeSensorInfo[4]));
                        } else if (ruleBased) {
                            hazardSensorList.add((HazardSensor) tempSensor);
                        }
                    } else if (storeSensorInfo[0].equals("NoiseSensor")) {
                        tempSensor = readNoise(storeSensorInfo[1], storeSensorInfo[2]);
                        if (weightingBased) {
                            sensorWeights.put((HazardSensor) tempSensor, Integer.parseInt(storeSensorInfo[3]));
                        } else if (ruleBased) {
                            hazardSensorList.add((HazardSensor) tempSensor);
                        }
                    } else if (storeSensorInfo[0].equals("TemperatureSensor")) {
                        tempSensor = readTemperature(storeSensorInfo[1]);
                        if (weightingBased) {
                            sensorWeights.put((HazardSensor) tempSensor, Integer.parseInt(storeSensorInfo[2]));
                        } else if (ruleBased) {
                            hazardSensorList.add((HazardSensor) tempSensor);
                        }
                    } else if (storeSensorInfo[0].equals("CarbonDioxideSensor")) {
                        tempSensor = readCarbon(storeSensorInfo[1], storeSensorInfo[2], storeSensorInfo[3], storeSensorInfo[4]);
                        if (weightingBased) {
                            sensorWeights.put((HazardSensor) tempSensor, Integer.parseInt(storeSensorInfo[5]));
                        } else if (ruleBased) {
                            hazardSensorList.add((HazardSensor) tempSensor);
                        }
                    } else {
                        throw new FileFormatException();
                    }
                } catch (NumberFormatException e) {
                    throw new FileFormatException();
                }
                try {
                    room.addSensor(tempSensor);
                } catch (IllegalArgumentException | DuplicateSensorException e) {
                    throw new FileFormatException();
                }
            }
            // Sets weighting/rule hazard evaluator to room
            if (weightingBased) {
                room.setHazardEvaluator(new WeightingBasedHazardEvaluator(sensorWeights));
            } else if (ruleBased) {
                room.setHazardEvaluator(new RuleBasedHazardEvaluator(hazardSensorList));
            }
            return room;
        } catch (NumberFormatException e) {
            throw new FileFormatException();
        }
    }

    /**
     * Private helper method to read constructor
     * variables in string form and
     * return a CarbonDioxideSensor
     *
     * @param readings a non-empty array of sensor readings
     * @param updateFreq indicates how often the sensor readings update,
     *                 in minutes
     * @param idealVal ideal CO2 value in ppm
     * @param variationLim acceptable range above and
     *                          below ideal value in ppm
     * @return the created CarbonDioxideSensor
     * @throws FileFormatException if the file format of the
     * given file is invalid according to the rules above
     * @ass2
     */
    private static CarbonDioxideSensor readCarbon(String readings,
                                                  String updateFreq,
                                                  String idealVal,
                                                  String variationLim) throws FileFormatException {
        try {
            int[] readingInt = Arrays.stream(readings.split(",")).mapToInt(Integer::parseInt).toArray();
            int updateFrequency = Integer.parseInt(updateFreq);
            int idealValue = Integer.parseInt(idealVal);
            int variationLimit = Integer.parseInt(variationLim);
            return new CarbonDioxideSensor(readingInt ,updateFrequency, idealValue, variationLimit);
        } catch (IllegalArgumentException e) {
            throw new FileFormatException();
        }
    }

    /**
     * Private helper method to read constructor
     * variables in string form and
     * return a OccupancySensor
     *
     * @param readings a non-empty array of sensor readings
     * @param updateFreq indicates how often the sensor readings update,
     *                        in minutes
     * @param cap maximum allowable number of people in the room
     * @return the created OccupancySensor
     * @throws FileFormatException if the file format of the
     * given file is invalid according to the rules above
     * @ass2
     */
    private static OccupancySensor readOccupant(String readings,
                                                String updateFreq,
                                                String cap) throws FileFormatException {
        try {
            int[] readingInt = Arrays.stream(readings.split(",")).mapToInt(Integer::parseInt).toArray();
            int updateFrequency = Integer.parseInt(updateFreq);
            int capacity = Integer.parseInt(cap);
            return new OccupancySensor(readingInt, updateFrequency, capacity);
        } catch (IllegalArgumentException e) {
            throw new FileFormatException();
        }
    }

    /**
     * Private helper method to read constructor
     * variables in string form and
     * return a TemperatureSensor
     *
     * @param readings a non-empty array of sensor readings
     * @return the created TemperatureSensor
     * @throws FileFormatException if the file format of the
     * given file is invalid according to the rules above
     * @ass2
     */
    private static TemperatureSensor readTemperature(String readings)
                                                    throws FileFormatException {
        try {
            int[] readingInt = Arrays.stream(readings.split(",")).mapToInt(Integer::parseInt).toArray();
            return new TemperatureSensor(readingInt);
        } catch (IllegalArgumentException e) {
            throw new FileFormatException();
        }
    }

    /**
     * Private helper method to read constructor
     * variables in string form and
     * return a NoiseSensor
     *
     * @param readings a non-empty array of sensor readings
     * @param updateFreq indicates how often the sensor readings update,
     *                        in minutes
     * @return the created NoiseSensor
     * @throws FileFormatException if the file format of the
     * given file is invalid according to the rules above
     * @ass2
     */
    private static NoiseSensor readNoise(String readings,
                                         String updateFreq) throws FileFormatException {
        try {
            int[] readingInt = Arrays.stream(readings.split(",")).mapToInt(Integer::parseInt).toArray();
            int updateFrequency = Integer.parseInt(updateFreq);
            return new NoiseSensor(readingInt, updateFrequency);
        } catch (IllegalArgumentException e) {
            throw new FileFormatException();
        }
    }
}

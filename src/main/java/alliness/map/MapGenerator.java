package alliness.map;

import java.util.ArrayList;
import java.util.List;

public class MapGenerator {

    private int mapWidth  = 50;
    private int mapHeight = 100;

    private int minRooms = 15;
    private int maxRooms = 15;

    private int minRoomSizeWidth  = 5;
    private int maxRoomSizeWidth  = 15;
    private int minRoomSizeHeight = 5;
    private int maxRoomSizeHeight = 25;

    private static MapGenerator instance;

    Tile[][]       tiles;
    List<Room>     rooms;
    List<Corridor> corridors;


    public static MapGenerator getInstance() {
        if (instance == null) {
            instance = new MapGenerator();
        }
        return instance;
    }

    private MapGenerator() {
        tiles = new Tile[mapWidth][mapHeight];
        rooms = new ArrayList<>();
        corridors = new ArrayList<>();
    }


    public void createGrid() {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                tiles[x][y] = new Tile(x, y, TileType.EMPTY, -1);
            }
        }
    }

    public void createRooms() {

        int roomsCount = Randomizer.range(minRooms, maxRooms);
        System.out.println("ROOMS: " + roomsCount);
        for (int i = 0; i < roomsCount; i++) {

            int roomW = Randomizer.range(minRoomSizeWidth, maxRoomSizeWidth);
            int roomH = Randomizer.range(minRoomSizeHeight, maxRoomSizeHeight);

            int  roomX = Randomizer.range(0, mapWidth - roomW);
            int  roomY = Randomizer.range(0, mapHeight - roomH);
            Room room  = new Room(roomX, roomY, roomW, roomH, i);
            room.create();
            if (!checkCollisions(room)) {
                rooms.add(room);
            } else {
                System.out.println(String.format("Room#%s is Collides. Removed.", room.getRoomId()));
            }
        }
    }

    private boolean checkCollisions(Room room) {
        for (Room room1 : rooms) {
            if (room.isColliding(room1)) {
                return true;
            }
        }
        return false;
    }

    public void render() {

        System.out.println("render rooms...");
        for (Room room : rooms) {
            for (Tile roomTile : room.getRoomTiles()) {
                tiles[roomTile.getX()][roomTile.getY()] = roomTile;
            }
        }
        System.out.println("render corridors...");
        for (Room room : rooms) {
            for (Corridor corridor : room.getCorridors()) {
                for (Tile t : corridor.getCorridorTiles()) {
                    tiles[t.getX()][t.getY()] = t;
                }
            }
        }
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile.getType().equals(TileType.EMPTY)) {

                    if (tile.getLeft() != null && tile.getLeft().getType().equals(TileType.CORRIDOR) ||
                            tile.getRight() != null && tile.getRight().getType().equals(TileType.CORRIDOR) ||
                            tile.getTop() != null && tile.getTop().getType().equals(TileType.CORRIDOR) ||
                            tile.getBottom() != null && tile.getBottom().getType().equals(TileType.CORRIDOR)) {

                        tile.setType(TileType.WALL);
                    }
                }
            }
        }
        System.out.println("done!");
    }

    public void createCorridors() {
        rooms.stream().filter(room -> room.getCorridors().size() < 2).forEach(this::createCorridor);
    }

    private void createCorridor(Room room) {
        Room toRoom = rooms.get(Randomizer.range(0, rooms.size() - 1));
        if (toRoom.getRoomId() == room.getRoomId()) {
            createCorridor(room);
        }
        room.connectRoom(toRoom);
    }


    public List<Room> getRooms() {
        return rooms;
    }

    public Room getRoom(int i) {
        return rooms.get(i);
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public void print() {
        StringBuilder grid = new StringBuilder();
        for (Tile[] row : getTiles()) {
            StringBuilder rowstring = new StringBuilder();
            for (Tile tile : row) {
                rowstring.append(tile.getName());
            }
            rowstring.append("\n");
            grid.append(rowstring.toString());
        }
        System.out.println(grid.toString());
    }

    public int width() {
        return mapWidth;
    }

    public int height() {
        return mapHeight;
    }
}

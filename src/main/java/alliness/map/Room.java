package alliness.map;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Room {

    private int roomId;
    private int x, y;
    private int width, height;
    private int left, right, top, bottom;
    private List<Tile> roomTiles;

    private List<Room>     connectedRooms;
    private List<Corridor> corridors;
    private boolean isConnected;

    public Room(int x, int y, int width, int height, int roomId) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.roomId = roomId;

        left = x;
        top = y;
        right = left + width - 1;
        bottom = top + height - 1;
        roomTiles = new ArrayList<>();
        connectedRooms = new ArrayList<>();
        corridors = new ArrayList<>();
    }


    public boolean isColliding(Room room) {
        if (left > room.right) {
            return false;
        }
        if (top > room.bottom) {
            return false;
        }
        if (right < room.left) {
            return false;
        }
        if (bottom < room.top) {
            return false;
        }
        return true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

    public void create() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int  gx = x + this.x;
                int  gy = y + this.y;
                Tile t;
                if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                    t = new Tile(gx, gy, TileType.WALL, roomId);
                } else {
                    t = new Tile(gx, gy, TileType.ROOM, roomId);
                }
                roomTiles.add(t);
            }
        }
    }

    public int getRoomId() {
        return roomId;
    }

    public List<Tile> getRoomTiles() {
        return roomTiles;
    }

    public List<Tile> getWalls() {
        return roomTiles.stream().filter(tile -> tile.getType().equals(TileType.WALL)).collect(Collectors.toList());
    }

    public List<Room> getConnectedRooms() {
        return connectedRooms;
    }

    public void connectRoom(Room otherRoom) {

        Corridor corridor = new Corridor();
        corridor.create(this, otherRoom);
        corridors.add(corridor);
    }

    public List<Corridor> getCorridors() {
        return corridors;
    }

    public Tile getRoomTile(int x, int y) {

            for (Tile roomTile : getRoomTiles()) {
            if(roomTile.getX()==x && roomTile.getY() == y){
                return roomTile;
            }
        }

        throw new NullPointerException(String.format("unable to find tile [%s:%s]", x,y));
    }

    public boolean isConnected(boolean connected) {
        this.isConnected = connected;
        return isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }
}

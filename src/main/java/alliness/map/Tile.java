package alliness.map;

public class Tile {

    private final int      roomId;
    private       int      x;
    private       int      y;
    private       TileType tileType;

    public Tile(int x, int y, TileType tileType, int roomId) {
        this.x = x;
        this.y = y;
        this.roomId = roomId;
        this.tileType = tileType;

    }

    public String getName() {
        return tileType.name;
    }

    public boolean isBusy() {
        return !tileType.equals(TileType.EMPTY);
    }

    public int getRoomId() {
        return roomId;
    }

    public TileType getType() {
        return tileType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Tile getLeft() {
        if (x == 0)
            return null;
        return MapGenerator.getInstance().getTile(x - 1, y);
    }

    public Tile getRight() {
        MapGenerator map = MapGenerator.getInstance();
        if (x + 1 > map.width() -1) {
            return null;
        }
        return map.getTile(x + 1, y);
    }

    public Tile getTop() {
        if (y == 0) {
            return null;
        }
        return MapGenerator.getInstance().getTile(x, y - 1);
    }

    public Tile getBottom() {
        MapGenerator map = MapGenerator.getInstance();
        if (y + 1 > map.height()-1) {
            return null;
        }
        return map.getTile(x, y + 1);
    }

    @Override
    public String toString() {
        return String.format("x:%s;y:%s\n", x, y);
    }

    public void setType(TileType tiletype) {
        this.tileType = tiletype;
    }
}

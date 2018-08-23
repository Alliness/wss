package alliness.map;

public enum TileType {
    EMPTY("0"),
    WALL("X"),
    ROOM(" "),
    CORRIDOR(" ");
    public String name;

    TileType(String name) {
        this.name = name;
    }
}

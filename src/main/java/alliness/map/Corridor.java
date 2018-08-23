package alliness.map;

import java.util.ArrayList;
import java.util.List;

public class Corridor {

    List<Tile> corridorTiles;
    Room       roomA, roomB;

    public Corridor() {
        corridorTiles = new ArrayList<>();
    }

    public void create(Room room1, Room room2) {

        roomA = room1;
        roomB = room2;

//        if (roomA.isConnected() || roomB.isConnected()) {
//            return;
//        }

        int sx = room1.getWidth() / 2 + room1.getX();
        int sy = room1.getHeight() / 2 + room1.getY();

        int ex = room2.getWidth() / 2 + room2.getX();
        int ey = room2.getHeight() / 2 + room2.getY();


        while (sx != ex) {
            Tile tile = new Tile(sx, sy, TileType.CORRIDOR, -2);
            corridorTiles.add(tile);
            sx += sx < ex ? 1 : -1;
        }
        while (sy != ey) {
            Tile tile = new Tile(sx, sy, TileType.CORRIDOR, -2);
            corridorTiles.add(tile);
            sy += sy < ey ? 1 : -1;
        }

        room1.isConnected(true);
        room2.isConnected(true);

    }

//
//
//        for (int i = startPoint.getX(); i < endPoint.getX(); i++) {
//
//            Tile t = new Tile(i, startPoint.getY(), TileType.CORRIDOR, -2);
//
//            corridorTiles.add(t);
//        }
//
//        for (int i = startPoint.getY(); i < endPoint.getY(); i++) {
//            if (MapGenerator.getInstance().getTile(startPoint.getX(), i).getType().equals(TileType.CORRIDOR)) {
//                return;
//            }
//            Tile t = new Tile(endPoint.getX(), i, TileType.CORRIDOR, -2);
//            corridorTiles.add(t);
//        }


    public Room getRoomA() {
        return roomA;
    }

    public Room getRoomB() {
        return roomB;
    }

    public List<Tile> getCorridorTiles() {
        return corridorTiles;
    }
}

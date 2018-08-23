package alliness.wss;


import alliness.map.MapGenerator;

public class Main {
    public static void main(String[] args) {
//        App.run();
        MapGenerator  map  = MapGenerator.getInstance();
        map.createGrid();
        map.createRooms();
        map.createCorridors();
        map.render();
        map.print();
    }
}

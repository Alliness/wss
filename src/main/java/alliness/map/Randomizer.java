package alliness.map;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {

    public static int range(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max+1);
    }

    public static <T> T fromList(List<T> list) {
        int max = list.size()-1;
        int i   = range(0, max);
        return list.get(i);

    }
}

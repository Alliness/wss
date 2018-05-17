package alliness.core.utils;

import java.util.Random;

/**
 * Created by zhitnikov on 7/10/2017.
 */
public class RandomUtils {


    /**
     * get Random int from(min) to(max)
     * @param from int
     * @param to int
     * @return int
     */
    public static int getRandomInt(int from, int to) {
        return new Random().nextInt(to + 1 - from) + from;
    }

}

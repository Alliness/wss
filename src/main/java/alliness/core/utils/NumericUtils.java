package alliness.core.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumericUtils {

    /**
     * return result of floor and !infinite for doubleValue
     * @param val {@link Double}
     * @return Boolean
     */
    public static boolean isInt(double val) {
        return ((val == Math.floor(val)) && !Double.isInfinite(val));
    }

    /**
     * return result is @value is in range of @min and @max
     * @param value target value
     * @param min min value data
     * @param max max value data
     * @return boolean
     */
    public static boolean isInaccuracy(double value, double min, double max) {
        return value >= min || value <= max;
    }

    /**
     * Get double value in format (d.dd)
     * @param data {@link Double}
     * @return double
     */
    public static double getDouble(double data){
        DecimalFormat df   = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.FLOOR);
        return Double.parseDouble(df.format(data).replace(",","."));
    }

    /**
     * get rounding value from double
     * @param d {@link Double}
     * @return rounding d
     */
    public static double getRoundingDouble(double d){
        return Math.round(d * 100.0) / 100.0;
    }

    /**
     * return digit values from String (ololo32323) - 32323
     * @param str {@link String}
     * @return int
     */
    public static int getDigitFormString(String str){
        return Integer.parseInt(str.replaceAll("\\D+", ""));
    }
}

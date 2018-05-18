package alliness.core;

/**
 * Created by zhitnikov on 6/6/2017.
 */
public class Dir {

    public static final String PROJECT   = System.getProperty("user.dir").replace("\\", "/");
    public static final String RESOURCES = PROJECT + "/src/main/resources";
    public static final String TARGET    = PROJECT + "/target";
    public static final String WEB       = PROJECT + "/web";
}

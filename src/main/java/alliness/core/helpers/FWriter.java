package alliness.core.helpers;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


/**
 *
 * Created by zhitnikov on 6/21/2017.
 */
public class FWriter {


    /**
     * get output OutputStream of saved file
     * @param fileName {@link String}
     * @return OutputStream
     */
    public static OutputStream saveStream(String fileName) {
        try {
            return new FileOutputStream(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Write JsonArray To file
     * @param jsonArray {@link JSONArray}
     * @param endFile {@link String}
     */
    public static void writeToFile(JSONArray jsonArray, String endFile) {
        try {
            FileWriter wf = new FileWriter(endFile, false);
            wf.write(jsonArray.toString() + "\n");
            wf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write stream to file
     * @param stream {@link InputStream}
     * @param fileName {@link String}
     */
    public static void writeToFile(InputStream stream, String fileName) {
        try {
            Files.copy(stream, new File(fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write String to file
     * @param string {@link String}
     * @param endFile {@link String}
     */
    public static void writeToFile(String string, String endFile) {
        try {
            FileWriter wf = new FileWriter(endFile, false);
            wf.write(string + "\n");
            wf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write List String data to file
     * @param row {@link String}
     * @param endFile {@link String}
     */
    public static void writeToFile(Object[] row, String endFile, boolean append) {
        try {
            String     parsed = StringUtils.join(row, ",");
            FileWriter wf     = new FileWriter(endFile, append);
            wf.write(parsed + "\n");
            wf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write String to file
     * @param string {@link String}
     * @param endFile {@link String}
     * @param append {@link Boolean} if true - append data
     */
    public static void writeToFile(String string, String endFile, boolean append) {
        try {
            FileWriter wf = new FileWriter(endFile, append);
            wf.write(string + "\n");
            wf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write String to file
     * @param string {@link String}
     * @param endFile {@link String}
     * @param append {@link Boolean}
     */
    public static void writeToFile(String string, File endFile, boolean append) {
        try {
            FileWriter wf = new FileWriter(endFile, append);
            wf.write(string + "\n");
            wf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write jsonobject to file
     * @param json {@link JSONObject}
     * @param file {@link File}
     * @param append boolean
     */
    public static void writeToFile(JSONObject json, File file, boolean append) {
        try {
            FileWriter wf = new FileWriter(file, append);
            wf.write(json.toString() + "\n");
            wf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write jsonobject to file
     * @param json {@link JSONObject}
     * @param file {@link File}
     */
    public static void writeToFile(JSONObject json, File file) {
        writeToFile(json, file, false);
    }
}

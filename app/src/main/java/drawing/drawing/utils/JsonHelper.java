package drawing.drawing.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * VectorDrawing for FretX
 * Created by pandor on 16/01/18 16:36.
 */

public class JsonHelper {
    public static String saveToJson(Object object) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(object);
    }
}

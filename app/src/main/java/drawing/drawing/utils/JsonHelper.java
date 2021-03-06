package drawing.drawing.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import drawing.drawing.storage.InterfaceAdapter;

/**
 * Created by leo on 16/01/18.
 */

public class JsonHelper<T> {
    private final Class<T> typeParameterClass;
    private GsonBuilder builder;

    public JsonHelper(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
        builder = new GsonBuilder();
    }

    public String saveToJson(T object) {
        Gson gson = builder.create();
        return gson.toJson(object);
    }

    public T loadToObject(String jsonString) {
        Gson gson = builder.create();
        return gson.fromJson(jsonString, typeParameterClass);
    }

    public <C> void registerTypeAdapter(Class<C> c) {
        builder.registerTypeAdapter(c, new InterfaceAdapter<C>());
    }
}

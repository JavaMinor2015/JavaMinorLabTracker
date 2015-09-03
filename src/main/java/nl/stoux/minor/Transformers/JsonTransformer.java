package nl.stoux.minor.Transformers;

/**
 * Created by Leon Stam on 3-9-2015.
 */
import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    private static Gson gson = new Gson();

    public String render(Object model) {
        return gson.toJson(model);
    }

}

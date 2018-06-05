package interview;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonReader;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

class FilterJsonTest {

    @Test
    void empty() throws Exception {
        List<DataExtractionAttribute> spec = Arrays.asList(new DataExtractionAttribute().name("test"));

        checkJsonFiltering(spec, "empty", "empty");
    }

    private void checkJsonFiltering(List<DataExtractionAttribute> spec, String inpFile, String expFile) throws JSONException {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        FilterJson.extract(jsonArrayFromFile(inpFile), spec , arrayBuilder);
        
        JSONAssert.assertEquals(jsonArrayFromFile(expFile).toString(), arrayBuilder.build().toString(), true);
    }

    protected JsonArray jsonArrayFromFile(String name) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("filter-json/" + name + ".json");
        JsonReader jsonReader = Json.createReader(is);
        return jsonReader.readArray();
    }
}

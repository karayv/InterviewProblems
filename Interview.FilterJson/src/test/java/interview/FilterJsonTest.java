
package interview;

import java.io.InputStream;
import java.util.Arrays;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonReader;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

class FilterJsonTest {

    @Test
    void empty1() throws Exception {
        checkJsonFiltering("empty", "empty", dea("test"));
    }

    @Test
    void empty2() throws Exception {
        checkJsonFiltering("all-cases", "empty", dea("miss"));
    }

    @Test
    void empty3() throws Exception {
        checkJsonFiltering("all-cases", "empty" /* no DEA, filter all */);
    }

    @Test
    void nullSpecNoFiltering() throws Exception {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonArray input = jsonArrayFromFile("all-cases");
        FilterJson.extract(input, null, arrayBuilder);

        JSONAssert.assertEquals(input.toString(), arrayBuilder.build().toString(), true);
    }

    @Test
    void nullObject() throws Exception {
        DataExtractionAttribute dea = dea("null-node");
        checkJsonFiltering("all-cases", "null-node", dea);

        // ignore, since null object cannot have children
        dea.addChildrenItem(dea("child"));
        checkJsonFiltering("all-cases", "empty", dea);
    }

    @Test
    void fullObjectIfNoChildrenInSpec() throws Exception {
        DataExtractionAttribute dea = dea("not-null-node");
        checkJsonFiltering("all-cases", "not-null-node", dea);

        // we ignore node if no children found
        dea.addChildrenItem(new DataExtractionAttribute().name("missing-child"));
        checkJsonFiltering("all-cases", "empty", dea);
    }

    @Test
    void filterByChildren() throws Exception {
        DataExtractionAttribute dea = dea("not-null-node") //
                .addChildrenItem(dea("not-null-node-ch1")) //
                .addChildrenItem(dea("not-null-node-ch2")) //
                .addChildrenItem(dea("not-null-node-ch3"));

        // filter out "not-null-node-ch4"
        checkJsonFiltering("all-cases", "not-null-node.no-ch4", dea);
    }

    @Test
    void filterByChildrenInDepth() throws Exception {
        DataExtractionAttribute dea = dea("not-null-node").addChildrenItem(dea("not-null-node-ch3") //
                .addChildrenItem(dea("not-null-node-ch3-ch1")) //
                .addChildrenItem(dea("not-null-node-ch3-other")));

        checkJsonFiltering("all-cases", "not-null-node-ch3", dea);

        // remove "not-null-node-ch3-ch1", and retain "not-null-node-ch3-other"
        // since there is no such node as "not-null-node-ch3-other", filter all
        dea.getChildren().get(0).getChildren().remove(0);
        checkJsonFiltering("all-cases", "empty", dea);
    }

    @Test
    void multipleRootAttributes() throws Exception {
        checkJsonFiltering("all-cases", "null-node.and.not-null-node", dea("not-null-node"), dea("null-node"));
    }

    @Test
    void arrayInChild() throws Exception {
        DataExtractionAttribute dea = dea("arr1");
        checkJsonFiltering("all-cases", "arr1", dea);

        dea.addChildrenItem(dea("arr1-ch3"));
        checkJsonFiltering("all-cases", "arr1-ch3", dea);

        dea.getChildren().remove(0);
        dea.addChildrenItem(dea("missing-child"));
        checkJsonFiltering("all-cases", "empty", dea);
    }

    @Test
    void arrayInRoot() throws Exception {
        checkJsonFiltering("all-cases", "arr2-ch1", dea("arr2-ch1"));

        checkJsonFiltering("all-cases", "arr2", dea("arr2-ch1"), dea("arr2-ch2"));
    }

    private void checkJsonFiltering(String inpFile, String expFile, DataExtractionAttribute... specs)
            throws JSONException {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        FilterJson.extract(jsonArrayFromFile(inpFile), Arrays.asList(specs), arrayBuilder);

        //        assertEquals(jsonArrayFromFile(expFile).toString(), arrayBuilder.build().toString());

        JSONAssert.assertEquals(jsonArrayFromFile(expFile).toString(), arrayBuilder.build().toString(),
            JSONCompareMode.STRICT);
    }

    protected JsonArray jsonArrayFromFile(String name) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("filter-json/" + name + ".json");
        JsonReader jsonReader = Json.createReader(is);
        return jsonReader.readArray();
    }

    protected DataExtractionAttribute dea(String name) {
        return new DataExtractionAttribute().name(name);
    }

}

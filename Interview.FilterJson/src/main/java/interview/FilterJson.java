
package interview;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
import javax.json.JsonValue;

/**
 * Given JSON and a specification of attributes of interest, produce the
 * corresponding JSON that only contains the specified attributes.
 * 
 * <p>
 * For instance, if the original JSON has employee records, the extraction
 * specification might ask for just the first names and the cities to be
 * collected, while discarding everything else.
 * </p>
 * 
 * <p>
 * Note that the extracted attributes should appear in their original structure
 * in the result.
 * </p>
 */

public final class FilterJson {

    public static void extract(final JsonArray src, final List<DataExtractionAttribute> spec,
            final JsonArrayBuilder dest) {
        // no spec, don't filter
        if (spec == null) {
            dest.addAll(Json.createArrayBuilder(src));
        }
        else {
            JsonArrayBuilder extractArray = extractArray(src, spec);
            if (extractArray != null) {
                dest.addAll(extractArray);
            }
        }

    }

    private static JsonArrayBuilder extractArray(final JsonArray src, final List<DataExtractionAttribute> spec) {
        if (spec == null) {
            return Json.createArrayBuilder(src);
        }

        // TODO potentially these builder objects may put extra load on GC. This can be optimized.
        JsonArrayBuilder res = Json.createArrayBuilder();
        boolean nodeMatched = false;

        for (JsonValue node : src) {
            // inside array we ignore all values, since they have no names in JSON array
            if (consumeStructureNode(node, spec, res::add, res::add)) {
                nodeMatched = true;
            }
        }
        return nodeMatched ? res : null;
    }

    private static JsonObjectBuilder extractObject(final JsonObject src, final List<DataExtractionAttribute> spec) {
        if (spec == null) {
            return Json.createObjectBuilder(src);
        }

        // hash map here optimize matching
        HashMap<String, DataExtractionAttribute> attrMap = spec.stream().collect(
            Collectors.toMap(DataExtractionAttribute::getName, (dea) -> dea, (ov, nv) -> nv, HashMap::new));

        JsonObjectBuilder res = Json.createObjectBuilder();
        boolean nodeMatched = false;

        // we pick smaller key set to optimize matching
        for (String name : smallerKeySet(src.keySet(), attrMap.keySet())) {
            if (attrMap.containsKey(name) && src.containsKey(name)) {
                List<DataExtractionAttribute> childrenSpec = attrMap.get(name).getChildren();

                if (consumeStructureNode(src.get(name), childrenSpec, (arr) -> res.add(name, arr),
                    (obj) -> res.add(name, obj))) {
                    nodeMatched = true;
                }
                else if (childrenSpec == null) {
                    // value node
                    if (src.isNull(name)) {
                        res.addNull(name);
                        nodeMatched = true;
                    }
                    else if (!(src.get(name) instanceof JsonStructure)) {
                        res.add(name, src.get(name));
                        nodeMatched = true;
                    }
                }
            }
        }

        return nodeMatched ? res : null;
    }

    private static boolean consumeStructureNode(JsonValue node, List<DataExtractionAttribute> spec,
            Consumer<JsonArrayBuilder> arrayConsumer, Consumer<JsonObjectBuilder> objectConsumer) {
        if (node instanceof JsonArray) {
            JsonArrayBuilder childArrayBuilder = extractArray((JsonArray) node, spec);
            if (childArrayBuilder != null) {
                arrayConsumer.accept(childArrayBuilder);
                return true;
            }
        }
        else if (node instanceof JsonObject) {
            JsonObjectBuilder objectBuilder = extractObject((JsonObject) node, spec);
            if (objectBuilder != null) {
                objectConsumer.accept(objectBuilder);
                return true;
            }
        }
        return false;
    }

    private static Set<String> smallerKeySet(Set<String> keySet1, Set<String> keySet2) {
        return keySet1.size() < keySet2.size() ? keySet1 : keySet2;
    }

}

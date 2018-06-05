package interview;

import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

/**
 * Given JSON and a specification of attributes of interest, produce the corresponding JSON that only contains the specified
 * attributes.
 * 
 * <p>For instance, if the original JSON has employee records, the extraction specification might ask for just the first names
 * and the cities to be collected, while discarding everything else.</p>
 * 
 * <p>Note that the extracted attributes should appear in their original structure in the result.</p>
 */

public final class FilterJson
{
    public static void extract( final JsonArray src, final List<DataExtractionAttribute> spec, final JsonArrayBuilder dest )
    {
        // TODO
    }
    
}

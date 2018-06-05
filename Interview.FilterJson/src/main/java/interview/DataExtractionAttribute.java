package interview;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataExtractionAttribute {
    private String name = null;
    private List<DataExtractionAttribute> children = null;

    public DataExtractionAttribute name(String name) {
        this.name = name;
        return this;
    }

    /**
     * The name of the attribute that needs to be extracted.
     **/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataExtractionAttribute children(List<DataExtractionAttribute> children) {
        this.children = children;
        return this;
    }

    public DataExtractionAttribute addChildrenItem(DataExtractionAttribute childrenItem) {
        if (this.children == null) {
            this.children = new ArrayList<DataExtractionAttribute>();
        }
        this.children.add(childrenItem);
        return this;
    }

    /**
     * The child attributes of interest; if not specified and this attribute has
     * children, rather than holding an atomic value, all nested content is to be
     * extracted
     **/

    public List<DataExtractionAttribute> getChildren() {
        return children;
    }

    public void setChildren(List<DataExtractionAttribute> children) {
        this.children = children;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataExtractionAttribute dataExtractionAttribute = (DataExtractionAttribute) o;
        return Objects.equals(this.name, dataExtractionAttribute.name)
                && Objects.equals(this.children, dataExtractionAttribute.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, children);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DataExtractionAttribute {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    children: ").append(toIndentedString(children)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

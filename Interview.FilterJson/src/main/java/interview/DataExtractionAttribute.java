
package interview;

import java.util.ArrayList;
import java.util.List;

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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((children == null) ? 0 : children.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DataExtractionAttribute other = (DataExtractionAttribute) obj;
        if (children == null) {
            if (other.children != null)
                return false;
        }
        else if (!children.equals(other.children))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        return true;
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

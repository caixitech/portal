package org.helloframework.codec.json;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.*;

import java.util.Iterator;

public abstract class JSONObjectAbstract<T> extends JsonDeserializer<T> {
    protected Object convertNode(TreeNode treeNode) {
        if (treeNode.isMissingNode())
            return null;
        if (treeNode.isValueNode()) {
            if (treeNode instanceof TextNode) {
                TextNode textNode = (TextNode) treeNode;
                return textNode.asText();
            }
            if (treeNode instanceof BinaryNode) {
                BinaryNode binaryNode = (BinaryNode) treeNode;
                return binaryNode.binaryValue();
            }
            if (treeNode instanceof BooleanNode) {
                BooleanNode booleanNode = (BooleanNode) treeNode;
                return booleanNode.booleanValue();
            }
            if (treeNode instanceof NumericNode) {
                NumericNode numericNode = (NumericNode) treeNode;
                return numericNode.numberValue();
            }
            if (treeNode instanceof NullNode) {
                return null;
            }
            return treeNode;
        }
        if (treeNode.isObject()) {
            JSONObject jsonObject = new JSONObject();
            Iterator<String> iterator = treeNode.fieldNames();
            while (iterator.hasNext()) {
                String fieldName = iterator.next();
                jsonObject.put(fieldName, convertNode(treeNode.get(fieldName)));
            }
            return jsonObject;
        }
        if (treeNode.isArray()) {
            int size = treeNode.size();
            JSONArray jsonArray = new JSONArray(size);
            for (int index = 0; index < size; index++) {
                jsonArray.add(convertNode(treeNode.get(index)));
            }
            return jsonArray;
        }
        return treeNode;
    }
}

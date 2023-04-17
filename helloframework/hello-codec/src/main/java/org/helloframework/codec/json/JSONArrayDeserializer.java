package org.helloframework.codec.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;

import java.io.IOException;

public class JSONArrayDeserializer extends JSONObjectAbstract<JSONArray> {
    @Override
    public JSONArray deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        if (!treeNode.isArray())
            return null;
        JSONArray jsonArray = (JSONArray) convertNode(treeNode);
        return jsonArray;
    }
}

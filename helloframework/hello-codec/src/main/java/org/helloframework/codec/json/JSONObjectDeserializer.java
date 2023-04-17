package org.helloframework.codec.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;

import java.io.IOException;

public class JSONObjectDeserializer extends JSONObjectAbstract<JSONObject> {
    @Override
    public JSONObject deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        if (!treeNode.isObject())
            return null;
        JSONObject jsonObject = (JSONObject) convertNode(treeNode);
        return jsonObject;
    }
}

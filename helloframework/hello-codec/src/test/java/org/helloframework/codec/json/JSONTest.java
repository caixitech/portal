package org.helloframework.codec.json;

import org.junit.Test;

import java.util.List;

public class JSONTest {

    @Test
    public void testParseArray(){
        String test = "[{\"key1\":\"value1\",\"kEY2\":1},{\"key1\":\"value2\",\"kEY2\":2},{\"key3\":\"value3\",\"kEY3\":3}]";
        JSONArray jsonArray = JSON.parseArray(test);
        System.err.println(jsonArray.toJSONString());
    }

    @Test
    public void testParseArray2(){
        String test = "[{\"key1\":\"value1\",\"kEY2\":1},{\"key1\":\"value2\",\"kEY2\":2},{\"key3\":\"value3\",\"kEY3\":3}]";
        List<JSONObject> parseArray = JSON.parseArray(test, JSONObject.class);
        System.err.println(JSON.toJSONString(parseArray));
    }

}
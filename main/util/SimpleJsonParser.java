package main.util;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class SimpleJsonParser {
    public static List<Map<String, String>> parseJsonArray(String json) {
        List<Map<String, String>> result = new ArrayList<>();
        int i = 0;
        int n = json.length();

        while (i < n && Character.isWhitespace(json.charAt(i)))
            i++;
        if (i >= n || json.charAt(i) != '[')
            throw new IllegalArgumentException("[JSON] Expected '['");
        i++; // skip '['

        while (true) {
            while (i < n && Character.isWhitespace(json.charAt(i)))
                i++;
            if (i >= n)
                throw new IllegalArgumentException("[JSON] Unexpected end");
            if (json.charAt(i) == ']') {
                i++; // end of array
                break;
            }

            if (json.charAt(i) != '{')
                throw new IllegalArgumentException("[JSON] Expected '{'");
            i++; // skip '{'

            Map<String, String> object = new HashMap<>();
            while (true) {
                while (i < n && Character.isWhitespace(json.charAt(i)))
                    i++;
                if (i >= n)
                    throw new IllegalArgumentException("[JSON] Unexpected end");
                if (json.charAt(i) == '}') {
                    i++; // end of object
                    break;
                }

                // read "key"
                if (json.charAt(i) != '"')
                    throw new IllegalArgumentException("[JSON] Expected key");
                i++; // skip '"' start of key
                int keyStart = i;
                while (i < n && json.charAt(i) != '"')
                    i++;
                if (i >= n)
                    throw new IllegalArgumentException("[JSON] Unclosed key");
                String key = json.substring(keyStart, i);
                i++; // skip '"' end of key

                while (i < n && Character.isWhitespace(json.charAt(i)))
                    i++;
                if (i >= n || json.charAt(i) != ':')
                    throw new IllegalArgumentException("[JSON] Expected ':'");
                i++; // skip ':'

                while (i < n && Character.isWhitespace(json.charAt(i)))
                    i++;

                // read "value"
                if (i >= n || json.charAt(i) != '"')
                    throw new IllegalArgumentException("[JSON] Expected string value");
                i++; // skip '"' start of value
                int valueStart = i;
                while (i < n && json.charAt(i) != '"')
                    i++;
                if (i >= n)
                    throw new IllegalArgumentException("[JSON] Unclosed value");
                String value = json.substring(valueStart, i);
                i++; // skip '"' end of value

                if (object.containsKey(key))
                    throw new IllegalArgumentException("[JSON] Duplicate key: " + key);
                object.put(key, value);

                while (i < n && Character.isWhitespace(json.charAt(i)))
                    i++;
                if (i >= n)
                    throw new IllegalArgumentException("[JSON] Unexpected end");

                // check next char of object
                if (json.charAt(i) == ',')
                    i++; // next "key":"value"
                else if (json.charAt(i) == '}') {
                    i++; // end of object
                    break;
                } else
                    throw new IllegalArgumentException("[JSON] Expected ',' or '}'");
            }
            result.add(object);

            while (i < n && Character.isWhitespace(json.charAt(i)))
                i++;
            if (i >= n)
                throw new IllegalArgumentException("[JSON] Unexpected end");

            // check next char in array
            if (json.charAt(i) == ',')
                i++; // next object
            else if (json.charAt(i) == ']') {
                i++; // end of array
                break;
            } else
                throw new IllegalArgumentException("[JSON] Expected ',' or ']'");
        }
        while (i < n && Character.isWhitespace(json.charAt(i)))
            i++;
        if (i != n)
            throw new IllegalArgumentException("[JSON] Trailing content");
        return result;
    }
}
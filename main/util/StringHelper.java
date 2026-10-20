package main.util;

import java.util.List;
import java.util.ArrayList;

public class StringHelper {
    public static String[] parseString(String input) {
        int i = 0;
        int n = input.length();
        List<String> list = new ArrayList<>();

        int start = i; // read command
        while (i < n && !Character.isWhitespace(input.charAt(i)) && input.charAt(i) != '-')
            i++;
        if (start == i)
            throw new IllegalArgumentException("Missing command");
        String cmd = input.substring(start, i);
        if (cmd.equals("mark") && input.charAt(i) != '-')
            throw new IllegalArgumentException("Invalid command. Usage: mark-status id");
        list.add(cmd);

        if (i < n && input.charAt(i) == '-') { // status in mark command
            i++; // skip '-'
            start = i; // read status
            while (i < n && !Character.isWhitespace(input.charAt(i)))
                i++;
            if (start == i)
                throw new IllegalArgumentException("Missing status");
            list.add(input.substring(start, i));
        }

        while (i < n) {
            if (input.charAt(i) == '"') {
                // read quote args
                start = ++i; // skip start '"'
                while (i < n && input.charAt(i) != '"')
                    i++;
                if (i >= n)
                    throw new IllegalArgumentException("Expected '\"'");
                String arg = input.substring(start, i);
                if (!arg.isBlank())
                    list.add(input.substring(start, i));
                i++; // skip close '"'
            } else {
                // read args
                start = i;
                while (i < n && !Character.isWhitespace(input.charAt(i)))
                    i++;
                String arg = input.substring(start, i);
                if (!arg.isBlank())
                    list.add(input.substring(start, i));
            }
            while (i < n && Character.isWhitespace(input.charAt(i)))
                i++;
        }
        if (list.size() > 3)
            throw new IllegalArgumentException("Too many arguments");
        return list.toArray(new String[0]);
    }
}

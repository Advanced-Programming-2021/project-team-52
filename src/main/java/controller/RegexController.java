package controller;

import model.tools.RegexPatterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class RegexController implements RegexPatterns {

    private static Matcher matcher;

    public static Matcher getMatcher(String command, Pattern pattern) {
        matcher = pattern.matcher(command);
        if (matcher.find())
            return matcher;
        else return null;
    }

    public static boolean hasField(Matcher matcher, String field) {
        try {
            return (matcher.group(field) != null);
        } catch (Exception e) {
            return false;
        }
    }

}

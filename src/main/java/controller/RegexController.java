package controller;

import model.tools.RegexPatterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class RegexController implements RegexPatterns{

    private static Matcher matcher;

    public static Matcher getMatcher(String command, Pattern pattern, boolean extractOnlyOneVariable){
        command = command.concat(" ");
        if (generalPattern.matcher(command).find() || extractOnlyOneVariable){
            matcher = pattern.matcher(command);
            if (matcher.find())
                return matcher;
        }
        return null;
    }

    public static Matcher getMatcherForAddOrRemoveCard(String command){
        command = command.concat(" ");
        if (addOrRemoveCardGeneralPattern.matcher(command).find()){
            matcher = addOrRemoveCardGeneralPattern.matcher(command);
            if (matcher.find())
                return matcher;
        }
        return null;
    }

    public static boolean hasField(Matcher matcher, String field){
        try {
            matcher.group(field);
            return true;
        } catch (Exception e){
            return false;
        }
    }

}

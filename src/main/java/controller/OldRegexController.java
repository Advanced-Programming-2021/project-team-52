package controller;

import java.util.HashMap;
import java.util.regex.Matcher;

import model.tools.OldRegexPattern;
// TODO : import and export regex
public class OldRegexController implements OldRegexPattern{

    private static Matcher generalMatcher;
    private static Matcher specificMatcher;
    private static HashMap<String, String> collected;

    {
        collected = new HashMap<>();
    }

    private OldRegexController(){}

    public static String getMenuInfo(String command){
        generalMatcher = menuRegex.matcher(command);
        if (generalMatcher.find())
            return generalMatcher.group(1);
        else return null;
    }

    public static String getCardShow(String command){
        generalMatcher = cardShowRegex.matcher(command);
        if (generalMatcher.find())
            return generalMatcher.group(1).trim();
        else return null;
    }

    public static String getDeckCreate(String command){
        generalMatcher = deckCreateRegex.matcher(command);
        if (generalMatcher.find())
            return generalMatcher.group(1).trim();
        else return null;
    }

    public static String getDeckDelete(String command){
        generalMatcher = deckDeleteRegex.matcher(command);
        if (generalMatcher.find())
            return generalMatcher.group(1).trim();
        else return null;
    }

    public static String getDeckActivate(String command){
        generalMatcher = deckActivateRegex.matcher(command);
        if (generalMatcher.find())
            return generalMatcher.group(1).trim();
        else return null;
    }

    public static String getPhaseInfo(String command){
        generalMatcher = phaseRegex.matcher(command);
        if (generalMatcher.find())
            return generalMatcher.group(1);
        else return null;
    }

    public static String getPositionInfo(String command){
        generalMatcher = positionRegex.matcher(command);
        if (generalMatcher.find())
            return generalMatcher.group(1);
        else return null;
    }

    public static String getAttackInfo(String command){
        generalMatcher = attackRegex.matcher(command);
        if (generalMatcher.find())
            return generalMatcher.group(1);
        else return null;
    }

    public static String getDuelWinnerByCheatInfo(String command){
        generalMatcher = cheatDuelWinnerRegex.matcher(command);
        if (generalMatcher.find())
            return generalMatcher.group(1);
        else return null;
    }

    public static String shopMatch(String command){
        if (command.startsWith("shop"))
            if (command.equals("shop show --all")) return "all";
            else {
                specificMatcher = shopBuyPattern.matcher(command);
                if (specificMatcher.find())
                    return specificMatcher.group(1);
            }
            return null;
    }

    public static HashMap<String, String> getLoginInfo(String command){
        collected.clear();
        command = command.replaceAll("^user login", "");
        generalMatcher = generalExtractor.matcher(command);
        while (generalMatcher.find()){
            if (getUsername() && getPassword(0, "p"))
                return null;
        }
        if (command.replaceAll(generalExtractor.toString(), "").matches("\\S+")) collected = null;
        return collected;
    }

    public static HashMap<String, String> getUserCreateInfo(String command){
        collected.clear();
        command = command.replaceAll("^user create", "");
        generalMatcher = generalExtractor.matcher(command);
        while (generalMatcher.find()){
            if (getUsername() && getPassword(0, "p") && getNickname())
                return null;
        }
        if (command.replaceAll(generalExtractor.toString(), "").matches("\\S+")) collected = null;
        return collected;
    }

    public static HashMap<String, String> getPasswordChangeInfo(String command){
        collected.clear();
        command = command.replaceAll("^profile change", "");
        generalMatcher = specialGeneralExtractor.matcher(command);
        if (generalMatcher.find()){
            if (generalMatcher.group(1).matches("^ --password| -p")){
                command = command.replaceAll("^ --password| -p", "");
                collected = new HashMap<>();
                generalMatcher = generalExtractor.matcher(command);
                while (generalMatcher.find())
                    if (getPassword(1, "c") && getPassword(2, "n"))
                        return null;
            }
        }
        if (command.replaceAll(generalExtractor.toString(), "").matches("\\S+")) collected = null;
        return collected;
    }

    public static HashMap<String, String> getAddCardInfo(String command){
        collected.clear();
        command = command.replaceAll("^deck add-card", "");
        if (getDeckAndCard(command)) return null;
        return collected;
    }

    public static HashMap<String, String> getRemoveCardInfo(String command){
        collected.clear();
        command = command.replaceAll("^deck rm-card","");
        if (getDeckAndCard(command)) return null;
        return collected;
    }

    private static boolean getDeckAndCard(String command) {
        command = findSideAndRemove(command);
        generalMatcher = generalExtractor.matcher(command);
        while (generalMatcher.find()) {
            if (getDeck() && getCard())
                return true;
        }
        if (command.replaceAll(generalExtractor.toString(), "").matches("\\S+")) collected = null;
        return false;
    }

    public static HashMap<String, String> getShowDeck(String command){
        collected.clear();
        command = command.replaceAll("^deck show", "");
        if (command.replaceAll("\\s", "").equals("--all")) {
            collected.put("all", "all");
        } else if (command.replaceAll("\\s", "").equals("--cards")){
            collected.put("cards", "cards");
        } else {
            command = findSideAndRemove(command);
            generalMatcher = generalExtractor.matcher(command);
            while (generalMatcher.find())
                if (getDeckName())
                    return null;
        }
        if (command.replaceAll(generalExtractor.toString(), "").matches("\\S+")) collected = null;
        return collected;
    }

    public static HashMap<String, String> getNewDuelInfo(String command){
        collected.clear();
        if (!command.matches("--new")) return null;
        command = command.replaceAll("--new", "");
        generalMatcher = generalExtractor.matcher(command);
        while (generalMatcher.find()){
            if (getNewDuelSecondPlayer() && getNewDuelRoundsInfo())
                return null;
        }
        if (command.replaceAll(generalExtractor.toString(), "").matches("\\S+")) collected = null;
        return collected;
    }

    public static HashMap<String, String> getSelectCardInfo(String command){
        collected.clear();
        command = command.replaceAll("^select", "");
        command = findFieldAndRemove(command);
        String wasThereForce = findForceAndRemove(command);
        if (command.equals(wasThereForce)) {
            command = findOpponentAndRemove(command);
            generalMatcher = generalExtractor.matcher(command);
            while (generalMatcher.find()) {
                if (getSelectMonsterInfo() && getSelectSpellInfo() && getSelectDInfo())
                    return null;
            }
        } else {
            command = wasThereForce;
            if (getSelectHandInfo())
                return null;
        }
        if (command.replaceAll(generalExtractor.toString(), "").matches("\\S+")) collected = null;
        return collected;
    }

    public static HashMap<String, String> getMoneyOrLPCheatInfo(String command){
        collected.clear();
        command = command.replaceAll("^increase", "");
        specificMatcher = cheatIncreaseMoneyRegex.matcher(command);
        if (specificMatcher.find())
            collected.put("m", specificMatcher.group(1));
        else {
            specificMatcher = cheatIncreaseLPRegex.matcher(command);
            if (specificMatcher.find())
                collected.put("lp", specificMatcher.group(1));
            else collected = null;
        }
        return collected;
    }

    private static boolean getUsername(){
        specificMatcher = usernameRegex.matcher(generalMatcher.group(1));
        if (specificMatcher.find()) {
            collected.put("u", specificMatcher.group(1));
            return false;
        } else return true;
    }

    private static boolean getPassword(int regexNumber, String name) {
        specificMatcher = passwordRegexes[regexNumber].matcher(generalMatcher.group(1));
        if (specificMatcher.find()) {
            collected.put(name, specificMatcher.group(1));
            return false;
        } else return true;
    }

    private static boolean getNickname(){
        specificMatcher = nicknameRegex.matcher(generalMatcher.group(1));
        if (specificMatcher.find()) {
            collected.put("n", specificMatcher.group(1));
            return false;
        } else return true;
    }

    private static boolean getDeck(){
        specificMatcher = deckRegex.matcher(generalMatcher.group(1));
        if (specificMatcher.find()){
            collected.put("de", specificMatcher.group(1));
            return false;
        } else return true;
    }

    private static boolean getCard(){
        specificMatcher = cardRegex.matcher(generalMatcher.group(1));
        if (specificMatcher.find()){
            collected.put("ca", specificMatcher.group(1));
            return false;
        } else return true;
    }

    private static String findSideAndRemove(String command){
        if (command.matches(sideRegex.toString())){
            collected.put("side", "side");
            command = command.replaceAll(sideRegex.toString(), "");
        }
        return command;
    }

    private static boolean getDeckName(){
        specificMatcher = deckNameRegex.matcher(generalMatcher.group(1));
        if (specificMatcher.find()){
            collected.put("ca", specificMatcher.group(1));
            return false;
        } else return true;
    }

    private static boolean getNewDuelSecondPlayer(){
        specificMatcher = secondPlayer.matcher(generalMatcher.group(1));
        if (specificMatcher.find()){
            if (specificMatcher.groupCount() == 1)
                collected.put("sp ai", "ai");
            else collected.put("sp", specificMatcher.group(2));
            return false;
        } else return true;
    }

    private static boolean getNewDuelRoundsInfo(){
        specificMatcher = roundsRegex.matcher(generalMatcher.group(1));
        if (specificMatcher.find()){
            collected.put("ca", specificMatcher.group(1));
            return false;
        } else return true;
    }

    private static String findOpponentAndRemove(String command){
        if (command.matches(opponentRegex.toString())){
            collected.put("opponent", "opponent");
            command = command.replaceAll(opponentRegex.toString(), "");
        }
        return command;
    }

    private static String findFieldAndRemove(String command){
        if (command.matches(fieldRegex.toString())){
            collected.put("field", "field");
            command = command.replaceAll(fieldRegex.toString(), "");
        }
        return command;
    }

    private static String findForceAndRemove(String command){
        if (command.matches(forceRegex.toString())){
            collected.put("field", "field");
            command = command.replaceAll(fieldRegex.toString(), "");
        }
        return command;
    }

    private static boolean getSelectMonsterInfo(){
        specificMatcher = monsterRegex.matcher(generalMatcher.group(1));
        if (specificMatcher.find()){
            collected.put("m", specificMatcher.group(1));
            return false;
        } else return true;
    }

    private static boolean getSelectSpellInfo(){
        specificMatcher = spellRegex.matcher(generalMatcher.group(1));
        if (specificMatcher.find()){
            collected.put("spe", specificMatcher.group(1));
            return false;
        } else return true;
    }

    private static boolean getSelectHandInfo(){
        specificMatcher = handRegex.matcher(generalMatcher.group(1));
        if (specificMatcher.find()){
            collected.put("h", specificMatcher.group(1));
            return false;
        } else return true;
    }

    private static boolean getSelectDInfo(){
        specificMatcher = DRegex.matcher(generalMatcher.group(1));
        if (specificMatcher.find()){
            collected.put("d", specificMatcher.group(1));
            return false;
        } else return true;
    }

}

package model.tools;

import java.util.regex.Pattern;

public interface RegexPatterns {
    Pattern loginPattern = Pattern.compile("^user login(?!.*(?:--password|-p).*(?:--password|-p))(?!.*(?:--username|" +
            "-u).*(?:--username|-u))(?=.*(?:--username|-u))(?=.*(?:--password|-p))(?: (?:(?:--password|-p) (?<passwo" +
            "rd>\\S+)|(?:--username|-u) (?<username>\\S+)))+$");
    Pattern menuPattern = Pattern.compile("^menu (?:(?:enter (?<enter>\\w+))|(?<exit>exit)|(?<showCurrent>show-current))" +
            "$");
    Pattern userCreatPattern = Pattern.compile("^user create(?!.*(?:--password|-p).*(?:--password|-p))(?!.*(?:--user" +
            "name|-u).*(?:--username|-u))(?!.*(?:--nickname|-n).*(?:--nickname|-n))(?=.*(?:--username|-u))(?=.*(?:--" +
            "password|-p))(?=.*(?:--nickname|-n))(?: (?:(?:--password|-p) (?<password>\\S+)|(?:--username|-u) (?<use" +
            "rname>\\S+)|(?:--nickname|-n) (?<nickname>\\S+)))+$");
    Pattern profileChangeNickNamePattern = Pattern.compile("^profile change --nickname (?<nickname>\\S+)$");
    Pattern profileChangePasswordPattern = Pattern.compile("^profile change(?!.*(?:--current|-c).*(?:--current|-c))(" +
            "?!.*(?:--new|-new).*(?:--new|-n))(?!.*(?:--password|-p).*(?:--password|-p))(?=.*(?:--current|-c))(?=.*(" +
            "?:--password|-p))(?=.*(?:--new|-n))(?: (?:(?:--current|-c) (?<current>\\S+)|(?:--new|-n) (?<new>\\S+)|(" +
            "?:--password|-p)))+$");
    Pattern cardShowPattern = Pattern.compile("^card show (?<card>[^-]+|--selected|-s)$");
    Pattern deckCreatePattern = Pattern.compile("^deck create (?<deck>[^-]+)$");
    Pattern deckDeletePattern = Pattern.compile("^deck delete (?<deck>[^-]+)$");
    Pattern deckSetActivePattern = Pattern.compile("^deck set-active (?<deck>[^-]+)$");
    Pattern deckAddCardPattern = Pattern.compile("^(?=deck (?:add|rm)-card(?: --?\\w+(?: [^-]+)?)+)deck (?<addOrRemove>ad" +
            "d|rm)-card(?!.*(?:--card|-c).*(?:--card|-c))(?!.*(?:--deck|-d).*(?:--deck|-d))(?!.*(?:--side|-s).*(?:--" +
            "side|-s))(?=.*(?:--card|-c))(?=.*(?:--deck|-d))(?: (?:(?:--card|-c) (?<card>[^-]+)|(?:--deck|-d) (?<dec" +
            "k>[^-]+)|(?<side>--side|-s)))+$");
    Pattern deckShowPattern = Pattern.compile("^deck show(?: (?:--all|-a)(?<all>)| (?:--cards|-c)(?<card>)|(?!.*(?:-" +
            "-deck|-d).*(?:--deck|-d))(?!.*(?:--side|-s).*(?:--side|-s))(?=.*(?:--deck|-d))(?: (?:(?:--deck|-d) (?<d" +
            "eck>[^-]+)|(?:--side|-s)(?<side>)))+)$");
    Pattern shopBuyPattern = Pattern.compile("^shop (?:buy (?<card>[^-]+)|show (?:--all|-a)(?<all>))$");
    Pattern newDuelPattern = Pattern.compile("^duel(?!.*(?:--second-player|-sp|--ai|-a).*(?:--second-player|-sp|--ai" +
            "|-a))(?!.*(?:--rounds|-r).*(?:--rounds|-r))(?!.*(?:--new|-n).*(?:--new|-n))(?=.*(?:--new|-n))(?=.*(?:--" +
            "rounds|-r))(?=.*(?:--second-player|-sp|--ai|-a))(?: (?:(?:(?:--second-player|-sp) (?<secondPlayer>\\w+)" +
            "|(?:--ai|-a))|(?:--rounds|-r) (?<rounds>[13])|--new|-n))+$");
    Pattern selectCardPattern = Pattern.compile("^(?=\\w+ --?\\w+(?: \\w+)?)select(?:(?!.*(?:(?:--monster|-m|--spell" +
            "|-s)(?: --opponent| -o)?|(?:--hand|-h)|(?:--field|-f)).*(?:(?:--monster|-m|--spell|-s)(?: --opponent| -" +
            "o)?|(?:--hand|-h)|(?:--field|-f)))(?!.*(?:--opponent|-o).*(?:--opponent|-o))(?=.*(?:(?:--monster|-m|--s" +
            "pell|-s)(?: --opponent| -o)?|(?:--hand|-h)(?!.*(?:--opponent|-o))|(?:--field|-f)))(?: (?:(?:(?<type>--m" +
            "onster|-m|--spell|-s)(?<opponent> --opponent| -o)?|(?<typeHand>--hand|-h)) (?<select>\\d+)|(?<typeField" +
            ">--field|-f)|(?<opponent3>--opponent|-o)))+| (?<delete>-d))$");
    Pattern setAttackOrDefensePattern = Pattern.compile("^set (?:--position|-o) (?<position>attack|defense)$");
    Pattern attackPattern = Pattern.compile("^attack (?<type>direct|\\w+)$");
    Pattern increaseMoneyOrLPByCheatPattern = Pattern.compile("^increase(?!.*(?:--money|-m).*(?:--money|-m))(?!.*(?:" +
            "--LP|-l).*(?:--LP|-l))(?: (?:(?:--money|-m) (?<money>\\d+)|(?:--LP|-l) (?<LP>\\d+)))+$");
    Pattern setDuelWinnerByCheat = Pattern.compile("^duel set-winner (?<username>\\w+)$");
    Pattern importOrExportCard = Pattern.compile("^(?:import|export) card [^-]+$");


    Pattern attackBoostPattern = Pattern.compile("attack boost (?<amount>\\d+) (?<all>all)");
    Pattern reduceAttackerAttackPattern = Pattern.compile("reduce attacker attack (?<amount>all|\\d+) (?<oneRound>oneRound)? (?<oneUse>oneUse)? (?<faceUp>faceUp)?");
    Pattern tributePattern = Pattern.compile("tribute (?<amount>\\d+) (?<faceUpOnly>face up only)? (?<alternative>.+)?");
    Pattern drawCardPattern = Pattern.compile("draw card (?<amount>\\d+)");

    Pattern extractEndingNumber = Pattern.compile("^\\w+(-?\\d+)$");

    Pattern standardPassword = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
    Pattern standardUsernameAndNickname = Pattern.compile("^\\w+$");


}


/* Pattern template:
^(?=\w+ (?:--?\w+(?: \w+)? )+)
\w+
(?!.*(?:--b|-b).*(?:--b|-b))(?!.*(?:--a|-a).*(?:--a|-a))(?!.*(?:--side|-s).*(?:--side|-s))
(?=.*(?:--a|-a))(?=.*(?:--b|-b))(?=.*(?:--s|-s))
(?: (?:(?:(?:--a|-a) (?<a>\w+))|(?:(?:--b|-b) (?<b>\w+))|(?:(?:--s|-s)(?<s>))))+$

explanation:
(?=\w+ (?:--?\w+(?: \w+)? )+) :
    checks if the String has the right format
    can be removed
\w+ :
    what the string starts with
(?!.*(?:--b|-b).*(?:--b|-b))(?!.*(?:--a|-a).*(?:--a|-a))(?!.*(?:--side|-s).*(?:--side|-s)) :
    checks if there is not any duplicate
(?=.*(?:--a|-a))(?=.*(?:--b|-b))(?=.*(?:--s|-s)) :
    checks so the mandatory fields exist
(?: (?:(?:(?:--a|-a) (?<a>\w+))|(?:(?:--b|-b) (?<b>\w+))|(?:(?:--s|-s)(?<s>))))+ :
    extract information
 */
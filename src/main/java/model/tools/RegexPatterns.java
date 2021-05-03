package model.tools;

import java.util.regex.Pattern;

public interface RegexPatterns {
    Pattern loginPattern = Pattern.compile("^(?=\\w+(?: --?\\w+(?: \\w+)?)+)login user(?!.*(?:--password|-p)" +
            ".*(?:--password|-p))(?!.*(?:--username|-u).*(?:--username|-u))(?=.*(?:--username|-u))(?=.*(?:--" +
            "password|-p))(?: (?:(?:--password|-p) (?<password>\\w+)|(?:--username|-u) (?<username>\\w+)))+$");
    Pattern menuPattern = Pattern.compile("^menu (?:enter(?<enter> \\w+)|(?<exit>exit)|(?<showCurrent>show-c" +
            "urrent))$");
    Pattern userCreatPattern = Pattern.compile("^(?=\\w+(?: --?\\w+(?: \\w+)?)+)user create(?!.*(?:--passwor" +
            "d|-p).*(?:--password|-p))(?!.*(?:--username|-u).*(?:--username|-u))(?!.*(?:--nickname|-n).*(?:-" +
            "-nickname|-n)).(?=.*(?:--username|-u))(?=.*(?:--password|-p))(?=.*(?:--nickname|-n))(?: (?:(?:-" +
            "-password|-p) (?<password>\\w+)|(?:--username|-u) (?<username>\\w+)|(?:--nickname|-n) (?<nickna" +
            "me>\\w+)))+$");
    Pattern profileChangeNickNamePattern = Pattern.compile("^profile change --nickname (?<nickname> \\w+)$");
    Pattern profileChangePasswordPattern = Pattern.compile("^(?=\\w+(?: --?\\w+(?: \\w+)?)+)profile change(?" +
            "!.*(?:--current|-c).*(?:--current|-c))(?!.*(?:--new|-new).*(?:--new|-n))(?!.*(?:--password|-p)." +
            "*(?:--password|-p))(?=.*(?:--current|-c))(?=.*(?:--password|-p))(?=.*(?:--new|-n))(?: (?:(?:--c" +
            "urrent|-c) (?<current>\\w+)|(?:--new|-n) (?<new>\\w+)|(?:--password|-p)))+$");
    Pattern cardShowPattern = Pattern.compile("^card show (?<card>[^-]+|--selected|-s)$");
    Pattern deckCreatePattern = Pattern.compile("^deck create (?<deck>[^-]+)$");
    Pattern deckDeletePattern = Pattern.compile("^deck delete (?<deck>[^-]+)$");
    Pattern deckSetActivePattern = Pattern.compile("^deck set-active (?<deck>[^-]+)$");
    Pattern deckAddCardPattern = Pattern.compile("^(?=deck (?:add|rm)(?: --?\\w+(?: [^-]+)?)+)deck (?<addOrR" +
            "emove>add|rm)-card(?!.*(?:--card|-c).*(?:--card|-c))(?!.*(?:--deck|-d).*(?:--deck|-d))(?!.*(?:-" +
            "-side|-s).*(?:--side|-s))(?=.*(?:--card|-c))(?=.*(?:--deck|-d))(?: (?:(?:--card|-c) (?<card>[^-" +
            "]+)|(?:--deck|-d) (?<deck>[^-]+)|(?<side>--side|-s)))+$");
    Pattern deckShowPattern = Pattern.compile("^deck show(?: (?:--all|-a)(?<all>)| (?:--cards|-c)(?<card>)|(" +
            "?!.*(?:--deck|-d).*(?:--deck|-d))(?!.*(?:--side|-s).*(?:--side|-s))(?=.*(?:--deck|-d))(?: (?:(?" +
            ":--deck|-d) (?<deck>[^-]+)|(?:--side|-s)(?<side>)))+)$");
    Pattern shopBuyPattern = Pattern.compile("^shop (?:buy (?<card>[^-]+)|show (?:--all|-a)(?<all>))$");
    Pattern newDuelPattern = Pattern.compile("^(?=\\w+(?: --?\\w+(?: \\w+)?)+)duel(?!.*(?:--second-player|-s" +
            "p|--ai|a).*(?:--second-player|-sp|--ai|-a))(?!.*(?:--rounds|-r).*(?:--rounds|-r))(?!.*(?:--new|" +
            "-n).*(?:--new|-n))(?=.*(?:--new|-n))(?=.*(?:--rounds|-r))(?=.*(?:--second-player|-sp|--ap|-a))(" +
            "?: (?:(?:(?:--second-player|-sp) (?<secondPlayer>\\w+)|(?:--ai|-a))|(?:--rounds|-r) (?<rounds>[" +
            "13])))+$");
    Pattern selectCardPattern = Pattern.compile("^(?=\\w+ --?\\w+(?: \\w+)?)select(?:(?!.*(?:(?:--monster|-m" +
            "|--spell|-s|--field|-f)(?: --opponent| -o)?|(?:--hand|-h)).*(?:(?:--monster|-m|--spell|-s|--fie" +
            "ld|-f)(?: --opponent| -o)?|(?:--hand|-h)))(?!.*(?:--opponent|-o).*(?:--opponent|-o))(?=.*(?:(?:" +
            "--monster|-m|--spell|-s|--field|-f)(?: --opponent| -o)?|(?:--hand|-h)(?!.*(?:--opponent|-o))))(" +
            "?: (?:(?:(?<type>--monster|-m|--spell|-s|--field|-f)(?<opponent> --opponent| -o)?|(?<typeHand>-" +
            "-hand|-h)) (?<select>\\d+)|(?<opponent2>--opponent|-o)))+| (?<delete>-d))$");
    Pattern setAttackOrDefensePattern = Pattern.compile("^set (?:--position|-o) (?<position>attack|defense) $");
    Pattern attackPattern = Pattern.compile("^attack (?<type>direct|\\w+)$");
    Pattern increaseMoneyOrLPByCheatPattern = Pattern.compile("^(?=\\w+(?: --?\\w+(?: \\w+)?)+)increase(?!.*" +
            "(?:--money|-m).*(?:--money|-m))(?!.*(?:--LP|-l).*(?:--LP|-l))(?: (?:(?:--money|-m) (?<money>\\d" +
            "+)|(?:--LP|-l) (?<LP>\\d+)))+$");
    Pattern setDuelWinnerByCheat = Pattern.compile("^duel set-winner (?<username>\\w+) $");
    Pattern importOrExportCard = Pattern.compile("^(?:import|export) card [^- ] $");
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
\w+ :
    what the string starts with
(?!.*(?:--b|-b).*(?:--b|-b))(?!.*(?:--a|-a).*(?:--a|-a))(?!.*(?:--side|-s).*(?:--side|-s)) :
    checks if there is not any duplicate
(?=.*(?:--a|-a))(?=.*(?:--b|-b))(?=.*(?:--s|-s)) :
    checks so the mandatory fields exist
(?: (?:(?:(?:--a|-a) (?<a>\w+))|(?:(?:--b|-b) (?<b>\w+))|(?:(?:--s|-s)(?<s>))))+ :
    extract information
 */
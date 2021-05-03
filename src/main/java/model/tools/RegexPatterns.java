package model.tools;

import java.util.regex.Pattern;

public interface RegexPatterns {
    /*^\w+(?=.* (?:--a|-a) (?<a>\w+)(?= ))(?=.* (?:--b|-b) (?<b>\w+)(?= ))(?:(?=.* (?:--side|-s)(?<side>)?(?: )))?
        (?!.*(?:--b|-b).*(?:--b|-b))(?!.*(?:--a|-a).*(?:--a|-a))(?!.*(?:--side|-s).*(?:--side|-s))
        .*(?:(?:(?:--a|-a) \w+)|(?:(?:--b|-b) \w+)|(?:--side|-s)) $*/
    Pattern generalPattern = Pattern.compile("^\\w+ (?:--?\\w+(?: \\w+)? )+$");
    Pattern loginPattern = Pattern.compile("^login user(?=.* (?:--password|-p) (?<password>\\w+)(?= ))(?=.* (?:--username|-u) (?<username>\\w+)(?= ))(?!.*(?:--password|-p).*(?:--password|-p))(?!.*(?:--username|-u).*(?:--username|-u)).*(?:(?:--password|-p) \\w+|(?:--username|-u) \\w+) $");
    Pattern menuPattern = Pattern.compile("^menu (?:enter(?<enter> \\w+)|(?<exit>exit)|(?<showCurrent>show-current))$");
    Pattern userCreatPattern = Pattern.compile("^user create(?=.* (?:--password|-p) (?<password>\\w+)(?= ))(?=.* (?:--username|-u) (?<username>\\w+)(?= ))(?=.* (?:--nickname|-n) (?<nickname>\\w+)(?= ))(?!.*(?:--password|-p).*(?:--password|-p))(?!.*(?:--username|-u).*(?:--username|-u))(?!.*(?:--nickname|-n).*(?:--nickname|-n)).*(?:(?:--password|-p) \\w+|(?:--username|-u) \\w+|(?:--nickname|-n) \\w+) $");
    Pattern profileChangeNickNamePattern = Pattern.compile("^profile change --nickname (?<nickname> \\w+)$");
    Pattern profileChangePasswordPattern = Pattern.compile("^profile change(?=.* (?:--current|-c) (?<current>\\w+)(?= ))(?=.* (?:--new|-n) (?<new>\\w+)(?= ))(?:(?=.* (?:--password|-p)(?<password>)?(?: )))?(?!.*(?:--current|-c).*(?:--current|-c))(?!.*(?:--new|-new).*(?:--new|-n))(?!.*(?:--password|-p).*(?:--password|-p)).*(?:(?:--current|-c) \\w+|(?:--new|-n) \\w+|(?:--password|-p)) $");
    Pattern cardShowPattern = Pattern.compile("^card show (?<card>[^-]+)$");
    Pattern deckCreatePattern = Pattern.compile("^deck create (?<deck>[^-]+)$");
    Pattern deckDeletePattern = Pattern.compile("^deck delete (?<deck>[^-]+)$");
    Pattern deckSetActivePattern = Pattern.compile("^deck set-active (?<deck>[^-]+)$");
    Pattern deckAddCardPattern = Pattern.compile("^deck (?<addOrRemove>add|rm)-card(?=.* (?:--card|-c) (?<card>[^-]+)(?= -))(?=.* (?:--deck|-d) (?<deck>[^-]+)(?= ))(?:(?=.* (?:--side|-s)(?<side>)?(?: )))?(?!.*(?:--card|-c).*(?:--card|-c))(?!.*(?:--deck|-d).*(?:--deck|-d))(?!.*(?:--side|-s).*(?:--side|-s)).*(?:(?:(?:--card|-c) [^-]+)|(?:(?:--deck|-d) [^-]+)|(?:--side|-s)) $");
    Pattern addOrRemoveCardGeneralPattern = Pattern.compile("^\\w+ (?:--?\\w+(?: [^-]+)? )+$");
    Pattern deckShowPattern = Pattern.compile("^deck show(?: (?:--all|-a)(?<all>)| (?:--cards|-c)(?<card>)|(?=.* (?:--deck|-d) (?<deck>[^-]+)(?= ))(?:(?=.* (?:--side|-s)(?<side>)?(?: )))?(?!.*(?:--deck|-d).*(?:--deck|-d))(?!.*(?:--side|-s).*(?:--side|-s)).*(?:(?:--deck|-d) [^-]+|(?:--side|-s))) $");
    Pattern shopBuyPattern = Pattern.compile("^shop (?:buy (?<card>[^-]+)|show (?:--all|-a)(?<all>))$");
    Pattern newDuelPattern = Pattern.compile("^duel(?:(?=.* (?:--second-player|-sp) (?<secondPlayer>\\w+)(?= ))|(?=.* (?:--ai|-a)(?<sconPlayer>)?(?: )))(?=.* (?:--rounds|-r) (?<rounds>[13])(?= ))(?=.* (?:--new|-n)(?<new>)?(?: ))(?:(?!.*(?:--second-player|-sp).*(?:--second-player|-sp))|(?!.*(?:--ai|-a).*(?:--ai|-a)))(?!.*(?:--rounds|-r).*(?:--rounds|-r))(?!.*(?:--new|-n).*(?:--new|-n)).*(?:(?:(?:--second-player|-sp) \\w+|(?:--ai|-a))|(?:(?:--rounds|-r) [13])|(?:--new|-n)) $");
}


/* Pattern template:
one : ^\w+
        (?=.* (?:--a|-a) (?<a>\w+)(?= ))(?=.* (?:--b|-b) (?<b>\w+)(?= ))(?:(?=.* (?:--side|-s)(?<side>)?(?: )))?
            (?!.*(?:--b|-b).*(?:--b|-b))(?!.*(?:--a|-a).*(?:--a|-a))(?!.*(?:--side|-s).*(?:--side|-s))
                .*(?:(?:(?:--a|-a) \w+)|(?:(?:--b|-b) \w+)|(?:--side|-s)) $
two : ^\w+ (?:--?\w+(?: \w+)? )+$

both of them should be used for correct result.

pattern one  :
\w+ :
    matches what the command starts with.
(?:--a|-a) (?<a>\w+)(?= ))(?=.* (?:--b|-b) (?<b>\w+)(?= )) :
    matches groups that need capturing information.
(?:(?=.* (?:--side|-s)(?<side>)?(?: )))? :
    matches groups that dont need capturing information ,
    their existence should be checked using try catch.
(?!.*(?:--b|-b).*(?:--b|-b))(?!.*(?:--a|-a).*(?:--a|-a))(?!.*(?:--side|-s).*(?:--side|-s)) :
    prevents regex from matching if there is a duplicate input.
.*(?:(?:(?:--a|-a) \w+)|(?:(?:--b|-b) \w+)|(?:--side|-s)) $ :
    checks if the end of the string is correct.

pattern two :
prevents regex from matching inputs like the following :
    "aa -a sdsd @#sds -ds adsda "

note :
one space ( ) should be added at the end of the string.
 */
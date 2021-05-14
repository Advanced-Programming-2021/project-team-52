package model.tools;

import java.util.regex.Pattern;

public interface OldRegexPattern {

    Pattern generalExtractor = Pattern.compile(" ((?:--|-)\\w+ \\w+)"); // notice there is a space at the start
    Pattern specialGeneralExtractor = Pattern.compile(" ((?:--|-)\\w+(?: (?:--|-)\\w+ \\w+){2})");
    Pattern usernameRegex = Pattern.compile("(?:--username|-u) (\\w+)");
    Pattern[] passwordRegexes = {
            Pattern.compile("(?:--password|-p) (\\w+)"),
            Pattern.compile("(?:--current|-c) (\\w+)"),
            Pattern.compile("(?:--new|-n) (\\w+)")
    };
    Pattern nicknameRegex = Pattern.compile(("(?:--nickname|-n) (\\w+)"));
    Pattern deckRegex = Pattern.compile(("(?:--deck|-de) (\\w+)"));
    Pattern cardRegex = Pattern.compile(("(?:--card|-ca) (\\w+)"));
    Pattern menuRegex = Pattern.compile("^menu enter (\\w+)$");
    Pattern cardShowRegex = Pattern.compile("^card show (.+)$");
    Pattern deckCreateRegex = Pattern.compile("^deck create (.+)$");
    Pattern deckDeleteRegex = Pattern.compile("^deck delete (.+)$");
    Pattern deckActivateRegex = Pattern.compile("^deck set-active (.+)$");
    Pattern sideRegex = Pattern.compile("--side|-s");
    Pattern deckNameRegex = Pattern.compile("(?:--deck-name|-dn) (\\w+)");
    Pattern shopBuyPattern = Pattern.compile("shop buy (\\w+)");
    Pattern secondPlayer = Pattern.compile("(--second-player|-sp)? (\\w+|--ai)");
    Pattern roundsRegex = Pattern.compile("(?:--rounds|-r) (\\d+)");
    Pattern monsterRegex = Pattern.compile("(?:--monster|-m) (\\d+)");
    Pattern spellRegex = Pattern.compile("(?:--spell|-spe) (\\d+)");
    Pattern handRegex = Pattern.compile("(?:--hand|-h) (\\d+)");
    Pattern opponentRegex = Pattern.compile("--opponent|-p");
    Pattern fieldRegex = Pattern.compile("--field|-f");
    Pattern forceRegex = Pattern.compile("--force|-fo");
    Pattern DRegex = Pattern.compile("-d");
    Pattern phaseRegex = Pattern.compile("^Phase: (\\w+)$");
    Pattern positionRegex = Pattern.compile("(?:--position|-po) (\\w+)");
    Pattern attackRegex = Pattern.compile("^attack (\\d+)$");
    Pattern cheatIncreaseMoneyRegex = Pattern.compile("(?:--money|-m) (\\d+)");
    Pattern cheatIncreaseLPRegex = Pattern.compile("(?:--LP|-lp) (\\d+)");
    Pattern cheatDuelWinnerRegex = Pattern.compile("^duel set-winner (.+)$");
}

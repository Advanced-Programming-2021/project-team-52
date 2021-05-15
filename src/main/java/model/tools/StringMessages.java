package model.tools;

public interface StringMessages {

    String drawPhase = "phase: draw phase";
    String mainPhase = "phase: main phase";
    String standbyPhase = "phase: standby phase";
    String endPhase = "phase: end phase";
    String battlePhase = "phase: battle phase";
    String invalidCommand = "invalid command";
    String noCardsIsSelectedYet = "no card is selected yet";
    String cantSummonThisCard = "you can’t summon this card";
    String actionNotAllowedInThisPhase = "action not allowed in this phase";
    String monsterCardZoneIsFull = "monster card zone is full";
    String alreadySummonedORSetOnThisTurn = "you already summoned/set on this turn";
    String summonedSuccessfully = "summoned successfully";
    String thereAreNotEnoughCardsForTribute = "there are not enough cards for tribute";
    String thereNoMonstersOneThisAddress = "there no monsters one this address";
    String setSuccessfully = "set successfully";
    String invalidPhase = "you can't do this answer in this phase";

    String cantAttackWithThisCard = "you can’t attack with this card";
    String cantAttackTheOpponentDirectly = "you can’t attack the opponent directly";
    String CardAlreadyAttacked = "this card already attacked";

    String cantChangeThisCardPosition = "you can’t change this card position";
    String cardIsAlreadyInTheWantedPosition = "this card is already in the wanted position";
    String alreadyChangedThisCardPositionInThisTurn = "you already changed this card position in this turn";
    String monsterCardPositionChangedSuccessfully = "monster card position changed successfully";

    String cantFlipSummonThisCard = "you can’t flip summon this card";
    String flipsSummonedSuccessfully = "flip summoned successfully";

    String noCardToAttackHere = "there is no card to attack here";
    String drawingAgainstOO = "both you and your opponent monster cards are destroyed and no one receives damage";
    String winingAgainstDO = "the defense position monster is destroyed";
    String drawingAgainstDO = "no card is destroyed";

    String getTribute = "please specify the next tribute :";
    String wrongTribute = "wrong tribute";
    String doAlternativeSpecial = "do alternative ?";
    String askStatus = "status ?";

    // ProfileController
    String nicknameChangedSuccessfully = "nickname changed successfully";
    String currentPasswordIsInvalid = "current password is invalid";
    String enterNewPassword = "please enter a new password";
    String passwordChangedSuccessfully = "password changed successfully";
    String menuNavigationIsNotPossible = "menu navigation is not possible";

    // ShopController
    String cardBoughtSuccessfully = "card bought successfully";
    String noCardWithThisName = "there is no card with this name";
    String notEnoughMoney = "not enough money";


    // Menu show current
    // todo : is this enough (without abbreviation mode) ??
    String showCurrentInProfileController = "Profile menu :\n" +
            "profile change --nickname <nickname>\n" +
            "profile change --password --current <current password> --new <new password>\n" +
            "menu show-current\n" +
            "menu enter <menu name>\n" +
            "menu exit";
    String showCurrentInShopController = "Shop menu :\n" +
            "shop buy <card name>\n" +
            "shop show --all\n" +
            "menu show-current\n" +
            "menu enter <menu name>\n" +
            "menu exit";
    String getShowCurrentInScoreboardController = "Scoreboard menu :\n" +
            "scoreboard show\n" +
            "menu show-current\n" +
            "menu enter <menu name>\n" +
            "menu exit";

    // DeckController
    String deckCreatedSuccessfully = "deck created successfully!";
    String deckDeletedSuccessfully = "deck deleted successfully";
    String deckActivatedSuccessfully = "deck activated successfully";
    String cardAddedToDeckSuccessfully = "card added to deck successfully";
    String mainDeckIsFull = "main deck is full";
    String sideDeckIsFull = "side deck is full";
    String cardRemovedFormDeckSuccessfully = "card removed form deck successfully";
    //just add this delete it in term of conff
    String userLoggedoutSuccessfully = "user Loggedout Successfully!";
    
    /*
     //MainController
    String impossibilityOfMenuNavigation = "menu navigation is not possible";
    String invalidMenu = "invalid menu";
    String showMainMenu = "Main menu :\n" +
            "menu show-current\n" +
            "menu enter <menu name>\n" +
            "menu exit";
    String shouldLogoutToExit = "You have to logout to exit";
    String userLoggedOutSuccessfully = "user logged out successfully!";

    //LoginController
    String loginFirst = "please login first";
    String showLoginMenu = "Login menu :\n" +
            "user create --username <username> --nickname <nickname> --password <password> :\n" +
            "user login --username <username> --password <password>\n" +
            "menu show-current\n" +
            "menu enter <menu name>\n" +
            "menu exit";
    String createUserFailedBecauseOfUsername = "create user failed! only letter, digit or underscore are valid for username";
    String createUserFailedBecauseOfNickname = "create user failed! only letter, digit or underscore are valid for nickname";
    String createUserFailedBecauseOfPasswordWeakness = "weak password! please choose a password with\n" +
            "minimum eight characters, at least one uppercase letter, one lowercase letter and  one number";
    String createUserSuccessfully = "user created successfully!";
    String usernameAndPasswordDoNotMatch = "Username and password didn't match!";
    String userLoggedInSuccessfully = "user logged in successfully!";
    */

}

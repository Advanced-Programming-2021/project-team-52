package sample.model.tools;

public interface StringMessages {


    // ProfileController
    String nicknameChangedSuccessfully = "nickname changed successfully";
    String currentPasswordIsInvalid = "current password is invalid";
    String usernameChangedSuccessfully = "username changed successfully";
    String enterNewPassword = "please enter a new password";
    String passwordChangedSuccessfully = "password changed successfully";
    String menuNavigationIsNotPossible = "menu navigation is not possible";
    String nonStandardNickname = "only letter, digit or underscore are valid for nickname";
    String nonStandardUsername = "only letter, digit or underscore are valid for nickname";
    String nonStandardPassword = "weak password! please choose a password with\n" +
            "minimum eight characters, at least one uppercase letter, one lowercase letter and  one number";
    String noImageWithThisName = "There is no image with this name";
    String imageChangedSuccessfully = "Image changed successfully";
    String inputPasswordsDoesNotMatch = "Input passwords doesn't match";
    String THERE_IS_NO_IMAGE_WITH_THIS_PATH = "There is no image with this path";


    // ShopController
    String cardBoughtSuccessfully = "card bought successfully";
    String noCardWithThisName = "there is no card with this name";
    String notEnoughMoney = "not enough money";

    // ImportAndExportController
    String cardWithThisNameIsNotValid = "card with this name is not valid";
    String cardWithThisNameHasAlreadyExported = "card with this name already exported";
    String cardExportedSuccessfully = "card has been exported successfully";
    String thereIsNoCardWithThisNameToImport = "there is no card with this name to import";
    String thereIsAlreadyACardWithThisName = "there is already a card with this name";
    String cardImportedSuccessfully = "card has been imported successfully";
    String error = "Error";


    // Menu show current
    String showCurrentInProfileController = "Profile menu :\n" +
            "profile change --nickname <nickname>\n" +
            "profile change --password --current <current password> --new <new password>\n" +
            "profile change -p -c <current password> -n <new password>\n" +
            "menu show-current\n" +
            "menu exit";
    String showCurrentInShopController = "Shop menu :\n" +
            "shop buy <card name>\n" +
            "shop show --all\n" +
            "shop show -a\n" +
            "card show <card name>\n" +
            "menu show-current\n" +
            "menu exit";
    String getShowCurrentInScoreboardController = "Scoreboard menu :\n" +
            "scoreboard show\n" +
            "menu show-current\n" +
            "menu exit";
    String showCurrentInExportAndImportController = "Import and Export menu :\n" +
            "import card <card name>\n" +
            "export card <card name>\n" +
            "menu show-current\n" +
            "menu exit";
    String showCurrentInDeckController = "Deck menu:\n" +
            "deck create <deck name>\n" +
            "deck delete <deck name>\n" +
            "deck set-activate <deck name>\n" +
            "deck add-card --card <card name> --deck <deck name> --side(optional)\n" +
            "deck add-card -c <card name> -d <deck name> -s(optional)\n" +
            "deck rm-card --card <card name> --deck <deck name> --side(optional)\n" +
            "deck rm-card -c <card name> -d <deck name> -s(optional)\n" +
            "deck show --all\n" +
            "deck show -a\n" +
            "deck show --deck-name <deck name> --side(Opt)\n" +
            "deck show -dn <deck name> -s(Opt)\n" +
            "deck show --cards\n" +
            "deck show -c\n" +
            "card show <card name>\n" +
            "menu show-current\n" +
            "menu exit";

    // DeckController
    String deckCreatedSuccessfully = "deck created successfully!";
    String deckDeletedSuccessfully = "deck deleted successfully";
    String deckActivatedSuccessfully = "deck activated successfully";
    String cardAddedToDeckSuccessfully = "card added to deck successfully";
    String mainDeckIsFull = "main deck is full";
    String sideDeckIsFull = "side deck is full";
    String cardRemovedFormDeckSuccessfully = "card removed form deck successfully";


    // DuelController
    String thereIsNoPlayerWithThisUsername = "there is no player with this username";
    String numberOfRoundsIsNotSupported = "number of rounds is not supported";
    String youCantDuelWithYourself = "you cant duel with yourself";

    //MainController
    String impossibilityOfMenuNavigation = "menu navigation is not possible";
    String invalidMenu = "invalid menu";
    String showMainMenu = "Main menu :\n" +
            "menu show-current\n" +
            "menu enter <menu name>\n" +
            "user logout";
    String shouldLogoutToExit = "You have to logout to exit";
    String userLoggedOutSuccessfully = "user logged out successfully!";

    //LoginController
    String loginFirst = "please login first";
    String showLoginMenu = "Login menu :\n" +
            "user create --username <username> --nickname <nickname> --password <password> :\n" +
            "user create -u <username> -n <nickname> -p <password> :\n" +
            "user login --username <username> --password <password>\n" +
            "user login -u <username> -p <password>\n" +
            "menu show-current\n" +
            "menu exit";
    String createUserFailedBecauseOfUsername = "create user failed! only letter," +
            " digit or underscore are valid for username";
    String createUserFailedBecauseOfNickname = "create user failed! only letter," +
            " digit or underscore are valid for nickname";
    String createUserFailedBecauseOfPasswordWeakness = "weak password! please choose a password with\n" +
            "minimum eight characters, at least one uppercase letter, one lowercase letter and  one number";
    String createUserSuccessfully = "user created successfully!";
    String usernameAndPasswordDoNotMatch = "Username and password didn't match!";
    String userLoggedInSuccessfully = "user logged in successfully!";


    //AI
    String nextPhaseAI = "next phase";
    String summonAI = "summon";
    String activateSpellAI = "activate effect";
    String activateTrapAI = "activate effect";
    String setSpellAI = "set";
    String setTrapAI = "set";
    String responseToStatusAI = "attack";
    String graveYardAI = "graveyard";
    String directAttackAI = "attack direct";
    String yesAI = "yes";
    String noAI = "no";
    String cancelAI = "cancel";
    String endAI = "end";

    //Card Creator
    String THERE_IS_NO_CARD_WITH_THIS_NAME_IN_MONSTERS = "There is no card with this name in Monsters";
    String THERE_IS_NO_CARD_WITH_THIS_NAME_IN_SPELLS = "There is no card with this name in Spells";
    String THERE_IS_NO_CARD_WITH_THIS_NAME_IN_TRAPS = "There is no card with this name in Traps";
    String invalidInputForCardCreator= "Invalid input";
    String thisCardDoesNotHaveSpecial= "This card doesn't have special";
    String successful= "Successful";
    String chooseNumberBetween1To3= "Choose a number between 1 to 3";
    String CHOOSE_1_OR_2 = "Choose 1 or 2";
    String PLEASE_CHOOSE_ALL_PROPERTIES = "Please choose all properties";
    String THERE_IS_AN_ERROR_IN_OUR_SIDE ="There is an error in our side";
    String CARD_CREATED_SUCCESSFULLY = "Card created successfully";
    String THERE_IS_ALREADY_A_CARD_WITH_THIS_NAME = "There is already a card with this name";
    String YOU_DON_T_HAVE_ENOUGH_MONEY = "You don't have enough money";

}

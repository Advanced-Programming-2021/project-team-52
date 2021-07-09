package sample.model.tools;

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
    String cardAlreadyAttacked = "this card already attacked";
    String cannotAttackRightNow = "you cannot attack right now";

    String cantChangeThisCardPosition = "you can’t change this card position";
    String cardIsAlreadyInTheWantedPosition = "this card is already in the wanted position";
    String alreadyChangedThisCardPositionInThisTurn = "you already changed this card position in this turn";
    String monsterCardPositionChangedSuccessfully = "monster card position changed successfully";

    String cantFlipSummonThisCard = "you can’t flip summon this card";
    String flipsSummonedSuccessfully = "flip summoned successfully";

    String cannotAttackWhileSet = "you cannot attack in set position";
    String noCardToAttackHere = "there is no card to attack here";
    String drawingAgainstOO = "both you and your opponent monster cards are destroyed and no one receives damage";
    String winingAgainstDO = "the defense position monster is destroyed";
    String drawingAgainstDO = "no card is destroyed";

    String getTribute = "please specify the next tribute :";
    String wrongTribute = "wrong tribute";
    String doAlternativeSpecial = "do alternative ?";
    String askStatus = "status ?";

    String cardIsNotVisible = "card is not visible";
    String notYourTurnToDoThat = "it’s not your turn to play this kind of moves";
    String askActivateSpecial = "Do you want to active this card's special ability?";
    String wrongCard = "wrong card. please select another one";
    String cannotDoThat = "unable to do that";
    String askForName = "please enter the card name";
    String yourGraveYardIsEmpty = "your graveyard is empty";
    String emptyOpponentGraveYard = "opponent graveyard is empty";
    String fullHand = "your hand is full";
    String emptyGraveYard = "all graveYards are empty";
    String fullMonsterPlaces = "your monster places are full";
    String couldNotFindASuitableCard = "could not find a suitable card";
    String emptyOpponentMonsterPlaces = "your opponent has no monster in the board";
    String doOnceMore = "do you want to activate this special ability once more?";
    String askForPlace = "please enter the number of card you want to sacrifice from hand to activate " +
            "the effect of this card, or enter cancel to cancel";
    String noMonsterInGraveYard = "there is no monster card in graveyard to summon";
    String doYouWantToTribute = "do you want to tribute?";
    String thereIsNoCard = "there is no card in the selected place. please select another";
    String summonWithoutTribute = "summon without Tribute?";
    String cannotSet = "unable to set";
    String askForSpellInGameToDestroy = "please enter a spell in the game to destroy\nuse positive number to destroy" +
            " a spell in your own board and a negative number to destroy a spell in enemy's board\nyou can enter \"cancel\"" +
            " to exit";
    String askForMonsterInGameToEquip = "please enter a monster in the game to equip\nuse positive number to equip" +
            " a monster in your own board and a negative number to equip a monster in enemy's board\nyou can enter \"cancel\"" +
            " to exit";

    String invalidSelection = "invalid selection";
    String noCardInGivenPosition = "no card found in the given position";
    String cardDeselected = "card deselected";
    String cardSelected = "card selected";
    String noMonsterOnOneOfTheseAddresses = "there is no monster on one of these addresses";
    String pleaseEnterNextCard = "please enter the next card";
    String cantSetThisCard = "you can't set this card";
    String spellCardZoneIsFull = "spell card zone is full";
    String cantDoInThisPhase = "you can’t do this action in this phase";
    String invalidInput = "your input is invalid please check it"; //TODO is different from invalid command, only used in specialSummonFromGraveYard in ActivateNoChain

    String swapFormat = "you can swap cards by entering the following command:\nswap <card from main deck> with" +
            " <card from side deck>\n you can enter cancel to exit";
    String wrongCardForSwap = "one of the cards that you have entered is wrong, either the name that you have entered" +
            " is wrong or the card isn't in your deck";
    String swappedSuccessfully = "swapped successfully";
    String cantFlipSummon = "you cant flip summon this card";
    String askActivateChain = "do you want to activate your trap or spell?";
    String cardNumber = "please enter the card number";
    String activateEffectIsOnlyForSpells = "activate effect is only for spell cards.";
    String youCantActivateOnThisTurn = "you can’t activate an effect on this turn";
    String youHaveAlreadyActivatedThisCard = "you have already activated this card";
    String cantActivateThisCard = "can't activate this card";
    String preparationsAreNotDoneYet = "preparations of this spell are not done yet";
    String spellActivated = "spell activated";
    String cantRitualSummon = "there is no way you could ritual summon a monster";
    String ritualSummonTribute = "please enter the card's number that you want to sacrifice with an space between them";
    String youShouldRitualSummonRightNow = "you should ritual summon right now";
    String pleaseEnterTheCardThatYouWantToRitualSummon = "please enter the card that you want to ritual summon";
    String wrongCardCombination = "selected monsters levels don’t match with ritual monster";
    String askActivateScanner = "do you want to activate scanner?";
    String wrongStatus = "wrong status";
    String cannotBeAttackedWhileThereAreMonsters = "cannot be attacked while there are other monsters";
    String cantSpecialSummon = "there is no way you could special summon a monster";
    String conditionsNotMet = "conditions have not been met";
    String whereDoYouWantToSpecialSummonFrom = "where do you want to special summon form? (deck/graveyard/hand)";
    String nameOfTheCardYouWantToSummon = "please enter the name of the card that you want to summon";


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
    String thereIsNoCardWithThisName= "There is no card with this name";
    String invalidInputForCardCreator= "Invalid input";
    String thisCardDoesNotHaveSpecial= "This card doesn't have special";

}

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
    String cardAlreadyAttacked = "this card already attacked";

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
    String askForPlace = "please enter the number of card you want to destroy";
    String noMonsterInGraveYard = "there is no monster card in graveyard to summon";
    String doYouWantToTribute = "do you want to tribute?";
    String thereIsNoCard = "there is no card in the selected place. please select another";
    String summonWithoutTribute = "summon without Tribute?";

    String invalidSelection = "invalid selection";
    String noCardInGivenPosition = "no card found in the given position";
    String cardDeselected = "card deselected";
    String cardSelected = "card selected";
    String noMonsterOnOneOfTheseAddresses = "there is no monster on one of these addresses";
    String pleaseEnterNextCard = "please enter the next card";
    String cantSetThisCard = "you can't set this card";
    String spellCardZoneIsFull = "spell card zone is full";
    String cantDoInThisPhase = "you can’t do this action in this phase";

    String askActivateChain = "do you want to activate your trap and spell?";
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


    // ProfileController
    String nicknameChangedSuccessfully = "nickname changed successfully";
    String currentPasswordIsInvalid = "current password is invalid";
    String enterNewPassword = "please enter a new password";
    String passwordChangedSuccessfully = "password changed successfully";
    String menuNavigationIsNotPossible = "menu navigation is not possible";
    String nonStandardNickname = "only letter, digit or underscore are valid for nickname";
    String nonStandardPassword = "weak password! please choose a password with\n" +
            "minimum eight characters, at least one uppercase letter, one lowercase letter and  one number";

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


    // DuelController
    String thereIsNoPlayerWithThisUsername = "there is no player with this username";
    String numberOfRoundsIsNotSupported = "number of rounds is not supported";
}

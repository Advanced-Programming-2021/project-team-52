package sample.view.listener;

import sample.controller.*;
import sample.controller.matchmaking.MatchMaker;
import sample.controller.matchmaking.ReadyUser;
import sample.controller.matchmaking.ReadyUser;
import sample.model.Shop;
import sample.model.User;
import sample.model.cards.Cards;
import sample.model.tools.StringMessages;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ActionFinder implements StringMessages {

    private static Map<String, User> authorizedUsers = new ConcurrentHashMap<>();
    private LoginController loginController = LoginController.getInstance();
    private ProfileController profileController = ProfileController.getInstance();
    private ScoreBoardController scoreBoardController = ScoreBoardController.getInstance();
    private DeckController deckController = DeckController.getInstance();
    private CardCreatorController cardCreatorController = CardCreatorController.getInstance();
    private ImportAndExportController importAndExportController = ImportAndExportController.getInstance();
    private ShopController shopController = ShopController.getInstance();
    private PrintBuilderController printBuilderController = PrintBuilderController.getInstance();
    private ChatroomController chatroomController = ChatroomController.getInstance();
    private AuctionController auctionController = AuctionController.getInstance();
    private final String LOGIN_PREFIX = "-LC-";
    private final String PROFILE_PREFIX = "-PC-";
    private final String SCOREBOARD_PREFIX = "-SBC-";
    private final String CARD_CREATOR_PREFIX = "-CCC-";
    private final String DECK_PREFIX = "-DC-";
    private final String IE_PREFIX = "-IEC-";
    private final String SHOP_PREFIX = "-SC-";
    private final String USER_PREFIX = "-UM-";
    private final String CARD_PREFIX = "-CM-";
    private final String PRINT_BUILDER_PREFIX = "-PBC-";
    private final String CHAT_PREFIX = "-CRC-";
    private final String AUCTION_PREFIX = "-AC-";
    private final String COMMUNICATOR_PREFIX = "-C-";
    private final String NEW_DUEL_PREFIX = "-ND-";
    private final String GAME_PLAY_CONTROLLER_PREFIX = "-GPC-";
    private String command = "";
    private User user;
    private String[] elements;
    private Communicator communicator;

    public ActionFinder(Communicator communicator) {
        this.communicator = communicator;
    }

    public Communicator getCommunicator() {
        return communicator;
    }

    public String chooseClass(String command) {
        this.command = command;
        this.elements = command.split(",");
        if (command.startsWith(GAME_PLAY_CONTROLLER_PREFIX))
            return saveCommand();
        else if (command.startsWith(LOGIN_PREFIX))
            return chooseMethodFromLogin();
        User user = getUserByToken(elements[elements.length - 1]);
        if (user == null)
            return "Invalid token";
        this.user = user;
        if (command.startsWith(PROFILE_PREFIX))
            return chooseMethodFromProfile();
        else if (command.startsWith(USER_PREFIX))
            return chooseMethodFromUser();
        else if (command.startsWith(SCOREBOARD_PREFIX))
            return chooseMethodFromScoreBoard();
        else if (command.startsWith(CARD_CREATOR_PREFIX))
            return chooseMethodFromCardCreator();
        else if (command.startsWith(DECK_PREFIX))
            return chooseMethodFromDeck();
        else if (command.startsWith(IE_PREFIX))
            return chooseMethodFromImportAndExport();
        else if (command.startsWith(SHOP_PREFIX))
            return chooseMethodFromShop();
        else if (command.startsWith(CARD_PREFIX))
            return chooseMethodFromCards();
        else if (command.startsWith(PRINT_BUILDER_PREFIX))
            return chooseMethodFromPrintBuilder();
        else if (command.startsWith(CHAT_PREFIX))
            return chooseMethodFromChat();
        else if (command.startsWith(AUCTION_PREFIX))
            return chooseMethodFromAuction();
        else if (command.startsWith(COMMUNICATOR_PREFIX))
            return chooseMethodFromCommunicator();
        else if (command.startsWith(NEW_DUEL_PREFIX))
            return chooseMethodFromNewDuel();
        return invalidCommand;
    }

    public String chooseMethodFromCommunicator(){
        if(command.startsWith(COMMUNICATOR_PREFIX + "getOnlineUsernames"))
        return Communicator.getOnlineUsernames();
        return invalidCommand;
    }

    public String chooseMethodFromAuction(){
        if (command.startsWith(AUCTION_PREFIX + "getAllActiveActionsInString"))
            return AuctionController.getAllActiveActionsInString();
        if (command.startsWith(AUCTION_PREFIX + "makeAnAuction"))
            return auctionController.makeAnAuction(user, elements[1], elements[2]);
        if (command.startsWith(AUCTION_PREFIX + "participateToAuction"))
            return auctionController.participateToAuction(user, elements[1], elements[2]);
        return invalidCommand;
    }

    public String chooseMethodFromChat(){
        if (command.startsWith(CHAT_PREFIX + "sendMessage"))
            return chatroomController.addMessage(user, elements[1], "null");
        if (command.startsWith(CHAT_PREFIX + "getAllMessages"))
            return chatroomController.getAllMessages();
        if (command.startsWith(CHAT_PREFIX + "searchMessage"))
            return chatroomController.getMessageByIdForClient(elements[1]);
        if (command.startsWith(CHAT_PREFIX + "editMessage"))
            return chatroomController.editMessage(user, elements[1], elements[2]);
        if (command.startsWith(CHAT_PREFIX + "deleteMessage"))
            return chatroomController.deleteMessage(user, elements[1]);
        if (command.startsWith(CHAT_PREFIX + "replyMessage"))
            return chatroomController.addMessage(user, elements[1], elements[2]);
        if (command.startsWith(CHAT_PREFIX + "pinMessage"))
            return chatroomController.pinMessage(elements[1]);
        if (command.startsWith(CHAT_PREFIX + "getPinMessage"))
            return chatroomController.getPinnedMessageContent();
        if (command.startsWith(CHAT_PREFIX + "getSenderProfile"))
            return chatroomController.getSenderProfile(elements[1]);
        if (command.startsWith(CHAT_PREFIX + "getAvatarAddress"))
            return chatroomController.getAvatarAddress(elements[1]);
        return invalidCommand;
    }

    public String chooseMethodFromCards(){
        if (command.startsWith(CARD_PREFIX + "isCardWithThisNameExist"))
            return convertBooleanToString(Cards.isCardWithThisNameExist(elements[1]));
        return invalidCommand;
    }

    public String chooseMethodFromPrintBuilder(){
        if (command.startsWith(PRINT_BUILDER_PREFIX + "showOneCard2"))
            return printBuilderController.showOneCard2(elements[1]);
        return invalidCommand;
    }

    public String chooseMethodFromLogin() {
        if (command.startsWith(LOGIN_PREFIX + "createUser"))
            return loginController.createUser(elements[1], elements[2], elements[3], LocalDate.now());
        else if (command.startsWith(LOGIN_PREFIX + "loginUser"))
            return loginController.loginUser(elements[1], elements[2], this);
        else if (command.startsWith(LOGIN_PREFIX + "isUserWithThisUsernameExists"))
            return convertBooleanToString(loginController.isUserWithThisUsernameExists(elements[1]));
        else if (command.startsWith(LOGIN_PREFIX + "logout"))
            return logout(elements[1]);
        return invalidCommand;
    }

    public String chooseMethodFromShop() {
        if (command.startsWith(SHOP_PREFIX + "checkBeforeTransaction"))
            return convertBooleanToString(shopController.checkBeforeTransaction(elements[1], Integer.parseInt(elements[2])));
        else if (command.startsWith(SHOP_PREFIX + "getCardPriceByName"))
            return String.valueOf(shopController.getCardPriceByName(elements[1]));
        else if (command.startsWith(SHOP_PREFIX + "buy"))
            return shopController.buy(user, elements[1]);
        else if (command.startsWith(SHOP_PREFIX + "getNumberOfThisCardOutOfDeck"))
            return String.valueOf(shopController.getNumberOfThisCardOutOfDeck(user, elements[1]));
        else if (command.startsWith(SHOP_PREFIX + "getCardImagePathByName"))
            return ShopController.getCardImagePathByName(elements[1]);
        else if (command.startsWith(SHOP_PREFIX + "getAllUnusedCardsByString"))
            return shopController.getAllUnusedCardsByString(user);
        else if (command.startsWith(SHOP_PREFIX + "getAllCardsWithPrice"))
            return Shop.getInstance().getAllCardsWithPrice();
        else if (command.startsWith(SHOP_PREFIX + "adminLogin"))
            return shopController.adminLogin(elements[1],elements[2]);
        else if (command.startsWith(SHOP_PREFIX + "restrictCard"))
            return shopController.addCardToBannedCards(elements[1]);
        else if (command.startsWith(SHOP_PREFIX + "allowCard"))
            return shopController.removeCardFromBannedCards(elements[1]);
        else if (command.startsWith(SHOP_PREFIX + "increaseNumberOfCard"))
            return shopController.addNumberOfCardToShop(elements[1], elements[2]);
        else if (command.startsWith(SHOP_PREFIX + "sellCard"))
            return shopController.sellCard(user, elements[1]);
        else if (command.startsWith(SHOP_PREFIX + "removeNumberOfCardToShop"))
            return shopController.removeNumberOfCardToShop(elements[1],elements[2]);
        return invalidCommand;
    }

    public String chooseMethodFromImportAndExport() {
        if (command.startsWith(IE_PREFIX + "exportCardInCSV"))
            return importAndExportController.exportCardInCSV(elements[1]);
        if (command.startsWith(IE_PREFIX + "importCardFromCSV"))
            return importAndExportController.importCardFromCSV(elements[1]);
        if (command.startsWith(IE_PREFIX + "exportCardInJson"))
            return importAndExportController.exportCardInJson(elements[1]);
        if (command.startsWith(IE_PREFIX + "importCardFromJson"))
            return importAndExportController.importCardFromJson(elements[1]);
        return invalidCommand;
    }

    public String chooseMethodFromProfile() {
        for (String element : elements) {
            System.out.println(element);
        }
        if (command.startsWith(PROFILE_PREFIX + "changeNickname"))
            return profileController.changeNickname(elements[1], user);
        else if (command.startsWith(PROFILE_PREFIX + "changePassword"))
            return profileController.changePassword(elements[1], elements[2], elements[3], user);
        else if (command.startsWith(PROFILE_PREFIX + "changeProfileImage"))
            return profileController.changeProfileImage(elements[1], user);
        return invalidCommand;
    }

    public String chooseMethodFromDeck() {
        if (command.startsWith(DECK_PREFIX + "createDeck"))
            return deckController.createDeck(elements[1], user);
        else if (command.startsWith(DECK_PREFIX + "deleteDeck"))
            return deckController.deleteDeck(elements[1], user);
        else if (command.startsWith(DECK_PREFIX + "activateDeck"))
            return deckController.activateDeck(elements[1], user);
        else if (command.startsWith(DECK_PREFIX + "addCardToDeck"))
            return deckController.addCardToDeck(elements[1], elements[2], convertStringToBoolean(elements[3]), user);
        else if (command.startsWith(DECK_PREFIX + "removeCardFromDeck"))
            return deckController.removeCardFromDeck(elements[1], elements[2], convertStringToBoolean(elements[3]), user);
        else if (command.startsWith(DECK_PREFIX + "showAllDecks"))
            return deckController.showAllDecks(user);
        else if (command.startsWith(DECK_PREFIX + "getMainDeckByString"))
            return deckController.getMainDeckByString(elements[1], user);
        else if (command.startsWith(DECK_PREFIX + "getSideDeckByString"))
            return deckController.getSideDeckByString(elements[1], user);
        return invalidCommand;
    }

    public String chooseMethodFromUser() {
        if (command.startsWith(USER_PREFIX + "getUsername"))
            return user.getUsername();
        else if (command.startsWith(USER_PREFIX + "getNickname"))
            return user.getNickname();
        else if (command.startsWith(USER_PREFIX + "getBalance"))
            return String.valueOf(user.getBalance());
        else if (command.startsWith(USER_PREFIX + "getImageAddress"))
            return user.getImageAddress();
        return invalidCommand;
    }

    public String chooseMethodFromScoreBoard() {
        if (command.startsWith(SCOREBOARD_PREFIX + "toString"))
            return scoreBoardController.toString();
        return invalidCommand;
    }

    private String saveCommand(){
        communicator.saveCommand(command, GAME_PLAY_CONTROLLER_PREFIX);
        return "";
    }

    public String chooseMethodFromCardCreator() {
        if (command.startsWith(CARD_CREATOR_PREFIX + "setName"))
            return cardCreatorController.setName(elements[1]);
        else if (command.startsWith(CARD_CREATOR_PREFIX + "setDescription"))
            return cardCreatorController.setDescription(elements[1]);
        else if (command.startsWith(CARD_CREATOR_PREFIX + "attackCounter"))
            return cardCreatorController.attackCounter(elements[1]);
        else if (command.startsWith(CARD_CREATOR_PREFIX + "defendCounter"))
            return cardCreatorController.defendCounter(elements[1]);
        else if (command.startsWith(CARD_CREATOR_PREFIX + "levelCounter"))
            return cardCreatorController.levelCounter(elements[1]);
        else if (command.startsWith(CARD_CREATOR_PREFIX + "statusCounterForMonsters"))
            return cardCreatorController.statusCounterForMonsters(elements[1]);
        else if (command.startsWith(CARD_CREATOR_PREFIX + "statusCounterForSpellAndTrap"))
            return cardCreatorController.statusCounterForSpellAndTrap(elements[1]);
        else if (command.startsWith(CARD_CREATOR_PREFIX + "specialCounterForMonster"))
            return cardCreatorController.specialCounterForMonster(elements[1]);
        else if (command.startsWith(CARD_CREATOR_PREFIX + "setAttribute"))
            return cardCreatorController.setAttribute(elements[1]);
        else if (command.startsWith(CARD_CREATOR_PREFIX + "speedCounter"))
            return cardCreatorController.speedCounter(elements[1]);
        else if (command.startsWith(CARD_CREATOR_PREFIX + "specialCounterForSpell"))
            return cardCreatorController.specialCounterForSpell(elements[1]);
        else if (command.startsWith(CARD_CREATOR_PREFIX + "specialCounterForTrap"))
            return cardCreatorController.specialCounterForTrap(elements[1]);
        else if (command.startsWith(CARD_CREATOR_PREFIX + "createMonsterCard"))
            return cardCreatorController.createMonsterCard(user);
        else if (command.startsWith(CARD_CREATOR_PREFIX + "createSpellCard"))
            return cardCreatorController.createSpellCard(user);
        else if (command.startsWith(CARD_CREATOR_PREFIX + "createTrapCard"))
            return cardCreatorController.createTrapCard(user);
        return invalidCommand;
    }

    private String chooseMethodFromNewDuel(){
//        NewDuelController newDuelController = new NewDuelController(user);
//        newDuelController.run(elements[1], elements[2]);
        if (command.startsWith("-ND-startANewGame")) {
            return startNewGame();
        } else if (command.startsWith("-ND-end")) {
            return endSearching();
        } /*if (command.startsWith("-ND-getActiveGames"))
            return getActiveGames();
        else if (command.startsWith("-ND-stream"))
            return stream();*/
        else if (command.startsWith("dummy"))
            return "dummy";
        return "";
    }

    private String endSearching() {
        MatchMaker.removeFromQueue(user);
        return "end";
    }

    private String startNewGame() {
        ReadyUser.tryToGetOpponent(user, Integer.parseInt(elements[1]));
        return "trying to find opponent";
    }

//    private String getActiveGames() {
//        return NewDuelController.getActiveGames();
//    }
//
//    private String stream(){
//        return NewDuelController.stream(Integer.parseInt(elements[1]), communicator);
//    }

    public boolean convertStringToBoolean(String message) {
        return message.toLowerCase().trim().equals("true");
    }

    public String convertBooleanToString(boolean message) {
        if (message)
            return "true";
        return "false";
    }

    public  void addUser(User user, String token) {
        ArrayList<String> offlineUsers = new ArrayList<>();
        for (String s : authorizedUsers.keySet()) {
            if (authorizedUsers.get(s).equals(user))
                offlineUsers.add(s);
        }
        for (String offlineUser : offlineUsers) {
            authorizedUsers.remove(offlineUser);
        }
        user.setActionFinder(this);
        authorizedUsers.put(token, user);
//        for (String s : authorizedUsers.keySet()) {
//            System.out.println(s);
//            System.out.println(authorizedUsers.get(s).getUsername());
//        }
    }

    public static User getUserByToken(String token) {
        if (authorizedUsers.containsKey(token))
            return authorizedUsers.get(token);
        return null;
    }

    public static String logout(String token){
        authorizedUsers.remove(token);
        return successful;
    }

    public boolean authorizeToken(String token) {
        return authorizedUsers.containsKey(token);
    }
}

package sample.controller;

import sample.model.Auction;
import sample.model.User;
import sample.model.cards.Cards;
import sample.model.tools.StringMessages;

import java.util.ArrayList;

public class AuctionController implements StringMessages {
    private static AuctionController auctionController = null;
    private static ArrayList<Auction> allActiveAuctions = new ArrayList<>();

    private AuctionController() {
    }

    public static AuctionController getInstance() {
        if (auctionController == null)
            auctionController = new AuctionController();
        return auctionController;
    }

    public static String getAllActiveActionsInString() {
        StringBuilder response = new StringBuilder();
        for (Auction auction : allActiveAuctions) {
            response.append(auction.toString());
        }
        return response.toString();
    }

    public static ArrayList<Auction> getAllActiveAuctions() {
        return allActiveAuctions;
    }

    public static Auction getActiveActionById(int id) {
        for (Auction auction : allActiveAuctions) {
            if (auction.getId() == id)
                return auction;
        }
        return null;
    }

    public String makeAnAuction(User auctioneer, String firstPriceByAuctioneer, String cardName) {
        if (!isUserValid(auctioneer))
            return USER_IS_INVALID;
        if (!isInputNumber(firstPriceByAuctioneer))
            return PLEASE_ONLY_ENTER_NUMBER_FOR_PRICE_OR_ID;

        int firstPrice = Integer.parseInt(firstPriceByAuctioneer);
        if (!isFirstPriceStandard(firstPrice))
            return FIRST_PRICE_MOST_BE_BETWEEN_100_AND_10000;
        Cards card = Cards.getCard(cardName);
        if (card == null)
            return CARD_NAME_IS_INVALID;
        if (!doesAuctioneerHaveThisCard(auctioneer, cardName))
            return YOU_DO_NOT_HAVE_ANY_OF_THIS_CARD_OUT_OF_DECK;

        Auction auction = new Auction(auctioneer, firstPrice, card);
        allActiveAuctions.add(auction);
        auctioneer.removeCardFromCardsWithoutDeck(cardName);
        auctioneer.addCardToAuctionCards(cardName);
        return AUCTION_HAS_BEEN_CREATED_SUCCESSFULLY;
    }

    public String participateToAuction(User participant, String idInString, String offerInString) {
        if (!isUserValid(participant))
            return USER_IS_INVALID;
        if (!isInputNumber(idInString) || !isInputNumber(offerInString))
            return PLEASE_ONLY_ENTER_NUMBER_FOR_PRICE_OR_ID;

        int id = Integer.parseInt(idInString);
        int offer = Integer.parseInt(offerInString);
        Auction auction = getActiveActionById(id);
        if (auction == null)
            return THERE_IS_NO_AUCTION_WITH_THIS_ID;
        if(auction.getAuctioneer() == participant)
            return YOU_CAN_NOT_ATTEND_TO_YOUR_OWN_AUCTION;
        if (offer <= auction.getLastOffer())
            return YOUR_OFFER_IS_LESS_OR_EQUAL_THAN_BEST_OFFER;
        if (offer > participant.getBalance())
            return YOU_DO_NOT_HAVE_ENOUGH_MONEY;

        User lastUserWithBestOffer = auction.getUserWithBestOffer();
        if (lastUserWithBestOffer != null)
            lastUserWithBestOffer.changeBalance(+auction.getLastOffer());

        participant.changeBalance(-offer);
        auction.setLastOffer(offer);
        auction.setTotalTime();
        auction.setOriginTime();
        auction.setUserWithBestOffer(participant);
        return YOU_ATTENDED_TO_AUCTION_SUCCESSFULLY;
    }

    public void finishAuction(Auction auction) {
        if (!allActiveAuctions.contains(auction))
            return;
        User auctioneer = auction.getAuctioneer();
        String cardName = auction.getCard().getName();
        User userWithBestOffer = auction.getUserWithBestOffer();

        auctioneer.removeCardFromAuctionCards(cardName);
        if (userWithBestOffer == null) {
            auctioneer.addCardToCardsWithoutDeck(cardName);
        } else {
            auctioneer.changeBalance(+auction.getLastOffer());
            userWithBestOffer.addCardToCardsWithoutDeck(cardName);
        }
        allActiveAuctions.remove(auction);
    }


    private boolean doesAuctioneerHaveThisCard(User auctioneer, String cardName) {
        return auctioneer.getCards().contains(cardName);
    }

    public boolean isInputNumber(String input) {
        return input.matches("^\\d+$");
    }


    private boolean isFirstPriceStandard(int firstPrice) {
        return firstPrice >= 100 && firstPrice <= 10000;
    }


    private boolean isUserValid(User user) {
        return user != null;
    }
}

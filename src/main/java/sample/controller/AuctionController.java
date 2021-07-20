package sample.controller;

import sample.view.sender.Sender;

public class AuctionController {
    private static AuctionController auctionController = null;
    private Sender sender = Sender.getInstance();
    private final String PREFIX = "-AC-";

    private AuctionController() {
    }

    public static AuctionController getInstance() {
        if (auctionController == null)
            auctionController = new AuctionController();
        return auctionController;
    }

    public String getAllActiveActionsInString() {
        return sender.getResponseWithToken(PREFIX, "getAllActiveActionsInString");
    }

    public String makeAnAuction(String firstPriceByAuctioneer, String cardName) {
        return sender.getResponseWithToken(PREFIX, "makeAnAuction", firstPriceByAuctioneer, cardName);
    }

    public String participateToAuction( String idInString, String offerInString){
        return sender.getResponseWithToken(PREFIX, "participateToAuction", idInString, offerInString);
    }
}

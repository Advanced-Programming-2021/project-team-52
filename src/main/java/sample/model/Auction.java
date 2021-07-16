package sample.model;

import sample.controller.AuctionController;
import sample.model.cards.Cards;

import java.util.Timer;
import java.util.TimerTask;

public class Auction {
    private static int idCounter = 1000;
    private AuctionController auctionController = AuctionController.getInstance();

    private User auctioneer;
    private Cards card;
    private int id;

    private User userWithBestOffer;
    private int lastOffer;

    private long startingTime;
    private long originTime;

    private Timer countdownTimer = new Timer();

    private long totalTime;

    private final long FIRST_TOTAL_TIME = 20000;
    private final long TOTAL_TIME_AFTER_OFFER = 10000;

    public Auction(User auctioneer, int firstPriceByAuctioneer, Cards card) {
        this.auctioneer = auctioneer;
        this.lastOffer = firstPriceByAuctioneer;
        this.card = card;
        this.startingTime = System.currentTimeMillis();
        this.originTime = this.startingTime;
        this.totalTime = FIRST_TOTAL_TIME;
        id = --idCounter;
        countdown();
    }

    public void setLastOffer(int lastOffer) {
        this.lastOffer = lastOffer;
    }

    public void setOriginTime() {
        this.originTime = System.currentTimeMillis();
    }

    public void setUserWithBestOffer(User userWithBestOffer) {
        this.userWithBestOffer = userWithBestOffer;
    }

    public void setTotalTime() {
        if (userWithBestOffer == null) {
            if ((System.currentTimeMillis() - originTime) > TOTAL_TIME_AFTER_OFFER)
                this.totalTime = TOTAL_TIME_AFTER_OFFER;
        }else{
            this.totalTime = TOTAL_TIME_AFTER_OFFER;
        }
    }

    public int getId() {
        return id;
    }

    public int getLastOffer() {
        return lastOffer;
    }

    public User getUserWithBestOffer() {
        return userWithBestOffer;
    }

    public User getAuctioneer() {
        return auctioneer;
    }

    public Cards getCard() {
        return card;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void countdown() {
        Auction thisAuction = this;
        countdownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - originTime > totalTime) {
                    auctionController.finishAuction(thisAuction);
                    countdownTimer.cancel();
                    countdownTimer.purge();
                }
            }
        }, 0, 1000);
    }


    @Override
    public String toString() {
        return "#" + this.id + ", " +
                "card: " + card.getName() + ", " +
                "owner: " + auctioneer.getUsername() + ", " +
                "last offer: " + lastOffer + " ," +
                "remaining timer: " + ((totalTime - (System.currentTimeMillis() - originTime)) / 1000) + "\n";

    }
}

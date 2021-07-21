package sample.controller.matchmaking;

import sample.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ReadyUser {

    private static List<ReadyUser> queuedUsers;

    private User user;
    private int rounds;
    private LocalDateTime localDateTime;

    static {
        queuedUsers = MatchMaker.getQueuedUsers();
    }

    private ReadyUser(User user, int rounds, LocalDateTime localDateTime){
        this.user= user;
        this.rounds = rounds;
        this.localDateTime = localDateTime;
    }

    public static void tryToGetOpponent(User user, int rounds){
        ReadyUser host;
        for (int i = 0; i < queuedUsers.size(); i++) {
            host = queuedUsers.get(i);
            if (host.rounds == rounds && Math.abs(host.user.getScore() - user.getScore()) <= 1000 && host.getUser() != user){
                queuedUsers.remove(host);
                MatchMaker.startANewGame(host.user, user, String.valueOf(rounds));
                return;
            }
        }
        if (MatchMaker.doesNotHaveThisUserInQueue(user)) {
            ReadyUser readyUser = new ReadyUser(user, rounds, LocalDateTime.now());
            MatchMaker.addToList(readyUser);
        }
    }

    public User getUser() {
        return user;
    }

    public int getRounds() {
        return rounds;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}

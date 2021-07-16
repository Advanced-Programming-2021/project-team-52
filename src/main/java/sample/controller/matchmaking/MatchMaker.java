package sample.controller.matchmaking;

import sample.controller.NewDuelController;
import sample.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MatchMaker {

    private static List<ReadyUser> queuedUsers;

    static {
        queuedUsers = Collections.synchronizedList(new LinkedList<>());
        ReadyUser.setQueuedUsers(queuedUsers);
        startMatchThread();
    }

    public static void addToList(ReadyUser readyUser){
        queuedUsers.add(readyUser);
    }

    public static void removeFromList(ReadyUser readyUser){
        queuedUsers.remove(readyUser);
    }

    private static void startMatchThread(){
        Thread thread = new Thread(() -> {
            while (true){
                if (queuedUsers.size() > 1){
                    if (queuedUsers.get(0).getLocalDateTime().minusMinutes(2).compareTo(LocalDateTime.now()) > 0){
                        queuedUsers.remove(0);
                        int opponent = getOpponent(queuedUsers.get(0).getRounds());
                        if (opponent != -1){
                            queuedUsers.remove(1);
                            queuedUsers.remove(opponent);
                            MatchMaker.startANewGame(queuedUsers.get(0).getUser(), queuedUsers.get(opponent).getUser(),
                                    String.valueOf(queuedUsers.get(0).getRounds()));
                        }
                    } else {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                    }
                } else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public static void startANewGame(User host, User guest, String rounds){
        host.getActionFinder().getCommunicator().sendMessage(getMessage("duel", host.getNickname(),
                host.getImageAddress(), guest.getNickname(), guest.getImageAddress()));
        guest.getActionFinder().getCommunicator().sendMessage(getMessage("duel", guest.getNickname(),
                guest.getImageAddress(), guest.getNickname(), guest.getImageAddress()));
        NewDuelController newDuelController = new NewDuelController(host);
        newDuelController.run(guest.getUsername(), rounds, host.getActionFinder().getCommunicator(),
                guest.getActionFinder().getCommunicator());
    }

    private static String getMessage(String... args){
        StringBuilder message = new StringBuilder(args[0]);
        for (int i = 1; i < args.length; i++) {
            message.append(",").append(args[i]);
        }
        return message.toString();
    }

    private static int getOpponent(int rounds){
        for (int i = 1; i < queuedUsers.size(); i++) {
            if (queuedUsers.get(i).getRounds() == rounds)
                return i;
        }
        return -1;
    }
}

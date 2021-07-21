package sample.controller.matchmaking;

import sample.controller.NewDuelController;
import sample.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MatchMaker {

    private static List<ReadyUser> queuedUsers;
    private static long leastWaitingTime;

    static {
        queuedUsers = Collections.synchronizedList(new ArrayList<>());
        leastWaitingTime = 60000;
        startMatchThread();
    }

    public static List<ReadyUser> getQueuedUsers() {
        return queuedUsers;
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
                    if (LocalDateTime.now().minusMinutes(1).compareTo(queuedUsers.get(0).getLocalDateTime()) >= 0){
                        int opponent = getOpponent(queuedUsers.get(0).getRounds());
                        if (opponent != -1){
                            int rounds = queuedUsers.get(0).getRounds();
                            User host = queuedUsers.get(0).getUser();
                            queuedUsers.remove(0);
                            User guest = queuedUsers.get(opponent - 1).getUser();
                            queuedUsers.remove(opponent - 1);
                            MatchMaker.startANewGame(host, guest, String.valueOf(rounds));
                        }
                    } else {
                        try {
                            Thread.sleep(leastWaitingTime);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                    }
                } else {
                    try {
                        Thread.sleep(leastWaitingTime);
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
        host.getActionFinder().getCommunicator().sendMessage("starting the match with " + guest.getUsername());
        guest.getActionFinder().getCommunicator().sendMessage("starting the match with " + host.getUsername());
        host.getActionFinder().getCommunicator().sendMessage(getMessage("duel", host.getNickname(),
                host.getImageAddress(), guest.getNickname(), guest.getImageAddress()));
        guest.getActionFinder().getCommunicator().sendMessage(getMessage("duel", guest.getNickname(),
                guest.getImageAddress(), host.getNickname(), host.getImageAddress()));
        NewDuelController newDuelController = new NewDuelController(host);
        newDuelController.run(guest.getUsername(), rounds, host.getActionFinder().getCommunicator(),
                guest.getActionFinder().getCommunicator());
    }

    private static String getMessage(String... args){
        StringBuilder message = new StringBuilder(args[0]);
        for (int i = 1; i < args.length; i++) {
            message.append("*").append(args[i]);
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

    public static boolean doesNotHaveThisUserInQueue(User user){
        for (int i = 0; i < queuedUsers.size(); i++) {
            if (queuedUsers.get(i).getUser() == user)
                return false;
        }
        return true;
    }

    public static void removeFromQueue(User user){
        for (int i = 0; i < queuedUsers.size(); i++) {
            if (queuedUsers.get(i).getUser() == user){
                queuedUsers.remove(queuedUsers.get(i));
                break;
            }
        }
    }
}

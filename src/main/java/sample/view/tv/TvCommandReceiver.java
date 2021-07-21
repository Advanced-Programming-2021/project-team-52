package sample.view.tv;

import javafx.scene.control.TitledPane;
import sample.view.sender.Sender;

import java.util.concurrent.ArrayBlockingQueue;

public class TvCommandReceiver implements Runnable{

    private boolean run;
    private Sender sender;
    private TitledPane host, guest;
    private ArrayBlockingQueue<String> hostTv, guestTv;

    public TvCommandReceiver(TitledPane host, TitledPane guest){
        this.run = true;
        this.sender = Sender.getInstance();
        this.host = host;
        this.guest = guest;
        hostTv = new ArrayBlockingQueue<>(2);
        guestTv = new ArrayBlockingQueue<>(2);
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    @Override
    public void run() {
        String message;
        while (run){
            message = sender.receive();
            if (!message.isEmpty())
                if (message.startsWith("host")) {
                    try {
                        hostTv.put(message.replaceAll("host\\*", ""));
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                } else if (message.startsWith("guest")) {
                    try {
                        guestTv.put(message.replaceAll("guest\\*", ""));
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
        }
    }

    public String getActiveGames(){
        return sender.getResponse(sender.setMessageWithToken("-ND-", "getActiveGames", "nothing"));
    }

    public void watchGame(int id){
        String response = sender.getResponse(sender.setMessageWithToken("-ND-", "stream", String.valueOf(id)));
    }

    public String receiveTv(boolean isHost){
        if (isHost) {
            try {
                return hostTv.take();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        else {
            try {
                return guestTv.take();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        return "";
    }
}

package model.cards.trap;

import controller.specialbilities.SpecialAbility;
import model.cards.Cards;
import model.tools.CHAIN_JOB;

import java.util.ArrayList;

//TODO add speed
public class TrapCards extends Cards {
    private String icon;
    private ArrayList<CHAIN_JOB> chainJobs;
    private String chainJobInString;

    public TrapCards(String name, String type, String icon, String description, String status,
                     int specialSpeed, ArrayList<SpecialAbility> special, ArrayList<CHAIN_JOB> chainJobs,
                     String specialsInString, String chainJobInString) {
        super(name, type, description, status, specialSpeed, special, specialsInString);
        this.icon = icon;
        this.chainJobs = chainJobs;
        this.chainJobInString = chainJobInString;
        addCard(this, name);

    }

    public String getIcon() {
        return icon;
    }

    public ArrayList<CHAIN_JOB> getChainJobs() {
        return chainJobs;
    }

    public String getChainJobInString() {
        return chainJobInString;
    }
}

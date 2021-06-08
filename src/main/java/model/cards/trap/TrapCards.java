package model.cards.trap;

import controller.specialbilities.SpecialAbility;
import model.cards.Cards;
import model.tools.CHAIN_JOB;

import java.util.ArrayList;

//TODO add speed
public class TrapCards extends Cards{
    private String icon;
    private ArrayList<CHAIN_JOB> chainJobs;

    public TrapCards(String name, String type, String icon, String description, String status,
                      int specialSpeed, ArrayList<SpecialAbility> special, ArrayList<CHAIN_JOB> chainJobs) {
        super( name, type, description, status, specialSpeed, special);
        this.icon = icon;
        this.chainJobs = chainJobs;
        addCard(this, name);

    }

    public String getIcon() {
        return icon;
    }

    public ArrayList<CHAIN_JOB> getChainJobs() {
        return chainJobs;
    }
}

package sample.model.cards.spell;

import sample.controller.specialbilities.SpecialAbility;
import sample.model.cards.Cards;
import sample.model.tools.CHAIN_JOB;

import java.util.ArrayList;

public class SpellCards extends Cards {
    private String icon;
    private ArrayList<CHAIN_JOB> chainJobs;
    private String chainJobInString;

    public SpellCards(String name, String type, String icon, String description, String status,
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

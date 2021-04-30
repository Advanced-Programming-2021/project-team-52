package controller;

import model.Shop;
import model.cards.monster.MonsterCards;
import model.cards.spell.SpellCards;
import model.cards.trap.TrapCards;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//TODO Change csv file Path?
public interface InstantiateCards {
    static void start() throws IOException, CsvException {
        FileReader file = new FileReader("Monster.csv");
        ArrayList<String[]> list = new ArrayList<>(new CSVReaderBuilder(file).withSkipLines(1).build().readAll());
        list.forEach(info -> {
            new MonsterCards(info[0], Integer.parseInt(info[1]), info[2], info[3], info[4],
                    Integer.parseInt(info[5]), Integer.parseInt(info[7]), info[8]);
            Shop.addCard(info[0], Integer.parseInt(info[9]));
        });
        file.close();
        file = new FileReader("Trap.csv");
        list = new ArrayList<>(new CSVReaderBuilder(file).withSkipLines(1).build().readAll());
        list.forEach(info -> {
            new TrapCards(info[0], info[1], info[2], info[3], info[4]);
            Shop.addCard(info[0], Integer.parseInt(info[5]));
        });
        file.close();
        file = new FileReader("Spell.csv");
        list = new ArrayList<>(new CSVReaderBuilder(file).withSkipLines(1).build().readAll());
        list.forEach(info -> {
            new SpellCards(info[0], info[1], info[2], info[3], info[4]);
            Shop.addCard(info[0], Integer.parseInt(info[5]));
        });
    }
}

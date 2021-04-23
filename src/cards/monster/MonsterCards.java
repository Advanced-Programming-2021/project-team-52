package cards.monster;

import cards.Cards;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class MonsterCards extends Cards {

    private int level, attack, defense, hasSpecialAbility, specialAbilityId;

    private MonsterCards(int level, int attack, int defense, String name, String type, String description, String cardID){
        super(name,type,description,null);
        Cards.addCard(this, name);
        this.level = level;
        this.attack = attack;
        this.defense = defense;
    }

    public static void initialize() throws IOException, CsvException {
        FileReader file = new FileReader("C:\\Users\\ALIREZA\\Desktop\\Project-main\\Project-main\\phase_1\\CSV\\Monster.csv");
        CSVReader csvReader = new CSVReader(file);
        List<String[]> all = csvReader.readAll();

        for (int i = 1; i < all.size(); i++) {
            String[] data = all.get(i);
            new MonsterCards(Integer.parseInt(data[1]), Integer.parseInt(data[5]), Integer.parseInt(data[6]), data[0], data[3], data[7], null);
        }
    }
    public int getLevel() {
        return level;
    }

    public int getDefense() {
        return defense;
    }

    public int getAttack() {
        return attack;
    }

    public int getHasSpecialAbility() {
        return hasSpecialAbility;
    }

    public int getSpecialAbilityId() {
        return specialAbilityId;
    }
}

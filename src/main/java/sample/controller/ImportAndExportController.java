package sample.controller;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import sample.model.Shop;
import sample.model.cards.Cards;
import sample.model.cards.monster.MonsterCards;
import sample.model.cards.spell.SpellCards;
import sample.model.cards.trap.TrapCards;
import sample.model.tools.RegexPatterns;
import sample.model.tools.StringMessages;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sample.view.PrinterAndScanner;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImportAndExportController implements RegexPatterns, StringMessages {
    private static ImportAndExportController importAndExportController = null;
    private PrintBuilderController printBuilderController = PrintBuilderController.getInstance();
    private PrinterAndScanner printerAndScanner = PrinterAndScanner.getInstance();
    private String FILE_PATH_OF_MONSTER_CSV = "./src/main/resources/exportedCardsInCSV/Monster.csv";
    private String FILE_PATH_OF_SPELL_CSV = "./src/main/resources/exportedCardsInCSV/Spell.csv";
    private String FILE_PATH_OF_TRAP_CSV = "./src/main/resources/exportedCardsInCSV/Trap.csv";

    private ImportAndExportController() {
    }

    public static ImportAndExportController getInstance() {
        if (importAndExportController == null)
            importAndExportController = new ImportAndExportController();
        return importAndExportController;
    }

    public static void writeDataLineByLine(String filePath, String[] data) {
        File file = new File(filePath);
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            CSVWriter csvWriter = new CSVWriter(fileWriter);
            csvWriter.writeNext(data);
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO : does create cards by constructor work?
    public String exportCardInCSV(String cardName) {
        Cards card = Cards.getCard(cardName);
        if (card == null) {
            return cardWithThisNameIsNotValid;
        }
        String filePath;
        String[] data;
        if (card instanceof MonsterCards) {
            filePath = FILE_PATH_OF_MONSTER_CSV;
            MonsterCards monsterCards = (MonsterCards) card;
            data = new String[]{card.getName(), String.valueOf(monsterCards.getLevel()), monsterCards.getAttribute(),
                    monsterCards.getMonsterType(), monsterCards.getType(), String.valueOf(monsterCards.getAttack()),
                    String.valueOf(monsterCards.getDefense()), monsterCards.getDescription(),
                    String.valueOf(Shop.getInstance().getItemPrize(cardName)), monsterCards.getStatus(),
                    String.valueOf(monsterCards.getSpecialSpeed()), monsterCards.getSpecialsInString()};
        } else if (card instanceof SpellCards) {
            filePath = FILE_PATH_OF_SPELL_CSV;
            SpellCards spellCards = (SpellCards) card;
            data = new String[]{spellCards.getName(), spellCards.getType(), spellCards.getIcon(),
                    spellCards.getDescription(), spellCards.getStatus(),
                    String.valueOf(Shop.getInstance().getItemPrize(cardName)),
                    String.valueOf(spellCards.getSpecialSpeed()), spellCards.getSpecialsInString(),
                    spellCards.getChainJobInString()};
        } else {
            filePath = FILE_PATH_OF_TRAP_CSV;
            TrapCards trapCards = (TrapCards) card;
            data = new String[]{trapCards.getName(), trapCards.getType(), trapCards.getIcon(),
                    trapCards.getDescription(), trapCards.getStatus(),
                    String.valueOf(Shop.getInstance().getItemPrize(cardName)),
                    String.valueOf(trapCards.getSpecialSpeed()), trapCards.getSpecialsInString(),
                    trapCards.getChainJobInString()};
        }
        AtomicBoolean isCardAlreadyExported = isCardWithThisNameExistsInExportedCSVCards(cardName, filePath);
        if (isCardAlreadyExported.get()) {
            return cardWithThisNameHasAlreadyExported;
        }
        writeDataLineByLine(filePath, data);
        return cardExportedSuccessfully;
    }

    private AtomicBoolean isCardWithThisNameExistsInExportedCSVCards(String cardName, String filePath) {
        File file = new File(filePath);
        AtomicBoolean isCardAlreadyExported = new AtomicBoolean(false);
        try {
            FileReader fileReader = new FileReader(file);
            ArrayList<String[]> list = new ArrayList<>(new CSVReaderBuilder(fileReader).
                    withSkipLines(1).build().readAll());
            list.forEach(info -> {
                if (info[0].equals(cardName)) {
                    isCardAlreadyExported.set(true);
                }
            });
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isCardAlreadyExported;
    }

    public String importCardFromCSV(String cardName) {
        StringBuilder cardProperties = new StringBuilder();
        try {
            FileReader fileReader;
            ArrayList<String[]> list;
            if (isCardWithThisNameExistsInExportedCSVCards(cardName, FILE_PATH_OF_MONSTER_CSV).get()) {
                fileReader = new FileReader(FILE_PATH_OF_MONSTER_CSV);
                list = new ArrayList<>(new CSVReaderBuilder(fileReader).withSkipLines(1).build().readAll());
                list.forEach(info -> {
                    if (info[0].equals(cardName)) {
                        if (Cards.getCard(cardName) != null) {
                            printerAndScanner.printNextLine(thereIsAlreadyACardWithThisName);
                            makeCardPropertiesForMonster(cardProperties, info);
                            return;
                        }
                        if (Cards.getCard(cardName) == null){
                            CardCreatorController.newCardNames.add(cardName); // to user a picture for it
                        }
                        try {
                            new MonsterCards(info[0], Integer.parseInt(info[1]), info[2], info[3], info[4],
                                    Integer.parseInt(info[5]), Integer.parseInt(info[6]), info[7], info[9],
                                    Integer.parseInt(info[10]), InstantiateCards.loadSpecialAbilities
                                    (info[11].split("&&")), info[11]);
                            Shop.addCard(info[0], Integer.parseInt(info[8]));
                            makeCardPropertiesForMonster(cardProperties, info);
                            printerAndScanner.printNextLine(cardImportedSuccessfully);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                fileReader.close();

            } else if (isCardWithThisNameExistsInExportedCSVCards(cardName, FILE_PATH_OF_SPELL_CSV).get()) {
                fileReader = new FileReader(FILE_PATH_OF_SPELL_CSV);
                list = new ArrayList<>(new CSVReaderBuilder(fileReader).withSkipLines(1).build().readAll());
                list.forEach(info -> {
                    if (info[0].equals(cardName)) {
                        if (Cards.getCard(cardName) != null) {
                            printerAndScanner.printNextLine(thereIsAlreadyACardWithThisName);
                            makeStringBuilderForSpellAndTrap(cardProperties, info);
                            return;
                        }
                        if (Cards.getCard(cardName) == null){
                            CardCreatorController.newCardNames.add(cardName); // to user a picture for it
                        }
                        try {
                            new SpellCards(info[0], info[1], info[2], info[3], info[4], Integer.parseInt(info[6]),
                                    InstantiateCards.loadSpecialAbilities(info[7].split("&&")),
                                    InstantiateCards.getChainJobs(info[8]), info[7], info[8]);
                            Shop.addCard(info[0], Integer.parseInt(info[5]));
                            makeStringBuilderForSpellAndTrap(cardProperties, info);
                            printerAndScanner.printNextLine(cardImportedSuccessfully);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                fileReader.close();
            } else if (isCardWithThisNameExistsInExportedCSVCards(cardName, FILE_PATH_OF_TRAP_CSV).get()) {
                fileReader = new FileReader(FILE_PATH_OF_TRAP_CSV);
                list = new ArrayList<>(new CSVReaderBuilder(fileReader).withSkipLines(1).build().readAll());
                list.forEach(info -> {
                    if (info[0].equals(cardName)) {
                        if (Cards.getCard(cardName) != null) {
                            printerAndScanner.printNextLine(thereIsAlreadyACardWithThisName);
                            makeStringBuilderForSpellAndTrap(cardProperties, info);
                            return;
                        }
                        if (Cards.getCard(cardName) == null){
                            CardCreatorController.newCardNames.add(cardName); // to user a picture for it
                        }
                        try {
                            new TrapCards(info[0], info[1], info[2], info[3], info[4], Integer.parseInt(info[6]),
                                    InstantiateCards.loadSpecialAbilities(info[7].split("&&")),
                                    InstantiateCards.getChainJobs(info[8]), info[7], info[8]);
                            Shop.addCard(info[0], Integer.parseInt(info[5]));
                            makeStringBuilderForSpellAndTrap(cardProperties, info);
                            printerAndScanner.printNextLine(cardImportedSuccessfully);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                fileReader.close();
            } else {
                return thereIsNoCardWithThisNameToImport;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error;
        }
        return cardProperties.toString();
    }

    private void makeCardPropertiesForMonster(StringBuilder cardProperties, String[] info) {
        cardProperties.append("name : ").append(info[0]).append("\n");
        cardProperties.append("type : ").append(info[4]).append("\n");
        cardProperties.append("description : ").append(info[7]).append("\n");
        cardProperties.append("status : ").append(info[9]).append("\n");
        cardProperties.append("Price : ").append(info[8]).append("\n");
        cardProperties.append("level : ").append(info[1]).append("\n");
        cardProperties.append("attack : ").append(info[5]).append("\n");
        cardProperties.append("defense : ").append(info[6]).append("\n");
        cardProperties.append("attribute : ").append(info[2]).append("\n");
        cardProperties.append("monsterType : ").append(info[3]).append("\n");
        cardProperties.append("Special : ").append(info[11]).append("\n");
    }

    private void makeStringBuilderForSpellAndTrap(StringBuilder cardProperties, String[] info) {
        cardProperties.append("name : ").append(info[0]).append("\n");
        cardProperties.append("type : ").append(info[1]).append("\n");
        cardProperties.append("description : ").append(info[3]).append("\n");
        cardProperties.append("status : ").append(info[4]).append("\n");
        cardProperties.append("Price : ").append(info[5]).append("\n");
        cardProperties.append("Icon : ").append(info[2]).append("\n");
        cardProperties.append("Special : ").append(info[7]).append("\n");
    }

    public String exportCardInJson(String cardName) {
        Cards card = Cards.getCard(cardName);
        if (card == null) {
            return cardWithThisNameIsNotValid;
        }
        File file = new File("./src/main/resources/exportedCardsInJson/" + cardName + ".json");
        if (file.exists()) {
            return cardWithThisNameHasAlreadyExported;
        }

        JSONObject cardContent;
        cardContent = setJsonOfCard(card);
        try {
            FileWriter writer = new FileWriter("./src/main/resources/exportedCardsInJson/" + card.getName() +
                    ".json");
            writer.write(cardContent.toJSONString());
            writer.close();
        } catch (IOException e) {
            return error;
        }
        return cardExportedSuccessfully;

    }

    public String importCardFromJson(String cardName) {
        File file = new File("./src/main/resources/exportedCardsInJson/" + cardName + ".json");
        if (!file.exists()) {
            return thereIsNoCardWithThisNameToImport;
        }
        Object object = null;
        try {
            object = new JSONParser().parse(new FileReader("./src/main/resources/exportedCardsInJson/"
                    + cardName + ".json"));
        } catch (IOException | ParseException e) {
            return error;
        }
        if (object == null) {
            return thereIsNoCardWithThisNameToImport;
        }
        if (Cards.getCard(cardName) == null){
            CardCreatorController.newCardNames.add(cardName); // to user a picture for it
        }
        JSONObject cardInJson = (JSONObject) object;

        Cards card = Cards.getCard(cardName);
        if (card != null) {
            printerAndScanner.printNextLine(thereIsAlreadyACardWithThisName);
            return createStringFromJson(cardInJson);
        }
        createCard(cardInJson);
        return createStringFromJson(cardInJson);
    }

    private void createCard(JSONObject cardInJson) {
        try {
            if (cardInJson.get("cardType").equals("monster")) {
                new MonsterCards((String) cardInJson.get("name"), Math.toIntExact((Long) cardInJson.get("level")),
                        (String) cardInJson.get("attribute"), (String) cardInJson.get("monsterType"),
                        (String) cardInJson.get("type"), Math.toIntExact((Long) cardInJson.get("attack")),
                        Math.toIntExact((Long) cardInJson.get("defense")), (String) cardInJson.get("description"),
                        (String) cardInJson.get("status"),Math.toIntExact((Long) cardInJson.get("specialSpeed")),
                        InstantiateCards.loadSpecialAbilities(((String) cardInJson.get("specialsInString"))
                                .split("&&")), (String) cardInJson.get("specialsInString"));
            } else if (cardInJson.get("cardType").equals("spell")) {
                new SpellCards((String) cardInJson.get("name"), (String) cardInJson.get("type"),
                        (String) cardInJson.get("icon"), (String) cardInJson.get("description"),
                        (String) cardInJson.get("status"), Math.toIntExact((Long) cardInJson.get("specialSpeed")), InstantiateCards.
                        loadSpecialAbilities(((String) cardInJson.get("specialsInString")).split("&&")),
                        InstantiateCards.getChainJobs((String) cardInJson.get("chainJobInString")),
                        (String) cardInJson.get("specialsInString"), (String) cardInJson.get("chainJobInString"));
            } else if (cardInJson.get("cardType").equals("trap")) {
                new TrapCards((String) cardInJson.get("name"), (String) cardInJson.get("type"),
                        (String) cardInJson.get("icon"), (String) cardInJson.get("description"),
                        (String) cardInJson.get("status"), Math.toIntExact((Long) cardInJson.get("specialSpeed")), InstantiateCards.
                        loadSpecialAbilities(((String) cardInJson.get("specialsInString")).split
                                ("&&")), InstantiateCards.getChainJobs((String) cardInJson.get("chainJobInString")
                ), (String) cardInJson.get("specialsInString"), (String) cardInJson.get("chainJobInString"));
            }
            Shop.addCard((String) cardInJson.get("name"), Math.toIntExact((Long) cardInJson.get("Price")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createStringFromJson(JSONObject cardInJson) {
        StringBuilder response = new StringBuilder();
        response.append("name : ").append((String) cardInJson.get("name")).append("\n");
        response.append("type : ").append((String) cardInJson.get("type")).append("\n");
        response.append("description : ").append((String) cardInJson.get("description")).append("\n");
        response.append("status : ").append((String) cardInJson.get("status")).append("\n");
        response.append("Price : ").append( cardInJson.get("Price")).append("\n");
        try {
            if (cardInJson.get("cardType").equals("monster")) {
                response.append("level : ").append( cardInJson.get("level")).append("\n");
                response.append("attack : ").append( cardInJson.get("attack")).append("\n");
                response.append("defense : ").append( cardInJson.get("defense")).append("\n");
                response.append("attribute : ").append((String) cardInJson.get("attribute")).append("\n");
                response.append("monsterType : ").append((String) cardInJson.get("monsterType")).append("\n");
            } else if (cardInJson.get("cardType").equals("spell")) {
                response.append("icon : ").append((String) cardInJson.get("icon")).append("\n");
            } else if (cardInJson.get("cardType").equals("trap")) {
                response.append("icon : ").append((String) cardInJson.get("icon")).append("\n");
            }
            response.append("Special : ").append((String) cardInJson.get("specialsInString"));

        } catch (Exception e) {
            e.printStackTrace();
            return error;
        }
        return response.toString();
    }

    public JSONObject setJsonOfCard(Cards card) {
        JSONObject cardContent = new JSONObject();
        cardContent.put("name", card.getName());
        cardContent.put("type", card.getType());
        cardContent.put("description", card.getDescription());
        cardContent.put("status", card.getStatus());
        cardContent.put("specialSpeed", card.getSpecialSpeed());
        cardContent.put("specialsInString", card.getSpecialsInString());
        cardContent.put("Price", Shop.getInstance().getItemPrize(card.getName()));
        if (card instanceof MonsterCards) {
            cardContent.put("cardType", "monster");
            MonsterCards monsterCards = (MonsterCards) card;
            cardContent.put("level", monsterCards.getLevel());
            cardContent.put("attack", monsterCards.getAttack());
            cardContent.put("defense", monsterCards.getDefense());
            cardContent.put("attribute", monsterCards.getAttribute());
            cardContent.put("monsterType", monsterCards.getMonsterType());
        } else if (card instanceof SpellCards) {
            cardContent.put("cardType", "spell");
            SpellCards spellCards = (SpellCards) card;
            cardContent.put("icon", spellCards.getIcon());
            cardContent.put("chainJobInString", spellCards.getChainJobInString());
        } else if (card instanceof TrapCards) {
            cardContent.put("cardType", "trap");
            TrapCards trapCards = (TrapCards) card;
            cardContent.put("icon", trapCards.getIcon());
            cardContent.put("chainJobInString", trapCards.getChainJobInString());
        }
        return cardContent;
    }

    public void showCurrentMenu() {
        printerAndScanner.printNextLine(showCurrentInExportAndImportController);
    }

}

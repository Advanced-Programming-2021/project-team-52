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
    public void exportCardInCSV(String cardName) {
        Cards card = Cards.getCard(cardName);
        if (card == null) {
            printerAndScanner.printNextLine(cardWithThisNameIsNotValid);
            return;
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
            System.out.println(cardWithThisNameHasAlreadyExported);
            return;
        }
        writeDataLineByLine(filePath, data);
        printerAndScanner.printNextLine(cardExportedSuccessfully);
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

    public void importCardFromCSV(String cardName) {
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
                            return;
                        }
                        try {
                            new MonsterCards(info[0], Integer.parseInt(info[1]), info[2], info[3], info[4],
                                    Integer.parseInt(info[5]), Integer.parseInt(info[6]), info[7], info[9],
                                    Integer.parseInt(info[10]), InstantiateCards.loadSpecialAbilities
                                    (info[11].split("&&")), info[11]);
                            Shop.addCard(info[0], Integer.parseInt(info[8]));
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
                            return;
                        }
                        try {
                            new SpellCards(info[0], info[1], info[2], info[3], info[4], Integer.parseInt(info[6]),
                                    InstantiateCards.loadSpecialAbilities(info[7].split("&&")),
                                    InstantiateCards.getChainJobs(info[8]), info[7], info[8]);
                            Shop.addCard(info[0], Integer.parseInt(info[5]));
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
                            return;
                        }
                        try {
                            new TrapCards(info[0], info[1], info[2], info[3], info[4], Integer.parseInt(info[6]),
                                    InstantiateCards.loadSpecialAbilities(info[7].split("&&")),
                                    InstantiateCards.getChainJobs(info[8]), info[7], info[8]);
                            Shop.addCard(info[0], Integer.parseInt(info[5]));
                            printerAndScanner.printNextLine(cardImportedSuccessfully);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                fileReader.close();
            } else {
                printerAndScanner.printNextLine(thereIsNoCardWithThisNameToImport);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void exportCardInJson(String cardName) {
        Cards card = Cards.getCard(cardName);
        if (card == null) {
            printerAndScanner.printNextLine(cardWithThisNameIsNotValid);
            return;
        }
        File file = new File("./src/main/resources/exportedCardsInJson/" + cardName + ".json");
        if (file.exists()) {
            System.out.println(cardWithThisNameHasAlreadyExported);
            return;
        }

        JSONObject cardContent;
        cardContent = setJsonOfCard(card);
        try {
            FileWriter writer = new FileWriter("./src/main/resources/exportedCardsInJson/" + card.getName() +
                    ".json");
            writer.write(cardContent.toJSONString());
            writer.close();
        } catch (IOException e) {
            System.out.println(error);
        }
        printerAndScanner.printNextLine(cardExportedSuccessfully);
    }

    public void importCardFromJson(String cardName) {
        File file = new File("./src/main/resources/exportedCardsInJson/" + cardName + ".json");
        if (!file.exists()) {
            printerAndScanner.printNextLine(thereIsNoCardWithThisNameToImport);
            return;
        }
        Object object = null;
        try {
            object = new JSONParser().parse(new FileReader("./src/main/resources/exportedCardsInJson/"
                    + cardName + ".json"));
        } catch (IOException | ParseException e) {
            printerAndScanner.printNextLine(error);
        }
        if (object == null) {
            printerAndScanner.printNextLine(thereIsNoCardWithThisNameToImport);
            return;
        }
        JSONObject cardInJson = (JSONObject) object;
        printerAndScanner.printNextLine(cardInJson.toJSONString());

        Cards card = Cards.getCard(cardName);
        if (card != null) {
            printerAndScanner.printNextLine(thereIsAlreadyACardWithThisName);
            return;
        }
        createCard(cardInJson);
        printerAndScanner.printNextLine(cardImportedSuccessfully);
    }

    private void createCard(JSONObject cardInJson) {
        try {
            if (cardInJson.get("cardType").equals("monster")) {
                new MonsterCards((String) cardInJson.get("name"), (Integer) cardInJson.get("level"),
                        (String) cardInJson.get("attribute"), (String) cardInJson.get("monsterType"),
                        (String) cardInJson.get("type"), (Integer) cardInJson.get("attack"),
                        (Integer) cardInJson.get("defense"), (String) cardInJson.get("description"),
                        (String) cardInJson.get("status"), (Integer) cardInJson.get("status"),
                        InstantiateCards.loadSpecialAbilities(((String) cardInJson.get("specialsInString"))
                                .split("&&")), (String) cardInJson.get("specialsInString"));
            } else if (cardInJson.get("cardType").equals("spell")) {
                new SpellCards((String) cardInJson.get("name"), (String) cardInJson.get("type"),
                        (String) cardInJson.get("icon"), (String) cardInJson.get("description"),
                        (String) cardInJson.get("status"), (Integer) cardInJson.get("specialSpeed"), InstantiateCards.
                        loadSpecialAbilities(((String) cardInJson.get("specialsInString")).split("&&")),
                        InstantiateCards.getChainJobs((String) cardInJson.get("chainJobInString")),
                        (String) cardInJson.get("specialsInString"), (String) cardInJson.get("chainJobInString"));
            } else if (cardInJson.get("cardType").equals("trap")) {
                new TrapCards((String) cardInJson.get("name"), (String) cardInJson.get("type"),
                        (String) cardInJson.get("icon"), (String) cardInJson.get("description"),
                        (String) cardInJson.get("status"), (Integer) cardInJson.get("specialSpeed"), InstantiateCards.
                        loadSpecialAbilities(((String) cardInJson.get("specialsInString")).split
                                ("&&")), InstantiateCards.getChainJobs((String) cardInJson.get("chainJobInString")
                ), (String) cardInJson.get("specialsInString"), (String) cardInJson.get("chainJobInString"));
            }
            Shop.addCard((String) cardInJson.get("name"), (Integer) cardInJson.get("Price"));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

package controller;

import model.cards.Cards;
import model.cards.monster.MonsterCards;
import model.cards.spell.SpellCards;
import model.cards.trap.TrapCards;
import model.tools.RegexPatterns;
import model.tools.StringMessages;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import view.PrinterAndScanner;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;

public class ImportAndExportController implements RegexPatterns, StringMessages {
    private static ImportAndExportController importAndExportController = null;
    private PrintBuilderController printBuilderController = PrintBuilderController.getInstance();
    private PrinterAndScanner printerAndScanner = PrinterAndScanner.getInstance();

    private ImportAndExportController() {
    }

    public static ImportAndExportController getInstance() {
        if (importAndExportController == null)
            importAndExportController = new ImportAndExportController();
        return importAndExportController;
    }

    public void run() {
        File file = new File("./src/main/resources/exportedCards");
        try {
            file.mkdir();
        } catch (Exception ignored) {
        }
        String command = printerAndScanner.scanNextLine();
        Matcher matcher;
        while (true) {
            if ((matcher = RegexController.getMatcher(command, importCardPattern)) != null)
                importCard(matcher.group("cardName"));
            else if ((matcher = RegexController.getMatcher(command, exportCardPattern)) != null)
                exportCard(matcher.group("cardName"));
            else if ((matcher = RegexController.getMatcher(command, menuPattern)) != null) {
                if (RegexController.hasField(matcher, "showCurrent")) {
                    showCurrentMenu();
                } else if (RegexController.hasField(matcher, "exit")) {
                    break;
                } else if (RegexController.hasField(matcher, "enter")) {
                    printerAndScanner.printNextLine(impossibilityOfMenuNavigation);
                } else
                    printerAndScanner.printNextLine(invalidCommand);
            } else
                printerAndScanner.printNextLine(invalidCommand);
            command = printerAndScanner.scanNextLine();
        }
    }

    public void exportCard(String cardName) {
        Cards card = Cards.getCard(cardName);
        if (card == null) {
            printerAndScanner.printNextLine(cardWithThisNameIsNotValid);
            return;
        }
        File file = new File("./src/main/resources/exportedCards/" + cardName + ".json");
        if (file.exists()) {
            System.out.println(cardWithThisNameHasAlreadyExported);
            return;
        }

        JSONObject cardContent;
        cardContent = setJsonOfCard(card);
        try {
            FileWriter writer = new FileWriter("./src/main/resources/exportedCards/" + card.getName() +
                    ".json");
            writer.write(cardContent.toJSONString());
            writer.close();
        } catch (IOException e) {
            System.out.println(error);
        }
        printerAndScanner.printNextLine(cardExportedSuccessfully);
    }

    public void importCard(String cardName) {
        File file = new File("./src/main/resources/exportedCards/" + cardName + ".json");
        if (!file.exists()) {
            printerAndScanner.printNextLine(thereIsNoCardWithThisNameToImport);
            return;
        }
        Object object = null;
        try {
            object = new JSONParser().parse(new FileReader("./src/main/resources/exportedCards/" + cardName +
                    ".json"));
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

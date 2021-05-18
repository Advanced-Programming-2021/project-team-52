package controller;

import controller.specialbilities.*;
import model.Shop;
import model.cards.monster.MonsterCards;
import model.cards.spell.SpellCards;
import model.cards.trap.TrapCards;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import model.game.Field;
import model.game.STATUS;
import model.tools.CHAIN_JOB;
import model.tools.RegexPatterns;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.lang.reflect.Method;

//TODO :  Change csv file Path
public abstract class InstantiateCards implements RegexPatterns {
    public static void start() throws IOException, CsvException {
        FileReader file = new FileReader("./src/main/resources/CSV/Monster.csv");
        ArrayList<String[]> list = new ArrayList<>(new CSVReaderBuilder(file).withSkipLines(1).build().readAll());
        list.forEach(info -> {
            try {
                new MonsterCards(info[0], Integer.parseInt(info[1]), info[2], info[3], info[4],
                        Integer.parseInt(info[5]), Integer.parseInt(info[6]), info[7], info[9],
                        Integer.parseInt(info[10]), loadSpecialAbilities(info[11].split("&&")));
                Shop.addCard(info[0], Integer.parseInt(info[8]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        file.close();
        file = new FileReader("./src/main/resources/CSV/Trap.csv");
        list = new ArrayList<>(new CSVReaderBuilder(file).withSkipLines(1).build().readAll());
        list.forEach(info -> {
            try {
                new TrapCards(info[0], info[1], info[2], info[3], info[4], Integer.parseInt(info[6]),
                        loadSpecialAbilities(info[7].split("&&")), getChainJobs(info[8]));
                Shop.addCard(info[0], Integer.parseInt(info[5]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        file.close();
        file = new FileReader("./src/main/resources/CSV/Spell.csv");
        list = new ArrayList<>(new CSVReaderBuilder(file).withSkipLines(1).build().readAll());
        list.forEach(info -> {
            try {
                new SpellCards(info[0], info[1], info[2], info[3], info[4], Integer.parseInt(info[6]),
                        loadSpecialAbilities(info[7].split("&&")), getChainJobs(info[8]));
                Shop.addCard(info[0], Integer.parseInt(info[5]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static ArrayList<CHAIN_JOB> getChainJobs(String chainJobs) throws Exception{
        ArrayList<CHAIN_JOB> jobs = new ArrayList<>();
        if (!chainJobs.equals("nothing")) {
            CHAIN_JOB thisJob;
            for (String chainJob : chainJobs.split("/")) {
                thisJob = CHAIN_JOB.getValueByString(chainJob);
                if (thisJob == null)
                    throw new Exception("could not find chain job : " + chainJob);
                else jobs.add(thisJob);
            }
        }
        return jobs;
    }

    private static ArrayList<SpecialAbility> loadSpecialAbilities(String[] abilities) throws Exception {
        ArrayList<String> test = new ArrayList<>(Arrays.asList(abilities));
        if (abilities[0].equals("nothing"))
            return new ArrayList<SpecialAbility>();
        else {
            ArrayList<SpecialAbility> specials = new ArrayList<>();
            for (String ability : abilities) {
                Matcher matcher = RegexController.getMatcher(ability, extractSpecial);
                findWhatSpecialItIs(specials, ability, matcher);
            }
            return specials;
        }
    }

    private static void findWhatSpecialItIs(ArrayList<SpecialAbility> specials, String ability, Matcher matcher) throws Exception {
        if (matcher == null)
            throw new Exception("unable to read special ability : " + ability);
        if (ability.startsWith("DeathWish"))
            specials.add(addDeathWishSpecial(matcher));
        else if (ability.startsWith("Tribute"))
            specials.add(addTributeSpecial(matcher));
        else if (ability.startsWith("AttackSpecial"))
            specials.add(addAttackSpecial(matcher));
        else if (ability.startsWith("Flip"))
            specials.add(addFlipSpecial(matcher));
        else if (ability.startsWith("ActivateNoChain"))
            specials.add(addActivateNoChainSpecial(matcher));
        else if (ability.startsWith("Continuous"))
            specials.add(addContinuousSpecial(matcher));
        else if (ability.startsWith("Success"))
            specials.add(addSuccessSpecial(matcher));
        else if (ability.startsWith("Conditions"))
            specials.add(addConditionsSpecial(matcher));
        else if (ability.startsWith("FaceUp"))
            specials.add(addFaceUpSpecial(matcher));
        else if (ability.startsWith("ActivateChain"))
            specials.add(addActivateChainSpecial(matcher));
        else if (ability.startsWith("FieldSpecial"))
            specials.add(addFieldSpecial(matcher));
        else if (ability.startsWith("Equip"))
            specials.add(addEquipSpecial(matcher));
        else if (ability.startsWith("RitualSummon"))
            specials.add(addRitualSummonSpecial(matcher));
        else if (ability.startsWith("UponActivation"))
            specials.add(addUponActivationSpecial(matcher));
        else throw new Exception("unable to instantiate this ability : " + ability);
    }

    private static SpecialAbility addDeathWishSpecial(Matcher matcher) throws NoSuchMethodException {
        DeathWish deathWish = new DeathWish();
        String[] subsetSplit = new String[0];
        if (matcher.group(2) != null) {
            subsetSplit = matcher.group(2).split("/");
        }
        if ("removeAllAttackBoost".equals(matcher.group("methodName"))) {
                deathWish.setAmount(Integer.parseInt(subsetSplit[0]));
        }
        deathWish.setMethod(deathWish.getClass().getDeclaredMethod(matcher.group("methodName")));
        return deathWish;
    }

    private static SpecialAbility addTributeSpecial(Matcher matcher) throws NoSuchMethodException{
        Tribute tribute = new Tribute();
        String[] subsetSplit = new String[0];
        if (matcher.group(2) != null)
            subsetSplit = matcher.group(2).split("/");
        switch (matcher.group("methodName")){
            case "canSummonNormally" :
                tribute.setKey(Integer.parseInt(subsetSplit[2]));
            case "summonWithTribute" :
                tribute.setAmount(Integer.parseInt(subsetSplit[0]));
                tribute.setSpecialSummon(matcher.group(1).equals("normal"));
                break;
        }
        tribute.setMethod(tribute.getClass().getDeclaredMethod(matcher.group("methodName")));
        return tribute;
    }

    private static SpecialAbility addAttackSpecial(Matcher matcher) throws NoSuchMethodException {
        AttackSpecial attackSpecial = new AttackSpecial();
        attackSpecial.setMethod(attackSpecial.getClass().getDeclaredMethod(matcher.group("methodName")));
        return attackSpecial;
    }

    private static SpecialAbility addFlipSpecial(Matcher matcher) throws NoSuchMethodException {
        Flip flip  = new Flip();
        flip.setMethod(flip.getClass().getDeclaredMethod(matcher.group("methodName")));
        return flip;
    }

    private static SpecialAbility addActivateNoChainSpecial(Matcher matcher) throws NoSuchMethodException {
        ActivateNoChain activateNoChain = new ActivateNoChain();
        String[] subsetSplit = new String[0];
        if (matcher.group(2) != null)
            subsetSplit = matcher.group(2).split("/");
        switch (matcher.group("methodName")){
            case "specialSummonFromGraveYard" :
                activateNoChain.setOnlyForOnePlayer(!subsetSplit[0].equals("both"));
                break;
            case "turnRemaining" :
            case "drawCard" :
                activateNoChain.setAmount(Integer.parseInt(subsetSplit[0]));
                break;
            case "killAllMonsters" :
                activateNoChain.setOnlyForOnePlayer(subsetSplit[0].equals("enemyOnly"));
                break;
        }
        activateNoChain.setMethod(activateNoChain.getClass().getDeclaredMethod(matcher.group("methodName")));
        return activateNoChain;
    }

    private static SpecialAbility addContinuousSpecial(Matcher matcher) throws NoSuchMethodException {
        Continuous continuous = new Continuous();
        String[] subsetSplit = new String[0];
        if (matcher.group(2) != null)
            subsetSplit = matcher.group(2).split("/");
        switch (matcher.group("methodName")){
            case "payHealthEveryRound" :
            case "monstersCannotAttack" :
            case "getLPIfEnemySpellIsActivated" :
                continuous.setAmount(Integer.parseInt(subsetSplit[0]));
                break;
        }
        continuous.setMethod(continuous.getClass().getDeclaredMethod(matcher.group("methodName")));
        return continuous;
    }

    private static SpecialAbility addSuccessSpecial(Matcher matcher) throws NoSuchMethodException {
        Success success = new Success();
        success.setMethod(success.getClass().getDeclaredMethod(matcher.group("methodName")));
        return success;
    }

    private static SpecialAbility addConditionsSpecial(Matcher matcher) throws NoSuchMethodException {
        Conditions condition = new Conditions();
        String[] subsetSplit = new String[0];
        if (matcher.group(2) != null)
            subsetSplit = matcher.group(2).split("/");
        if (matcher.group("methodName").equals("affectHasAtLeastThisDamage"))
            condition.setAmount(Integer.parseInt(subsetSplit[0]));
        condition.setMethod(condition.getClass().getDeclaredMethod(matcher.group("methodName")));
        return condition;
    }

    private static SpecialAbility addFaceUpSpecial(Matcher matcher) throws NoSuchMethodException {
        FaceUp faceUp = new FaceUp();
        String[] subsetSplit = new String[0];
        if (matcher.group(2) != null)
            subsetSplit = matcher.group(2).split("/");
        if ("boostAllAttack".equals(matcher.group("methodName"))) {
            faceUp.setBoostAmount(Integer.parseInt(subsetSplit[0]));
        }
        faceUp.setMethod(faceUp.getClass().getDeclaredMethod(matcher.group("methodName")));
        return faceUp;
    }

    private static SpecialAbility addActivateChainSpecial(Matcher matcher) throws NoSuchMethodException {
        ActivateChain activateChain = new ActivateChain();
        String[] subsetSplit = new String[0];
        if (matcher.group(2) != null)
            subsetSplit = matcher.group(2).split("/");
        switch (matcher.group("methodName")){
            case "destroySpellAndTraps" :
                activateChain.setAmount(Integer.parseInt(subsetSplit[0]));
                break;
            case "destroyAllEnemyMonstersInThisStatus" :
                activateChain.setStatus(STATUS.getStatusByString(subsetSplit[0]));
                break;
        }
        activateChain.setMethod(activateChain.getClass().getDeclaredMethod(matcher.group("methodName")));
        return activateChain;
    }

    private static SpecialAbility addFieldSpecial(Matcher matcher) throws NoSuchMethodException {
        FieldSpecial fieldSpecial = new FieldSpecial();
        String[] subsetSplit = new String[0];
        if (matcher.group(2) != null)
            subsetSplit = matcher.group(2).split("/");
        switch (matcher.group("methodName")) {
            case "attackChange" :
            case "defenseChange" :
                fieldSpecial.setAmount(Integer.parseInt(subsetSplit[0]));
                break;
            case "attackBoostForGraveyard" :
                fieldSpecial.setQuantifier(Integer.parseInt(subsetSplit[0]));
        }
        fieldSpecial.setEnemyAsWell(subsetSplit[1].equals("enemyAsWell"));
        ArrayList<String> types = new ArrayList<>();
        if (subsetSplit.length == 2)
            types.addAll(Arrays.asList(subsetSplit[2].split("\\+")));
        fieldSpecial.setType(types);
        fieldSpecial.setMethod(fieldSpecial.getClass().getDeclaredMethod(matcher.group("methodName")));
        return fieldSpecial;
    }

    private static SpecialAbility addEquipSpecial(Matcher matcher) throws NoSuchMethodException {
        Equip equip = new Equip();
        String[] subsetSplit = new String[0];
        if (matcher.group(2) != null)
            subsetSplit = matcher.group(2).split("/");
        switch (matcher.group("methodName")){
            case "normalEquip" : {
                equip.setAttackChange(Integer.parseInt(subsetSplit[0]));
                equip.setDefenseChange(Integer.parseInt(subsetSplit[1]));
                ArrayList<String> types = new ArrayList<>();
                if (subsetSplit.length == 3)
                    types.addAll(Arrays.asList(subsetSplit[2].split("\\+")));
                equip.setTypes(types);
            }
                break;
            case "boostByControlledMonsters" :
                equip.setQuantifier(Integer.parseInt(subsetSplit[0]));
                break;
            case "dynamicEquip" : {
                ArrayList<String> types = new ArrayList<>();
                if (subsetSplit.length == 1)
                    types.addAll(Arrays.asList(subsetSplit[0].split("\\+")));
                equip.setTypes(types);
            }
        }
        equip.setMethod(equip.getClass().getDeclaredMethod(matcher.group("methodName")));
        return equip;
    }

    private static SpecialAbility addRitualSummonSpecial(Matcher matcher) throws NoSuchMethodException {
        RitualSummon ritualSummon = new RitualSummon();
        ritualSummon.setMethod(ritualSummon.getClass().getMethod(matcher.group("methodName")));
        return ritualSummon;
    }

    private static SpecialAbility addUponActivationSpecial(Matcher matcher) throws NoSuchMethodException {
        UponActivation uponActivation = new UponActivation();
        String[] subsetSplit = new String[0];
        if (matcher.group(2) != null)
            subsetSplit = matcher.group(2).split("/");
        if (matcher.group("methodName").equals("payLpForActivation"))
            uponActivation.setAmount(Integer.parseInt(subsetSplit[0]));
        uponActivation.setMethod(uponActivation.getClass().getDeclaredMethod(matcher.group("methodName")));
        return uponActivation;
    }
}

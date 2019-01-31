package transformer.Service;

import org.springframework.stereotype.Service;
import transformer.model.Transformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author paula hara
 */
@Service
public class BattleScoreService {

    private List<Transformer> autobots;
    private List<Transformer> decepticons;
    private List<Transformer> winnersAutobots;
    private List<Transformer> winnersDecepticons;
    private List<Transformer> losersAutobots;
    private List<Transformer> losersDecepticons;

    /**
     * Set autobot and decepticon teams.<br>
     * Then do the sort based on their rank.
     *
     * @param transformers
     */
    private void setTeam(List<Transformer> transformers) {
        for(Transformer transformer : transformers) {
            if(transformer.getType() == 'A') {
                autobots.add(transformer);
            } else {
                decepticons.add(transformer);
            }
        }

        Collections.sort(autobots, Comparator.comparingInt(Transformer::getRank).reversed());
        Collections.sort(decepticons, Comparator.comparingInt(Transformer::getRank).reversed());
    }

    /**
     * Get the value of overall rating => Strength + Intelligence + Speed + Endurance + Firepower
     *
     * @param transformer
     * @return
     */
    private int getRating(Transformer transformer){
        return transformer.getStrength() + transformer.getIntelligence() + transformer.getSpeed()
                + transformer.getEndurance() + transformer.getFirepower();
    }

    /**
     * Evaluates a fight between Autobots and decepticons. <br>
     * The final score will be given in a String.
     *
     * @param transformers
     * @return String with the final result of the battle
     */
    public String transformerBattleScore(List<Transformer> transformers){
        initializeLists();

        setTeam(transformers);

        int numOfFights = decepticons.size() < autobots.size() ? decepticons.size() : autobots.size();

        for (int index = 0; index < numOfFights; index++) {
            boolean continueFight;
            Transformer decepticon = decepticons.get(index);
            Transformer autobot = autobots.get(index);

            if(decepticon.getName().equalsIgnoreCase("Predaking")
                    || autobot.getName().equalsIgnoreCase("Optimus Prime")) {

                continueFight = evaluateSpecialRules(decepticon, autobot);

                if(!continueFight) {
                    return getFinalBattleScore(numOfFights);
                }
            } else {
                evaluateFight(decepticon, autobot);
            }
        }

        return getFinalBattleScore(numOfFights);
    }

    private void initializeLists() {
        autobots = new ArrayList<>();
        decepticons = new ArrayList<>();
        winnersAutobots = new ArrayList<>();
        winnersDecepticons = new ArrayList<>();
        losersAutobots = new ArrayList<>();
        losersDecepticons = new ArrayList<>();
    }

    /**
     * Final output should be: <br>
     * 'numOfBattles' battle <br>
     * Winning team ('WinningTeamName'): 'MembersOfWinningTeam' <br>
     * Survivors from the losing team ('LoserTeamName'): 'SurvivorsOfLosingTeam' <br>
     *
     * @param numOfBattles
     * @return String with the final result of the battle
     */
    private String getFinalBattleScore(int numOfBattles) {
        String battleScore = numOfBattles + " battle\nWinning team (";
        if(winnersAutobots.size() > winnersDecepticons.size()) {
            decepticons.removeAll(losersDecepticons);

            battleScore += "Autobots): " + winnersAutobots.toString()
                    .replace("[", "").replace("]", "")
                    + "\nSurvivors from the losing team (Decepticons): " + decepticons.toString()
                    .replace("[", "").replace("]", "");
        } else if(winnersAutobots.size() < winnersDecepticons.size()) {
            autobots.removeAll(losersAutobots);

            battleScore += "Decepticons): " + winnersDecepticons.toString()
                    .replace("[", "").replace("]", "")
                    + "\nSurvivors from the losing team (Autobots): " + autobots.toString()
                    .replace("[", "").replace("]", "");
        } else {
            battleScore = numOfBattles + " battle\nTied battle.";
        }
        return battleScore;
    }

    /**
     * A battle between opponents uses the following rules:
     * <ul>
     *  <li>If any fighter is down 4 or more points of courage and 3 or more points of strength
     *  compared to their opponent, the opponent automatically wins the face-off
     *  regardless of overall rating (opponent has ran away)</li>
     *  <li>Otherwise, if one of the fighters is 3 or more points of skill above their opponent,
     *  they win the fight regardless of overall rating</li>
     *  <li>The winner is the Transformer with the highest overall rating</li>
     *  <li>In the event of a tie, both are destroyed</li>
     * </ul>
     *
     * @param decepticon
     * @param autobot
     */
    private void evaluateFight(Transformer decepticon, Transformer autobot) {
        if ((decepticon.getCourage() - autobot.getCourage() >= 4 &&
                decepticon.getStrength() - autobot.getStrength() >= 3)
                || decepticon.getSkill() - autobot.getSkill() >= 3) {
            winnersDecepticons.add(decepticon);
            losersAutobots.add(autobot);
        } else if ((autobot.getCourage() - decepticon.getCourage() >= 4 &&
                autobot.getStrength() - decepticon.getStrength() >= 3)
                || autobot.getSkill() - decepticon.getSkill() >= 3) {
            winnersAutobots.add(autobot);
            losersDecepticons.add(decepticon);
        } else if (getRating(decepticon) > getRating(autobot)) {
            winnersDecepticons.add(decepticon);
            losersAutobots.add(autobot);
        } else if (getRating(decepticon) < getRating(autobot)) {
            winnersAutobots.add(autobot);
            losersDecepticons.add(decepticon);
        }else {
            // Tie -> Both lose
            losersAutobots.add(autobot);
            losersDecepticons.add(decepticon);
        }
    }

    /**
     * Any Transformer named Optimus Prime or Predaking wins his fight automatically
     * regardless of any other criteria <br>
     * In the event either of the above face each other (or a duplicate of each other),
     * the game immediately ends with all competitors destroyed
     *
     * @param decepticon
     * @param autobot
     */
    private boolean evaluateSpecialRules(Transformer decepticon, Transformer autobot) {
        if(decepticon.getName().equalsIgnoreCase("Predaking")
                || decepticon.getName().equalsIgnoreCase("Optimus Prime")
                || autobot.getName().equalsIgnoreCase("Optimus Prime")
                || autobot.getName().equalsIgnoreCase("Predaking")) {

            if (decepticon.getName().equalsIgnoreCase("Predaking")
                    && autobot.getName().equalsIgnoreCase("Optimus Prime")
                || autobot.getName().equalsIgnoreCase("Predaking")
                    && decepticon.getName().equalsIgnoreCase("Optimus Prime")) {
                autobots.removeAll(autobots);
                decepticons.removeAll(decepticons);
                return false;
            }

            if (decepticon.getName().equalsIgnoreCase("Predaking")
                || decepticon.getName().equalsIgnoreCase("Optimus Prime")) {
                winnersDecepticons.add(decepticon);
                losersAutobots.add(autobot);
            } else if (autobot.getName().equalsIgnoreCase("Optimus Prime")
                    || autobot.getName().equalsIgnoreCase("Predaking")) {
                winnersAutobots.add(autobot);
                losersDecepticons.add(decepticon);
            }
        }

        return true;
    }

}

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
    private void setTeam(List<Transformer> transformers) throws RuntimeException {
        if(transformers == null || transformers != null && transformers.size() == 0) {
            throw new RuntimeException("List of transformer can't be empty!");
        }

        for(Transformer transformer : transformers) {
            if(transformer.getType() == 'A') {
                this.autobots.add(transformer);
            } else {
                this.decepticons.add(transformer);
            }
        }

        Collections.sort(this.autobots, Comparator.comparingInt(Transformer::getRank).reversed());
        Collections.sort(this.decepticons, Comparator.comparingInt(Transformer::getRank).reversed());
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
     * Initialize all lists
     */
    private void initializeLists() {
        this.autobots = new ArrayList<>();
        this.decepticons = new ArrayList<>();
        this.winnersAutobots = new ArrayList<>();
        this.winnersDecepticons = new ArrayList<>();
        this.losersAutobots = new ArrayList<>();
        this.losersDecepticons = new ArrayList<>();
    }

    /**
     * Evaluates a fight between Autobots and decepticons. <br>
     * The final score will be given in a String.
     *
     * @param transformers
     * @return String with the final result of the battle
     */
    public String transformerBattleScore(List<Transformer> transformers) throws RuntimeException {
        this.initializeLists();

        this.setTeam(transformers);

        this.verifyNumOfTransformersToFight();

        int numOfFights = decepticons.size() < autobots.size() ? decepticons.size() : autobots.size();

        for (int index = 0; index < numOfFights; index++) {
            boolean continueFight;
            Transformer decepticon = decepticons.get(index);
            Transformer autobot = autobots.get(index);

            if(decepticon.getName().equalsIgnoreCase("Predaking")
                    || autobot.getName().equalsIgnoreCase("Optimus Prime")) {

                continueFight = this.evaluateSpecialRules(decepticon, autobot);

                if(!continueFight) {
                    return this.getFinalBattleScore(numOfFights);
                }
            } else {
                this.evaluateFight(decepticon, autobot);
            }
        }

        return this.getFinalBattleScore(numOfFights);
    }

    /**
     * Verify if there is at least 1 autobot and 1 decepticon.
     *
     * @throws Exception
     */
    private void verifyNumOfTransformersToFight() throws RuntimeException {
        if(this.decepticons.size() == 0 || this.autobots.size() == 0){
            throw new RuntimeException("There is not enough autobots or decepticons to fight!");
        }
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
        if(this.winnersAutobots.size() > this.winnersDecepticons.size()) {
            this.decepticons.removeAll(this.losersDecepticons);

            battleScore += "Autobots): " + this.winnersAutobots.toString()
                    .replace("[", "").replace("]", "")
                    + "\nSurvivors from the losing team (Decepticons): " + this.decepticons.toString()
                    .replace("[", "").replace("]", "");
        } else if(this.winnersAutobots.size() < this.winnersDecepticons.size()) {
            this.autobots.removeAll(this.losersAutobots);

            battleScore += "Decepticons): " + this.winnersDecepticons.toString()
                    .replace("[", "").replace("]", "")
                    + "\nSurvivors from the losing team (Autobots): " + this.autobots.toString()
                    .replace("[", "").replace("]", "");
        } else {
            battleScore = numOfBattles + " battle\nTied battle.";
        }
        return battleScore;
    }

    /**
     * A battle between opponents uses the following rules:
     * <ul>
     *     <li>First verify this conditions:</li>
     *     <ul>
     *        <li>If any fighter is down 4 or more points of courage and 3 or more points of strength
     *            compared to their opponent, the opponent automatically wins the face-off
     *            regardless of overall rating (opponent has ran away)</li>
     *        <li>Otherwise, if one of the fighters is 3 or more points of skill above their opponent,
     *            they win the fight regardless of overall rating</li>
     *      </ul>
     *  <li>The winner is the Transformer with the highest overall rating</li>
     *  <li>In the event of a tie, both are destroyed</li>
     * </ul>
     *
     * @param decepticon
     * @param autobot
     */
    private void evaluateFight(Transformer decepticon, Transformer autobot) {
        boolean evaluationComplete = false;

        if ((decepticon.getCourage() - autobot.getCourage() >= 4 &&
                decepticon.getStrength() - autobot.getStrength() >= 3)) {
            addWinnerAndOrLoser(decepticon, autobot, this.winnersDecepticons, this.losersAutobots);
            evaluationComplete = true;
        } else if ((autobot.getCourage() - decepticon.getCourage() >= 4 &&
                autobot.getStrength() - decepticon.getStrength() >= 3)) {
            addWinnerAndOrLoser(autobot, decepticon, this.winnersAutobots, this.losersDecepticons);
            evaluationComplete = true;
        }

        if(!evaluationComplete) {
            if (decepticon.getSkill() - autobot.getSkill() >= 3) {
                addWinnerAndOrLoser(decepticon, autobot, this.winnersDecepticons, this.losersAutobots);
                evaluationComplete = true;
            } else if (autobot.getSkill() - decepticon.getSkill() >= 3) {
                addWinnerAndOrLoser(autobot, decepticon, this.winnersAutobots, this.losersDecepticons);
                evaluationComplete = true;
            }
        }

        if(!evaluationComplete) {
            if (getRating(decepticon) > getRating(autobot)) {
                addWinnerAndOrLoser(decepticon, autobot, this.winnersDecepticons, this.losersAutobots);
            } else if (getRating(decepticon) < getRating(autobot)) {
                addWinnerAndOrLoser(autobot, decepticon, this.winnersAutobots, this.losersDecepticons);
            } else {
                // Tie -> Both lose
                addWinnerAndOrLoser(autobot, decepticon, this.losersAutobots, this.losersDecepticons);
            }
        }
    }

    /**
     * Add loser and winner or only losers to the lists
     *
     * @param winnerOrLoser
     * @param loser
     * @param winnerOrLoserList
     * @param loserList
     */
    private void addWinnerAndOrLoser(Transformer winnerOrLoser, Transformer loser,
                                     List<Transformer> winnerOrLoserList, List<Transformer> loserList) {
        winnerOrLoserList.add(winnerOrLoser);
        loserList.add(loser);
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
                this.autobots.removeAll(autobots);
                this.decepticons.removeAll(decepticons);
                return false;
            }

            if (decepticon.getName().equalsIgnoreCase("Predaking")
                || decepticon.getName().equalsIgnoreCase("Optimus Prime")) {
                addWinnerAndOrLoser(decepticon, autobot, this.winnersDecepticons, this.losersAutobots);
            } else if (autobot.getName().equalsIgnoreCase("Optimus Prime")
                    || autobot.getName().equalsIgnoreCase("Predaking")) {
                addWinnerAndOrLoser(autobot, decepticon, this.winnersAutobots, this.losersDecepticons);
            }
        }

        return true;
    }

}

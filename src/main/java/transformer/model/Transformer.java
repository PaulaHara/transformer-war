package transformer.model;

import javax.persistence.*;

/**
 * @author paula hara
 */
@Entity
@Table(name = "transformer")
public class Transformer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String name;

    @Column
    private char type;

    @Column
    private int strength;

    @Column
    private int intelligence;

    @Column
    private int speed;

    @Column
    private int endurance;

    @Column
    private int rank;

    @Column
    private int courage;

    @Column
    private int firepower;

    @Column
    private int skill;

    public Transformer(){}

    public Transformer(String name, char type, int strength, int intelligence, int speed,
                       int endurance, int rank, int courage, int firepower, int skill) {
        this.name = name;
        this.type = type;
        this.strength = strength;
        this.intelligence = intelligence;
        this.speed = speed;
        this.endurance = endurance;
        this.rank = rank;
        this.courage = courage;
        this.firepower = firepower;
        this.skill = skill;
    }

    public Transformer(Integer id, String name, char type, int strength, int intelligence, int speed,
                       int endurance, int rank, int courage, int firepower, int skill) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.strength = strength;
        this.intelligence = intelligence;
        this.speed = speed;
        this.endurance = endurance;
        this.rank = rank;
        this.courage = courage;
        this.firepower = firepower;
        this.skill = skill;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(char type) {
        this.type = type;
    }

    public char getType() {
        return type;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getStrength() {
        return strength;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setEndurance(int endurance) {
        this.endurance = endurance;
    }

    public int getEndurance() {
        return endurance;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public void setCourage(int courage) {
        this.courage = courage;
    }

    public int getCourage() {
        return courage;
    }

    public void setFirepower(int firepower) {
        this.firepower = firepower;
    }

    public int getFirepower() {
        return firepower;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public int getSkill() {
        return skill;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}

package alliness.wss.game.player;

import alliness.core.Serializable;

public class PlayerDTO extends Serializable{

    private double health;
    private double mana;
    private double exp;
    private int level;
    private String name;
    private String icon;


    public PlayerDTO(double health, double mana, double exp, int level, String name, String icon) {
        this.health = health;
        this.mana = mana;
        this.exp = exp;
        this.level = level;
        this.name = name;
        this.icon = icon;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getMana() {
        return mana;
    }

    public void setMana(double mana) {
        this.mana = mana;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

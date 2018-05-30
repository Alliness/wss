package alliness.wss.game.player.dto;

import alliness.core.Serializable;
import com.google.gson.annotations.SerializedName;

public class PlayerDTO extends Serializable {

    private double maxHealth;
    private double maxMana;
    private double currentHealth;
    private double currentMana;
    private double baseAttack;
    private double baseDefence;
    private double exp;
    private int level;
    private String name;
    private String icon;
    private String race;
    @SerializedName("class")
    private String playerClass;

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(double maxMana) {
        this.maxMana = maxMana;
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

    public String getRace() {
        return race;
    }

    public String getPlayerClass() {
        return playerClass;
    }

    public double getBaseAttack() {
        return baseAttack;
    }

    public double getBaseDefence() {
        return baseDefence;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public double getCurrentMana() {
        return currentMana;
    }

    public void setCurrentHealth(double currentHealth) {
        this.currentHealth = currentHealth;
    }

}

package alliness.wss.game.player;

import alliness.wss.game.player.dto.PlayerDTO;

public class Player extends PlayerDTO {

    private double currentHealth;
    private double currentMana;
    private String name;

    public Player(PlayerDTO dto) {
        currentHealth = dto.getMaxHealth();
        currentMana = dto.getMaxMana();
    }

    public void setCurrentMana(double currentMana) {
        this.currentMana = currentMana;
        if(this.currentMana < 0)
        {
            this.currentMana = 0;
        }
        if(this.currentMana > getMaxMana())
        {
            this.currentMana = getMaxMana();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public double getCurrentMana() {
        return currentMana;
    }

    public String getName() {
        return name;
    }

    public void setCurrentHealth(double currentHealth) {
        this.currentHealth = currentHealth;
        if (this.currentHealth < 0) {
            this.currentHealth = 0;
        }
        if(this.currentHealth > getMaxHealth())
        {
            this.currentHealth = getMaxHealth();
        }
    }
}

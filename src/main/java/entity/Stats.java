package entity;

import entity.Entity.EntityType;
import object.OBJ_Equipment;
import object.OBJ_Weapon;
import object.SuperObject;

public class Stats {
    protected Entity.EntityType type;
    protected String name;
    protected int maxLife, life, speed, level, str, mag, dex, atk, def, exp, nextLvlExp, gil, mana, maxMana;
    protected SuperObject weapon, shield;

    public Stats() {
        this.name = "";
        this.maxLife = 0;
        this.life = 1;
        this.speed = 0;
        this.level = 0;
        this.str = 0;
        this.mag = 0;
        this.dex = 0;
        this.atk = 0;
        this.def = 0;
        this.exp = 0;
        this.nextLvlExp = 0;
        this.gil = 0;
        this.mana = 0;
        this.maxMana = 0;
    }

    public EntityType getType() {
        return type;
    }

    public void setType(Entity.EntityType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxLife() {
        return maxLife;
    }

    public void setMaxLife(int maxLife) {
        this.maxLife = maxLife;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getDex() {
        return dex;
    }

    public void setDex(int dex) {
        this.dex = dex;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getNextLvlExp() {
        return nextLvlExp;
    }

    public void setNextLvlExp(int nextLvlExp) {
        this.nextLvlExp = nextLvlExp;
    }
    
    public int getGil() {
        return gil;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public int getMag() {
        return mag;
    }

    public void setMag(int mag) {
        this.mag = mag;
    }

    public void setGil(int gil) {
        this.gil = gil;
    }

    public SuperObject getWeapon() {
        return weapon;
    }

    public void setWeapon(SuperObject weapon) {
        this.weapon = weapon;
    }

    public SuperObject getShield() {
        return shield;
    }

    public void setShield(SuperObject shield) {
        this.shield = shield;
    }

    public void calculateAtk(OBJ_Weapon curWeapon) {
        atk = str + curWeapon.getAttackPoints() - 1;
    }

    public void calculateDef(OBJ_Equipment curWeapon) {
        def = dex + curWeapon.getDefensePoints() - 1;
    }
    
}

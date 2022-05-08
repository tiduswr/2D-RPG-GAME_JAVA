package entity;

import entity.Entity.EntityType;
import object.OBJ_Equipment;
import object.OBJ_Weapon;
import object.SuperObject;

public class Stats {
    protected Entity.EntityType type;
    protected String name;
    protected int maxLife, life, speed, level, str, dex, atk, def, exp, nextLvlExp, gil;
    protected SuperObject weapon, shield;

    public Stats() {
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

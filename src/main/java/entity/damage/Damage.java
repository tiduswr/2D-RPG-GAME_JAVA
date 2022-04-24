package entity.damage;

import entity.Entity;

public class Damage implements DamageAction{
    
    private final Entity attacker,defender;

    public Damage(Entity attacker, Entity defender) {
        this.attacker = attacker;
        this.defender = defender;
    }
    
    @Override
    public int calculateDamage(){
        int dmg = attacker.getStats().getAtk() - defender.getStats().getDef();
        if(dmg < 0){
            dmg = 0;
        }
        return dmg;
    }
    
}

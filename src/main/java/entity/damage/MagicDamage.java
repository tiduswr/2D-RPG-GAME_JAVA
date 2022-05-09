package entity.damage;

import entity.Entity;

public class MagicDamage implements DamageAction{

    private final Entity defender;
    private final int magicAttack;

    public MagicDamage(int magicAttack, Entity defender) {
        this.defender = defender;
        this.magicAttack = magicAttack;
    }
    
    @Override
    public int calculateDamage(){
        int dmg = magicAttack - defender.getStats().getDef();
        if(dmg < 0){
            dmg = 0;
        }
        return dmg;
    }
    
}

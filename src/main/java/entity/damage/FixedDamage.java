package entity.damage;

public class FixedDamage implements DamageAction {
    private final int DAMAGE;
    
    public FixedDamage(int damage){
        this.DAMAGE = damage;
    }
    
    @Override
    public int calculateDamage() {
        return DAMAGE;
    }
    
}

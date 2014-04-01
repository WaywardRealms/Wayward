package net.wayward_realms.waywardlib.skills;

public abstract class SpellBase extends SkillBase implements Spell {

    private int manaCost;

    @Override
    public int getManaCost() {
        return manaCost;
    }

    @Override
    public void setManaCost(int cost) {
        this.manaCost = cost;
    }

}

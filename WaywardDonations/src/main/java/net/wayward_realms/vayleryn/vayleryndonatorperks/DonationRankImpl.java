package net.wayward_realms.vayleryn.vayleryndonatorperks;

import net.wayward_realms.waywardlib.donation.DonationRank;
import net.wayward_realms.waywardlib.essentials.Kit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DonationRankImpl implements DonationRank, ConfigurationSerializable, Serializable {

    private static final long serialVersionUID = 4006488893217579179L;
    private String name;
    private Kit kit;
    private int money;
    private int levels;

    public DonationRankImpl(String name, Kit kit, int money, int levels) {
        this.kit = kit;
        this.money = money;
        this.levels = levels;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Kit getKit() {
        return kit;
    }

    @Override
    public void setKit(Kit kit) {
        this.kit = kit;
    }

    @Override
    public int getMoney() {
        return money;
    }

    @Override
    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public int getLevels() {
        return levels;
    }

    @Override
    public void setLevels(int levels) {
        this.levels = levels;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", name);
        serialised.put("kit", kit);
        serialised.put("money", money);
        serialised.put("levels", levels);
        return serialised;
    }

    public static DonationRankImpl deserialize(Map<String, Object> serialised) {
        return new DonationRankImpl((String) serialised.get("name"), (Kit) serialised.get("kit"), (Integer) serialised.get("money"), (Integer) serialised.get("levels"));
    }

}

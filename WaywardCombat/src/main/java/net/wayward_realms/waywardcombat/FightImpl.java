package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.combat.Turn;
import net.wayward_realms.waywardlib.skills.Skill;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.*;

public class FightImpl implements Fight {

    private Map<Integer, Location> characterLocations = new HashMap<>();
    private List<Integer> turnOrder = new ArrayList<>();
    private int turn = -1;
    private boolean active;
    private Turn activeTurn;
    private Map<Integer, Turn> savedTurns = new HashMap<>();

    private Inventory turnOptions = Bukkit.createInventory(null, 18, "Skill type");

    public FightImpl() {
        ItemStack meleeOffence = new ItemStack(Material.IRON_SWORD);
        ItemMeta meleeOffenceMeta = meleeOffence.getItemMeta();
        meleeOffenceMeta.setDisplayName("Melee offence");
        meleeOffence.setItemMeta(meleeOffenceMeta);

        ItemStack meleeDefence = new ItemStack(Material.IRON_CHESTPLATE);
        ItemMeta meleeDefenceMeta = meleeDefence.getItemMeta();
        meleeDefenceMeta.setDisplayName("Melee defence");
        meleeDefence.setItemMeta(meleeDefenceMeta);

        ItemStack rangedOffence = new ItemStack(Material.BOW);
        ItemMeta rangedOffenceMeta = rangedOffence.getItemMeta();
        rangedOffenceMeta.setDisplayName("Ranged offence");
        rangedOffence.setItemMeta(rangedOffenceMeta);

        ItemStack rangedDefence = new ItemStack(Material.FLINT);
        ItemMeta rangedDefenceMeta = rangedDefence.getItemMeta();
        rangedDefenceMeta.setDisplayName("Ranged defence");
        rangedDefence.setItemMeta(rangedDefenceMeta);

        ItemStack magicOffence = new ItemStack(Material.BLAZE_ROD);
        ItemMeta magicOffenceMeta = magicOffence.getItemMeta();
        magicOffenceMeta.setDisplayName("Magic offence");
        magicOffence.setItemMeta(magicOffenceMeta);

        ItemStack magicDefence = new ItemStack(Material.EMERALD);
        ItemMeta magicDefenceMeta = magicDefence.getItemMeta();
        magicDefenceMeta.setDisplayName("Magic defence");
        magicDefence.setItemMeta(magicDefenceMeta);

        ItemStack magicHealing = new ItemStack(Material.NETHER_STAR);
        ItemMeta magicHealingMeta = magicHealing.getItemMeta();
        magicHealingMeta.setDisplayName("Magic healing");
        magicHealing.setItemMeta(magicHealingMeta);

        ItemStack magicNature = new ItemStack(Material.VINE);
        ItemMeta magicNatureMeta = magicNature.getItemMeta();
        magicNatureMeta.setDisplayName("Magic nature");
        magicNature.setItemMeta(magicNatureMeta);

        ItemStack magicIllusion = new ItemStack(Material.WEB);
        ItemMeta magicIllusionMeta = magicIllusion.getItemMeta();
        magicIllusionMeta.setDisplayName("Magic illusion");
        magicIllusion.setItemMeta(magicIllusionMeta);

        ItemStack magicSummoning = new ItemStack(Material.BONE);
        ItemMeta magicSummoningMeta = magicSummoning.getItemMeta();
        magicSummoningMeta.setDisplayName("Magic summoning");
        magicSummoning.setItemMeta(magicSummoningMeta);

        ItemStack magicSword = new ItemStack(Material.DIAMOND_SWORD);
        magicSword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        ItemMeta magicSwordMeta = magicSword.getItemMeta();
        magicSwordMeta.setDisplayName("Magic sword");
        magicSword.setItemMeta(magicSwordMeta);

        ItemStack speedNimble = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta speedNimbleMeta = speedNimble.getItemMeta();
        speedNimbleMeta.setDisplayName("Speed nimble");
        speedNimble.setItemMeta(speedNimbleMeta);

        ItemStack supportPerform = new ItemStack(Material.FIREWORK);
        ItemMeta supportPerformMeta = supportPerform.getItemMeta();
        supportPerformMeta.setDisplayName("Support perform");
        supportPerform.setItemMeta(supportPerformMeta);

        turnOptions.setItem(1, meleeOffence);
        turnOptions.setItem(2, meleeDefence);
        turnOptions.setItem(3, rangedOffence);
        turnOptions.setItem(4, rangedDefence);
        turnOptions.setItem(5, magicOffence);
        turnOptions.setItem(6, magicDefence);
        turnOptions.setItem(7, magicHealing);
        turnOptions.setItem(8, magicNature);
        turnOptions.setItem(9, magicIllusion);
        turnOptions.setItem(10, magicSummoning);
        turnOptions.setItem(11, magicSword);
        turnOptions.setItem(12, speedNimble);
        turnOptions.setItem(13, supportPerform);
    }

    @Override
    public void start() {
        active = true;
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            for (int cid : characterLocations.keySet()) {
                Character character = characterPlugin.getCharacter(cid);
                if (character.getPlayer().isOnline()) {
                    Player player = character.getPlayer().getPlayer();
                    characterLocations.put(character.getId(), player.getLocation());
                }
            }
        }
        incrementTurn();
        if (getNextTurn().getPlayer().isOnline()) {
            activeTurn = new TurnImpl(this);
            Turn turn = getActiveTurn();
            turn.setAttacker(getNextTurn());
            getNextTurn().getPlayer().getPlayer().sendMessage(new String[] {ChatColor.GREEN + "It's your turn.",
                    (turn.getSkill() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Skill type: " + (turn.getSkill() != null ? ChatColor.GREEN + turn.getSkill().getType().toString() : ChatColor.RED + "NOT CHOSEN - use /turn skill to choose"),
                    (turn.getSkill() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Skill: " + (turn.getSkill() != null ? ChatColor.GREEN + turn.getSkill().getName() : ChatColor.RED + "NOT CHOSEN - use /turn skill to choose"),
                    (turn.getDefender() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Target: " + (turn.getDefender() != null ? ChatColor.GREEN + turn.getDefender().getName() + "(" + ((Character) turn.getDefender()).getPlayer().getName() + "'s character)" : ChatColor.RED + "NOT CHOSEN - use /turn target to choose"),
                    (turn.getWeapon() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Weapon: " + (turn.getWeapon() != null ? ChatColor.GREEN + turn.getWeapon().getType().toString() : ChatColor.RED + "NOT CHOSEN - use /turn weapon to choose"),
                    (turn.getSkill() != null && turn.getDefender() != null && turn.getWeapon() != null ? ChatColor.GREEN + "Ready to make a move! Use /turn complete to complete your turn." : ChatColor.RED + "There are still some options you must set before completing your turn.")});
        }
    }

    @Override
    public void end() {
        active = false;
        turnOrder.clear();
        characterLocations.clear();
        turn = -1;
    }

    @Override
    public Character getNextTurn() {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            return characterPlugin.getCharacter(turnOrder.get(turn));
        }
        return null;
    }

    @Override
    public void doTurn(Turn turn) {
        savedTurns.put(((Character) turn.getAttacker()).getId(), turn);
        doTurn(turn.getAttacker(), turn.getDefender(), turn.getWeapon(), turn.getSkill());
    }

    @Override
    public void doTurn(Combatant attacking, Combatant defending, ItemStack weapon, Skill skill) {
        doTurn((Character) attacking, (Character) defending, weapon, skill);
    }

    public void doTurn(Character attacking, Character defending, ItemStack weapon, Skill skill) {
        incrementTurn();
        boolean hit = skill.use(this, attacking, defending, weapon);
        while (!getNextTurn().getPlayer().isOnline()) {
            incrementTurn();
        }
        sendMessage(hit ? ChatColor.YELLOW + "The attack hit! " + defending.getName() + " has " + Math.max((Math.round(defending.getHealth() * 100D) / 100D), 0D) + "/" + (Math.round(defending.getMaxHealth() * 100D) / 100D) + " health remaining." : ChatColor.YELLOW + "The attack missed.");
        if (defending.getHealth() <= 0D) {
            removeCharacter(defending);
            defending.getPlayer().getPlayer().sendMessage(ChatColor.RED + "You lost the fight.");
            defending.getPlayer().getPlayer().damage(defending.getPlayer().getPlayer().getHealth());
        }
        if (getCharacters().size() == 1) {
            Character character = getCharacters().iterator().next();
            character.getPlayer().getPlayer().sendMessage(ChatColor.GREEN + "You win.");
            RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
            if (classesPluginProvider != null) {
                ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
                classesPlugin.giveExperience(character, 50);
            }
        }
        if (getCharacters().size() <= 1) {
            end();
            return;
        }
        sendMessage(ChatColor.YELLOW + "It's " + getNextTurn().getName() + "'s turn.");
        activeTurn = savedTurns.get(getNextTurn().getId()) == null ? new TurnImpl(this) : savedTurns.get(getNextTurn().getId());
        Turn turn = getActiveTurn();
        turn.setAttacker(getNextTurn());
        getNextTurn().getPlayer().getPlayer().sendMessage(new String[] {ChatColor.GREEN + "It's your turn.",
                            (turn.getSkill() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Skill type: " + (turn.getSkill() != null ? ChatColor.GREEN + turn.getSkill().getType().toString() : ChatColor.RED + "NOT CHOSEN - use /turn skill to choose"),
                            (turn.getSkill() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Skill: " + (turn.getSkill() != null ? ChatColor.GREEN + turn.getSkill().getName() : ChatColor.RED + "NOT CHOSEN - use /turn skill to choose"),
                            (turn.getDefender() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Target: " + (turn.getDefender() != null ? ChatColor.GREEN + turn.getDefender().getName() + " (" + ((Character) turn.getDefender()).getPlayer().getName() + "'s character)" : ChatColor.RED + "NOT CHOSEN - use /turn target to choose"),
                            (turn.getWeapon() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Weapon: " + (turn.getWeapon() != null ? ChatColor.GREEN + turn.getWeapon().getType().toString() : ChatColor.RED + "NOT CHOSEN - use /turn weapon to choose"),
                            (turn.getSkill() != null && turn.getDefender() != null && turn.getWeapon() != null ? ChatColor.GREEN + "Ready to make a move! Use /turn complete to complete your turn." : ChatColor.RED + "There are still some options you must set before completing your turn.")});
    }

    @Override
    public Set<Character> getCharacters() {
        Set<Character> characters = new HashSet<>();
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            for (int cid : characterLocations.keySet()) {
                characters.add(characterPlugin.getCharacter(cid));
            }
        }
        return characters;
    }

    @Override
    public Collection<? extends Combatant> getCombatants() {
        return getCharacters();
    }

    public void addCharacter(Character character) {
        characterLocations.put(character.getId(), character.getLocation());
        turnOrder.add(character.getId());
        if (character.getPlayer().isOnline()) {
            Player player = character.getPlayer().getPlayer();
            characterLocations.put(character.getId(), player.getLocation());
        }
    }

    @Override
    public void addCombatant(Combatant combatant) {
        if (combatant instanceof Character) {
            addCharacter((Character) combatant);
        }
    }

    public void removeCharacter(Character character) {
        turnOrder.remove((Integer) character.getId());
        characterLocations.remove(character.getId());
    }

    @Override
    public void removeCombatant(Combatant combatant) {

    }

    private void incrementTurn() {
        turn = turn < turnOrder.size() - 1 ? turn + 1 : 0;
    }

    public Turn getActiveTurn() {
        return activeTurn;
    }

    public void showTurnOptions(Player player) {
        player.openInventory(turnOptions);
    }

    public void showSkillOptions(Player player, Collection<Skill> skills) {
        Inventory skillOptions = Bukkit.createInventory(null, (int) Math.ceil((double) skills.size() / 9D) * 9, "Skill");
        for (Skill skill : skills) {
            skillOptions.addItem(skill.getIcon());
        }
        player.openInventory(skillOptions);
    }

    public void showCharacterOptions(Player player) {
        Inventory characterOptions = Bukkit.createInventory(null, (int) Math.ceil((double) characterLocations.keySet().size() / 9D) * 9, "Target");
        for (Character character : getCharacters()) {
            ItemStack characterIcon = new ItemStack(Material.LEATHER_HELMET, 1);
            ItemMeta characterIconMeta = characterIcon.getItemMeta();
            characterIconMeta.setDisplayName(character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName());
            List<String> lore = new ArrayList<>();
            lore.add("" + character.getId());
            characterIconMeta.setLore(lore);
            characterIcon.setItemMeta(characterIconMeta);
            characterOptions.addItem(characterIcon);
        }
        player.openInventory(characterOptions);
    }

    public void showWeaponOptions(Player player) {
        Inventory weaponOptions = Bukkit.createInventory(null, (int) Math.ceil((double) player.getInventory().getSize() / 9D) * 9, "Weapon");
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                weaponOptions.addItem(item);
            }
        }
        player.openInventory(weaponOptions);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void sendMessage(String message) {
        Set<Player> players = new HashSet<>();
        for (Character character : getCharacters()) {
            if (character.getPlayer().isOnline()) {
                players.add(character.getPlayer().getPlayer());
            }
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Player fightingPlayer : players) {
                if (player.getLocation().distanceSquared(fightingPlayer.getLocation()) <= 256) {
                    player.sendMessage(message);
                    break;
                }
            }
        }
    }

}

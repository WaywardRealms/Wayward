package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Party;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.skills.Stat;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.combat.StatusEffect;
import net.wayward_realms.waywardlib.combat.Turn;
import net.wayward_realms.waywardlib.skills.Skill;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.*;

import static net.wayward_realms.waywardlib.combat.StatusEffect.*;

public class FightImpl implements Fight {

    private Map<Integer, Location> characterLocations = new HashMap<>();
    private List<Integer> turnOrder = new ArrayList<>();
    private int turn = -1;
    private boolean active;
    private Turn activeTurn;
    private Map<Integer, Turn> savedTurns = new HashMap<>();

    private Map<Integer, EnumMap<StatusEffect, Integer>> statusEffectTurns = new HashMap<>();
    private Map<Integer, Map<Skill, Integer>> coolDownTurns = new HashMap<>();
    private Map<Runnable, Integer> scheduledTasks = new HashMap<>();

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

        turnOptions.setItem(0, meleeOffence);
        turnOptions.setItem(1, meleeDefence);
        turnOptions.setItem(2, rangedOffence);
        turnOptions.setItem(3, rangedDefence);
        turnOptions.setItem(4, magicOffence);
        turnOptions.setItem(5, magicDefence);
        turnOptions.setItem(6, magicHealing);
        turnOptions.setItem(7, magicNature);
        turnOptions.setItem(8, magicIllusion);
        turnOptions.setItem(9, magicSummoning);
        turnOptions.setItem(10, magicSword);
        turnOptions.setItem(11, speedNimble);
        turnOptions.setItem(12, supportPerform);
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
        sortTurns();
        incrementTurn();
        if (getNextTurn().getPlayer().isOnline()) {
            activeTurn = new TurnImpl(this);
            Turn turn = getActiveTurn();
            turn.setAttacker(getNextTurn());
            getNextTurn().getPlayer().getPlayer().sendMessage(new String[] {ChatColor.GREEN + "It's your turn.",
                    (turn.getSkill() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Skill type: " + (turn.getSkill() != null ? ChatColor.GREEN + turn.getSkill().getType().toString() : ChatColor.RED + "NOT CHOSEN - use /turn skill to choose"),
                    (turn.getSkill() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Skill: " + (turn.getSkill() != null ? ChatColor.GREEN + turn.getSkill().getName() : ChatColor.RED + "NOT CHOSEN - use /turn skill to choose"),
                    (turn.getDefender() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Target: " + (turn.getDefender() != null ? ChatColor.GREEN + (((Character) turn.getDefender()).isNameHidden() ? ChatColor.MAGIC + turn.getDefender().getName() + ChatColor.RESET : turn.getDefender().getName()) + ChatColor.GREEN + "(" + ((Character) turn.getDefender()).getPlayer().getName() + "'s character)" : ChatColor.RED + "NOT CHOSEN - use /turn target to choose"),
                    (turn.getWeapon() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Weapon: " + (turn.getWeapon() != null ? ChatColor.GREEN + turn.getWeapon().getType().toString() : ChatColor.RED + "NOT CHOSEN - use /turn weapon to choose"),
                    (turn.getSkill() != null && turn.getDefender() != null && turn.getWeapon() != null ? ChatColor.GREEN + "Ready to make a move! Use /turn complete to complete your turn." : ChatColor.RED + "There are still some options you must set before completing your turn.")});
        }
    }

    private int partition(int left, int right) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            int i = left, j = right;
            int tmp;
            Character pivot = characterPlugin.getCharacter(turnOrder.get((left + right) / 2));
            while (i <= j) {
                while (characterPlugin.getCharacter(turnOrder.get(i)).getStatValue(Stat.SPEED) > pivot.getStatValue(Stat.SPEED)) i++;
                while (characterPlugin.getCharacter(turnOrder.get(j)).getStatValue(Stat.SPEED) < pivot.getStatValue(Stat.SPEED)) j--;
                if (i <= j) {
                    tmp = turnOrder.get(i);
                    turnOrder.set(i, turnOrder.get(j));
                    turnOrder.set(j, tmp);
                    i++;
                    j--;
                }
            }
            return i;
        }
        return 0;
    }

    private void quickSort(int left, int right) {
        int index = partition(left, right);
        if (left < index - 1) quickSort(left, index - 1);
        if (index < right) quickSort(index, right);
    }

    private void sortTurns() {
        quickSort(0, turnOrder.size() - 1);
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
        if (getCoolDownTurnsRemaining(attacking, skill) > 0) {
            attacking.getPlayer().getPlayer().sendMessage(ChatColor.RED + "That skill is still on cooldown.");
            return;
        }
        incrementTurn();
        boolean hit = canMove(attacking, skill) && skill.use(this, attacking, defending, weapon);
        RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
        if (skillsPluginProvider != null) {
            SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
            for (Skill skill1 : skillsPlugin.getSkills()) {
                setCoolDownTurnsRemaining(attacking, skill, Math.max(getCoolDownTurnsRemaining(attacking, skill1) - 1, 0));
            }
        }
        setCoolDownTurnsRemaining(attacking, skill, skill.getCoolDownTurns());
        while (!getNextTurn().getPlayer().isOnline()) {
            incrementTurn();
        }
        sendMessage(hit ? ChatColor.YELLOW + "The attack hit! " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " has " + Math.max((Math.round(defending.getHealth() * 100D) / 100D), 0D) + "/" + (Math.round(defending.getMaxHealth() * 100D) / 100D) + " health remaining." : ChatColor.YELLOW + "The attack missed.");
        if (defending.getHealth() <= 0D) {
            removeCharacter(defending);
            defending.getPlayer().getPlayer().sendMessage(ChatColor.RED + "You lost the fight.");
            defending.getPlayer().getPlayer().damage(defending.getPlayer().getPlayer().getHealth());
        }
        doStatusEffects();
        doScheduledTasks();
        if (getCharacters().size() == 1) {
            Character character = getCharacters().iterator().next();
            character.getPlayer().getPlayer().sendMessage(ChatColor.GREEN + "You win.");
            RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
            if (classesPluginProvider != null) {
                ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
                classesPlugin.giveExperience(character, 50);
            }
        } else if (getCharacters().size() > 1) {
            Character character = getCharacters().iterator().next();
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (characterPluginProvider != null) {
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                Party party = characterPlugin.getParty(character);
                if (party != null) {
                    boolean partyWin = true;
                    for (Character character1 : getCharacters()) {
                        boolean containsCharacter = false;
                        for (Character character2 : party.getMembers()) {
                            if (character1.getId() == character2.getId()) {
                                containsCharacter = true;
                                break;
                            }
                        }
                        if (!containsCharacter) {
                            partyWin = false;
                            break;
                        }
                    }
                    if (partyWin) {
                        party.sendMessage(ChatColor.GREEN + "You win.");
                        RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
                        if (classesPluginProvider != null) {
                            ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
                            classesPlugin.giveExperience(character, (int) Math.round(75D / (double) party.getMembers().size()));
                        }
                        end();
                        return;
                    }
                }
            }
        }
        if (getCharacters().size() <= 1) {
            end();
            return;
        }
        Character nextTurn = getNextTurn();
        sendMessage(ChatColor.YELLOW + "It's " + (nextTurn.isNameHidden() ? ChatColor.MAGIC + nextTurn.getName() + ChatColor.RESET : nextTurn.getName()) + ChatColor.YELLOW + "'s turn.");
        activeTurn = savedTurns.get(getNextTurn().getId()) == null ? new TurnImpl(this) : savedTurns.get(getNextTurn().getId());
        Turn turn = getActiveTurn();
        turn.setAttacker(getNextTurn());
        getNextTurn().getPlayer().getPlayer().sendMessage(new String[] {ChatColor.GREEN + "It's your turn.",
                            (turn.getSkill() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Skill type: " + (turn.getSkill() != null ? ChatColor.GREEN + turn.getSkill().getType().toString() : ChatColor.RED + "NOT CHOSEN - use /turn skill to choose"),
                            (turn.getSkill() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Skill: " + (turn.getSkill() != null ? ChatColor.GREEN + turn.getSkill().getName() : ChatColor.RED + "NOT CHOSEN - use /turn skill to choose"),
                            (turn.getDefender() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Target: " + (turn.getDefender() != null ? ChatColor.GREEN + (((Character) turn.getDefender()).isNameHidden() ? ChatColor.MAGIC + turn.getDefender().getName() + ChatColor.RESET : turn.getDefender().getName()) + ChatColor.GREEN + " (" + ((Character) turn.getDefender()).getPlayer().getName() + "'s character)" : ChatColor.RED + "NOT CHOSEN - use /turn target to choose"),
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
        if (combatant instanceof Character) removeCharacter((Character) combatant);
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
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            Character character = characterPlugin.getActiveCharacter(player);
            Inventory skillOptions = Bukkit.createInventory(null, (int) Math.ceil((double) skills.size() / 9D) * 9, "Skill");
            for (Skill skill : skills) {
                ItemStack skillIcon = new ItemStack(skill.getIcon());
                skillIcon.setAmount(Math.max(getCoolDownTurnsRemaining(character, skill), 1));
                skillOptions.addItem(skillIcon);
            }
            player.openInventory(skillOptions);
        }
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

    @Override
    public int getStatusTurns(Combatant combatant, StatusEffect statusEffect) {
        if (!statusEffectTurns.containsKey(((Character) combatant).getId())) return 0;
        Map<StatusEffect, Integer> combatantStatusEffectTurns = statusEffectTurns.get(((Character) combatant).getId());
        if (!combatantStatusEffectTurns.containsKey(statusEffect)) return 0;
        return statusEffectTurns.get(((Character) combatant).getId()).get(statusEffect);
    }

    @Override
    public void setStatusTurns(Combatant combatant, StatusEffect statusEffect, int turns) {
        if (turns > 0) {
            if (statusEffectTurns.get(((Character) combatant).getId()) == null) {
                statusEffectTurns.put(((Character) combatant).getId(), new EnumMap<StatusEffect, Integer>(StatusEffect.class));
            }
            statusEffectTurns.get(((Character) combatant).getId()).put(statusEffect, turns);
        } else {
            statusEffectTurns.get(((Character) combatant).getId()).remove(statusEffect);
            if (statusEffectTurns.get(((Character) combatant).getId()).isEmpty()) {
                statusEffectTurns.remove(((Character) combatant).getId());
            }
        }
    }

    @Override
    public int getCoolDownTurnsRemaining(Combatant combatant, Skill skill) {
        if (!coolDownTurns.containsKey(((Character) combatant).getId())) return 0;
        if (!coolDownTurns.get(((Character) combatant).getId()).containsKey(skill)) return 0;
        return coolDownTurns.get(((Character) combatant).getId()).get(skill);
    }

    @Override
    public void setCoolDownTurnsRemaining(Combatant combatant, Skill skill, int turns) {
        if (!coolDownTurns.containsKey(((Character) combatant).getId())) {
            coolDownTurns.put(((Character) combatant).getId(), new HashMap<Skill, Integer>());
        }
        if (turns > 0) {
            coolDownTurns.get(((Character) combatant).getId()).put(skill, turns);
        } else {
            coolDownTurns.get(((Character) combatant).getId()).remove(skill);
        }
    }

    public boolean hasStatusEffect(Combatant combatant, StatusEffect statusEffect) {
        return statusEffectTurns.containsKey(((Character) combatant).getId()) && statusEffectTurns.get(((Character) combatant).getId()).containsKey(statusEffect);
    }

    public void doStatusEffects() {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            for (Iterator<Integer> iterator = statusEffectTurns.keySet().iterator(); iterator.hasNext(); ) {
                Character character = characterPlugin.getCharacter(iterator.next());
                for (Iterator<Map.Entry<StatusEffect, Integer>> iterator1 = statusEffectTurns.get(character.getId()).entrySet().iterator(); iterator1.hasNext(); ) {
                    Map.Entry<StatusEffect, Integer> entry = iterator1.next();
                    doStatusEffect(character, entry.getKey());
                    if (character.getHealth() <= 0D) {
                        removeCombatant(character);
                        OfflinePlayer player = character.getPlayer();
                        player.getPlayer().sendMessage(ChatColor.RED + "You lost the fight.");
                        player.getPlayer().damage(player.getPlayer().getHealth());
                    }
                    if (entry.getValue() > 0) {
                        entry.setValue(entry.getValue() - 1);
                    } else {
                        iterator1.remove();
                    }
                }
                if (statusEffectTurns.get(character.getId()).isEmpty()) iterator.remove();
            }
        }

    }

    public void doStatusEffect(Combatant combatant, StatusEffect statusEffect) {
        double damage;
        Character character = (Character) combatant;
        OfflinePlayer player;
        switch (statusEffect) {
            case POISON:
                damage = 0.05D * combatant.getMaxHealth();
                combatant.setHealth(combatant.getHealth() - damage);
                sendMessage(ChatColor.DARK_PURPLE + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.DARK_PURPLE + " took " + damage + " poison damage.");
                player = character.getPlayer();
                if (player.isOnline()) {
                    player.getPlayer().setHealth(character.getHealth());
                }
                if (combatant.getHealth() <= 0D) {
                    removeCombatant(combatant);
                }
                break;
            case PARALYSIS:
                sendMessage(ChatColor.GOLD + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.GOLD + " is paralysed.");
                break;
            case BURNED:
                damage = 0.05D * combatant.getMaxHealth();
                combatant.setHealth(combatant.getHealth() - damage);
                sendMessage(ChatColor.DARK_RED + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.DARK_RED + " took " + damage + " burn damage.");
                player = character.getPlayer();
                if (player.isOnline()) {
                    player.getPlayer().setHealth(character.getHealth());
                }
                break;
            case FROZEN:
                sendMessage(ChatColor.AQUA + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.AQUA + " is frozen solid.");
                break;
            case CONFUSED:
                sendMessage(ChatColor.YELLOW + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.YELLOW + " is confused.");
                Random random = new Random();
                if (random.nextBoolean()) {
                    damage = random.nextDouble() * (combatant.getMaxHealth() / 2D);
                    combatant.setHealth(combatant.getHealth() - damage);
                    sendMessage(ChatColor.YELLOW + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.YELLOW + " hurt themself while confused.");
                }
                break;
            case ASLEEP:
                sendMessage(ChatColor.GRAY + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.GRAY + " is asleep.");
                break;
            case BLIND:
                sendMessage(ChatColor.DARK_GRAY + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.DARK_GRAY + " is blinded.");
                break;
            case DOOM:
                sendMessage(ChatColor.DARK_GRAY + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.DARK_GRAY + " is doomed - " + getStatusTurns(combatant, statusEffect) + " turns remaining until they pass out.");
                if (getStatusTurns(combatant, statusEffect) <= 0) {
                    sendMessage(ChatColor.DARK_GRAY + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.DARK_GRAY + " was knocked out.");
                    combatant.setHealth(0);
                    player = character.getPlayer();
                    if (player.isOnline()) {
                        player.getPlayer().setHealth(character.getHealth());
                    }
                    removeCombatant(combatant);
                }
                break;
            case SILENCED:
                sendMessage(ChatColor.GRAY + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.GRAY + " is silenced.");
                break;
        }
    }

    public void doScheduledTasks() {
        for (Iterator<Map.Entry<Runnable, Integer>> iterator = scheduledTasks.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<Runnable, Integer> task = iterator.next();
            if (task.getValue() == 0) {
                task.getKey().run();
                iterator.remove();
            } else if (task.getValue() > 0) {
                task.setValue(task.getValue() - 1);
            } else {
                iterator.remove();
            }
        }
    }

    public boolean canMove(Combatant combatant, Skill skill) {
        Random random = new Random();
        Character character = (Character) combatant;
        if (skill.getType() == SkillType.MELEE_OFFENCE ||
                skill.getType() == SkillType.MELEE_DEFENCE ||
                skill.getType() == SkillType.RANGED_OFFENCE ||
                skill.getType() == SkillType.RANGED_DEFENCE) {
            if (hasStatusEffect(combatant, PARALYSIS)) {
                sendMessage(ChatColor.GOLD + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.GOLD + " could not move due to paralysis.");
                if (random.nextInt(100) > 20) return false;
            }
            if (hasStatusEffect(combatant, FROZEN)) {
                sendMessage(ChatColor.AQUA + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.AQUA + " is frozen and could not move.");
                return false;
            }

        }
        if (skill.getType() == SkillType.MAGIC_OFFENCE ||
                skill.getType() == SkillType.MAGIC_DEFENCE ||
                skill.getType() == SkillType.MAGIC_HEALING ||
                skill.getType() == SkillType.MAGIC_ILLUSION ||
                skill.getType() == SkillType.MAGIC_NATURE ||
                skill.getType() == SkillType.MAGIC_SUMMONING ||
                skill.getType() == SkillType.MAGIC_SWORD) {
            if (hasStatusEffect(combatant, SILENCED)) {
                sendMessage(ChatColor.GRAY + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.GRAY + " is silenced and could not cast.");
                return false;
            }
        }
        if (hasStatusEffect(combatant, ASLEEP)) {
            sendMessage(ChatColor.GRAY + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.GRAY + " is asleep and can not move.");
        }
        if (hasStatusEffect(combatant, BLIND)) {
            if (random.nextInt(100) > 10) {
                sendMessage(ChatColor.DARK_GRAY + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.DARK_GRAY + " is blinded and missed.");
                return false;
            }
        }
        return true;
    }

    @Override
    public void scheduleTask(Runnable runnable, int turns) {
        scheduledTasks.put(runnable, turns);
    }

    @Override
    public void cancelTask(Runnable runnable) {
        scheduledTasks.remove(runnable);
    }

}

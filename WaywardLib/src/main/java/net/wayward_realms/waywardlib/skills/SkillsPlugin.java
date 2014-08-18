package net.wayward_realms.waywardlib.skills;

import net.wayward_realms.waywardlib.WaywardPlugin;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.Pet;

import java.io.File;
import java.util.Collection;

/**
 * Represents a skills plugin
 *
 */
public interface SkillsPlugin extends WaywardPlugin {

    /**
     * Gets the spell by the given name
     *
     * @param name the name of the spell
     * @return the spell
     */
    public Spell getSpell(String name);

    /**
     * Adds a spell
     *
     * @param spell the spell to add
     */
    public void addSpell(Spell spell);

    /**
     * Removes a spell
     *
     * @param spell the spell to remove
     */
    public void removeSpell(Spell spell);

    /**
     * Gets all the available spells
     *
     * @return a collection containing all spells
     */
    public Collection<? extends Spell> getSpells();

    /**
     * Gets a skill by name
     *
     * @param name the name of the skill
     * @return the skill
     */
    public Skill getSkill(String name);

    /**
     * Adds a skill
     *
     * @param skill the skill to add
     */
    public void addSkill(Skill skill);

    /**
     * Removes a skill
     *
     * @param skill the skill to remove
     */
    public void removeSkill(Skill skill);

    /**
     * Hets all the available skills
     *
     * @return a collection containing all skills
     */
    public Collection<? extends Skill> getSkills();

    /**
     * Attempts to load a skill from the given file
     *
     * @param file the file to load from
     * @return the skill
     */
    public Skill loadSkill(File file);

    /**
     * Attempts to load a skill from the given class
     *
     * @param clazz the class
     * @return the skill
     */
    public Skill loadSkill(Class<? extends Skill> clazz);

    /**
     * Gets the root specialisation
     *
     * @return the root specialisation
     */
    public Specialisation getRootSpecialisation();

    /**
     * Gets how far a character has specialised into a given specialisation.
     * Numbers are out of 100, with 100 being the maximum possible in any given specialisation.
     *
     * @param character the character
     * @param specialisation the specialisation
     * @return the percentage completion of the specialisation
     */
    public int getSpecialisationValue(Character character, Specialisation specialisation);

    /**
     * Sets how far a character has specialised into a given specialisation.
     * Numbers are out of 100, with 100 being the maximum possible in any given specialisation.
     *
     * @param character the character
     * @param specialisation the specialisation
     * @param value the percentage completion of the specialisation
     */
    public void setSpecialisationValue(Character character, Specialisation specialisation, int value);

    /**
     * Gets how far a pet has specialised into a given specialisation.
     * Numbers are out of 100, with 100 being the maximum possible in any given specialisation.
     *
     * @param pet the pet
     * @param specialisation the specialisation
     * @return the percentage completion of the specialisation
     */
    public int getSpecialisationValue(Pet pet, Specialisation specialisation);

    /**
     * Sets how far a pet has specialised into a given specialisation.
     * Numbers are out of 100, with 100 being the maximum possible in any given specialisation.
     *
     * @param pet the pet
     * @param specialisation the specialisation
     * @param value the percentage completion of the specialisation
     */
    public void setSpecialisationValue(Pet pet, Specialisation specialisation, int value);

    /**
     * Gets a specialisation by name
     *
     * @param name the name of the specialisation
     * @return the specialisation
     */
    public Specialisation getSpecialisation(String name);

    Collection<Specialisation> getSpecialisations();

    int getAssignedSpecialisationPoints(Character character);

    int getUnassignedSpecialisationPoints(Character character);

    /**
     * Gets the total experience earnt by a character
     *
     * @param character the character
     * @return the total amount of experience earnt
     */
    public int getTotalExperience(Character character);

    /**
     * Sets the total experience earnt by a character
     *
     * @param character the character
     * @param experience the amount of experience to set
     */
    public void setTotalExperience(Character character, int experience);

    /**
     * Gets the amount of experience earnt by a character since the last time they levelled up
     *
     * @param character the character
     * @return the amount of experience earnt by the character since the last time they levelled up
     */
    public int getExperience(Character character);

    /**
     * Sets the amount of experience earnt by a character since the last time they levelled up
     *
     * @param character the character
     * @param experience the amount of experience to set
     */
    public void setExperience(Character character, int experience);

    /**
     * Gets a character's level
     *
     * @param character the character
     * @return the level
     */
    public int getLevel(Character character);

    /**
     * Sets a character's level
     *
     * @param character the character
     * @param level the level
     */
    public void setLevel(Character character, int level);

    /**
     * Gives experience to a character, showing any messages to the player if online
     *
     * @param character the character
     * @param amount the amount of experience to add
     */
    public void giveExperience(Character character, int amount);

    /**
     * Gets the amount of experience to level up to a level from the one before
     *
     * @param level the level
     * @return the amount of experience to level up to the level from the one before
     */
    public int getExperienceForLevel(int level);

    /**
     * Gets the total amount of experience for a level from level 1
     *
     * @param level the level
     * @return the total amount of experience for the level, from level 1
     */
    public int getTotalExperienceForLevel(int level);

    /**
     * Gets the max level a character may reach
     *
     * @return the max level
     */
    public int getMaxLevel();

    /**
     * Gets the max health the character has
     *
     * @param character the character
     * @return the max health
     */
    public double getMaxHealth(Character character);

    /**
     * Gets the max mana the character has
     *
     * @param character the character
     * @return the max mana
     */
    public int getMaxMana(Character character);

    /**
     * Gets the value of a character's stat
     *
     * @param character the character
     * @param stat the stat
     * @return the value of the stat
     */
    public int getStatValue(Character character, Stat stat);

    /**
     * Gets the character's attack roll
     *
     * @param character the character
     * @param attack the specialisation being used to attack
     * @param onHand whether the character is using their onhand weapon
     * @return the roll string
     */
    public String getAttackRoll(Character character, Specialisation attack, boolean onHand);

    /**
     * Gets the character's defence roll
     *
     * @param character the character
     * @param defence the specialisation being used to defend
     * @param onHand whether the character is using their onhand weapon
     * @return the roll string
     */
    public String getDefenceRoll(Character character, Specialisation defence, boolean onHand);

    /**
     * Gets the damage roll
     *
     * @param attacking the character attacking
     * @param specialisation the specialisation the character is using
     * @param onHand whether the character is using their onhand weapon
     * @param defending the character defending
     * @return the roll string for calculating damage
     */
    public String getDamageRoll(Character attacking, Specialisation specialisation, boolean onHand, Character defending);

    /**
     * Gets the armour rating for a character
     *
     * @param character the character
     * @return the armour rating
     */
    public int getArmourRating(Character character);

    /**
     * Gets the damage roll for a character against a given armour rating
     *
     * @param attacking the character attacking
     * @param specialisation the specialisation the character is using
     * @param onHand whether the character is using their onhand weapon
     * @param armourRating the armour rating
     * @return the damage roll string
     */
    public String getDamageRoll(Character attacking, Specialisation specialisation, boolean onHand, int armourRating);
}

package net.wayward_realms.waywardlib.skills;

import net.wayward_realms.waywardlib.WaywardPlugin;

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

}

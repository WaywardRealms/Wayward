package net.wayward_realms.waywardskills;

import net.wayward_realms.waywardlib.skills.Skill;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardlib.skills.Spell;
import net.wayward_realms.waywardskills.skill.SkillManager;
import net.wayward_realms.waywardskills.spell.SpellManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class WaywardSkills extends JavaPlugin implements SkillsPlugin {

    private SpellManager spellManager;
    private SkillManager skillManager;

    @Override
    public void onEnable() {
        spellManager = new SpellManager(this);
        skillManager = new SkillManager(this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getCommand("skill").setExecutor(new SkillCommand(this));
        getCommand("spell").setExecutor(new SpellCommand(this));
        getCommand("bindskill").setExecutor(new BindSkillCommand(this));
        getCommand("bindspell").setExecutor(new BindSpellCommand(this));
    }

    @Override
    public Spell getSpell(String name) {
        return spellManager.getSpell(name);
    }

    @Override
    public void addSpell(Spell spell) {
        spellManager.addSpell(spell);
    }

    @Override
    public void removeSpell(Spell spell) {
        spellManager.removeSpell(spell);
    }

    @Override
    public Collection<? extends Spell> getSpells() {
        return spellManager.getSpells();
    }

    @Override
    public Skill getSkill(String name) {
        return skillManager.getSkill(name);
    }

    @Override
    public void addSkill(Skill skill) {
        skillManager.addSkill(skill);
    }

    @Override
    public void removeSkill(Skill skill) {
        skillManager.removeSkill(skill);
    }

    @Override
    public Collection<? extends Skill> getSkills() {
        return skillManager.getSkills();
    }

    @SuppressWarnings("resource")
    @Override
    public Skill loadSkill(File file) {
        try {
            JarFile jarFile;
            jarFile = new JarFile(file);
            Enumeration<JarEntry> entries = jarFile.entries();
            String mainClass = null;
            while (entries.hasMoreElements()) {
                JarEntry element = entries.nextElement();
                if (element.getName().equalsIgnoreCase("skill.yml")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(element)));
                    mainClass = reader.readLine();
                    if (mainClass != null) {
                        mainClass = mainClass.substring(6);
                    }
                    break;
                }
            }
            if (mainClass != null) {
                ClassLoader loader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
                Class<?> clazz = Class.forName(mainClass, true, loader);
                for (Class<?> subclazz : clazz.getClasses()) {
                    Class.forName(subclazz.getName(), true, loader);
                }
                Class<? extends Skill> skillClass = clazz.asSubclass(Skill.class);
                Skill skill = skillClass.newInstance();
                if (skill instanceof Spell) {
                    addSpell((Spell) skill);
                } else {
                    addSkill(skill);
                }
                return skill;
            } else {
                getLogger().severe("Main class not found for skill: " + file.getName());
            }
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public Skill loadSkill(Class<? extends Skill> clazz) {
        try {
            Skill skill = clazz.newInstance();
            if (skill instanceof Spell) {
                addSpell((Spell) skill);
            } else {
                addSkill(skill);
            }
            return skill;
        } catch (InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public void loadState() {
        File skillDirectory = new File(getDataFolder(), "skills");
        if (skillDirectory.exists()) {
            for (File file : skillDirectory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return !file.isDirectory() && file.getName().endsWith(".jar");
                }
            })) {
                loadSkill(file);
            }
        }
    }

    @Override
    public void saveState() {

    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public SpellManager getSpellManager() {
        return spellManager;
    }
}

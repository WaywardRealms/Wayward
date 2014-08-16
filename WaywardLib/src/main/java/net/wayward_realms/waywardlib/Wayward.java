package net.wayward_realms.waywardlib;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.chat.ChatPlugin;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.combat.CombatPlugin;
import net.wayward_realms.waywardlib.death.DeathPlugin;
import net.wayward_realms.waywardlib.economy.EconomyPlugin;
import net.wayward_realms.waywardlib.essentials.EssentialsPlugin;
import net.wayward_realms.waywardlib.events.EventsPlugin;
import net.wayward_realms.waywardlib.items.ItemsPlugin;
import net.wayward_realms.waywardlib.lock.LockPlugin;
import net.wayward_realms.waywardlib.mechanics.MechanicsPlugin;
import net.wayward_realms.waywardlib.moderation.ModerationPlugin;
import net.wayward_realms.waywardlib.monsters.MonstersPlugin;
import net.wayward_realms.waywardlib.permissions.PermissionsPlugin;
import net.wayward_realms.waywardlib.professions.ProfessionsPlugin;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardlib.travel.TravelPlugin;
import net.wayward_realms.waywardlib.util.player.PlayerNamePlateUtils;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableChunk;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import net.wayward_realms.waywardlib.worldgen.WorldgenPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class, allows the obtaining of registered plugins for various services and registering of plugins
 *
 */
public class Wayward extends JavaPlugin implements WaywardPlugin {

    @Override
    public void onEnable() {
        PlayerNamePlateUtils.init(this);
        ConfigurationSerialization.registerClass(SerialisableLocation.class);
        ConfigurationSerialization.registerClass(SerialisableChunk.class);
        loadState();
        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPluginEnable(PluginEnableEvent event) {
                Plugin plugin = event.getPlugin();
                if (plugin instanceof WaywardPlugin) {
                    ServicesManager servicesManager = getServer().getServicesManager();
                    if (plugin instanceof CharacterPlugin) {
                        servicesManager.register(CharacterPlugin.class, (CharacterPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof ChatPlugin) {
                        servicesManager.register(ChatPlugin.class, (ChatPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof ClassesPlugin) {
                        servicesManager.register(ClassesPlugin.class, (ClassesPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof CombatPlugin) {
                        servicesManager.register(CombatPlugin.class, (CombatPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof DeathPlugin) {
                        servicesManager.register(DeathPlugin.class, (DeathPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof EconomyPlugin) {
                        servicesManager.register(EconomyPlugin.class, (EconomyPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof EssentialsPlugin) {
                        servicesManager.register(EssentialsPlugin.class, (EssentialsPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof EventsPlugin) {
                        servicesManager.register(EventsPlugin.class, (EventsPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof ItemsPlugin) {
                        servicesManager.register(ItemsPlugin.class, (ItemsPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof LockPlugin) {
                        servicesManager.register(LockPlugin.class, (LockPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof ModerationPlugin) {
                        servicesManager.register(ModerationPlugin.class, (ModerationPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof MonstersPlugin) {
                        servicesManager.register(MonstersPlugin.class, (MonstersPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof PermissionsPlugin) {
                        servicesManager.register(PermissionsPlugin.class, (PermissionsPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof ProfessionsPlugin) {
                        servicesManager.register(ProfessionsPlugin.class, (ProfessionsPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof TravelPlugin) {
                        servicesManager.register(TravelPlugin.class, (TravelPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof SkillsPlugin) {
                        servicesManager.register(SkillsPlugin.class, (SkillsPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof WorldgenPlugin) {
                        servicesManager.register(WorldgenPlugin.class, (WorldgenPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    loadState();
                }
            }
        }, this);
        getCommand("refreshprofile").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (sender instanceof Player) {
                    PlayerNamePlateUtils.invalidate((Player) sender);
                    sender.sendMessage(getPrefix() + ChatColor.GREEN + "Your profile has been invalidated, it will be re-requested from Mojang's servers as needed.");
                } else {
                    sender.sendMessage(getPrefix() + ChatColor.RED + "You cannot attempt to refresh your profile if you are not a player.");
                }
                return true;
            }
        });
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public void loadState() {
        if (loadPluginState(CharacterPlugin.class)) {
            if (loadPluginState(ClassesPlugin.class)) {
                loadPluginState(CombatPlugin.class);
            }
            loadPluginState(EconomyPlugin.class);
        }
        loadPluginState(LockPlugin.class);
        loadPluginState(ModerationPlugin.class);
        loadPluginState(PermissionsPlugin.class);
        loadPluginState(TravelPlugin.class);
        loadPluginState(DeathPlugin.class);
        loadPluginState(EventsPlugin.class);
        loadPluginState(EssentialsPlugin.class);
        loadPluginState(ChatPlugin.class);
        loadPluginState(MonstersPlugin.class);
        loadPluginState(ProfessionsPlugin.class);
        loadPluginState(SkillsPlugin.class);
        loadPluginState(WorldgenPlugin.class);
        loadPluginState(ItemsPlugin.class);
        loadPluginState(MechanicsPlugin.class);
    }

    private boolean loadPluginState(Class<? extends WaywardPlugin> clazz) {
        RegisteredServiceProvider<? extends WaywardPlugin> provider = getServer().getServicesManager().getRegistration(clazz);
        return provider != null && loadPluginState(provider.getProvider());
    }

    private boolean loadPluginState(WaywardPlugin plugin) {
        if (plugin != null) {
            plugin.loadState();
            return true;
        }
        return false;
    }

    @Override
    public void saveState() {
        if (savePluginState(CharacterPlugin.class)) {
            if (savePluginState(ClassesPlugin.class)) {
                savePluginState(CombatPlugin.class);
            }
            savePluginState(EconomyPlugin.class);
        }
        savePluginState(LockPlugin.class);
        savePluginState(ModerationPlugin.class);
        savePluginState(PermissionsPlugin.class);
        savePluginState(TravelPlugin.class);
        savePluginState(DeathPlugin.class);
        savePluginState(EventsPlugin.class);
        savePluginState(EssentialsPlugin.class);
        savePluginState(ChatPlugin.class);
        savePluginState(MonstersPlugin.class);
        savePluginState(ProfessionsPlugin.class);
        savePluginState(SkillsPlugin.class);
        savePluginState(WorldgenPlugin.class);
        savePluginState(ItemsPlugin.class);
        savePluginState(MechanicsPlugin.class);
    }
    
    private boolean savePluginState(Class<? extends WaywardPlugin> clazz) {
        RegisteredServiceProvider<? extends WaywardPlugin> provider = getServer().getServicesManager().getRegistration(clazz);
        return provider != null && savePluginState(provider.getProvider());
    }

    private boolean savePluginState(WaywardPlugin plugin) {
        if (plugin != null) {
            plugin.saveState();
            return true;
        }
        return false;
    }

}

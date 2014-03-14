package net.wayward_realms.waywardlib;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.chat.ChatPlugin;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.combat.CombatPlugin;
import net.wayward_realms.waywardlib.death.DeathPlugin;
import net.wayward_realms.waywardlib.donation.DonationPlugin;
import net.wayward_realms.waywardlib.economy.EconomyPlugin;
import net.wayward_realms.waywardlib.essentials.EssentialsPlugin;
import net.wayward_realms.waywardlib.events.DungeonPlugin;
import net.wayward_realms.waywardlib.lock.LockPlugin;
import net.wayward_realms.waywardlib.moderation.ModerationPlugin;
import net.wayward_realms.waywardlib.permissions.PermissionsPlugin;
import net.wayward_realms.waywardlib.portals.PortalsPlugin;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableChunk;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
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
                    if (plugin instanceof DonationPlugin) {
                        servicesManager.register(DonationPlugin.class, (DonationPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof DungeonPlugin) {
                        servicesManager.register(DungeonPlugin.class, (DungeonPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof EconomyPlugin) {
                        servicesManager.register(EconomyPlugin.class, (EconomyPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof EssentialsPlugin) {
                        servicesManager.register(EssentialsPlugin.class, (EssentialsPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof LockPlugin) {
                        servicesManager.register(LockPlugin.class, (LockPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof ModerationPlugin) {
                        servicesManager.register(ModerationPlugin.class, (ModerationPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof PermissionsPlugin) {
                        servicesManager.register(PermissionsPlugin.class, (PermissionsPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    if (plugin instanceof PortalsPlugin) {
                        servicesManager.register(PortalsPlugin.class, (PortalsPlugin) plugin, plugin, ServicePriority.Normal);
                    }
                    loadState();
                }
            }
        }, this);
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
        loadPluginState(DonationPlugin.class);
        loadPluginState(LockPlugin.class);
        loadPluginState(ModerationPlugin.class);
        loadPluginState(PermissionsPlugin.class);
        loadPluginState(PortalsPlugin.class);
        loadPluginState(DeathPlugin.class);
        loadPluginState(DungeonPlugin.class);
        loadPluginState(EssentialsPlugin.class);
        loadPluginState(ChatPlugin.class);
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
        if (savePluginState(getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider())) {
            if (savePluginState(getServer().getServicesManager().getRegistration(ClassesPlugin.class).getProvider())) {
                savePluginState(getServer().getServicesManager().getRegistration(CombatPlugin.class).getProvider());
            }
            savePluginState(getServer().getServicesManager().getRegistration(EconomyPlugin.class).getProvider());
        }
        savePluginState(getServer().getServicesManager().getRegistration(DonationPlugin.class).getProvider());
        savePluginState(getServer().getServicesManager().getRegistration(LockPlugin.class).getProvider());
        savePluginState(getServer().getServicesManager().getRegistration(ModerationPlugin.class).getProvider());
        savePluginState(getServer().getServicesManager().getRegistration(PermissionsPlugin.class).getProvider());
        savePluginState(getServer().getServicesManager().getRegistration(PortalsPlugin.class).getProvider());
        savePluginState(getServer().getServicesManager().getRegistration(DeathPlugin.class).getProvider());
        savePluginState(getServer().getServicesManager().getRegistration(DungeonPlugin.class).getProvider());
        savePluginState(getServer().getServicesManager().getRegistration(EssentialsPlugin.class).getProvider());
        savePluginState(getServer().getServicesManager().getRegistration(ChatPlugin.class).getProvider());
    }

    private boolean savePluginState(WaywardPlugin plugin) {
        if (plugin != null) {
            plugin.saveState();
            return true;
        }
        return false;
    }

}

package net.wayward_realms.waywardpermissions;

import net.wayward_realms.waywardlib.permissions.PermissionsPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class WaywardPermissions extends JavaPlugin implements PermissionsPlugin {

    public Map<UUID, PermissionAttachment> permissionAttachments = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getConfig().options().pathSeparator('/');
        if (getServer().getOnlinePlayers().size() != 0) {
            for (Player player : getServer().getOnlinePlayers()) {
                assignPermissions(player);
            }
        }
        registerListeners(new PlayerJoinListener(this), new PlayerQuitListener(this));
        getCommand("setgroup").setExecutor(new SetGroupCommand(this));
        getCommand("addgroup").setExecutor(new AddGroupCommand(this));
        getCommand("removegroup").setExecutor(new RemoveGroupCommand(this));
        getCommand("listgroups").setExecutor(new ListGroupsCommand(this));
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public void onDisable() {
        if (getServer().getOnlinePlayers().size() != 0) {
            for (Player player : getServer().getOnlinePlayers()) {
                unassignPermissions(player);
            }
        }
    }

    public void assignPermissions(Player player) {
        if (permissionAttachments.get(player.getUniqueId()) == null) {
            permissionAttachments.put(player.getUniqueId(), player.addAttachment(this));
        } else {
            unassignPermissions(player);
            permissionAttachments.put(player.getUniqueId(), player.addAttachment(this));
        }
        if (getConfig().getConfigurationSection("users").contains(player.getName())) {
            getConfig().set("users/" + player.getUniqueId().toString(), new ArrayList<>(getConfig().getStringList("users/" + player.getName())));
            getConfig().set("users/" + player.getName(), null);
            saveConfig();
        }
        if (getConfig().getConfigurationSection("users").contains(player.getUniqueId().toString())) {
            for (String group : getConfig().getStringList("users/" + player.getUniqueId().toString())) {
                assignGroupPermissions(player, group);
            }
        } else {
            assignGroupPermissions(player, "default");
        }
    }

    public void unassignPermissions(Player player) {
        player.removeAttachment(permissionAttachments.get(player.getUniqueId()));
        permissionAttachments.remove(player.getUniqueId());
    }

    public void assignGroupPermissions(Player player, String group) {
        if (getConfig().getString("groups/" + group + "/inheritsfrom") != null) {
            assignGroupPermissions(player, getConfig().getString("groups/" + group + "/inheritsfrom"));
        }
        for (String node : getConfig().getStringList("groups/" + group + "/allow")) {
            if (permissionAttachments.get(player.getUniqueId()).getPermissions().containsKey(node)) {
                permissionAttachments.get(player.getUniqueId()).unsetPermission(node);
            }
            permissionAttachments.get(player.getUniqueId()).setPermission(node, true);
        }
        for (String node : getConfig().getStringList("groups/" + group + "/deny")) {
            if (permissionAttachments.get(player.getUniqueId()).getPermissions().containsKey(node)) {
                permissionAttachments.get(player.getUniqueId()).unsetPermission(node);
            }
            permissionAttachments.get(player.getUniqueId()).setPermission(node, false);
        }
    }

    @Override
    public String getPrefix() {
        //return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + "WaywardPermissions" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
        return "";
    }

    @Override
    public void loadState() {

    }

    @Override
    public void saveState() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> getGroups(OfflinePlayer player) {
        Set<String> groups = new HashSet<>();
        groups.addAll(getConfig().getStringList("users/" + player.getUniqueId().toString()));
        return groups;
    }

    @Override
    public Set<String> getGroups(OfflinePlayer player, World world) {
        return getGroups(player);
    }

    @Override
    public void setGroup(OfflinePlayer player, String groupName) {
        Set<String> groups = new HashSet<>();
        groups.add(groupName);
        getConfig().set("users/" + player.getUniqueId().toString(), groups);
        saveConfig();
    }

    @Override
    public void setGroup(OfflinePlayer player, World world, String groupName) {
        setGroup(player, groupName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addGroup(OfflinePlayer player, String groupName) {
        Set<String> groups = (Set<String>) getConfig().get("users/" + player.getUniqueId().toString());
        groups.add(groupName);
        getConfig().set("users/" + player.getUniqueId().toString(), groups);
        saveConfig();
    }

    @Override
    public void addGroup(OfflinePlayer player, World world, String groupName) {
        addGroup(player, groupName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeGroup(OfflinePlayer player, String groupName) {
        Set<String> groups = (Set<String>) getConfig().get("users/" + player.getUniqueId().toString());
        groups.remove(groupName);
        getConfig().set("users/" + player.getUniqueId().toString(), groups);
        saveConfig();
    }

    @Override
    public void removeGroup(OfflinePlayer player, World world, String groupName) {
        removeGroup(player, groupName);
    }

}
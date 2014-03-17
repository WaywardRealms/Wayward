package net.wayward_realms.waywardpermissions;

import net.wayward_realms.waywardlib.permissions.PermissionsPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WaywardPermissions extends JavaPlugin implements PermissionsPlugin {

    public Map<String, PermissionAttachment> permissionAttachments = new HashMap<>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getConfig().options().pathSeparator('/');
        if (this.getServer().getOnlinePlayers().length != 0) {
            for (Player player : this.getServer().getOnlinePlayers()) {
                this.assignPermissions(player);
            }
        }
        this.registerListeners(new PlayerJoinListener(this), new PlayerQuitListener(this));
        this.getCommand("setgroup").setExecutor(new SetGroupCommand(this));
        this.getCommand("addgroup").setExecutor(new AddGroupCommand(this));
        this.getCommand("removegroup").setExecutor(new RemoveGroupCommand(this));
        this.getCommand("listgroups").setExecutor(new ListGroupsCommand(this));
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public void onDisable() {
        if (this.getServer().getOnlinePlayers().length != 0) {
            for (Player player : this.getServer().getOnlinePlayers()) {
                this.unassignPermissions(player);
            }
        }
    }

    public void assignPermissions(Player player) {
        if (permissionAttachments.get(player.getName()) == null) {
            permissionAttachments.put(player.getName(), player.addAttachment(this));
        } else {
            this.unassignPermissions(player);
            permissionAttachments.put(player.getName(), player.addAttachment(this));
        }
        if (this.getConfig().getConfigurationSection("users").contains(player.getName())) {
            for (String group : this.getConfig().getStringList("users/" + player.getName())) {
                this.assignGroupPermissions(player, group);
            }
        } else {
            this.assignGroupPermissions(player, "default");
        }
    }

    public void unassignPermissions(Player player) {
        player.removeAttachment(this.permissionAttachments.get(player.getName()));
        this.permissionAttachments.remove(player.getName());
    }

    public void assignGroupPermissions(Player player, String group) {
        if (this.getConfig().getString("groups/" + group + "/inheritsfrom") != null) {
            this.assignGroupPermissions(player, this.getConfig().getString("groups/" + group + "/inheritsfrom"));
        }
        for (String node : this.getConfig().getStringList("groups/" + group + "/allow")) {
            if (permissionAttachments.get(player.getName()).getPermissions().containsKey(node)) {
                permissionAttachments.get(player.getName()).unsetPermission(node);
            }
            permissionAttachments.get(player.getName()).setPermission(node, true);
        }
        for (String node : this.getConfig().getStringList("groups/" + group + "/deny")) {
            if (permissionAttachments.get(player.getName()).getPermissions().containsKey(node)) {
                permissionAttachments.get(player.getName()).unsetPermission(node);
            }
            permissionAttachments.get(player.getName()).setPermission(node, false);
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
        groups.addAll(getConfig().getStringList("users/" + player.getName()));
        return groups;
    }

    @Override
    public Set<String> getGroups(OfflinePlayer player, World world) {
        Set<String> groups = new HashSet<>();
        groups.addAll(getConfig().getStringList("groups"));
        return groups;
    }

    @Override
    public void setGroup(OfflinePlayer player, String groupName) {
        Set<String> groups = new HashSet<>();
        groups.add(groupName);
        getConfig().set("users/" + player.getName(), groups);
        saveConfig();
    }

    @Override
    public void setGroup(OfflinePlayer player, World world, String groupName) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void addGroup(OfflinePlayer player, String groupName) {
        Set<String> groups = (Set<String>) this.getConfig().get("users/" + player.getName());
        groups.add(groupName);
        getConfig().set("users/" + player.getName(), groups);
        saveConfig();
    }

    @Override
    public void addGroup(OfflinePlayer player, World world, String groupName) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeGroup(OfflinePlayer player, String groupName) {
        Set<String> groups = (Set<String>) this.getConfig().get("users/" + player.getName());
        groups.remove(groupName);
        getConfig().set("users/" + player.getName(), groups);
        saveConfig();
    }

    @Override
    public void removeGroup(OfflinePlayer player, World world, String groupName) {

    }

}
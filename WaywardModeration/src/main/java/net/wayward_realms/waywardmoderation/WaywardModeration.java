package net.wayward_realms.waywardmoderation;

import net.wayward_realms.waywardlib.moderation.ModerationPlugin;
import net.wayward_realms.waywardlib.moderation.Ticket;
import net.wayward_realms.waywardlib.moderation.Warning;
import net.wayward_realms.waywardmoderation.ticket.*;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class WaywardModeration extends JavaPlugin implements ModerationPlugin {

    private LogManager logManager;
    private VanishManager vanishManager;
    private WarningManager warningManager;
    private TicketManager ticketManager;
    private ReputationManager reputationManager;

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(TicketImpl.class);
        ConfigurationSerialization.registerClass(WarningImpl.class);
        logManager = new LogManager();
        vanishManager = new VanishManager(this);
        warningManager = new WarningManager(this);
        ticketManager = new TicketManager(this);
        reputationManager = new ReputationManager(new File(getDataFolder().getPath() + File.separator + "reputation.yml"));
        getCommand("reputation").setExecutor(new ReputationCommand(this));
        getCommand("ticket").setExecutor(new TicketCommand(this));
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getCommand("warn").setExecutor(new WarnCommand(this));
        getCommand("unwarn").setExecutor(new UnwarnCommand(this));
        getCommand("warnings").setExecutor(new WarningsCommand(this));
        getCommand("amivanished").setExecutor(new AmIVanishedCommand(this));
        registerListeners(new PlayerJoinListener(this));
        for (Ticket ticket : getTickets()) {
            if (ticket.getId() > TicketImpl.getNextId()) {
                TicketImpl.setNextId(ticket.getId());
            }
        }
    }

    @Override
    public String getPrefix() {
        //return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + "WaywardModeration" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
        return "";
    }

    @Override
    public void loadState() {

    }

    @Override
    public void saveState() {

    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public Map<Date, Material> getBlockMaterialChanges(Block block) {
        return logManager.getBlockMaterialChanges(block);
    }

    @Override
    public Map<Date, Byte> getBlockDataChanges(Block block) {
        return logManager.getBlockDataChanges(block);
    }

    @Override
    public Map<Date, ItemStack> getInventoryContentChanges(Inventory inventory) {
        return logManager.getInventoryContentChanges(inventory);
    }

    @Override
    public Material getBlockMaterialAtTime(Block block, Date date) {
        return logManager.getBlockMaterialAtTime(block, date);
    }

    @Override
    public Byte getBlockDataAtTime(Block block, Date date) {
        return logManager.getBlockDataAtTime(block, date);
    }

    @Override
    public ItemStack[] getInventoryContentsAtTime(Inventory inventory, Date date) {
        return logManager.getInventoryContentsAtTime(inventory, date);
    }

    @Override
    public boolean isVanished(Player player) {
        return vanishManager.isVanished(player);
    }

    @Override
    public void setVanished(Player player, boolean vanished) {
        vanishManager.setVanished(player, vanished);
    }

    public Set<Player> getVanishedPlayers() {
        return vanishManager.getVanishedPlayers();
    }

    public boolean canSee(Player player, Player target) {
        return vanishManager.canSee(player, target);
    }

    @Override
    public Collection<Warning> getWarnings(OfflinePlayer player) {
        return warningManager.getWarnings(player);
    }

    @Override
    public void addWarning(OfflinePlayer player, Warning warning) {
        warningManager.addWarning(player, warning);
    }

    @Override
    public void removeWarning(OfflinePlayer player, Warning warning) {
        warningManager.removeWarning(player, warning);
    }

    @Override
    public Collection<Ticket> getTickets() {
        return ticketManager.getTickets();
    }

    @Override
    public Collection<Ticket> getTickets(OfflinePlayer player) {
        return ticketManager.getTickets(new IssuerTicketFilter(player));
    }

    public Collection<Ticket> getTickets(TicketFilter filter) {
        return ticketManager.getTickets(filter);
    }

    public int getTicketId(Ticket ticket) {
        return ticketManager.getTicketId(ticket);
    }

    public Ticket getTicket(int id) {
        return ticketManager.getTicket(id);
    }

    @Override
    public void addTicket(OfflinePlayer player, Ticket ticket) {
        ticketManager.addTicket(ticket);
    }

    @Override
    public void removeTicket(Ticket ticket) {
        ticketManager.removeTicket(ticket);
    }

    public int getReputation(OfflinePlayer player) {
        return reputationManager.getReputation(player);
    }

    public void setReputation(OfflinePlayer player, int reputation) {
        reputationManager.setReputation(player, reputation);
    }

    public boolean hasSetReputation(OfflinePlayer setter, OfflinePlayer player) {
        return reputationManager.hasSetReputation(setter, player);
    }

    public int getGivenReputation(OfflinePlayer setter, OfflinePlayer player) {
        return reputationManager.getGivenReputation(setter, player);
    }

    public void setGivenReputation(OfflinePlayer setter, OfflinePlayer player, int amount) {
        reputationManager.setGivenReputation(setter, player, amount);
    }

}

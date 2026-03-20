package me.arismc;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ArisFly extends JavaPlugin implements Listener {

    private String flyRegion;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        flyRegion = getConfig().getString("fly-region", "spawn");
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("ArisFly đã hoạt động trên ArisMC!");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
            event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;

        Player player = event.getPlayer();
        if (isInFlyRegion(player.getLocation())) {
            if (!player.getAllowFlight()) {
                player.setAllowFlight(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                    "&aʙᴀ̣ɴ Đᴀ̃ ᴠᴀ̀ᴏ ᴠᴜ̀ɴɢ ᴄᴏ́ ᴛʜᴇ̂̉ ʙᴀʏ!"));
            }
        } else {
            if (player.getAllowFlight() && player.getGameMode().getValue() != 1) {
                player.setAllowFlight(false);
                player.setFlying(false);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                    "&cʙᴀ̣ɴ Đᴀ̃ ʀᴏ̛̀ɪ ᴠᴜ̀ɴɢ ʙᴀʏ, ᴛᴀ̆́ᴛ ꜰʟʏ!"));
            }
        }
    }

    private boolean isInFlyRegion(Location loc) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        for (ProtectedRegion rg : container.get(BukkitAdapter.adapt(loc.getWorld())).getApplicableRegions(BukkitAdapter.asBlockVector(loc))) {
            if (rg.getId().equalsIgnoreCase(flyRegion)) return true;
        }
        return false;
    }
}

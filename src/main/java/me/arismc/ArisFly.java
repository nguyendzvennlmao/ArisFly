package me.arismc;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

public class ArisFly extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("ᴀʀɪsғʟʏ ғᴏʟɪᴀ Đᴀ̃ ᴋɪ́ᴄʜ ʜᴏᴀ̣ᴛ!");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        
        // Lấy dữ liệu từ file config.yml
        String regionName = getConfig().getString("fly-region", "spawn");
        String msgEnter = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.enter-fly-zone"));
        String msgExit = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.exit-fly-zone"));

        if (isInRegion(loc, regionName)) {
            // Nếu ở trong vùng và chưa được phép bay
            if (!player.getAllowFlight()) {
                player.setAllowFlight(true);
                player.sendMessage(msgEnter);
            }
        } else {
            // Nếu ra khỏi vùng, đang được phép bay và không phải Admin
            if (player.getAllowFlight() && !player.hasPermission("arisfly.admin")) {
                player.setAllowFlight(false);
                player.setFlying(false);
                player.sendMessage(msgExit);
            }
        }
    }

    private boolean isInRegion(Location loc, String name) {
        try {
            ApplicableRegionSet set = WorldGuard.getInstance().getPlatform().getRegionContainer()
                    .createQuery().getApplicableRegions(BukkitAdapter.adapt(loc));
            for (ProtectedRegion region : set) {
                if (region.getId().equalsIgnoreCase(name)) return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}

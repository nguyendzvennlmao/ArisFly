package me.arismc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ArisLogin extends JavaPlugin implements Listener {

    private final Set<UUID> needAuth = new HashSet<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);

        // Lệnh Đăng Ký
        getCommand("dangky").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof Player player)) return true;
            if (args.length < 2) {
                player.sendMessage("§cᴄᴀ́ᴄʜ ᴅᴜ̀ɴɢ: /ᴅᴀɴɢᴋʏ <ᴍᴋ> <ᴍᴋ>");
                return true;
            }
            if (!args[0].equals(args[1])) {
                player.sendMessage("§cᴍᴀ̣̂ᴛ ᴋʜᴀ̂̉ᴜ ᴋʜᴏ̂ɴɢ ᴋʜᴏ̛́ᴘ!");
                return true;
            }
            getConfig().set("users." + player.getUniqueId() + ".password", args[0]);
            saveConfig();
            needAuth.remove(player.getUniqueId());
            player.sendTitle("§a§lᴛʜᴀ̀ɴʜ ᴄᴏ̂ɴɢ", "§fʙᴀ̣ɴ Đᴀ̃ Đᴀ̆ɴɢ ᴋʏ́ ᴛᴀ̀ɪ ᴋʜᴏᴀ̉ɴ!", 10, 40, 10);
            return true;
        });

        // Lệnh Đăng Nhập
        getCommand("dangnhap").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof Player player)) return true;
            String pass = getConfig().getString("users." + player.getUniqueId() + ".password");
            if (pass == null) {
                player.sendMessage("§cʙᴀ̣ɴ ᴄʜᴜ̛ᴀ Đᴀ̆ɴɢ ᴋʏ́!");
                return true;
            }
            if (args.length < 1 || !args[0].equals(pass)) {
                player.sendMessage("§cꜱᴀɪ ᴍᴀ̣̂ᴛ ᴋʜᴀ̂̉ᴜ!");
                return true;
            }
            needAuth.remove(player.getUniqueId());
            player.sendTitle("§a§lᴛʜᴀ̀ɴʜ ᴄᴏ̂ɴɢ", "§fᴄʜᴀ̀ᴏ ᴍᴜ̛̀ɴɢ ʙᴀ̣ɴ ᴛʀᴏ̛̉ ʟᴀ̣ɪ!", 10, 40, 10);
            return true;
        });

        // Task hiển thị Title (AsyncScheduler cho Folia)
        Bukkit.getAsyncScheduler().runAtFixedRate(this, (task) -> {
            for (UUID uuid : needAuth) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null) {
                    boolean hasAccount = getConfig().contains("users." + uuid);
                    String title = "§6§lᴀʀɪs ɴᴇᴛᴡᴏʀᴋ";
                    String subtitle = hasAccount ? "§eʜᴀ̃ʏ ᴅᴜ̀ɴɢ: §b/ᴅᴀɴɢɴʜᴀᴘ <ᴍᴋ>" : "§eʜᴀ̃ʏ ᴅᴜ̀ɴɢ: §b/ᴅᴀɴɢᴋʏ <ᴍᴋ> <ᴍᴋ>";
                    p.sendTitle(title, subtitle, 0, 25, 0);
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        needAuth.add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (needAuth.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (needAuth.contains(event.getDamager().getUniqueId()) || needAuth.contains(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage().toLowerCase();
        if (needAuth.contains(event.getPlayer().getUniqueId())) {
            if (!msg.startsWith("/dangky") && !msg.startsWith("/dangnhap")) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cʙᴀ̣ɴ ᴘʜᴀ̉ɪ Đᴀ̆ɴɢ ɴʜᴀ̣̂ᴘ Đᴇ̂̉ ꜱᴜ̛̉ ᴅᴜ̣ɴɢ ʟᴇ̣̂ɴʜ!");
            }
        }
    }
  }

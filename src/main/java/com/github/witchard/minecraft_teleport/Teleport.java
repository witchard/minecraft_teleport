package com.github.witchard.minecraft_teleport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Paths;
import java.util.logging.Logger;

public final class Teleport extends JavaPlugin {
    private Logger log;
    private State state;
    private final String STATE_FILE = "teleport.state";

    @Override
    public void onEnable() {
        // Plugin startup logic
        log = getLogger();
        var path = Paths.get(getDataFolder().getAbsolutePath(), STATE_FILE).toString();
        log.info("loading state from " + path);
        state = State.load(path, log);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (command.getName().equalsIgnoreCase("tps") && args.length == 1) {
                log.info("Saving " + p.getLocation().toString() + " as " + args[0]);
                state.teleports.put(args[0], p.getLocation());
                state.save();
                return true;
            }
            if (command.getName().equalsIgnoreCase("tpl") && args.length == 0) {
                p.sendMessage(state.teleports.keySet().toString());
                return true;
            }
            if (command.getName().equalsIgnoreCase("tpd") && args.length == 1) {
                if (state.teleports.containsKey(args[0])) {
                    state.teleports.remove(args[0]);
                    state.save();
                    return true;
                }
            }
            if ((command.getName().equalsIgnoreCase("tpg") || command.getName().equalsIgnoreCase("tpp"))
                    && args.length == 1) {
                if (state.teleports.containsKey(args[0])) {
                    p.teleport(state.teleports.get(args[0]));
                    return true;
                }
            }
            if (command.getName().equalsIgnoreCase("tpa") && args.length == 1) {
                if (state.teleports.containsKey(args[0])) {
                    var dest = state.teleports.get(args[0]);
                    getServer().getOnlinePlayers().forEach((pl -> pl.teleport(dest)));
                    return true;
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (command.getName().equalsIgnoreCase("tpl") && args.length == 0) {
                log.info(state.teleports.keySet().toString());
                return true;
            }
            if (command.getName().equalsIgnoreCase("tpd") && args.length == 1) {
                if (state.teleports.containsKey(args[0])) {
                    state.teleports.remove(args[0]);
                    state.save();
                    return true;
                }
            }
            if (command.getName().equalsIgnoreCase("tpa") && args.length == 1) {
                if (state.teleports.containsKey(args[0])) {
                    var dest = state.teleports.get(args[0]);
                    getServer().getOnlinePlayers().forEach((pl -> pl.teleport(dest)));
                    return true;
                }
            }
        }
        return false;
    }
}

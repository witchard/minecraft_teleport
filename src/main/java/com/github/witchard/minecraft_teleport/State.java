package com.github.witchard.minecraft_teleport;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;


public class State implements Serializable {

    public HashMap<String, Location> teleports;
    private String path;

    public State(String filePath) {
        teleports = new HashMap<>();
        path = filePath;
        saveData();
    }

    public boolean saveData() {
        try {
            var parent = Paths.get(path).getParent();
            if (!Files.exists(parent) && !Files.isDirectory(parent)) {
                Files.createDirectories(parent);
            }
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(path)));
            out.writeObject(this);
            out.close();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
    public static State loadState(String filePath, Logger log) {
        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(filePath)));
            State state = (State)in.readObject();
            state.path = filePath;
            in.close();
            return state;
        } catch (ClassNotFoundException | IOException e) {
            log.warning(filePath + " not found, creating");
            e.printStackTrace();
            return new State(filePath);
        }
    }
}
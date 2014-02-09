package com.oresomecraft.findregion;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class FindRegion extends JavaPlugin {

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

    @Command(aliases = {"findregions"},
            desc = "Displays regions within a worldedit selection",
            min = 0,
            max = 0)
    @CommandPermissions({"worldguard.region.info.*"})
    public void findRegion(CommandContext args, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            Selection selection = worldEdit.getSelection((Player) sender);
            if (selection != null) {
                List<ProtectedRegion> regions = new ArrayList<ProtectedRegion>();
                for (Location location : getLocations(selection.getMaximumPoint(), selection.getMinimumPoint())) {
                    ApplicableRegionSet set = WGBukkit.getRegionManager(location.getWorld()).getApplicableRegions(location);
                    for (ProtectedRegion region : set) {
                        if (!regions.contains(region)) {
                            regions.add(region);
                        }
                    }
                }
                if (regions.size() != 0) {
                    sender.sendMessage(ChatColor.GREEN + "Your WorldEdit selection contained the following regions:");
                    sender.sendMessage(regions.toString());
                } else sender.sendMessage(ChatColor.RED + "No regions could be found inside your WorldEdit selection!");
            } else sender.sendMessage(ChatColor.RED + "You need to define a WorldEdit region!");
        } else sender.sendMessage("You need to be a player to execute this command!");
    }

    public List<Location> getLocations(Location l1, Location l2) {
        List<Location> locations = new ArrayList<Location>();
        int topBlockX = (l1.getBlockX() < l2.getBlockX() ? l2.getBlockX() : l1.getBlockX());
        int bottomBlockX = (l1.getBlockX() > l2.getBlockX() ? l2.getBlockX() : l1.getBlockX());

        int topBlockY = (l1.getBlockY() < l2.getBlockY() ? l2.getBlockY() : l1.getBlockY());
        int bottomBlockY = (l1.getBlockY() > l2.getBlockY() ? l2.getBlockY() : l1.getBlockY());

        int topBlockZ = (l1.getBlockZ() < l2.getBlockZ() ? l2.getBlockZ() : l1.getBlockZ());
        int bottomBlockZ = (l1.getBlockZ() > l2.getBlockZ() ? l2.getBlockZ() : l1.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    Location location = new Location(l1.getWorld(), x, y, z);
                    locations.add(location);
                }
            }
        }
        return locations;
    }
}

/*
 * This file is part of HoloAPI.
 *
 * HoloAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HoloAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HoloAPI.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.holoapi.command;

import com.dsh105.holoapi.HoloAPI;
import com.dsh105.holoapi.HoloAPICore;
import com.dsh105.holoapi.reflection.utility.CommonReflection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Random;

public class HoloDebugCommand implements CommandExecutor {

    private static String[] messages = {"A cow ain't a pig!", "Leave flappy bird alone!", "I don't like this",
            "Stop spamming", "Goodmorning America!", "DSH105 is sexy!", "PressHearthToContinue",
            "Stay away from my budder!", "Wot", "If HoloAPI works then DSH105 and CaptainBern wrote it, else I have no idea who wrote that crap"};

    private static Random generator = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!sender.hasPermission("holo.debug")) {
            sender.sendMessage(ChatColor.RED + "Stop (de)bugging me...");
            return false;
        }

        sender.sendMessage("Debug results: ");
        sender.sendMessage("--------------[HoloAPI Stuff]--------------");
        sender.sendMessage("HoloAPI-Version: " + HoloAPI.getCore().getDescription().getVersion());
        sender.sendMessage("Message of the day: " + messages[generator.nextInt(messages.length)]);
        sender.sendMessage("--------------[CraftBukkit Stuff]--------------");
        sender.sendMessage("Version tag: " + CommonReflection.getVersionTag());
        sender.sendMessage("NMS-package: " + CommonReflection.getMinecraftPackage());
        sender.sendMessage("CB-package: " + CommonReflection.getCraftBukkitPackage());
        sender.sendMessage("Bukkit version: " + Bukkit.getBukkitVersion());
        sender.sendMessage("--------------[Minecraft Server Stuff]--------------");
        sender.sendMessage("Using Netty: " + CommonReflection.isUsingNetty());
        sender.sendMessage("MinecraftServer: " + CommonReflection.getMinecraftClass("MinecraftServer").getCanonicalName());
        sender.sendMessage("Entity: " + CommonReflection.getMinecraftClass("Entity"));
        sender.sendMessage("Is (forge) Modded: " + (CommonReflection.getMinecraftClass("Entity").getCanonicalName().startsWith("net.minecraft.entity") ? "Definitely" : "Probably not"));
        sender.sendMessage("Injection strategy: " + HoloAPICore.getInjectionManager().getStrategy().name());

        return true;
    }
}

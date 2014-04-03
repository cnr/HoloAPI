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

package com.dsh105.holoapi;

import com.dsh105.dshutils.DSHPlugin;
import com.dsh105.dshutils.config.YAMLConfig;
import com.dsh105.holoapi.api.HoloManager;
import com.dsh105.holoapi.hook.VanishProvider;
import com.dsh105.holoapi.hook.VaultProvider;
import com.dsh105.holoapi.image.*;
import com.dsh105.holoapi.api.TagFormatter;

public class HoloAPI extends DSHPlugin {

    private static HoloAPICore CORE;

    public static final ModuleLogger LOGGER = new ModuleLogger("HoloAPI");
    public static final ModuleLogger LOGGER_REFLECTION = LOGGER.getModule("Reflection");

    public static void setPlugin(HoloAPICore plugin) {
        if (CORE != null) {
            return;
        }
        CORE = plugin;
    }

    public static HoloAPICore getPlugin() {
        return CORE;
    }

    public static String getPrefix() {
        return getPrefix("HoloAPI") + "••• ";
    }

    public static String getPrefix(String internalText) {
        return getPlugin().prefix.replace("%text%", internalText);
    }

    /**
     * Gets the HoloAPI Hologram Manager.
     * <p/>
     * The Hologram Manager is used to register and manage the holograms created from both within and outside the HoloAPI plugin
     *
     * @return {@link com.dsh105.holoapi.api.HoloManager} that manages and controls registration of holograms
     */
    public static HoloManager getManager() {
        return getPlugin().MANAGER;
    }

    /**
     * Gets the HoloAPI Image Loader
     * <p/>
     * The Image Loader stores and handles registration of all images configured in the HoloAPI Configuration file
     *
     * @return Image Loader that controls and stores all pre-loaded image generators
     */
    public static ImageLoader<ImageGenerator> getImageLoader() {
        return getPlugin().IMAGE_LOADER;
    }

    /**
     * Gets the HoloAPI Animation Loader
     * <p/>
     * The Animated Loader stores and handles registration of all animations configured in the HoloAPI Configuration file
     *
     * @return Animation Loader that controls and stores all pre-loaded animation generators
     */
    public static ImageLoader<AnimatedImageGenerator> getAnimationLoader() {
        return getPlugin().ANIMATION_LOADER;
    }

    public static TagFormatter getTagFormatter() {
        return getPlugin().TAG_FORMATTER;
    }

    /**
     * Gets the spacing between hologram lines
     *
     * @return line spacing between holograms
     */
    public static double getHologramLineSpacing() {
        return getPlugin().LINE_SPACING;
    }

    public static int getTagEntityMultiplier() {
        return getPlugin().TAG_ENTITY_MULTIPLIER;
    }

    public static String getCommandLabel() {
        return getPlugin().OPTIONS.getConfig().getString("command", "holo");
    }

    public static YAMLConfig getConfig(ConfigType type) {
        if (type == ConfigType.MAIN) {
            return getPlugin().config;
        } else if (type == ConfigType.DATA) {
            return getPlugin().dataConfig;
        } else if (type == ConfigType.LANG) {
            return getPlugin().langConfig;
        }
        return null;
    }

    public static VaultProvider getVaultProvider() {
        if (getPlugin().vaultProvider == null) {
            throw new RuntimeException("VaultProvider is NULL!");
        }
        return getPlugin().vaultProvider;
    }

    public static VanishProvider getVanishProvider() {
        if (getPlugin().vanishProvider == null) {
            throw new RuntimeException("VanishProvider is NULL!");
        }
        return getPlugin().vanishProvider;
    }

    public enum ConfigType {
        MAIN, DATA, LANG;
    }
}

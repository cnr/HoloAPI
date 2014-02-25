package com.dsh105.holoapi.image;

import com.dsh105.dshutils.config.YAMLConfig;
import com.dsh105.dshutils.util.EnumUtil;
import com.dsh105.holoapi.HoloAPI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;

public class SimpleImageLoader implements ImageLoader {

    private final HashMap<String, ImageGenerator> KEY_TO_IMAGE_MAP = new HashMap<String, ImageGenerator>();

    public void loadImageConfiguration(YAMLConfig config) {
        File imageFolder = new File(HoloAPI.getInstance().getDataFolder() + File.separator + "images");
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }
        ConfigurationSection cs = config.getConfigurationSection("images");
        if (cs != null) {
            for (String key : cs.getKeys(false)) {
                String path = "images." + key + ".";
                //boolean staticImage = config.getBoolean(path + "static");    // TODO: Animated images
                String imagePath = config.getString(path + "path");
                int imageHeight = config.getInt(path + "height", 10);
                String imageChar = config.getString(path + "characterType", ImageChar.DARK_SHADE.getHumanName());
                String imageType = config.getString(path + "type");
                if (!EnumUtil.isEnumType(ImageLoader.ImageLoadType.class, imageType.toUpperCase())) {
                    continue;
                }
                ImageLoader.ImageLoadType type = ImageLoader.ImageLoadType.valueOf(imageType.toUpperCase());

                ImageGenerator generator = findGenerator(type, imagePath, imageHeight, imageChar);
                if (generator != null) {
                    this.KEY_TO_IMAGE_MAP.put(key, generator);
                }
            }
        }
    }

    private ImageGenerator findGenerator(ImageLoader.ImageLoadType type, String imagePath, int imageHeight, String imageCharType) {
        ImageChar c = ImageChar.fromHumanName(imageCharType, true);
        if (c == null) {
            return null;
        }
        switch (type) {
            case URL:
                return new ImageGenerator(imagePath, imageHeight, c);
            case FILE:
                File f = new File(HoloAPI.getInstance().getDataFolder() + File.separator + "images" + File.separator + imagePath);
                return new ImageGenerator(f, imageHeight, c);
        }
        return null;
    }

    @Override
    public ImageGenerator getGenerator(String key) {
        return this.KEY_TO_IMAGE_MAP.get(key);
    }
}
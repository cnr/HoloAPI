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

package com.dsh105.holoapi.api;

import com.dsh105.holoapi.HoloAPI;
import com.dsh105.holoapi.util.TimeFormat;
import com.dsh105.holoapi.util.UnicodeFormatter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagFormatter {

    private HashMap<String, TagFormat> tagFormats = new HashMap<String, TagFormat>();
    private HashMap<Pattern, DynamicTagFormat> dynamicTagFormats = new HashMap<Pattern, DynamicTagFormat>();

    public TagFormatter() {
        this.addFormat("%time%", new TagFormat() {
            @Override
            public String getValue(Player observer) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.HOUR_OF_DAY, HoloAPI.getConfig(HoloAPI.ConfigType.MAIN).getInt("timezone.offset", 0));
                return new SimpleDateFormat("h:mm a" + (HoloAPI.getConfig(HoloAPI.ConfigType.MAIN).getBoolean("timezone.showZoneMarker") ? " z" : "")).format(c.getTime());
            }
        });

        this.addFormat("%mctime%", new TagFormat() {
            @Override
            public String getValue(Player observer) {
                return TimeFormat.format12(observer.getWorld().getTime());
            }
        });

        this.addFormat("%name%", new TagFormat() {
            @Override
            public String getValue(Player observer) {
                return observer.getName();
            }
        });

        this.addFormat("%displayname%", new TagFormat() {
            @Override
            public String getValue(Player observer) {
                return observer.getDisplayName();
            }
        });

        this.addFormat("%balance%", new TagFormat() {
            @Override
            public String getValue(Player observer) {
                return HoloAPI.getVaultProvider().getBalance(observer);
            }
        });

        this.addFormat("%rank%", new TagFormat() {
            @Override
            public String getValue(Player observer) {
                return HoloAPI.getVaultProvider().getRank(observer);
            }
        });

        this.addFormat("%world%", new TagFormat() {
            @Override
            public String getValue(Player observer) {
                return observer.getWorld().getName();
            }
        });

        this.addFormat("%health%", new TagFormat() {
            @Override
            public String getValue(Player observer) {
                return String.valueOf(observer.getHealth() == (int) observer.getHealth() ? (int) observer.getHealth() : observer.getHealth());
            }
        });

        this.addFormat("%playercount%", new TagFormat() {
            @Override
            public String getValue(Player observer) {
                return String.valueOf(Bukkit.getOnlinePlayers().length);
            }
        });

        this.addFormat("%maxplayers%", new TagFormat() {
            @Override
            public String getValue(Player observer) {
                return String.valueOf(Bukkit.getMaxPlayers());
            }
        });

        this.addFormat(Pattern.compile("\\%date:(.+?)\\%"), new DynamicTagFormat() {
            @Override
            public String match(Matcher matcher, String lineContent, Hologram h, Player observer) {
                SimpleDateFormat format = new SimpleDateFormat(matcher.group(1));

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR_OF_DAY, HoloAPI.getConfig(HoloAPI.ConfigType.MAIN).getInt("timezone.offset", 0));
                return lineContent.replace(matcher.group(), format.format(calendar.getTime()));
            }
        });
    }

    /**
     * Adds a formatter for all Holograms to utilise
     *
     * @param tag    tag to be formatted (replaced)
     * @param format format to apply
     */
    public void addFormat(String tag, TagFormat format) {
        this.tagFormats.put(tag, format);
    }

    /**
     * Removes a formatter from the list of applied formats
     *
     * @param tag tag of the format to remove
     */
    public void removeFormat(String tag) {
        this.tagFormats.remove(tag);
    }

    /**
     * Adds a dynamic formatter for all Holograms to utilise
     *
     * @param pattern regex pattern to apply
     * @param format  format to apply
     */
    public void addFormat(Pattern pattern, DynamicTagFormat format) {
        this.dynamicTagFormats.put(pattern, format);
    }

    /**
     * Removes a formatter from the list of applied formats
     *
     * @param pattern pattern of the format to remove
     */
    public void removeFormat(Pattern pattern) {
        this.dynamicTagFormats.remove(pattern);
    }

    public String formatForOldClient(String content) {
        if (content.length() > 64 && !HoloAPI.getCore().isUsingNetty) {
            // 1.6.x client crashes if a name tag is longer than 64 characters
            // Unfortunate, but it must be accounted for
            content = content.substring(0, 64);
        }

        return content;
    }

    public String formatBasic(String content) {
        content = ChatColor.translateAlternateColorCodes('&', content);
        content = UnicodeFormatter.replaceAll(content);

        content = formatForOldClient(content);

        return content;
    }

    public String formatTags(Hologram h, Player observer, String content) {
        for (Map.Entry<String, TagFormat> entry : this.tagFormats.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue().getValue(h, observer));
        }

        for (Map.Entry<Pattern, DynamicTagFormat> entry : this.dynamicTagFormats.entrySet()) {
            Matcher matcher = entry.getKey().matcher(content);
            while (matcher.find()) {
                content = entry.getValue().match(matcher, content, h, observer);
            }
        }


        //content = matchDate(content);
        content = formatForOldClient(content);
        return content;
    }

    public String format(Hologram h, Player observer, String content) {
        content = formatTags(h, observer, content);
        content = formatBasic(content);

        return content;
    }

    protected int matchItem(String content) {
        Pattern itemPattern = Pattern.compile("\\%item:([0-9]+?)\\%");
        Matcher matcher = itemPattern.matcher(content);
        while (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return -1;
    }
}
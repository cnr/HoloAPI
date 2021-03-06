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

package com.dsh105.holoapi.api.events;

import com.dsh105.holoapi.api.Hologram;

import java.util.Map;

/**
 * Called when TouchAction data is loaded from file.
 */
public class HoloTouchActionLoadEvent extends HoloDataLoadEvent {

    public HoloTouchActionLoadEvent(Hologram hologram, String saveKey, Map<String, Object> configMap) {
        super(hologram, saveKey, configMap);
    }

    /**
     * Gets the save key of the saved data
     *
     * @return key of the saved data
     * @deprecated use {@link HoloDataLoadEvent#getSaveKey()}
     */
    @Deprecated
    public String getLoadedTouchActionKey() {
        return getSaveKey();
    }

}
package com.lt2333.simplicitytools.tiles

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class AllowScreenshots : TileService() {
    private val key = "disable_flag_secure"

    override fun onClick() {
        super.onClick()
        try {
            val pref = getSharedPreferences("config", MODE_WORLD_READABLE)
            val prefEditor = pref.edit()
            if (pref.getBoolean(key, false)) {
                prefEditor.putBoolean(key, false)
                qsTile.state = Tile.STATE_INACTIVE
            } else {
                prefEditor.putBoolean(key, true)
                qsTile.state = Tile.STATE_ACTIVE
            }
            prefEditor.commit()
            qsTile.updateTile()
        } catch (e: SecurityException) {
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        try {
            val pref = getSharedPreferences("config", MODE_WORLD_READABLE)
            if (pref.getBoolean(key, false)) {
                qsTile.state = Tile.STATE_ACTIVE
            } else {
                qsTile.state = Tile.STATE_INACTIVE
            }
            qsTile.updateTile()
        } catch (e: SecurityException) {
        }
    }
}
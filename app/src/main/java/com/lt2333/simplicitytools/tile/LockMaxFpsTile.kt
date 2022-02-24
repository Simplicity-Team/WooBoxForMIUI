package com.lt2333.simplicitytools.tile

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class LockMaxFpsTile : TileService() {

    val key = "lock_max_fps"

    override fun onClick() {
        super.onClick()
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
    }

    override fun onStartListening() {
        super.onStartListening()
        val pref = getSharedPreferences("config", MODE_WORLD_READABLE)
        if (pref.getBoolean(key, false)) {
            qsTile.state = Tile.STATE_ACTIVE
        } else {
            qsTile.state = Tile.STATE_INACTIVE
        }
        qsTile.updateTile()
    }

}
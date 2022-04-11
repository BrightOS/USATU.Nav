package su.usatu.navigator.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PreferenceRepository(private val sharedPreferences: SharedPreferences) {

    // Night mode repository

    var nightMode: Int
        get() = sharedPreferences.getInt(PREFERENCE_NIGHT_MODE, PREFERENCE_NIGHT_MODE_DEF_VAL)
        set(value) {
            sharedPreferences.edit().putInt(PREFERENCE_NIGHT_MODE, value).apply()
            AppCompatDelegate.setDefaultNightMode(value)
            _nightModeLive.value = value
        }

    private val _nightModeLive: MutableLiveData<Int> = MutableLiveData()
    val nightModeLive: LiveData<Int>
        get() = _nightModeLive

    // Animations repository

    var animations: Boolean
        get() = sharedPreferences.getBoolean(PREFERENCE_ANIMATIONS, true)
        set(value) = sharedPreferences.edit().putBoolean(PREFERENCE_ANIMATIONS, value).apply()

    private val _animationsLive: MutableLiveData<Boolean> = MutableLiveData()
    val animationsLive: LiveData<Boolean>
        get() = _animationsLive

    companion object {
        private const val PREFERENCE_NIGHT_MODE = "preference_night_mode"
        private const val PREFERENCE_NIGHT_MODE_DEF_VAL = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

        private const val PREFERENCE_ANIMATIONS = "preference_animations"
    }
}
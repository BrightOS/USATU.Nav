package su.usatu.navigator.data.repository

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
        set(value) {
            sharedPreferences.edit().putBoolean(PREFERENCE_ANIMATIONS, value).apply()
            _animationsLive.value = value
        }

    private val _animationsLive: MutableLiveData<Boolean> = MutableLiveData()
    val animationsLive: LiveData<Boolean>
        get() = _animationsLive

    // Swipe guide repository

    var swipe: Boolean
        get() = sharedPreferences.getBoolean(PREFERENCE_SWIPE, false)
        set(value) {
            sharedPreferences.edit().putBoolean(PREFERENCE_SWIPE, value).apply()
            _swipeLive.value = value
        }

    private val _swipeLive: MutableLiveData<Boolean> = MutableLiveData()
    val swipeLive: LiveData<Boolean>
        get() = _swipeLive

    // Swipe guide repository

    var lastDate: String
        get() = sharedPreferences.getString(PREFERENCE_LAST_DATE, "").toString()
        set(value) {
            sharedPreferences.edit().putString(PREFERENCE_LAST_DATE, value).apply()
            _lastDateLive.value = value
        }

    private val _lastDateLive: MutableLiveData<String> = MutableLiveData()
    val lastDateLive: LiveData<String>
        get() = _lastDateLive

    companion object {
        private const val PREFERENCE_NIGHT_MODE = "preference_night_mode"
        private const val PREFERENCE_NIGHT_MODE_DEF_VAL = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

        private const val PREFERENCE_ANIMATIONS = "preference_animations"
        private const val PREFERENCE_SWIPE = "preference_swipe"
        private const val PREFERENCE_LAST_DATE = "preference_last_date"
    }
}
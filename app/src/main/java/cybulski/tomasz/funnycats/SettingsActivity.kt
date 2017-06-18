package cybulski.tomasz.funnycats

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val soundId = sharedPref.getInt(getString(R.string.shared_pref_key_select_sound), R.raw.regular_meow)
        when(soundId){
            R.raw.regular_meow -> {
                rb_sound_regular.isChecked = true
            }
            R.raw.lion_meow -> {
                rb_sound_lion.isChecked = true
            }
            R.raw.dog_meow -> {
                rb_sound_dog.isChecked = true
            }
        }
    }

    fun setSound(view: View): Unit {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        when(view.id){
            R.id.rb_sound_regular -> {
                sharedPref.edit().putInt(getString(R.string.shared_pref_key_select_sound), R.raw.regular_meow).apply()
            }
            R.id.rb_sound_lion -> {
                sharedPref.edit().putInt(getString(R.string.shared_pref_key_select_sound), R.raw.lion_meow).apply()
            }
            R.id.rb_sound_dog -> {
                sharedPref.edit().putInt(getString(R.string.shared_pref_key_select_sound), R.raw.dog_meow).apply()
            }
        }
    }

}

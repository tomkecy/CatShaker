package cybulski.tomasz.funnycats

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.media.MediaPlayer
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*

class MainActivity : AppCompatActivity(), ShakeHandler.ShakeListener {
    private var shakeHandler = ShakeHandler()
    private val random: Random = Random()
    private val imageList: MutableList<Int> = mutableListOf()
    private var meowMediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        iv_container.setImageResource(R.drawable.cat_main)
        loadImages()

        setSound()
        shakeHandler.setListener(this)
        shakeHandler.initialize(this)
    }

    override fun onRestart() {
        super.onRestart()
        setSound()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState!!.putInt(getString(R.string.pic_instance_state_key), iv_container.tag as Int)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val index = savedInstanceState!!.getInt(getString(R.string.pic_instance_state_key))
        iv_container.setImageResource(imageList[index])
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.mi_meow -> {
                if(!meowMediaPlayer!!.isPlaying) {
                    meowMediaPlayer!!.start()
                }
                return true
            }
            R.id.mi_meow_toast -> {
                Toast.makeText(applicationContext, R.string.meow, Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.gi_color_black -> {
                layout_main.layout_main.setBackgroundColor(Color.BLACK)
                menuItem.isChecked = true
                return true
            }
            R.id.gi_color_white -> {
                layout_main.layout_main.setBackgroundColor(Color.WHITE)
                menuItem.isChecked = true
                return true
            }
            R.id.mi_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onShake() {
        if(!meowMediaPlayer!!.isPlaying) {
            meowMediaPlayer!!.start()
            val index = random.nextInt(imageList.size)
            iv_container.setImageResource(imageList[index])
            iv_container.tag = index
        }
    }

    override fun onResume() {
        super.onResume()
        shakeHandler.registerListener()
    }

    override fun onPause() {
        super.onPause()
        shakeHandler.unregisterListener()
    }

    private fun loadImages(){
        imageList.add(R.drawable.cat2)
        imageList.add(R.drawable.cat3)
        imageList.add(R.drawable.cat4)
    }
    private fun setSound() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val soundId = sharedPref.getInt(getString(R.string.shared_pref_key_select_sound), R.raw.regular_meow)
        meowMediaPlayer = MediaPlayer.create(applicationContext, soundId)
    }
}

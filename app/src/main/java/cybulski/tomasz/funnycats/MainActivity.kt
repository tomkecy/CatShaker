package cybulski.tomasz.funnycats

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.media.MediaPlayer
import kotlinx.android.synthetic.main.activity_main.*
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
        meowMediaPlayer = MediaPlayer.create(applicationContext, R.raw.meow)
        shakeHandler.setListener(this)
        shakeHandler.initialize(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onShake() {
        if(!meowMediaPlayer!!.isPlaying) {
            meowMediaPlayer!!.start()
            val index = random.nextInt(imageList.size)
            iv_container.setImageResource(imageList[index])
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
        imageList.add(R.drawable.cat1)
        imageList.add(R.drawable.cat2)
        imageList.add(R.drawable.cat3)
        imageList.add(R.drawable.cat4)
    }
}

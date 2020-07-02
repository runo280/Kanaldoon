package app.runo.kanaldoon

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import app.runo.kanaldoon.model.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class ForegroundService : Service() {
    private val CHANNEL_ID = "Kanaldoon"

    companion object {
        fun startService(context: Context, id: Int) {
            val startIntent = Intent(context, ForegroundService::class.java)
            startIntent.putExtra("id", id)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, ForegroundService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //do heavy work on a background thread
        val channelId = intent?.getIntExtra("id", -1)
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Extracting info")
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.get(application).channelStore()
            channelId?.let {
                val channel = db.getChannel(it)
                val html = getHtml(channel.username)
                val title = getTitle(html)
                if ("none" != title) {
                    val img = getAvatarUrl(html)
                    channel.title = title
                    channel.avatarUrl = img
                    db.updateChannel(channel)
                }
                stopForeground(true)
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Kanaldoon Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}

fun getHtml(username: String) = Jsoup.connect("https://t.me/$username").get()
fun getTitle(html: Document): String {
    val all = html.select("div.tgme_page_title")
    return if (all.size > 0)
        all.first().text().trim()
    else
        "none"
}

fun getAvatarUrl(html: Document): String {
    val all = html.select("img.tgme_page_photo_image")
    return if (all.size > 0)
        all.first().attr("src")
    else
        "none"
}

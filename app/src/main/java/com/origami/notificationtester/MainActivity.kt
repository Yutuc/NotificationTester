package com.origami.notificationtester

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RemoteViews
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        var active = false
    }

    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder

    private val channelId = "com.origami.notificationtester"
    private val description = "Test notification"
    private val notificationID = 1999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        show_notification_button_main.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val contentView = RemoteViews(packageName,R.layout.notification_layout)

            //default title and description
            contentView.setTextViewText(R.id.notification_title,"Origami Technologies")
            contentView.setTextViewText(R.id.notification_description,"Test notification")

            if(!active){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.GREEN
                    notificationChannel.enableVibration(false)
                    notificationManager.createNotificationChannel(notificationChannel)

                    builder = Notification.Builder(this, channelId)
                        .setContent(contentView)
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher_round))
                        .setContentIntent(pendingIntent)
                }
                else {
                    builder = Notification.Builder(this)
                        .setContent(contentView)
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher_round))
                        .setContentIntent(pendingIntent)
                }
                notificationManager.notify(notificationID, builder.build())
            }
        }
    }

    override fun onStart() {
        active = true
        super.onStart()
    }

    override fun onStop() {
        active = false
        super.onStop()
    }
}

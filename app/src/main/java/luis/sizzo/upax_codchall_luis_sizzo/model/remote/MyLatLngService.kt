package luis.sizzo.upax_codchall_luis_sizzo.model.remote

import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import luis.sizzo.upax_codchall_luis_sizzo.common.CheckConnection
import luis.sizzo.upax_codchall_luis_sizzo.view.MainViewActivity
import java.text.SimpleDateFormat
import java.util.*


class MyLatLngService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    fun startNotificationListener() {
        //start's a new thread
        try {
            val handler1 = Handler()
            val timer1 = Timer()
            val task: TimerTask = object : TimerTask() {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun run() {
                    handler1.post(Runnable {
                        try {
                            onPermisosConcedidos()
                        } catch (e: Exception) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG)
                                .show()
                        }
                    })
                }
            }
            timer1.schedule(task, 0, 5000)
        } catch (e: Exception) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startNotificationListener()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun onPermisosConcedidos() {
        // Hasta aquí sabemos que los permisos ya están concedidos
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    val catchinData = GlobalScope.async {
                        imprimirUbicacion(location)
                    }

                }
        } catch (e: SecurityException) {
            Log.d("", "Tal vez no solicitaste permiso antes")
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun imprimirUbicacion(ubicacion: Location) {
        try {
            if (CheckConnection().isConnected()) {
                insertLatLngSuspend(LatLng(ubicacion.latitude, ubicacion.longitude))
            }
        }catch (e:Exception) {
            Log.d("Error Service", "Este es el result ${e.toString()}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun insertLatLngSuspend(location: LatLng): String {
        var result: String = ""

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val items = HashMap<String, Any>()
        items.put("date", currentDate)
        items.put("lat", "${location.latitude}")
        items.put("lng", "${location.longitude}")
        val fireStoreDatabase = FirebaseFirestore.getInstance()
        fireStoreDatabase.collection("location")
            .add(items)
            .addOnSuccessListener {
                result = "Insertado con exito"
//                createNotification(
//                    this,
//                    "Aviso de Localizacion",
//                    "Localizacion guardada",
//                )
            }
            .addOnFailureListener { exception ->
                result = "${exception.message.toString()}"
            }.await()

        Log.d("", "Este es el result ${result}")
        return result
    }

//    fun ShowNotification(titulo: String?, mensaje: String?, context: Context) {
//
//        lateinit var notifManager: NotificationManager
//        val YES_ACTION = "YES_ACTION"
//        val NO_ACTION = "NO_ACTION"
//
//        val NOTIFY_ID = 101
//        // ID of notification
//        val id = context.getString(context.R.string.default_notification_channel_id) // default_channel_id
//        val title =
//            context.getString(R.string.default_notification_channel_title) // Default Channel
//        val intent: Intent
//        val pendingIntent: PendingIntent
//        val builder: NotificationCompat.Builder
//        val intentMin = Intent(this, MainViewActivity::class.java)
//        intentMin.action = YES_ACTION
//        intentMin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntentMin =
//            PendingIntent.getActivity(this, 0, intentMin, PendingIntent.FLAG_ONE_SHOT)
//        if (notifManager == null) {
//            notifManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            var mChannel: NotificationChannel = notifManager.getNotificationChannel(id)
//            if (mChannel == null) {
//                mChannel = NotificationChannel(id, title, importance)
//                mChannel.enableVibration(true)
//                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
//                notifManager.createNotificationChannel(mChannel)
//            }
//            builder = NotificationCompat.Builder(context, id)
//            intent = Intent(context, MainViewActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
//            builder.setContentTitle(titulo)
//                .setSmallIcon(context.R.mipmap.ic_launcher) // required
//                .setContentText(mensaje) // required
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
//                .setTicker(titulo)
//                .addAction(R.mipmap.yesicon, "VER RECORDATORIO", pendingIntentMin)
//                .setVibrate(longArrayOf(500, 500))
//
//            /*Intent noReceive = new Intent();
//            noReceive.setAction(NO_ACTION);
//            PendingIntent pendingIntentNo = PendingIntent.getBroadcast(this, 12345, noReceive, PendingIntent.FLAG_UPDATE_CURRENT);
//            builder.addAction(R.mipmap.noicon, "No", pendingIntentNo);*/
//        } else {
//            builder = NotificationCompat.Builder(context, id)
//            intent = Intent(context, MainViewActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
//            builder.setContentTitle(titulo)
//                .setSmallIcon(R.mipmap.ic_launcher) // required
//                .setContentText(mensaje) // required
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
//                .setTicker(titulo)
//                .addAction(R.mipmap.yesicon, "VER RECORDATORIO", pendingIntentMin)
//                .setVibrate(longArrayOf(500, 500))
//                .setPriority(Notification.PRIORITY_HIGH)
//            //No intent
//            val noReceive = Intent()
//            noReceive.setAction(NO_ACTION)
//            val pendingIntentNo = PendingIntent.getBroadcast(
//                this,
//                12345,
//                noReceive,
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//            builder.addAction(R.mipmap.noicon, "No", pendingIntentNo)
//        }
//        val notification = builder.build()
//        notifManager.notify(NOTIFY_ID, notification)
//        startForeground(1, notification)
//
//
//        //Toast.makeText(getApplicationContext(), "Entra a la notificacion", Toast.LENGTH_LONG).show();
//        //the notification is not showing
//    }

}
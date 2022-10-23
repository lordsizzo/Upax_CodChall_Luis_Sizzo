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
            }
            .addOnFailureListener { exception ->
                result = exception.message.toString()
            }.await()

        Log.d("", "Este es el result ${result}")
        return result
    }
}
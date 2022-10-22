package luis.sizzo.upax_codchall_luis_sizzo.model.res

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import luis.sizzo.upax_codchall_luis_sizzo.common.CheckConnection
import luis.sizzo.upax_codchall_luis_sizzo.model.LatLngFirebaseModel
import luis.sizzo.upax_codchall_luis_sizzo.model.UIViewState
import luis.sizzo.upax_codchall_luis_sizzo.model.local.Movies_Dao
import luis.sizzo.upax_codchall_luis_sizzo.model.local.movies_local.MoviesLocalEntity
import luis.sizzo.upax_codchall_luis_sizzo.model.remote.MoviesResults
import luis.sizzo.upax_codchall_luis_sizzo.model.remote.RemoteAPIConnection
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

interface Repository {
    fun getMovies(search: String): Flow<UIViewState>
    fun insertLatLng(search: LatLng): Flow<UIViewState>
    fun returnLatLng(): Flow<UIViewState>
    fun insertImage(uri: ArrayList<Uri>): Flow<UIViewState>
}

class RepositoryImpl @Inject constructor(
    private val service: RemoteAPIConnection,
    private val movieDato: Movies_Dao,
) : Repository {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getMovies(section: String)= flow {
        emit(UIViewState.LOADING)
        try {
            if (CheckConnection().isConnected()) {
                val response = service.getMovies(section)
                if (response.isSuccessful) {
                    response.body()?.let { result ->
                        movieDato.deleteMoviesInfo()
                        result.results.forEach {
                            val sat = MoviesLocalEntity(
                                it.id,
                                it.title,
                                section,
                                it.popularity,
                                it.poster_path,
                                it.release_date
                            )
                            movieDato.insertAllMoviesInfo(sat)
                        }
                        val cache = movieDato.getAllMoviesInfoBySection(section)
                        val items: ArrayList<MoviesResults> = ArrayList()
                        Log.d(ContentValues.TAG, "Error getMovies: ${cache}")
                        emit(UIViewState.SUCCESS(cache))
                    } ?: throw Exception("Response null")
                } else {
                    throw Exception(response.errorBody().toString())
                }
            } else {
                val cache = movieDato.getAllMoviesInfoBySection(section)
                if (!cache.isEmpty()) {
                    emit(UIViewState.SUCCESS(cache))
                } else {
                    emit(UIViewState.ERROR("Cache failed"))
                }
            }
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "Error getMovies: ${e.message}")
            emit(UIViewState.ERROR(e.toString()))
        }
    }

    override fun insertLatLng(location: LatLng)= flow {
        emit(UIViewState.LOADING)

        try {
            if (CheckConnection().isConnected()) {
                val catchinData = GlobalScope.async {
                    insertLatLngSuspend(location)
                }
                Log.d("insertLatLng", "DocumentSnapshot data: ${catchinData.await()}")
                emit(UIViewState.SUCCESS(catchinData.await()))

            } else {
                emit(UIViewState.ERROR("No tienes internet"))
            }

        }catch (e:Exception) {
            emit(UIViewState.ERROR(e.toString()))
        }
    }
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
                result = "${exception.message.toString()}"
            }.await()
        return result
    }

    override fun returnLatLng()= flow {
        emit(UIViewState.LOADING)

        try {
            if (CheckConnection().isConnected()) {
                Log.d("getReturnLatLng", "llega aqui")

                emit(UIViewState.SUCCESS(getReturnLatLng()))
            } else {
                emit(UIViewState.ERROR("No tienes internet"))
            }



        }catch (e:Exception) {
            emit(UIViewState.ERROR(e.toString()))
        }
    }

    override fun insertImage(uri: ArrayList<Uri>)= flow {
        emit(UIViewState.LOADING)

        try {
            if (CheckConnection().isConnected()) {
                val catchinData = GlobalScope.async {
                    uri.forEach {
                        insertImageToFirebase(it)
                    }
                }
                Log.d("insertImage", "DocumentSnapshot data: ${catchinData.await()}")
                emit(UIViewState.SUCCESS(catchinData.await()))

            } else {
                emit(UIViewState.ERROR("No tienes internet"))
            }

        }catch (e:Exception) {
            emit(UIViewState.ERROR(e.toString()))
        }
    }

    private suspend fun getReturnLatLng(): ArrayList<LatLngFirebaseModel>{
        var items: ArrayList<LatLngFirebaseModel> = ArrayList<LatLngFirebaseModel>()
        Log.d("getReturnLatLng", "Entra aqui")
        val fireStoreDatabase = FirebaseFirestore.getInstance()
        fireStoreDatabase.collection("location")
            .get()
            .addOnSuccessListener { document ->
                val user = document

                if (user != null) {

                    document.documents.forEach {
                        val model = LatLngFirebaseModel(it.get("date").toString(), it.get("lat").toString(), it.get("lng").toString())
                        items.add(model)
                    }
                    Log.d("getReturnLatLng", "DocumentSnapshot data: ${items}")
                }
            }
            .addOnFailureListener {
                Log.d("getReturnLatLng", "DocumentSnapshot data: ${it.toString()}")
            }.await()
        return items
    }


    private suspend fun insertImageToFirebase(uri: Uri): String {
        var result: String = ""
        val storageRef = FirebaseStorage.getInstance().reference
        Log.d("URI", uri.toString())
        val sdf = SimpleDateFormat("ddMyyyyhhmmss")
        val currentDate = sdf.format(Date())
        val fileName = currentDate+"_upax-luis-sizzo"+".jpg"

        val task = storageRef.child("upax-luis-sizzo-testing/$fileName").putFile(uri)
        task.addOnSuccessListener {
            result = "Imagen subida con exito"
        }.addOnFailureListener {
            result = "Fallo al subir la imagen: ${it.printStackTrace()}"
        }.await()
        return result
    }
}
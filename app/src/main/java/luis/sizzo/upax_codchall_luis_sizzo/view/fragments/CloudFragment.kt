package luis.sizzo.upax_codchall_luis_sizzo.view.fragments

import android.annotation.SuppressLint
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import luis.sizzo.upax_codchall_luis_sizzo.R
import luis.sizzo.upax_codchall_luis_sizzo.common.*
import luis.sizzo.upax_codchall_luis_sizzo.databinding.FragmentCloudBinding
import luis.sizzo.upax_codchall_luis_sizzo.model.LatLngFirebaseModel
import luis.sizzo.upax_codchall_luis_sizzo.model.UIViewState
import luis.sizzo.upax_codchall_luis_sizzo.model.remote.MoviesObject
import luis.sizzo.upax_codchall_luis_sizzo.view.adapters.MoviesAdapter
import luis.sizzo.upax_codchall_luis_sizzo.view_model.view_model_fragments.CloudFragmentViewModel
import luis.sizzo.upax_codchall_luis_sizzo.view_model.view_model_fragments.MovieFragmentViewModel
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CloudFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentCloudBinding
    private val viewModel: CloudFragmentViewModel by lazy {
        ViewModelProvider(this).get(CloudFragmentViewModel::class.java)
    }
    lateinit var mapa: GoogleMap


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCloudBinding.inflate(inflater, container, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        if (!Utilities.isStoragePermissionGranted(requireContext())) {
            Dialogs().bottomDialogCerrarSesion(requireContext(), requireActivity())
        }else{
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }

        viewModel.firebaseCloudResponse.observe(viewLifecycleOwner){ latLngResponse ->
            when(latLngResponse){
                is UIViewState.LOADING ->{
                    Log.d("CloudFragment", "Cargando")
                    requireActivity().toast("Cargando... espere")
                }
                is UIViewState.SUCCESS<*> ->{

                    Log.d("CloudFragment", "${latLngResponse.response}")
                    requireActivity().toast("Esta es la respuesta: ${latLngResponse.response}")


                }
                is UIViewState.ERROR ->{
                    requireActivity().toast("${latLngResponse.error}")
                }
            }
        }

        viewModel.getFirebaseCloudResponse.observe(viewLifecycleOwner){ latLngResponse ->
            when(latLngResponse){
                is UIViewState.LOADING ->{
                    Log.d("CloudFragment", "Cargando")
                    requireActivity().toast("Cargando... espere")
                }
                is UIViewState.SUCCESS<*> ->{
                    val listLatLng = latLngResponse.response as List<LatLngFirebaseModel>
                    listLatLng.let {list ->
                        list.forEach {
                            Log.d("CloudFragment", "${it.lat.toDouble()} - ${it.lng.toDouble()}")
                            addMarkersKeepItInFirebase(LatLng(it.lat.toDouble(), it.lng.toDouble()), it.date)
                        }
                    }
                }
                is UIViewState.ERROR ->{
                    requireActivity().toast("${latLngResponse.error}")
                }
            }
        }

        viewModel.returnLatLng()

        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        try {
            mapa = map
            mapa.isMyLocationEnabled = true
            mapa.uiSettings.isZoomControlsEnabled = false
            mapa.uiSettings.isMyLocationButtonEnabled = false
            mapa.uiSettings.isCompassEnabled = false
            mapa.uiSettings.isRotateGesturesEnabled = false
            mapa.uiSettings.isZoomGesturesEnabled = true

            mapa.clickMap{ loc ->

                mapa.clear()

                val geocoder =  Geocoder(requireContext(), Locale.getDefault());
                var addresses: List<Address> =
                    geocoder.getFromLocation(loc.latitude, loc.longitude, 1)!!
                binding.tvStreet.text = "Calle: ${addresses[0].thoroughfare} ${addresses[0].subThoroughfare}"
                binding.tvCity.text = "Ciudad: ${addresses[0].locality}"
                binding.tvPostalCode.text = "C.P.: ${addresses[0].postalCode}"
                binding.tvState.text = "Estado: ${addresses[0].adminArea}"
                binding.tvCountry.text = "País: ${addresses[0].countryName}"

                val miMarker: View = (requireContext().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                    R.layout.marker_location,
                    null)

                mapa.addMarker(
                    MarkerOptions()
                        .position(loc)
                        .icon(BitmapDescriptorFactory.fromBitmap(Utilities.createDrawableFromView(requireContext(), miMarker)!!))
                        .title("Estoy aqui"))

                viewModel.insertLatLng(loc)
                binding.lyAddress.fadeVisibility(View.VISIBLE)
            }

            binding.fabLocation.click {
                changeCamera(LatLng(20.66682, -103.39182))
            }
        }catch (e: Exception){
            requireActivity().toast(e.toString())
        }
    }


    private val CAMERA_ZOOM: Float = 16.0F;
    private val CAMERA_TILT: Float = 0.0F;
    private var CAMERA_BEARING: Float = 90.0F;
    fun changeCamera(location: LatLng){
        try {



            mapa.clear()

            val geocoder =  Geocoder(requireContext(), Locale.getDefault());
            var addresses: List<Address> =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)!!
            binding.tvStreet.text = "Calle: ${addresses[0].thoroughfare} ${addresses[0].subThoroughfare}"
            binding.tvCity.text = "Ciudad: ${addresses[0].locality}"
            binding.tvPostalCode.text = "C.P.: ${addresses[0].postalCode}"
            binding.tvState.text = "Estado: ${addresses[0].adminArea}"
            binding.tvCountry.text = "País: ${addresses[0].countryName}"

            val miMarker: View = (requireContext().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.marker_location,
                null)

            mapa.addMarker(
                MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromBitmap(Utilities.createDrawableFromView(requireContext(), miMarker)!!))
                    .title("Estoy aqui"))

            binding.lyAddress.fadeVisibility(View.VISIBLE)


            val cameraPosition = CameraPosition.Builder()
                .target(location) // Sets the center of the map to Mountain View
                .zoom(CAMERA_ZOOM)
                .bearing(CAMERA_BEARING)         // Sets the orientation of the camera to east
                .tilt(CAMERA_TILT)            // Sets the tilt of the camera to 30 degrees
                .build()
            mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            viewModel.insertLatLng(location)

        }catch (e: Exception){
            requireContext().toast(e.toString())
            //Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun addMarkersKeepItInFirebase(loc: LatLng, date: String){
        val miMarker: View = (requireContext().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.marker_location,
            null)

        mapa.addMarker(
            MarkerOptions()
                .position(loc)
                .icon(BitmapDescriptorFactory.fromBitmap(Utilities.createDrawableFromView(requireContext(), miMarker)!!))
                .title(date))

        val cameraPosition = CameraPosition.Builder()
            .target(loc) // Sets the center of the map to Mountain View
            .zoom(CAMERA_ZOOM)
            .bearing(CAMERA_BEARING)         // Sets the orientation of the camera to east
            .tilt(CAMERA_TILT)            // Sets the tilt of the camera to 30 degrees
            .build()
        mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        Log.d("addMarkers", "$loc")
    }

}
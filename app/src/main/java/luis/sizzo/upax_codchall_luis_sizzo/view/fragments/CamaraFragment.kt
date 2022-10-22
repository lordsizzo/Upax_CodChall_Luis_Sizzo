package luis.sizzo.upax_codchall_luis_sizzo.view.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import dagger.hilt.android.AndroidEntryPoint
import luis.sizzo.upax_codchall_luis_sizzo.common.click
import luis.sizzo.upax_codchall_luis_sizzo.common.toast
import luis.sizzo.upax_codchall_luis_sizzo.databinding.FragmentCamaraBinding
import luis.sizzo.upax_codchall_luis_sizzo.model.LatLngFirebaseModel
import luis.sizzo.upax_codchall_luis_sizzo.model.UIViewState
import luis.sizzo.upax_codchall_luis_sizzo.view_model.view_model_fragments.CamaraFragmentViewModel


@AndroidEntryPoint
class CamaraFragment : Fragment() {

    var position = 0
    val GALLERY_REQUEST_CODE = 123
    lateinit var binding: FragmentCamaraBinding
    private val viewModel: CamaraFragmentViewModel by lazy {
        ViewModelProvider(this).get(CamaraFragmentViewModel::class.java)
    }
    var mArrayUri: ArrayList<Uri> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCamaraBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        binding.btnFotos.click {
            selectImageFromGallery()
        }
        binding.btnSubir.click {
            if(mArrayUri.size>0){
                viewModel.insertImageToFirebase(mArrayUri)
            }else{
                requireContext().toast("Selecciona al menos una imagen")
            }
        }
        viewModel.insertImageToFirebase.observe(viewLifecycleOwner) { latLngResponse ->
            when (latLngResponse) {
                is UIViewState.LOADING -> {
                    Log.d("CamaraFragment", "Cargando")
                    requireActivity().toast("Cargando... espere")
                }
                is UIViewState.SUCCESS<*> -> {

                    Log.d("CamaraFragment", "${latLngResponse.response}")
                    requireActivity().toast("Esta es la respuesta: ${latLngResponse.response}")

                }
                is UIViewState.ERROR -> {
                    requireActivity().toast("${latLngResponse.error}")
                }
            }
        }
    }

    private fun selectImageFromGallery() {

//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//
//        startActivityForResult(
//            Intent.createChooser(
//                intent,
//                "Please select..."
//            ),
//            GALLERY_REQUEST_CODE
//        )

        // initialising intent
        val intent = Intent()

        // setting type to select to be image
        intent.type = "image/*"

        // allowing multiple image to be selected
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE)
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        // When an Image is picked
//        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
//            // Get the Image from data
//            if (data.clipData != null) {
//                val mClipData = data.clipData
//                val cout = data.clipData!!.itemCount
//                for (i in 0 until cout) {
//                    // adding imageuri in array
//                    val imageurl = data.clipData!!.getItemAt(i).uri
//                    mArrayUri?.add(imageurl)
//                }
//                // setting 1st selected image into image switcher
//                binding.image.setImageURI(mArrayUri?.get(0))
//                position = 0
//            } else {
//                val imageurl = data.data
//                if (imageurl != null) {
//                    mArrayUri?.add(imageurl)
//                }
//                binding.image.setImageURI(mArrayUri?.get(0))
//                position = 0
//            }
//        } else {
//            // show this if no image is selected
//            Toast.makeText(requireContext(), "You haven't picked Image", Toast.LENGTH_LONG).show()
//        }
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
        // Get the Uri of data
            mArrayUri.clear()
            val cout = data.clipData!!.itemCount
            for (i in 0 until cout) {
                // adding imageuri in array
                val imageurl = data.clipData!!.getItemAt(i).uri
                mArrayUri.add(imageurl)
            }
            Log.d("onActivityResultCamara", "${mArrayUri}")

            val file_uri = data.data
            if (file_uri != null) {

            }
        }
    }
}
package luis.sizzo.upax_codchall_luis_sizzo.view.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import luis.sizzo.upax_codchall_luis_sizzo.common.click
import luis.sizzo.upax_codchall_luis_sizzo.common.toast
import luis.sizzo.upax_codchall_luis_sizzo.databinding.FragmentCamaraBinding
import luis.sizzo.upax_codchall_luis_sizzo.model.UIViewState
import luis.sizzo.upax_codchall_luis_sizzo.view_model.view_model_fragments.CamaraFragmentViewModel


@AndroidEntryPoint
class CamaraFragment : Fragment() {

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
                    binding.btnFotos.isEnabled = false
                    binding.btnSubir.isEnabled = false
                    requireActivity().toast("Cargando... espere")
                }
                is UIViewState.SUCCESS<*> -> {

                    Log.d("CamaraFragment", "${latLngResponse.response}")
                    binding.container.removeAllViews()
                    binding.btnFotos.isEnabled = true
                    binding.btnSubir.isEnabled = true

                }
                is UIViewState.ERROR -> {
                    requireActivity().toast("${latLngResponse.error}")
                    binding.btnFotos.isEnabled = true
                    binding.btnSubir.isEnabled = true
                }
            }
        }
    }

    private fun selectImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
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
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            mArrayUri.clear()
            val cout = data.clipData!!.itemCount
            binding.container.removeAllViews();
            for (i in 0 until cout) {
                val imageurl = data.clipData!!.getItemAt(i).uri
                val imageView = ImageView(requireContext())
                imageView.layoutParams = LinearLayout.LayoutParams(400, 400)
                imageView.x = 20F
                imageView.y = 20F
                imageView.setImageURI(imageurl)

                binding.container.addView(imageView)
                mArrayUri.add(imageurl)
            }
        }
    }
}
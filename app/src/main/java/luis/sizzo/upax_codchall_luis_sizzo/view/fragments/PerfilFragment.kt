package luis.sizzo.upax_codchall_luis_sizzo.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import luis.sizzo.upax_codchall_luis_sizzo.databinding.FragmentPerfilBinding
import luis.sizzo.upax_codchall_luis_sizzo.view_model.view_model_fragments.PerfilFragmentViewModel

@AndroidEntryPoint
class PerfilFragment : Fragment() {
    lateinit var binding: FragmentPerfilBinding
    private lateinit var viewModel: PerfilFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)

        return binding.root
    }

}
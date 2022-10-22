package luis.sizzo.upax_codchall_luis_sizzo.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import luis.sizzo.upax_codchall_luis_sizzo.common.settingsGrid
import luis.sizzo.upax_codchall_luis_sizzo.common.toast
import luis.sizzo.upax_codchall_luis_sizzo.databinding.FragmentMovieBinding
import luis.sizzo.upax_codchall_luis_sizzo.model.UIViewState
import luis.sizzo.upax_codchall_luis_sizzo.model.remote.MoviesObject
import luis.sizzo.upax_codchall_luis_sizzo.view.adapters.MoviesAdapter
import luis.sizzo.upax_codchall_luis_sizzo.view_model.view_model_fragments.MovieFragmentViewModel

@AndroidEntryPoint
class MovieFragment : Fragment() {
    lateinit var binding: FragmentMovieBinding
    private val viewModel: MovieFragmentViewModel by lazy {
        ViewModelProvider(this).get(MovieFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        viewModel.moviesPopularResponse.observe(viewLifecycleOwner){ popularMovies ->
            when(popularMovies){
                is UIViewState.LOADING ->{
                    Log.d("MovieFragment", "Cargando")
                    requireActivity().toast("Cargando... espere")
                }
                is UIViewState.SUCCESS<*> ->{
                    val categories = popularMovies.response as? MoviesObject
                    categories?.let { moviesObject ->
                        MoviesAdapter(moviesObject.results).apply {
                            binding.listMoviesRecyclerView.settingsGrid(this)
                        }

                    } ?: showError("Error at casting")
                }
                is UIViewState.ERROR ->{
                    requireActivity().toast("${popularMovies.error}")
                }
            }
        }
        viewModel.getMovies("popular")

        viewModel.moviesTopRatedResponse.observe(viewLifecycleOwner){ popularMovies ->
            when(popularMovies){
                is UIViewState.LOADING ->{
                    Log.d("MovieFragment", "Cargando")
                    requireActivity().toast("Cargando... espere")
                }
                is UIViewState.SUCCESS<*> ->{
                    val categories = popularMovies.response as? MoviesObject
                    categories?.let { moviesObject ->
                        MoviesAdapter(moviesObject.results).apply {
                            binding.listMoviesMasCalificadasRecyclerView.settingsGrid(this)
                        }

                    } ?: showError("Error al buscar")
                }
                is UIViewState.ERROR ->{
                    requireActivity().toast("${popularMovies.error}")
                }
            }
        }
        viewModel.getMovies("top_rated")
    }

    private fun showError(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error occurred")
            .setMessage(message)
            .setNegativeButton("CLOSE") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
package luis.sizzo.upax_codchall_luis_sizzo.view_model.view_model_fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import luis.sizzo.upax_codchall_luis_sizzo.model.UIViewState
import luis.sizzo.upax_codchall_luis_sizzo.model.res.Repository
import javax.inject.Inject

@HiltViewModel
class MovieFragmentViewModel @Inject constructor(
    private val repository: Repository,
    private val coroutineScope: CoroutineScope,
) : ViewModel() {

    private val _moviesPopularResponse = MutableLiveData<UIViewState>()
    val moviesPopularResponse: MutableLiveData<UIViewState>
        get() = _moviesPopularResponse

    private val _moviesTopRatedResponse = MutableLiveData<UIViewState>()
    val moviesTopRatedResponse: MutableLiveData<UIViewState>
        get() = _moviesTopRatedResponse

    fun getMovies(section: String) {
        coroutineScope.launch {
            when(section){
                "popular" ->{
                    repository.getMovies(section).collect { state_result ->
                        _moviesPopularResponse.postValue(state_result)
                    }
                }
                "top_rated" ->{
                    repository.getMovies(section).collect { state_result ->
                        _moviesTopRatedResponse.postValue(state_result)
                    }
                }
                "" ->{

                }
            }

        }
    }

}
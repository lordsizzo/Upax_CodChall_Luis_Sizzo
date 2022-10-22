package luis.sizzo.upax_codchall_luis_sizzo.view_model

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import luis.sizzo.upax_codchall_luis_sizzo.model.UIViewState
import luis.sizzo.upax_codchall_luis_sizzo.model.res.Repository
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: Repository,
    private val coroutineScope: CoroutineScope,
) : ViewModel() {

    private val _moviesResponse = MutableLiveData<UIViewState>()
    val moviesResponse: MutableLiveData<UIViewState>
        get() = _moviesResponse

    fun getSearchUser(section: String) {
        coroutineScope.launch {
            repository.getMovies(section).collect { state_result ->
                _moviesResponse.postValue(state_result)
            }
        }
    }
}
package luis.sizzo.upax_codchall_luis_sizzo.view_model.view_model_fragments

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import luis.sizzo.upax_codchall_luis_sizzo.model.res.Repository
import javax.inject.Inject

@HiltViewModel
class PerfilFragmentViewModel @Inject constructor(
    private val repository: Repository,
    private val coroutineScope: CoroutineScope,
) : ViewModel() {

}
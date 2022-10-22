package luis.sizzo.upax_codchall_luis_sizzo.view_model.view_model_fragments

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import luis.sizzo.upax_codchall_luis_sizzo.model.UIViewState
import luis.sizzo.upax_codchall_luis_sizzo.model.res.Repository
import javax.inject.Inject

@HiltViewModel
class CamaraFragmentViewModel @Inject constructor(
    private val repository: Repository,
    private val coroutineScope: CoroutineScope,
) : ViewModel() {

    private val _insertImageToFirebase = MutableLiveData<UIViewState>()
    val insertImageToFirebase: MutableLiveData<UIViewState>
        get() = _insertImageToFirebase

    fun insertImageToFirebase(uri: ArrayList<Uri>) {
        coroutineScope.launch {
            repository.insertImage(uri).collect { state_result ->
                insertImageToFirebase.postValue(state_result)
            }
        }
    }

}
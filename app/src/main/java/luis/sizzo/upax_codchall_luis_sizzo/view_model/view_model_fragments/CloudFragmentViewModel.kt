package luis.sizzo.upax_codchall_luis_sizzo.view_model.view_model_fragments

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
class CloudFragmentViewModel @Inject constructor(
    private val repository: Repository,
    private val coroutineScope: CoroutineScope,
) : ViewModel() {
    private val _firebaseCloudResponse = MutableLiveData<UIViewState>()
    val firebaseCloudResponse: MutableLiveData<UIViewState>
        get() = _firebaseCloudResponse

    private val _getFirebaseCloudResponse = MutableLiveData<UIViewState>()
    val getFirebaseCloudResponse: MutableLiveData<UIViewState>
        get() = _getFirebaseCloudResponse

    fun insertLatLng(latLng: LatLng) {
        coroutineScope.launch {
            repository.insertLatLng(latLng).collect { state_result ->
                _firebaseCloudResponse.postValue(state_result)
            }
        }
    }

    fun returnLatLng() {
        coroutineScope.launch {
            repository.returnLatLng().collect { state_result ->
                _getFirebaseCloudResponse.postValue(state_result)
            }
        }
    }
}
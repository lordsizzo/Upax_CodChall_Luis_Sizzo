package luis.sizzo.upax_codchall_luis_sizzo.model

sealed class UIViewState {
    object LOADING : UIViewState()
    class SUCCESS<T>(val response : T) : UIViewState()
    class ERROR(val error: String) : UIViewState()
}
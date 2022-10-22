package luis.sizzo.upax_codchall_luis_sizzo.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import luis.sizzo.upax_codchall_luis_sizzo.databinding.BottomSheetPermissionsBinding

class Dialogs {

    fun showError(context: Context, message: String) {
        AlertDialog.Builder(context)
            .setTitle("Ocurrio un error")
            .setMessage(message)
            .setNegativeButton("CLOSE") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    companion object{
        var PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        var PERMISSION_ALL = 1
    }

    fun bottomDialogCerrarSesion(context: Context, activity: Activity) {

        var bindingLogout: BottomSheetPermissionsBinding =
            BottomSheetPermissionsBinding.inflate(LayoutInflater.from(context))
        val dialogBSDPermissions = BottomSheetDialog(context)
        dialogBSDPermissions.setCancelable(false)

        bindingLogout.btnCancelar.setOnClickListener {
            (context as Activity).finish()
            dialogBSDPermissions.dismiss()
        }
        bindingLogout.btnAceptarPermisos.setOnClickListener {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL)
            dialogBSDPermissions.dismiss()
        }
        dialogBSDPermissions.setContentView(bindingLogout.root)
        dialogBSDPermissions.show()
    }
}
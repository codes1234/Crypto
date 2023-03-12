package com.akguptaboy.crypto


import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.akguptaboy.crypto.databinding.ActivityMainBinding
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.OnSuccessListener


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment=supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        val navController = navHostFragment!!.findNavController()

        val popupMenu = PopupMenu(this,null)
        popupMenu.inflate(R.menu.bottom_nav_item)
        binding.bottomBar.setupWithNavController(popupMenu.menu,navController)

    }

    override fun onResume() {
        super.onResume()
        updateApps()
    }

    fun updateApps() {
        val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask: com.google.android.play.core.tasks.Task<AppUpdateInfo> =
            appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { result ->
            if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                //                requestUpdate(result);
                val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                alertDialogBuilder.setTitle("Update ")
                alertDialogBuilder.setCancelable(false)
                alertDialogBuilder.setIcon(R.drawable.plays)
                alertDialogBuilder.setMessage(
                    "Scratch App recommends that you update " +
                            "to " +
                            "the " +
                            "latest version for a seamless & enhanced performance of the app."
                )
                alertDialogBuilder.setPositiveButton("Update",
                    DialogInterface.OnClickListener { dialog, id ->
                        try {
                            startActivity(
                                Intent(
                                    "android.intent.action.VIEW",
                                    Uri.parse("market://details?id=$packageName")
                                )
                            )
                        } catch (e: ActivityNotFoundException) {
                            startActivity(
                                Intent(
                                    "android.intent.action.VIEW",
                                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                                )
                            )
                        }
                    })
                alertDialogBuilder.show()
            } else {
            }
        }
    }
}


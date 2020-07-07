package me.yaoandy107.triplecoupon

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import me.yaoandy107.triplecoupon.ui.filter.FilterFragment
import me.yaoandy107.triplecoupon.ui.store.StoreFragment
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private val filterFragment: FilterFragment = FilterFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        bottom_navigation.setupWithNavController(navController)
        setSupportActionBar(toolbar)

        getLocationPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            this
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Snackbar
            .make(findViewById(android.R.id.content), "無法取得您當前位置，因此無法依距離排序", Snackbar.LENGTH_LONG)
            .show()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    @AfterPermissionGranted(RC_LOCATION_FINE_PERM)
    fun getLocationPermission() {
        val perms = arrayOf<String>(
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (EasyPermissions.hasPermissions(this, *perms)) {
            // Have permissions, do the thing!
        } else {
            // Ask for permissions
            EasyPermissions.requestPermissions(
                this,
                "此 App 需要開啟權限才能正常使用",
                RC_LOCATION_FINE_PERM,
                *perms
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    companion object {
        private const val RC_LOCATION_FINE_PERM = 124
    }

}

//// Extension function to replace fragment
//fun AppCompatActivity.replaceFragment(fragment: Fragment) {
//    val transaction = supportFragmentManager.beginTransaction()
//    transaction.replace(R.id.fragment_container, fragment)
//    transaction.commit()
//}
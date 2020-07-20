package me.yaoandy107.triplecoupon

import android.Manifest
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var adView: AdView
    private val adSize: AdSize
        get() {
            val display = windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = ad_view_container.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavAndToolbar()

        getLocationPermission()

        setupAds()

        showInfoDialog()
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
        val perms = arrayOf(
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

    private fun setupNavAndToolbar() {
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_store,
                R.id.navigation_map
            )
        )
        toolbar.setupWithNavController(navController, appBarConfiguration)
        bottom_navigation.setupWithNavController(navController)
        setSupportActionBar(toolbar)
    }

    private fun setupAds() {
        MobileAds.initialize(this) { }
        adView = AdView(this)
        ad_view_container.addView(adView)
        loadBanner()
    }

    private fun loadBanner() {
        adView.adSize = adSize
        adView.adUnitId = AD_UNIT_ID
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun showInfoDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("注意事項")
            .setMessage(
                "請攜帶： \n" +
                        "・健保卡 和 新臺幣 1,000 元。\n" +
                        "\n領取時間：\n" +
                        "・109/7/15 ~ 109/12/31\n" +
                        "・身分證末碼(單號)：週一、三、五\n" +
                        "・身分證末碼(雙號)：週二、四\n" +
                        "・週六則單雙號均可購買\n"
            )
            .setPositiveButton("關閉") { _, _ -> }
            .show()
    }

    companion object {
        private const val RC_LOCATION_FINE_PERM = 124

        //        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111" // Debug use
        private const val AD_UNIT_ID = "ca-app-pub-5236873091720551/5185055171"
    }

}
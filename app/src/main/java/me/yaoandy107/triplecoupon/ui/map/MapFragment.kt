package me.yaoandy107.triplecoupon.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.yaoandy107.triplecoupon.R
import me.yaoandy107.triplecoupon.ui.store.StoreViewModel

class MapFragment : Fragment(), OnMapReadyCallback {

    private val storeViewModel: StoreViewModel by activityViewModels()
    private var markerOptionsList: List<MarkerOptions>? = null
    private var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map_view.onCreate(savedInstanceState)

        setupLocationService()
        map_view.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        map_view?.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_view?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map_view?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view?.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(), R.raw.style_json
            )
        )
        storeViewModel.stores.value?.forEach {
            val options = MarkerOptions()
                .position(LatLng(it.latitude.toDouble(), it.longitude.toDouble()))
                .title(it.storeNm)
                .snippet("庫存：${it.total}")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            if (it.total.toInt() <= 0) {
                options.alpha(0.3f)
            }
            map!!.addMarker(options)
        }
    }

    private fun setupLocationService() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location ->
                    val currentPosition = LatLng(location.latitude, location.longitude)
                    map?.moveCamera(CameraUpdateFactory.newLatLng(currentPosition))

                }
        }
    }

}
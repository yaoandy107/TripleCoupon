package me.yaoandy107.triplecoupon.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.location.Location
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_map.*
import me.yaoandy107.triplecoupon.R
import me.yaoandy107.triplecoupon.model.Store
import me.yaoandy107.triplecoupon.ui.store.StoreViewModel


class MapFragment : Fragment(), OnMapReadyCallback {

    private val storeViewModel: StoreViewModel by activityViewModels()
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
        setupLocationEnable()
        setupMapStyle()
        setupInfoWindowAdapter()
        setupMarkers()
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


    private fun setupLocationEnable() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map?.isMyLocationEnabled = true
        }
    }

    private fun setupMapStyle() {
        map?.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(), R.raw.style_json
            )
        )
    }

    private fun setupInfoWindowAdapter() {
        map?.setInfoWindowAdapter(object : InfoWindowAdapter {
            override fun getInfoContents(marker: Marker?): View {
                val infoView = LinearLayout(requireContext())
                infoView.orientation = LinearLayout.VERTICAL

                val title = TextView(requireContext())
                title.text = marker?.title
                title.setTextColor(Color.BLACK)
                title.setTypeface(null, Typeface.BOLD)
                title.gravity = Gravity.CENTER

                val snippet = TextView(requireContext())
                snippet.text = marker?.snippet
                snippet.setTextColor(Color.GRAY)

                infoView.addView(title)
                infoView.addView(snippet)

                return infoView
            }

            override fun getInfoWindow(p0: Marker?): View? {
                return null
            }

        })
    }

    private fun setupMarkers() {
        storeViewModel.stores.value?.forEach { store ->
            setupMarker(store)
        }
    }

    private fun setupMarker(store: Store) {
        val regex = Regex(pattern = "[0-9]{2}:[0-9]{2}-[0-9]{2}:[0-9]{2}")
        val businessTime = regex.find(store.busiTime)?.value
        val options = MarkerOptions()
            .position(LatLng(store.latitude.toDouble(), store.longitude.toDouble()))
            .title(store.storeNm)
            .snippet("庫存：${store.total}\n時間：$businessTime")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        if (store.total.toInt() <= 0) {
            options.alpha(0.3f)
        }
        map!!.addMarker(options)
    }
}
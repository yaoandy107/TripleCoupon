package me.yaoandy107.triplecoupon.ui.store

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_store.*
import me.yaoandy107.triplecoupon.R
import me.yaoandy107.triplecoupon.model.Store


class StoreFragment : Fragment() {

    private val viewModel: StoreViewModel by activityViewModels()
    private var stores: List<Store> = ArrayList()
    private var displayStores: MutableList<Store> = ArrayList()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    private lateinit var adapter: StoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchStores()
        setupLocationService()
        setupStoreList()
        setupLoadingIndicator()
    }

    private fun setupLocationService() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    currentLocation = location
                    handleDisplayStores()
                }
        }
    }

    private fun setupStoreList() {
        val layoutManager = LinearLayoutManager(context)
        adapter = StoreAdapter(displayStores)
        rv_stores.apply {
            setHasFixedSize(true)
            this.layoutManager = layoutManager
            this.adapter = this@StoreFragment.adapter
        }
        viewModel.stores.observe(viewLifecycleOwner, Observer { stores ->
            this.stores = stores
            handleDisplayStores()
            this.adapter.notifyDataSetChanged()
        })
    }

    private fun handleDisplayStores() {
        val sortedStores = stores.sortedBy {
            val targetLocation = Location("locationA")
            targetLocation.latitude = it.latitude.toDouble()
            targetLocation.longitude = it.longitude.toDouble()
            currentLocation?.distanceTo(targetLocation)
        }
        with(this.displayStores) {
            clear()
            addAll(sortedStores)
        }

    }

    private fun setupLoadingIndicator() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            swipe_refresh.isRefreshing = isLoading
        })

        swipe_refresh.setOnRefreshListener {
            viewModel.fetchStores()
        }
    }
}
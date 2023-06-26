package com.example.weatherapp.presentation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.presentation.MainViewModel
import com.example.weatherapp.presentation.model.Weather
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

private const val LOCATION_PERMISSION_ID: Int = 870989

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.searchText.doAfterTextChanged {
            viewModel.setSearchQuery(it.toString())
        }

        binding.searchLocation.setOnClickListener {
            getLocation()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResult.collect { weather ->
                    bindWeather(weather)
                }
            }
        }

        viewModel.stateView.observe(this) { weather ->
            bindWeather(weather)
        }

    }

    private fun bindWeather(weather: Weather) {
        if (!weather.isEmpty()) {
            binding.country.text = weather.countryName
            binding.description.text = weather.description
            binding.temp.text = getString(R.string.temp, weather.temp)
            binding.feelsLike.text = getString(R.string.feels_like, weather.feelsLike)
            binding.tempMin.text = getString(R.string.temp_min, weather.tempMin)
            binding.tempMax.text = getString(R.string.temp_max, weather.tempMax)
            binding.humidity.text = getString(R.string.humidity, weather.humidity)
            binding.weatherContainer.visibility = View.VISIBLE
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_PERMISSION_ID
        )
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                 //   val location: Location? = task
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list: MutableList<Address?>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                        val latitude: Float? = list?.getOrNull(0)?.latitude?.toFloat()
                        val longitude: Float? = list?.getOrNull(0)?.longitude?.toFloat()

                        if (latitude != null && longitude != null) {
                            // fetch weather by coordinates
                            viewModel.searchByCoordinates(latitude, longitude)
                        } else {
                            Snackbar.make(binding.root, "Unable to get Location", Snackbar.LENGTH_LONG)
                                .setAction("Action", null)
                                .show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}
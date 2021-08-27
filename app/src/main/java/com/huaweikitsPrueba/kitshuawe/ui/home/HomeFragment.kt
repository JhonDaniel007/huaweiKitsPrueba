package com.huaweikitsPrueba.kitshuawe.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions
import com.huaweikitsPrueba.kitshuawe.R
import com.huaweikitsPrueba.kitshuawe.databinding.FragmentHomeBinding

class HomeFragment : Fragment() , OnMapReadyCallback {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    lateinit var huaweiMap: HuaweiMap


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.idMapView.onCreate(null)
        binding.idMapView.getMapAsync(this)

    }

    override fun onMapReady(p0: HuaweiMap?) {
        Log.d("TAG", "mapa listo")
        if (p0!=null){
            huaweiMap=p0
            huaweiMap.isMyLocationEnabled = false
            val coordenadas= LatLng(2.4826211,-76.5620209)
            var algo= huaweiMap
                .addMarker(MarkerOptions().position(coordenadas)
                .title("Sena CTPI"))

/*
                huaweiMap.setOnCameraIdleListener {
                    Toast.makeText(requireContext(), "posicion", Toast.LENGTH_SHORT).show()
                    comprarPlato()
                }*/

            huaweiMap.setOnMarkerClickListener {
                Toast.makeText(requireContext(), "posicion$coordenadas", Toast.LENGTH_SHORT).show()
                comprarPlato()
            }

            Toast.makeText(requireContext(), "Algo $algo", Toast.LENGTH_LONG).show()

            huaweiMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(2.4826211,-76.5620209), 10f))

            huaweiMap.uiSettings.isMyLocationButtonEnabled = true
            huaweiMap.uiSettings.isZoomGesturesEnabled = true
            huaweiMap.uiSettings.isScrollGesturesEnabled = true
            huaweiMap.uiSettings.isTiltGesturesEnabled = true
            huaweiMap.uiSettings.isRotateGesturesEnabled = true

        }
    }
    fun comprarPlato(): Boolean {
        var alertDialog=AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Sena CTPI")
        alertDialog.setMessage("Compra plato del dia, precio 20.0000 :     "+"      "+huaweiMap.cameraPosition)
        alertDialog.setCancelable(false)
        alertDialog.setIcon(R.drawable.hwid_auth_button_normal)
        alertDialog.setPositiveButton("Comprar"){
            _,_->
            alertDialog.setCancelable(true)
            Toast.makeText(requireContext(), "Plato Comprado", Toast.LENGTH_SHORT).show()
        }
        alertDialog.setNegativeButton("Cancelar"){
            _,_->
            alertDialog.setCancelable(false)
        }
        alertDialog.create().show()
        return true
    }
}
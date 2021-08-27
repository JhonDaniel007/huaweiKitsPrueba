package com.huaweikitsPrueba.kitshuawe.ui.notifications

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions
import com.huaweikitsPrueba.kitshuawe.databinding.FragmentNotificationsBinding


class NotificationsFragment : Fragment(),OnMapReadyCallback {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null
    lateinit var huaweiMap: HuaweiMap

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root


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
            val coordenadas=LatLng(2.4826211,-76.5620209)
            var algo= huaweiMap.addMarker(
                MarkerOptions().position(coordenadas)
                    .title("Sena CTPI")
            )

            Toast.makeText(requireContext(), "Algo $algo", Toast.LENGTH_LONG).show()
            huaweiMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(2.4826211,-76.5620209), 10f))
            huaweiMap.uiSettings.isMyLocationButtonEnabled = true
            huaweiMap.uiSettings.isZoomGesturesEnabled = true
            huaweiMap.uiSettings.isScrollGesturesEnabled = true
            huaweiMap.uiSettings.isTiltGesturesEnabled = true
            huaweiMap.uiSettings.isRotateGesturesEnabled = true

        }
    }


}

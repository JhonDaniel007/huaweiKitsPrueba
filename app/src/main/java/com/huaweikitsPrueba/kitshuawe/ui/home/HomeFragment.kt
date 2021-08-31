package com.huaweikitsPrueba.kitshuawe.ui.home

import android.app.AlertDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.huawei.hms.framework.common.ContextCompat.getSystemService
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions
import com.huawei.hms.push.HmsMessaging
import com.huaweikitsPrueba.kitshuawe.InicioActivity
import com.huaweikitsPrueba.kitshuawe.R
import com.huaweikitsPrueba.kitshuawe.ScanKit
import com.huaweikitsPrueba.kitshuawe.databinding.FragmentHomeBinding
import com.huaweikitsPrueba.kitshuawe.push.MyApplication


class HomeFragment : Fragment() , OnMapReadyCallback {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    lateinit var huaweiMap: HuaweiMap



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
            val coordenadas= LatLng(2.4826211,-76.5620209)

            huaweiMap.addMarker(MarkerOptions().position(LatLng(2.48262,-76.56202)).snippet("Nueva").clusterable(true))
            huaweiMap.addMarker(MarkerOptions().position(LatLng(2.4826112,-76.562202)).snippet("DOS").clusterable(true))
            huaweiMap.addMarker(MarkerOptions().position(LatLng(2.4826142,-76.562052)).snippet("TRES").clusterable(true))

            huaweiMap.addMarker(MarkerOptions().position(coordenadas).title("Sena CTPI"))

            huaweiMap.setOnMarkerClickListener { comprarPlato()
                false
            }

            huaweiMap.setMarkersClustering(true)
            huaweiMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(2.4826211,-76.5620209), 10f))
            huaweiMap.isMyLocationEnabled= true
        }
    }
    fun comprarPlato(){
        var alertDialog=AlertDialog.Builder(requireContext())
        alertDialog.setTitle("${huaweiMap.setMarkersClustering(true)}")
        alertDialog.setMessage("Compra el plato del dia, a un magnifico precio precio $20.0000 ")
        alertDialog.setCancelable(false)
        alertDialog.setIcon(R.drawable.hwid_auth_button_normal)
        alertDialog.setPositiveButton("Comprar"){
            _,_->
            alertDialog.setCancelable(true)
            val intent=Intent(requireContext(), ScanKit::class.java)
            startActivity(intent)
            displayNotify()
            Toast.makeText(requireContext(), "Plato Comprado", Toast.LENGTH_SHORT).show()
        }
        alertDialog.setNegativeButton("Cancelar"){
            _,_->
            alertDialog.setCancelable(false)
        }
        alertDialog.create().show()
    }

    private fun subscribe(topic: String) {
        try {
            HmsMessaging.getInstance(requireContext())
                .subscribe(topic)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "Subscripcion exitosa")
                        Toast.makeText(requireContext(), "Subscripcion", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("TAG", "SUBSCRIPCION TOPIC ELSE" + task.exception.message)
                        Toast.makeText(requireContext(), "SubscripcionSinIdea", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Log.d("TAG", "No se pudo hacer la supscripcion")
            Toast.makeText(requireContext(), "Subscripcion fallida", Toast.LENGTH_SHORT).show()

        }
    }

    private fun displayNotify(){
        val varColorNotify=resources.getColor(R.color.comprar)

        val intent=Intent(requireContext(), InicioActivity::class.java)
        val pendingIntent=PendingIntent.getActivity(requireContext(), 100,intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val notificacionBuilder=NotificationCompat.Builder(requireContext(), MyApplication.CHANNEL_IDD)
        notificacionBuilder.setContentIntent(pendingIntent)
            .setContentTitle("Compra exitosa")
            .setContentText("Has hecho una compra en "+ MarkerOptions.PARCELABLE_WRITE_RETURN_VALUE)
            .setSmallIcon(R.drawable.ic_help)
            .setAutoCancel(true)
            .color = varColorNotify

        val notificationManager=getSystemService(requireContext(), NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificacionBuilder.build() )

    }

}
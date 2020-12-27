package com.example.qrscan.ui.scan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.qrcodescan.ListItem
import com.example.qrscan.R
import com.example.qrscan.SharedViewModel
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import java.text.SimpleDateFormat
import java.util.*

class ScanFragment: Fragment() {

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10
    }

    private lateinit var textureView: TextureView
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.hide()

        val root = inflater.inflate(R.layout.fragment_scan, container, false)



        textureView = root.findViewById(R.id.texture_view)

        if (isCameraPermissionGranted()) {
            textureView.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }

        return root
    }

    private fun startCamera() {
        val previewConfig = PreviewConfig.Builder()
            // We want to show input from back camera of the device
            .setLensFacing(CameraX.LensFacing.BACK)
            .build()

        val preview = Preview(previewConfig)

        preview.setOnPreviewOutputUpdateListener { previewOutput ->
            val parent = textureView.parent as ViewGroup
            parent.removeView(textureView)
            textureView.setSurfaceTexture(previewOutput.surfaceTexture)
            parent.addView(textureView, 0)
        }

        val imageAnalysisConfig = ImageAnalysisConfig.Builder()
            .build()
        val imageAnalysis = ImageAnalysis(imageAnalysisConfig)

        val qrCodeAnalyzer = QrCodeAnalyzer { qrCodes ->
            qrCodes.forEach {

                // Pass barcode to viewmodel
                sharedViewModel.barcode.value = it.rawValue

                // Generate and pass scan time
                val sdf = SimpleDateFormat("dd.M.yyyy HH:mm")
                val scanTime = sdf.format(Date())
                sharedViewModel.barcodeScanTime.value = scanTime
                sharedViewModel.navigatedFrom.value = "scanFragment"

                CameraX.unbindAll()

                // Move to qrcode fragment
                view?.findNavController()?.navigate(R.id.action_navigation_scan_to_navigation_qrcode)

            }
        }

        imageAnalysis.analyzer = qrCodeAnalyzer

        // We need to bind preview and imageAnalysis use cases
        CameraX.bindToLifecycle(this as LifecycleOwner, preview, imageAnalysis)
    }

    private fun isCameraPermissionGranted(): Boolean {
        val selfPermission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        return selfPermission == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (isCameraPermissionGranted()) {
                textureView.post { startCamera() }
            } else {
                Toast.makeText(context, "Camera permission is required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.item_delete).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

}
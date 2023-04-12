package ghoudan.anfaSolution.com.ui.clients

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import ghoudan.anfaSolution.com.R
import ghoudan.anfaSolution.com.databinding.FragmentImageChooserBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase


class ImageChooserFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentImageChooserBinding
    private lateinit var callback: (Bitmap?) -> Unit

    companion object {
        fun newInstance(callback: (Bitmap?) -> Unit): BottomSheetDialogFragment {
            return ImageChooserFragment().apply {
                this.callback = callback
            }
        }
    }

    private val takePictureTask = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        try {
            if (activityResult.resultCode == Activity.RESULT_OK) {
                val imageBitmap = activityResult.data?.extras?.get("data") as? Bitmap
                callback.invoke(imageBitmap)
                dismiss()
            }
        } catch (e: java.lang.Exception) {
            Firebase.crashlytics.recordException(e)
        }
    }

    private val getPictureFromGalleryTask = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->

        uri?.let { _uri ->
            // Handle the returned Uri
            val imageBitmap: Bitmap
            val contentResolver = requireActivity().contentResolver
            try {
                imageBitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
                } else {
                    val source: ImageDecoder.Source =
                        ImageDecoder.createSource(contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
                callback.invoke(imageBitmap)
                dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
                Firebase.crashlytics.recordException(e)
            }
        } ?: kotlin.run {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.select_image),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.takePictureBtn.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureTask.launch(takePictureIntent)
        }
        binding.pickFromGalleryBtn.setOnClickListener {
            getPictureFromGalleryTask.launch("image/*")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        takePictureTask.unregister()
        getPictureFromGalleryTask.unregister()
    }
}

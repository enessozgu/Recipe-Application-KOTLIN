package com.example.mekan.view


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.mekan.R
import com.example.mekan.database.AppDatabase
import com.example.mekan.database.Food
//import com.example.mekan.database.Food
import com.example.mekan.databinding.FragmentYemekTanitimBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class YemekTanitim : Fragment() {
    private var _binding: FragmentYemekTanitimBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: AppDatabase
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionResultLauncher: ActivityResultLauncher<String>

    private var secilenGorsel: Uri? = null
    private var secilenBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLaunchers()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentYemekTanitimBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        temizle()

        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "mekan-database2"
        ).build()

        binding.imageView.setOnClickListener {
            gorselSec(it)
        }

        binding.kaydet.setOnClickListener {
            val yemekAdi = binding.editTextText.text.toString()
            val malzemeler = binding.editTextText2.text.toString()
            val yapilisi = binding.editTextTextMultiLine.text.toString()

            if (yemekAdi.isEmpty() || malzemeler.isEmpty() || yapilisi.isEmpty() || secilenBitmap == null) {
                Toast.makeText(requireContext(), "Tüm alanları doldurun ve bir görsel seçin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val kucukBitmap = kucukBitmap(secilenBitmap!!, 300)
            val outputStream = ByteArrayOutputStream()
            kucukBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            val bytDizisi = outputStream.toByteArray()

            val food = Food(
                yemekAdi = yemekAdi,
                malzeme = malzemeler,
                yapilis = yapilisi,
                gorsel = bytDizisi
            )

            lifecycleScope.launch(Dispatchers.IO) {
                db.foodDao().insertAll(food)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Yemek kaydedildi!", Toast.LENGTH_SHORT).show()

                    temizle()

                }
            }
        }

        // Load food data for demonstration
        lifecycleScope.launch(Dispatchers.IO) {
            val foodList = db.foodDao().getAll()
            if (foodList.isNotEmpty()) {
                val firstFood = foodList[0]
                withContext(Dispatchers.Main) {
                    binding.editTextText.setText(firstFood.yemekAdi)
                    binding.editTextText2.setText(firstFood.malzeme)
                    binding.editTextTextMultiLine.setText(firstFood.yapilis)
                    val bitmap = BitmapFactory.decodeByteArray(firstFood.gorsel, 0, firstFood.gorsel.size)
                    binding.imageView.setImageBitmap(bitmap)
                    temizle()
                }
            }
        }


    }

    private fun kucukBitmap(kullanicininSectigiBitmap: Bitmap, maximumBoyut: Int): Bitmap {
        var width = kullanicininSectigiBitmap.width
        var height = kullanicininSectigiBitmap.height
        val bitmapOrani: Double = width.toDouble() / height.toDouble()

        if (bitmapOrani > 1) {
            width = maximumBoyut
            height = (width / bitmapOrani).toInt()
        } else {
            height = maximumBoyut
            width = (height * bitmapOrani).toInt()
        }
        return Bitmap.createScaledBitmap(kullanicininSectigiBitmap, width, height, true)
    }

    private fun gorselSec(view: View) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
                Snackbar.make(view, "Galeriye gitmek için izin verin.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("İzin Ver") {
                        permissionResultLauncher.launch(permission)
                    }.show()
            } else {
                permissionResultLauncher.launch(permission)
            }
        } else {
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }

    private fun registerLaunchers() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    secilenGorsel = intentFromResult.data
                    try {
                        secilenBitmap = if (Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(requireActivity().contentResolver, secilenGorsel!!)
                            ImageDecoder.decodeBitmap(source)
                        } else {
                            MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, secilenGorsel)
                        }
                        binding.imageView.setImageBitmap(secilenBitmap)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        permissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                Toast.makeText(requireContext(), "Galeriye erişim reddedildi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun temizle(){
        binding.editTextText.text.clear()
        binding.editTextText2.text.clear()
        binding.editTextTextMultiLine.text.clear()
        binding.imageView.setImageResource(R.drawable.ass)
    }
}

package net.pacofloridoquesada.teammanager.ui.main.equipo

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.FragmentActualizarJugadorBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentDetalleEquipoBinding
import net.pacofloridoquesada.teammanager.databinding.FragmentEquipoBinding
import net.pacofloridoquesada.teammanager.utils.ImageUtils
import net.pacofloridoquesada.teammanager.viewmodel.TeamManagerViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class DetalleEquipoFragment : Fragment() {

    private var _binding: FragmentDetalleEquipoBinding? = null
    private val binding get() = _binding!!
    private val equipoViewModel: EquipoViewModel by activityViewModels()
    private val teamManagerViewModel: TeamManagerViewModel by activityViewModels()
    private lateinit var uriFoto: String
    private lateinit var storageRef: StorageReference

    private fun setupEquipo() {
        teamManagerViewModel.team.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvCiudad.text = it.city
                binding.tvNombreEquipo.text = it.name
                if (it.image != null) {
                    Firebase.storage.reference.child("image").child(it.image!!)
                        .downloadUrl.addOnSuccessListener {uri ->
                            Glide.with(binding.cvPerfil.context)
                                .load(uri)
                                .error(R.drawable.ic_logo_app)
                                .into(binding.ivPerfil)
                        }
                }
            }
        }
    }

    private fun setupEquipoEstats() {
        equipoViewModel.jugadoresEquipo.observe(viewLifecycleOwner) {
            if (it != null) {
                var goles = 0
                var assistencias = 0
                var amarillas = 0
                var rojas = 0
                for (player in it) {
                    goles += player.playerReport!!.goals
                    assistencias += player.playerReport.assists
                    amarillas += player.playerReport.yellowCards
                    rojas += player.playerReport.redCards
                }

                binding.tvGolesEstats.text = goles.toString()
                binding.tvAsistenciasEstats.text = assistencias.toString()
                binding.tvAmarillasEstats.text = amarillas.toString()
                binding.tvRojasEstats.text = rojas.toString()

            }
        }
    }

    private val PERMISOS_REQUERIDOS = when {
        Build.VERSION.SDK_INT >= 33 -> Manifest.permission.READ_MEDIA_IMAGES
        else -> Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private fun permisosAceptados() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            PERMISOS_REQUERIDOS
        ) == PackageManager.PERMISSION_GRANTED

    private val solicitudFotoGalleryMenorV33 = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            //uri de la foto elegida

            val uri = result.data?.data

            val uriCopia = saveBitmapImage(loadFromUri(uri)!!)
            binding.ivPerfil.setImageURI(uriCopia)

            val uriii = ImageUtils.getRealPathFromURI(uriCopia!!, requireContext()).split("/")
            uriFoto = uriii[uriii.size-1]

            storageRef.child("image").child(uriFoto).putFile(uriCopia)

            teamManagerViewModel.team.observe(viewLifecycleOwner) {
                if (it != null) {
                    it.image = uriFoto
                    teamManagerViewModel.updateTeam(it)
                }
            }


            Log.i("URIGUARDAR", uriFoto)
        }
    }

    // Petición de foto de la galería version >=33
    private val solicitudFotoGalleryV33 = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val uriCopia = saveBitmapImage(loadFromUri(uri)!!)
            binding.ivPerfil.setImageURI(uriCopia)

            val uriii = ImageUtils.getRealPathFromURI(uriCopia!!, requireContext()).split("/")
            uriFoto = uriii[uriii.size-1]

            storageRef.child("image").child(uriFoto).putFile(uriCopia)

            Log.i("URIGUARDAR", uriFoto)
        }
    }

    private fun seleccionarFoto() {
        if (Build.VERSION.SDK_INT >= 33)
            solicitudFotoGalleryV33.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            solicitudFotoGalleryMenorV33.launch(intent)
        }
    }

    private val solicitudPermisosLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission has been granted.
                seleccionarFoto()
            } else {
                // Permission request was denied.
                explicarPermisos()
            }
        }

    private fun explicarPermisos() {
        AlertDialog.Builder(requireContext())
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage(getString(R.string.son_necesarios_los_permisos_de_lectura_de_ficheros_para_poder_a_adir_una_im_gen_estas_seguro_de_rechazarlos))
            //acción si pulsa si
            .setPositiveButton(android.R.string.ok) { v, _ ->
                //Solicitamos los permisos de nuevo
                solicitudPermisosLauncher.launch(PERMISOS_REQUERIDOS)
                //cerramos el dialogo
                v.dismiss()
            }
            //accion si pulsa no
            .setNegativeButton(android.R.string.cancel) { v, _ -> v.dismiss() }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun setupCambiarImagen() {
        binding.ivPerfil.setOnClickListener {
            when {
                Build.VERSION.SDK_INT >= 34 -> seleccionarFoto()
                permisosAceptados() -> seleccionarFoto()
                else -> solicitudPermisosLauncher.launch(PERMISOS_REQUERIDOS)
            }
        }
    }

    private fun setupVolver() {
        binding.btVolver.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupEquipo()
        this.setupEquipoEstats()
        this.setupCambiarImagen()
        this.setupVolver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetalleEquipoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        storageRef = FirebaseStorage.getInstance().reference
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val TAG = "TeamManager"
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    private fun saveBitmapImage(bitmap: Bitmap): Uri? {
        val timestamp = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        var uri: Uri? = null
        //Tell the media scanner about the new file so that it is immediately available to the user.
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, timestamp)
        //mayor o igual a version 29
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
            values.put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "Pictures/" + getString(R.string.app_name)
            )
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            uri = requireContext().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
            if (uri != null) {
                try {
                    val outputStream = requireContext().contentResolver.openOutputStream(uri)
                    if (outputStream != null) {
                        try {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                            outputStream.close()
                        } catch (e: Exception) {
                            Log.e(TAG, "saveBitmapImage: ", e)
                        }
                    }
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    requireContext().contentResolver.update(uri, values, null, null)
                    // Toast.makeText(requireContext(), "Saved...", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e(TAG, "saveBitmapImage: ", e)
                }
            }
        } else {//no me funciona bien en versiones inferiores a la 29(Android 10)
            val imageFileFolder = File(
                Environment.getExternalStorageDirectory()
                    .toString() + '/' + getString(R.string.app_name)
            )
            if (!imageFileFolder.exists()) {
                imageFileFolder.mkdirs()
            }
            val mImageName = "$timestamp.png"
            val imageFile = File(imageFileFolder, mImageName)
            try {
                val outputStream: OutputStream = FileOutputStream(imageFile)
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                } catch (e: Exception) {
                    Log.e(TAG, "saveBitmapImage: ", e)
                }
                values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                requireContext().contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )
                uri = imageFile.toUri()
                // Toast.makeText(requireContext(), "Saved...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e(TAG, "saveBitmapImage: ", e)
            }
        }
        return uri
    }

    fun loadFromUri(photoUri: Uri?): Bitmap? {
        var image: Bitmap? = null
        try {
            // check version of Android on device
            image = if (Build.VERSION.SDK_INT > 27) {
                // on newer versions of Android, use the new decodeBitmap method
                val source = ImageDecoder.createSource(
                    requireContext().contentResolver,
                    photoUri!!
                )
                ImageDecoder.decodeBitmap(source)
            } else {
                // support older versions of Android by using getBitmap
                MediaStore.Images.Media.getBitmap(
                    requireContext().contentResolver,
                    photoUri
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }
}
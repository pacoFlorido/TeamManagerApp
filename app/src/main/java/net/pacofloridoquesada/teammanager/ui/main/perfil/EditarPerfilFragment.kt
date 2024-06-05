package net.pacofloridoquesada.teammanager.ui.main.perfil

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
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import net.pacofloridoquesada.teammanager.R
import net.pacofloridoquesada.teammanager.databinding.FragmentEditarPerfilBinding
import net.pacofloridoquesada.teammanager.utils.ImageUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class EditarPerfilFragment : Fragment() {

    private var _binding: FragmentEditarPerfilBinding? = null
    private val binding get() = _binding!!
    private val perfilViewModel: PerfilViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private var uriFoto: String? = null
    private lateinit var storageRef: StorageReference

    private val PERMISOS_REQUERIDOS = when {
        Build.VERSION.SDK_INT >= 33 -> Manifest.permission.READ_MEDIA_IMAGES
        else -> Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private fun permisosAceptados() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            PERMISOS_REQUERIDOS
        ) == PackageManager.PERMISSION_GRANTED

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

    private val solicitudFotoGalleryMenorV33 = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            //uri de la foto elegida

            val uri = result.data?.data

            val uriCopia = saveBitmapImage(loadFromUri(uri)!!)
            binding.ivImagenPerfil.setImageURI(uriCopia)

            val uriii = ImageUtils.getRealPathFromURI(uriCopia!!, requireContext()).split("/")
            uriFoto = uriii[uriii.size-1]

            storageRef.child("image").child(uriFoto!!).putFile(uriCopia)


            Log.i("URIGUARDAR", uriFoto!!)
        }
    }

    // Petición de foto de la galería version >=33
    private val solicitudFotoGalleryV33 = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val uriCopia = saveBitmapImage(loadFromUri(uri)!!)
            binding.ivImagenPerfil.setImageURI(uriCopia)

            val uriii = ImageUtils.getRealPathFromURI(uriCopia!!, requireContext()).split("/")
            uriFoto = uriii[uriii.size-1]

            storageRef.child("image").child(uriFoto!!).putFile(uriCopia)

            Log.i("URIGUARDAR", uriFoto!!)
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

    private fun setupEmpezarSeleccionarFoto() {
        binding.ivImagenPerfil.setOnClickListener {
            when {
                Build.VERSION.SDK_INT >= 34 -> seleccionarFoto()
                permisosAceptados() -> seleccionarFoto()
                else -> solicitudPermisosLauncher.launch(PERMISOS_REQUERIDOS)
            }
        }
    }

    private fun setupDatosJugadorOEntrenador() {
        var jugador = true

        perfilViewModel.trainer.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.etNombreCompletoEditar.setText(it.name)

                if (it.image != null) {
                    Firebase.storage.reference.child("image").child(it.image!!)
                        .downloadUrl.addOnSuccessListener {uri ->
                            Glide.with(binding.cvEditPerfil.context)
                                .load(uri)
                                .error(R.drawable.ic_logo_app)
                                .into(binding.ivImagenPerfil)
                        }
                }

                binding.tvAliasEdicion.visibility = View.INVISIBLE
                binding.etAliasEdicion.visibility = View.INVISIBLE
                this.actualizarEntrenador()
                jugador = false
            } else {
                binding.tvAliasEdicion.visibility = View.VISIBLE
                binding.etAliasEdicion.visibility = View.VISIBLE
            }
        }
        if (jugador) {

            perfilViewModel.player.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.etNombreCompletoEditar.setText(it.name)
                    binding.etAliasEdicion.setText(it.alias)
                    if (it.image != null) {
                        Firebase.storage.reference.child("image").child(it.image!!)
                            .downloadUrl.addOnSuccessListener {uri ->
                                Glide.with(binding.cvEditPerfil.context)
                                    .load(uri)
                                    .error(R.drawable.ic_logo_app)
                                    .into(binding.ivImagenPerfil)
                            }
                    }
                }
            }

            this.actualizarJugador()
        }
    }

    fun setupVolver() {
        binding.btVolver.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun actualizarJugador() {
        binding.btGuardar.setOnClickListener {
            perfilViewModel.player.observe(viewLifecycleOwner) { player ->
                if (player != null) {

                    if (!binding.etNombreCompletoEditar.text.isEmpty()) {
                        player.name = binding.etNombreCompletoEditar.text.toString()
                        player.alias = binding.etAliasEdicion.text.toString()
                        if (uriFoto != null) {
                            player.image = uriFoto
                        }

                        perfilViewModel.updatePlayerRecord(player)

                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(
                            this.requireContext(),
                            "El nombre es obligatorio.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }
        }
    }

    private fun actualizarEntrenador() {
        binding.btGuardar.setOnClickListener {
            perfilViewModel.trainer.observe(viewLifecycleOwner) { trainer ->
                if (trainer != null) {

                    if (!binding.etNombreCompletoEditar.text.isEmpty()) {
                        trainer.name = binding.etNombreCompletoEditar.text.toString()
                        if (uriFoto != null) {
                            trainer.image = uriFoto
                        }
                        perfilViewModel.updateTrainer(trainer)

                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(
                            this.requireContext(),
                            "El nombre es obligatorio.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupDatosJugadorOEntrenador()
        this.setupEmpezarSeleccionarFoto()
        this.setupVolver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditarPerfilBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
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
package com.sumeyra.storyenglish.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.sumeyra.storyenglish.adapter.ProfileAdapter
import com.sumeyra.storyenglish.databinding.ProfileFragmentBinding
import com.sumeyra.storyenglish.model.Profile
import java.util.UUID

class ProfileFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var storage : FirebaseStorage

    private lateinit var adapter : ProfileAdapter
    val postProfileList = ArrayList<Profile>()




    private lateinit var goToGalleryLauncher: ActivityResultLauncher<Intent>

    var selectedBitmap : Bitmap? = null
    private var selectedImageUrl: String? = null

    private var _binding : ProfileFragmentBinding ?= null
    private val binding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
        storage = Firebase.storage

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileImage.setOnClickListener { selectPhoto(it) }
        getUser()
        getStoryFromFirebase()


        val layoutManager = LinearLayoutManager(requireContext())
        binding.profileRecyclerview.layoutManager = layoutManager
        adapter = ProfileAdapter(postProfileList)
        binding.profileRecyclerview.adapter = adapter

        setImage()

    }
    private fun setImage(){
        // Galeri başlatıcı için sonuç kaydını yap ve sonuçları işle
        goToGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Galeri intent'inden gelen verileri al
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    val imageURI = intentFromResult.data

                    if (imageURI != null) {
                        // Firebase Storage referansı ve benzersiz isim oluştur
                        val reference = storage.reference
                        val privateNumber = UUID.randomUUID()
                        val imageName = "${privateNumber}.jpg"

                        // Kullanıcının UID'sini al
                        val uid = FirebaseAuth.getInstance().currentUser!!.uid

                        // Firebase Storage'da resim referansı oluştur
                        val imageReference = reference.child("Images").child("UserPhoto").child(uid).child(imageName)

                        // Resmi yükle ve başarıyla yüklenirse işlemleri gerçekleştir
                        imageReference.putFile(imageURI).addOnSuccessListener { task ->

                            // Yüklenen resmin referansını al
                            val uploadedImageReference = reference.child("Images").child("UserPhoto").child(uid).child(imageName)

                            // Resmin URL'sini al ve kullanıcı profiline kaydet
                            uploadedImageReference.downloadUrl.addOnSuccessListener { uri ->
                                val downloadUrl = uri.toString()
                                selectedImageUrl = downloadUrl

                                // Firestore'da kullanıcının profili oluştur ve güncelle
                                if (auth.currentUser != null){
                                    val currentUserUid = auth.currentUser!!.uid
                                    val profileImageUrl = selectedImageUrl.toString()
                                    val currentUserEmail = auth.currentUser!!.email.toString()

                                    val userMap = hashMapOf<String, Any>()
                                    userMap.put("currentUserUid",currentUserUid)
                                    userMap.put("profileImageUrl",profileImageUrl)
                                    userMap.put("currentUserEmail",currentUserEmail)

                                    db.collection("Users").document(currentUserUid).set(userMap).addOnCompleteListener { task->
                                        if (task.isSuccessful){
                                            Glide.with(requireContext()).load(selectedImageUrl).into(binding.profileImage)

                                        }else{
                                            Toast.makeText(requireContext(), "Güncelleme başarısız", Toast.LENGTH_SHORT).show()

                                        }

                                    }.addOnFailureListener { error ->
                                        // Hata durumunda hata mesajını göster ve hatayı yazdır
                                        error.printStackTrace()
                                        Toast.makeText(requireContext(), "Güncelleme başarısız: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
                                        println(error)
                                    }

                                }

                            }

                        }.addOnFailureListener { error ->
                            // Resmi yüklerken hata durumunda hata mesajını göster
                            Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT).show()
                        }

                        // Seçilen resmi göster
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(requireContext().contentResolver, imageURI)
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                binding.profileImage.setImageBitmap(selectedBitmap)
                            } else {
                                // SDK sürümü 28'den küçükse eski yöntemle resmi al ve göster
                                selectedBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageURI)
                                binding.profileImage.setImageBitmap(selectedBitmap)

                            }

                        } catch (e: Exception) {
                            // Hata durumunda hatayı yazdır
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }



    override fun onResume() {
        super.onResume()
        getImageUrl()
    }


    private fun getImageUrl() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val currentUserUid = currentUser.uid

            // Firestore'dan kullanıcının profili belgesini al
            db.collection("Users").document(currentUserUid).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        // Firestore'dan alınan profil resmi URL'sini kullanarak güncelleme
                        val profileImageUrl = document.getString("profileImageUrl")
                        if (!profileImageUrl.isNullOrEmpty()) {
                            selectedImageUrl = profileImageUrl
                            // Güncellenmiş URL ile profil resmini yükleyebilirsiniz
                            Glide.with(requireContext()).load(selectedImageUrl).into(binding.profileImage)
                        }
                    }
                } else {
                    // Firestore'dan veri alırken bir hata oluştu
                    val errorMessage = task.exception?.localizedMessage ?: "Bilinmeyen hata"
                    Toast.makeText(requireContext(), "Profil resmi URL'si alınırken hata oluştu: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // İzin verildiyse galeriye gitme işlemini gerçekleştir
                launchGalleryLauncher()
            } else {
                // İzin verilmediyse kullanıcıya uygun bir geri bildirim göster
                Snackbar.make(requireView(), "Permission Denied", Snackbar.LENGTH_SHORT).show()
            }
        }

    private fun selectPhoto(view: View) {
        //gallery gitme izin işlemleri için izin kontrolleri ve ana işlem

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            // Android 33+
            // READ_MEDIA_IMAGES izni istenecek
            checkPermission(Manifest.permission.READ_MEDIA_IMAGES)

        }else{
            // Android 33-
            // READ_EXTERNAL_STORAGE izni istenecek
            checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)

        }

    }



    private fun checkPermission(permission: String){
        if (ContextCompat.checkSelfPermission(requireContext(),permission) != PackageManager.PERMISSION_GRANTED){
            // İzin verilmediyse
            if (shouldShowRequestPermissionRationale(permission)){
                // Kullanıcıya neden izin istediğinizi göster
                showRationale(permission)

            }else{
                //izin iste
                requestPermissionLauncher.launch(permission)

            }

        }else{
            // İzin zaten verilmişse galeriye gitme işlemini gerçekleştir
            goToGallery()
        }
    }

     override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)
    }

    private fun showRationale(permission: String) {
        Snackbar.make(
            requireView(),
            "Permission Needed!",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Give Permission"){
            //izin iste
            requestPermissionLauncher.launch(permission)
        }.show()

    }
    private fun launchGalleryLauncher() {
        val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        goToGalleryLauncher.launch(intentToGallery)
    }

    private fun goToGallery(){
        launchGalleryLauncher()

    }


    private fun getUser(){
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val currentUserEmail = currentUser.email
            val currentUserName = currentUser.displayName

            binding.profileUserEmailText.text = currentUserEmail
            binding.profileUserNameText.text = currentUserName



        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun getStoryFromFirebase() {
        db.collection("Posts").orderBy("date" , Query.Direction.DESCENDING).addSnapshotListener { snapshot, error ->
            if (error != null) {
                val context: Context = requireContext()
                Toast.makeText(context , error.localizedMessage, Toast.LENGTH_SHORT).show()
            }else {
                if (snapshot != null && !snapshot.isEmpty){
                    val documents = snapshot.documents

                    postProfileList.clear()

                    for (document in documents) {

                        if (document.get("userEmail") == auth.currentUser!!.email){
                            val storyHeader = document.get("storyHeader") as String
                            val story = document.get("story") as String
                            val word = document.get("words") as String

                            val downloadedPost = Profile( word, storyHeader,story)
                            postProfileList.add(downloadedPost)
                        }
                    }

                    adapter.notifyDataSetChanged()
                }

            }
        }
    }



}
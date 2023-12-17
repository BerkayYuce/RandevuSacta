package com.example.randevusata

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.randevusata.databinding.ActivityMainBinding

import com.example.randevusata.databinding.ActivityMaingirisEkraniBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.lang.StringBuilder

class MaingirisEkrani : AppCompatActivity() {

    lateinit var binding : ActivityMaingirisEkraniBinding
    lateinit var binding2 : ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaingirisEkraniBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        // Veritabanı referansını alın
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("musteriler") // Örneğin, "musteriler" düğümüne referans oluşturun
        val sb = StringBuilder()
        binding.randevuEkrani.text = sb.toString()

        val randevuSaatleri = resources.getStringArray(R.array.RandevuSaatleri)
        if (binding.spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, randevuSaatleri)
            binding.spinner.adapter = adapter

            binding.siraAl.setOnClickListener {

                val istekSac = binding.istekSac.text.toString()
                val telefonNo = binding.telefonNo.text.toString()
                val secilenSaat = binding.spinner.selectedItem.toString()

                val sharedPreferences = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                editor.putString("telefonNo", telefonNo)
                editor.putString("istekSac", istekSac)
                editor.putString("saat", secilenSaat)
                editor.apply()

                // Kullanıcı verilerini SharedPreferences'ten alın
                val isim = sharedPreferences.getString("isim", "")
                val email = sharedPreferences.getString("email", "")

                // Verileri Firebase'e ekleyin
                val yeniKullaniciRef = ref.push() // Yeni bir düğüm oluşturun
                yeniKullaniciRef.child("isim").setValue(isim)
                yeniKullaniciRef.child("email").setValue(email)
                yeniKullaniciRef.child("telefonNo").setValue(telefonNo)
                yeniKullaniciRef.child("istekSac").setValue(istekSac)
                yeniKullaniciRef.child("saat").setValue(secilenSaat)

                // Firebase verileri alın
                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val sb = StringBuilder()
                        for (musteriSnapshot in dataSnapshot.children) {
                            val isim = musteriSnapshot.child("isim").getValue(String::class.java)
                            val email = musteriSnapshot.child("email").getValue(String::class.java)
                            val telefonNo =
                                musteriSnapshot.child("telefonNo").getValue(String::class.java)
                            val istekSac =
                                musteriSnapshot.child("istekSac").getValue(String::class.java)
                            val saat = musteriSnapshot.child("saat").getValue(String::class.java)

                            sb.append("Müşteri: $isim $telefonNo $saat \n")
                        }
                        // TextView'e tüm verileri yazdırma
                        binding.randevuEkrani.text = sb.toString()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Veri alımı iptal edildi
                    }
                })

                Toast.makeText(this@MaingirisEkrani, "Randevu Talebiniz Alınmıştır!", Toast.LENGTH_SHORT).show()
            }
            fetchFirebaseData(ref)
        }

        binding.cikisYap.setOnClickListener {
            finish()
        }
    }
    private fun fetchFirebaseData(ref: DatabaseReference) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val sb = StringBuilder()
                for (musteriSnapshot in dataSnapshot.children) {
                    val isim = musteriSnapshot.child("isim").getValue(String::class.java)
                    val email = musteriSnapshot.child("email").getValue(String::class.java)
                    val telefonNo = musteriSnapshot.child("telefonNo").getValue(String::class.java)
                    val istekSac = musteriSnapshot.child("istekSac").getValue(String::class.java)
                    val saat = musteriSnapshot.child("saat").getValue(String::class.java)

                    sb.append("Müşteri: $isim $telefonNo $saat \n")
                }
                // TextView'e tüm verileri yazdırma
                binding.randevuEkrani.text = sb.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Veri alımı iptal edildi
            }
        })
    }
   }






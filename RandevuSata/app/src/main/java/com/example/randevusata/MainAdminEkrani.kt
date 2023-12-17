package com.example.randevusata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.randevusata.databinding.ActivityMainAdminEkraniBinding
import com.example.randevusata.databinding.ActivityMaingirisEkraniBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainAdminEkrani : AppCompatActivity() {

    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var kullaniciArrayList: ArrayList<Kullanici>
    private lateinit var userAdapter: MyAdapter
    lateinit var binding: ActivityMainAdminEkraniBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main_admin_ekrani)
        binding = ActivityMainAdminEkraniBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userRecyclerview = findViewById(R.id.kullaniciList)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)
        kullaniciArrayList = arrayListOf()
        userAdapter = MyAdapter(kullaniciArrayList) // Adapter oluşturuldu
        userRecyclerview.adapter = userAdapter // RecyclerView'e adaptörü atandı

        getKullaniciData() // Kullanıcı verilerini çeken fonksiyon çağrıldı
        // Müşteri ekle butonuna tıklandığında

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("musteriler") // Örneğin, "musteriler" düğümüne referans oluşturun

        binding.musteriEkle.setOnClickListener {

            val isim = binding.yeniMusteri.text.toString()
            val email = "yeni.musteri@email.com"
            val saat = binding.zaman.text.toString()

            // Yeni müşteri bilgilerini Firebase veritabanına ekle
            val yeniMusteri = Kullanici(isim, email)
            val yeniKullaniciRef = ref.push() // Yeni bir düğüm oluşturun
            yeniKullaniciRef.child("isim").setValue(isim)
            yeniKullaniciRef.child("email").setValue(email)
            yeniKullaniciRef.child("saat").setValue(saat)

            // Ekledikten sonra RecyclerView'i güncelle
            kullaniciArrayList.add(yeniMusteri)
            userAdapter.notifyDataSetChanged()
        }

        binding.musteriSil.setOnClickListener {

            val secilen = binding.secilenSira.text.toString()
            val secilenIndex: Int = secilen.toInt() - 1 // Kullanıcıdan aldığınız değeri indekse dönüştürün

            if (secilenIndex >= 0 && secilenIndex < kullaniciArrayList.size) {

                val silinecekKullanici = kullaniciArrayList[secilenIndex]
                val silinecekMusteriId = silinecekKullanici.id // Kullanıcı nesnesinden ID'yi alın

                ref.child(silinecekMusteriId.toString()).removeValue().addOnSuccessListener {
                    kullaniciArrayList.removeAt(secilenIndex)
                    userAdapter.notifyDataSetChanged()
                }.addOnFailureListener {
                    // Silme başarısız oldu, bir hata oluştu
                }
            } else {
                // Geçersiz seçim, kullanıcıya hata mesajı göster
            }
        }
    }

    private fun getKullaniciData() {

        dbref = FirebaseDatabase.getInstance().getReference("musteriler")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    kullaniciArrayList.clear()

                    for (kullaniciSnapshot in snapshot.children) {
                        val kullaniciId = kullaniciSnapshot.key
                        val email = kullaniciSnapshot.child("email").getValue(String::class.java)
                        val isim = kullaniciSnapshot.child("isim").getValue(String::class.java)
                        val istekSac = kullaniciSnapshot.child("istekSac").getValue(String::class.java) ?: ""
                        val saat = kullaniciSnapshot.child("saat").getValue(String::class.java)
                        val kullanici = Kullanici(email, isim, kullaniciId, istekSac, saat)
                        kullanici?.let {
                            kullaniciArrayList.add(it)
                        }
                    }

                    userAdapter.notifyDataSetChanged()
                } else {
                    // Veri bulunamadı veya boşsa bir işlem yapabilirsiniz
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Hata durumunda bir işlem yapabilirsiniz
            }
        })
    }
}

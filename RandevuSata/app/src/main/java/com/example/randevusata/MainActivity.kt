package com.example.randevusata

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.randevusata.databinding.ActivityMainBinding
import com.example.randevusata.databinding.ActivityMaingirisEkraniBinding
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var binding2: ActivityMaingirisEkraniBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


       firebaseAuth = FirebaseAuth.getInstance()

        binding.girisYap.setOnClickListener {

            val isim = binding.girisisimSoyisim.text.toString()
            val email = binding.girisEmail.text.toString()
            var sifre = binding.girisSifre.text.toString()



            val sharedPreferences = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("isim", isim)
            editor.putString("email", email)


            editor.apply()

            if(email.isNotEmpty() && sifre.isNotEmpty()){
                    var donguKirici : Boolean = false
                firebaseAuth.signInWithEmailAndPassword(email , sifre).addOnCompleteListener {
                    if (it.isSuccessful){

                        if(sifre == "123admin123admin"){
                            Toast.makeText(this, "Admin Girişi!", Toast.LENGTH_SHORT).show()
                            val girisAdminIntent = Intent (this@MainActivity, MainAdminEkrani::class.java)
                            startActivity(girisAdminIntent)
                           donguKirici = true
                        }
                        if(donguKirici == false){
                        Toast.makeText(this@MainActivity, "Giriş Başarılı", Toast.LENGTH_SHORT).show()
                        val girisIntent = Intent (this@MainActivity, MaingirisEkrani::class.java)
                        startActivity(girisIntent)
                        /*intent = Intent(applicationContext, MainHosgeldiniz::class.java)
                        startActivity(intent)*/}

                    }else {
                        Toast.makeText(this@MainActivity, "Hesap Kayıtlı Değil", Toast.LENGTH_SHORT).show()
                    } //Hesap kayılı değil yerine it.exception.toString() bu uyarı mesajı gösterilebilir.
                }
            }else {
                Toast.makeText(this, "Alanlar boş bırakılamaz!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.kayitOl.setOnClickListener {

            val kayitOlIntent = Intent(this@MainActivity, Mainkayitol::class.java)
            startActivity(kayitOlIntent)
        }


    }

}
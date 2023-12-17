package com.example.randevusata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.randevusata.databinding.ActivityMainBinding
import com.example.randevusata.databinding.ActivityMainkayitolBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.tan

class Mainkayitol : AppCompatActivity() {

    private lateinit var binding2: ActivityMainkayitolBinding
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         //setContentView(R.layout.activity_mainkayitol)
        binding2 = ActivityMainkayitolBinding.inflate(layoutInflater)
        setContentView(binding2.root)
        /*binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)*/
        firebaseAuth = FirebaseAuth.getInstance()

        binding2.kaydet.setOnClickListener {
            var eposta : String = binding2.kayitEmail.text.toString()
            //var isim : String = binding2.kayitisimSoyisim.text.toString()
            var parola : String = binding2.kayitSifre.text.toString()
            var parolaonay : String = binding2.kayitsifreOnayla.text.toString()

            if(eposta.isNotEmpty() && parola.isNotEmpty()){

                if(parola == parolaonay){
                    firebaseAuth.createUserWithEmailAndPassword(eposta, parola).addOnCompleteListener{
                        if(it.isSuccessful){

                            val intent = Intent(this@Mainkayitol, MainActivity::class.java)
                            startActivity(intent)

                        }else{
                            Toast.makeText(this, "Güvensiz Şifre!", Toast.LENGTH_SHORT).show()
                        }
                    }

                }else {
                    Toast.makeText(this, "Şifreler uyuşmuyor!", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(this, "Alan boş bırakılamaz!", Toast.LENGTH_SHORT).show()
            }
        }
        binding2.giriseDon.setOnClickListener {
             intent = Intent(this@Mainkayitol, MainActivity::class.java)
            startActivity(intent)
        }
    }
    }





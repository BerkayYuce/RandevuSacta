package com.example.randevusata

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.randevusata.databinding.ActivityMainAdminEkraniBinding
import com.google.firebase.database.FirebaseDatabase

class MyAdapter(private val kullanicilist : ArrayList<Kullanici>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val database = FirebaseDatabase.getInstance()
        val dbref = database.getReference("musteriler")
        val isimSoyisim: TextView = itemView.findViewById(R.id.tvisimsoyisim)
        val istekSac: TextView = itemView.findViewById(R.id.tvIstekSac)
        val saat: TextView = itemView.findViewById(R.id.tvsaat)
        //val id: TextView = itemView.findViewById(R.id.tvId) // ID'nin görüneceği TextView

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = kullanicilist[position]
                    val silinecekMusteriId = clickedItem.id // Kullanıcı nesnesinden ID'yi alın
                    // Şimdi bu silinecekMusteriId değerini kullanarak Firebase'den veriyi silebilirsiniz
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.kullanici_item,
            parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return kullanicilist.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = kullanicilist[position]
        holder.isimSoyisim.text = currentitem.isim
        holder.istekSac.text = currentitem.istekSac
        holder.saat.text = currentitem.saat
        //holder.id.text = currentitem.id

    }
}

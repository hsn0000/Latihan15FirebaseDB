package com.example.androidlatihan15firebasedb_mhusin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.FieldPosition

class BukuAdapter : RecyclerView.Adapter<BukuAdapter.BukuViewHolder> {

    lateinit var mContext : Context
    lateinit var itemBuku : List<BukuModel>
    lateinit var listener: FirebaseDataListener

    constructor() {}
    constructor(mContext : Context, list : List<BukuModel>){
        this.mContext = mContext
        this.itemBuku = list
        listener = mContext as HalamanDepan
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BukuViewHolder {
        val view : View = LayoutInflater.from(p0.context).inflate(
            R.layout.show_data,p0, false)
        val bukuViewHolder = BukuViewHolder(view)
        return bukuViewHolder

    }

    override fun getItemCount(): Int {
        return itemBuku.size

    }

    override fun onBindViewHolder(p0: BukuViewHolder, p1: Int) {
        val bukuModel : BukuModel = itemBuku.get(p1)
        p0.tv_nama.text = bukuModel.getNama()
        p0.tv_tanggal.text = bukuModel.getTanggal()
        p0.tv_judul.text = bukuModel.getJudulBuku()
        p0.ll_content.setOnClickListener {
            Toast.makeText(mContext, " contoh touch listener",
                Toast.LENGTH_SHORT).show()
        }
        p0.ll_content.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                val  builder = AlertDialog.Builder(mContext)
                builder.setMessage("Pilih Oprasi data !!")
                builder.setPositiveButton("Update") {
                    dialog, i ->
 //                   Toast.makeText(mContext, " hallo saya update",
//                        Toast.LENGTH_SHORT).show()
//                    val intent = Intent(mContext, Tambah_Data::class.java)
 //                   intent.putExtras()
                    listener.onUpdated(bukuModel, p1)
                }
                builder.setNegativeButton("Dellete") {
                    dialog, i ->
                    listener.onDeletedData(bukuModel, p1)
                }
                val dialog : AlertDialog= builder.create()
                dialog.show()

                return true

            }
        })

    }


    inner class BukuViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){
        var ll_content : LinearLayout
        var tv_nama : TextView
        var tv_tanggal : TextView
        var tv_judul : TextView
        init {
            ll_content = itemview.findViewById(R.id.all_counter)
            tv_nama = itemview.findViewById(R.id.tv_penulis)
            tv_judul = itemview.findViewById(R.id.tv_title)
            tv_tanggal = itemview.findViewById(R.id.tv_tgl)
        }

    }

    interface FirebaseDataListener {
        fun onDeletedData ( buku : BukuModel, position: Int)
        fun onUpdated(buku : BukuModel, position: Int)
    }
}
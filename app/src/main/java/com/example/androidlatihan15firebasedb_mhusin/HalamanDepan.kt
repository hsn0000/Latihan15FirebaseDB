package com.example.androidlatihan15firebasedb_mhusin

import android.content.Intent
import android.os.Bundle
import android.util.Log.e
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.halaman_depan.*

class HalamanDepan : AppCompatActivity(), BukuAdapter.FirebaseDataListener {



    private var bukuAdapter : BukuAdapter? = null
    private var rcView : RecyclerView? = null
    private var list : MutableList<BukuModel> = ArrayList<BukuModel>()
    lateinit var dbref : DatabaseReference
    lateinit var helperPrefs : PrefsHerper

    lateinit var fAuth : FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_depan)

        fAuth = FirebaseAuth.getInstance()

        btn_logout.setOnClickListener {
            fAuth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        rcView = findViewById(R.id.rc_view)
        rcView!!.layoutManager = LinearLayoutManager(this)
        rcView!!.setHasFixedSize(true)
        helperPrefs = PrefsHerper(this)

        dbref = FirebaseDatabase.getInstance()
            .getReference("dataBuku/${helperPrefs.getUID()}")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (dataSnapshot in p0.children){
                    val addDataAll = dataSnapshot.getValue(BukuModel::class.java)
                    addDataAll!!.setKey(dataSnapshot.key!!)
                    list.add(addDataAll!!)

                    bukuAdapter= BukuAdapter (
                        this@HalamanDepan,
                        list!!
                    )
                    rcView!!.adapter = bukuAdapter
                }
  //              bukuAdapter = BukuAdapter(applicationContext, list)
  //              rcView!!.adapter = bukuAdapter

            }

            override fun onCancelled(p0: DatabaseError) {
                e("TAGEERROR", p0.message)
            }

        })



        floatingActionButton.setOnClickListener {
            // wekweww
            startActivity(Intent(this,Tambah_Data::class.java))
        }
    }
    override fun onDeletedData(buku: BukuModel, position: Int) {
        dbref = FirebaseDatabase.getInstance()
            .getReference("dataBuku/${helperPrefs.getUID()}")
        if (dbref != null){
            dbref.child(buku.getKey()).removeValue().addOnSuccessListener {
                Toast.makeText(this, "data berhasil di hapus",
                    Toast.LENGTH_SHORT).show()
                bukuAdapter!!.notifyDataSetChanged()
            }
        }

    }
    override fun onUpdated(buku: BukuModel, position: Int) {
        dbref = FirebaseDatabase.getInstance()
            .getReference("dataBuku/${helperPrefs.getUID()}")
        if (dbref != null) {
            val datax = dbref.child(buku.getKey()).key
            val intent = Intent ( this, Tambah_Data::class.java)
            intent.putExtra("kode", datax)
            startActivity(intent)
        }

    }

}
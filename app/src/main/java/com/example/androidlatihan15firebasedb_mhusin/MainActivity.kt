package com.example.androidlatihan15firebasedb_mhusin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.e
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.sign

class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 7
    private lateinit var mGoogleSignIn: GoogleSignInClient
    private lateinit var fAuth : FirebaseAuth
    private lateinit var helperPref : PrefsHerper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        helperPref = PrefsHerper(this)

        fAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN)

            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignIn = GoogleSignIn.getClient(this, gso)
        sign_in_button.setOnClickListener {
            signIN()
        }
    }
    fun signIN(){
        val signinIntent = mGoogleSignIn.signInIntent
        startActivityForResult(signinIntent, RC_SIGN_IN)
    }
    fun firebaseAuthWithGoogle(acct : GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        fAuth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                val user =fAuth.currentUser
                updateUI(user)
            }else{
                e("TAG_ERROR", "${it.exception}")
            }
        }
    }
    fun updateUI(user : FirebaseUser?){
        if(user != null){
            helperPref.saveUID(user.uid) //fungsi save id ke sharepreences
           startActivity(Intent(this,HalamanDepan::class.java))

        }else{
            e("TAG_ERROR","user tida ada")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account!!)

                }catch (x: ApiException){
                    x.printStackTrace()
                }
            }
        }

        override fun onStart(){
            super.onStart()
            val user = fAuth.currentUser
            if(user !=null) {
                updateUI(user)
            }

        }

    }


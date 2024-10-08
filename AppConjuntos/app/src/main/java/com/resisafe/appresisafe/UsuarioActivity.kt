package com.resisafe.appresisafe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.resisafe.appresisafe.databinding.ActivityUsuarioBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsuarioActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarUsuario.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController: NavController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_profileSelectorFragment,
                R.id.nav_userInfoFragment,
                R.id.nav_userLoginActivity
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        Log.d("Tag", "Ventana Cambiada")

        val tokenResponse = ManejadorDeTokens.cargarTokenUsuario(this)
        val apiService = RetrofitClient.apiService


        if (tokenResponse != null) {
            Log.d("Tag", "Token: ${tokenResponse.token}, UserID: ${tokenResponse.userID}")

            apiService.getUserData(tokenResponse.userID, tokenResponse.token)
                .enqueue(object : Callback<UserData> {
                    override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                        Log.d(
                            "Tag",
                            "Enviada Peticion Get con token ${tokenResponse.token} y userId ${tokenResponse.userID}"
                        )
                        if (response.isSuccessful) {
                            val post = response.body()
                            val a: TextView = findViewById(R.id.headerMenuName)
                            val textView = findViewById<TextView>(R.id.textView)
                            if (post != null) {
                                a.text = post.nombre + " " + post.apellido
                                textView.text = post.cedula.toString()
                            } else {
                                a.text = post
                            }
                        } else {
                            Log.e("Tag", "Response body is null")

                        }
                    }

                    override fun onFailure(call: Call<UserData>, t: Throwable) {
                        Log.e("Tag", "FalloPeticion")

                    }
                })
        }
        navView.menu.findItem(R.id.nav_userLoginActivity).setOnMenuItemClickListener {
            ManejadorDeTokens.eliminarTokenUsuario(this.baseContext)
            Log.d("Tag", "Sesion Cerrada")
            val intent = Intent(this, UserLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            true
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.usuario, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
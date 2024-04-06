package com.resisafe.appconjuntos

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.resisafe.appconjuntos.databinding.ActivityUsuarioBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class UsuarioActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarUsuario.toolbar)

        binding.appBarUsuario.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_usuario)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        Log.d("Tag", "Ventana Cambiada")

        val tokenResponse = ManejadorDeTokens.cargarTokenUsuario(this)
        val apiService = RetrofitClient.apiService


        if (tokenResponse != null) {
            Log.d("Tag", "Token: ${tokenResponse.token}, UserID: ${tokenResponse.userID}")

            apiService.getUserData(tokenResponse.userID,tokenResponse.token).enqueue(object : Callback<UserData> {
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    Log.d("Tag", "Enviada Peticion Get con token ${tokenResponse.token} y userId ${tokenResponse.userID}")
                    if (response.isSuccessful) {
                        val post = response.body()
                        val a : TextView = findViewById(R.id.text_home)
                        if (post != null) {
                            a.text = "${post.cedula} , ${post.nombre}, ${post.apellido}"
                        }
                        else{ a.text=post}
                    } else {
                        Log.e("Tag", "Response body is null")

                    }
                }

                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    Log.e("Tag", "Response body is dsafadfafdasf")

                }
            })
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.usuario, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_usuario)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
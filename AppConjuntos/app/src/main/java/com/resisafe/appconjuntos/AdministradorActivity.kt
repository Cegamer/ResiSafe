package com.resisafe.appconjuntos

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.resisafe.appconjuntos.databinding.ActivityAdministradorBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdministradorActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAdministradorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdministradorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarAdministrador.toolbarAdministrador)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navViewAdministrador
        val navController = findNavController(R.id.nav_host_fragment_content_administrador)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.administradorHomeFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val b = intent.extras
        var idPerfil = -1 // or other values

        if (b != null) idPerfil = b.getInt("idPerfil")

        val apiService = RetrofitClient.apiService
        val context = this
        val token = ManejadorDeTokens.cargarTokenUsuario(context)?.token;

        if (token != null) {
            apiService.getPerfil(idPerfil,token).enqueue(object : Callback<PerfilesDTO> {
                override fun onResponse(
                    call: Call<PerfilesDTO>,
                    response: Response<PerfilesDTO>
                ) {
                    if (response.isSuccessful) {
                        val datos = response.body()
                        if (datos != null) {
                            Log.d("AAAAAAAAAA", datos.idConjunto.toString())
                            navView.menu.findItem(R.id.action_administradorHomeFragment_to_conjuntoPerfilesListaFragment2).setOnMenuItemClickListener {
                                val bundle = Bundle()
                                bundle.putInt("idConjunto", datos.idConjunto);
                                navController.navigate(R.id.action_administradorHomeFragment_to_conjuntoPerfilesListaFragment2, bundle)
                                true
                            }
                        }
                    } else {
                        Log.e("Tag", "Response body is null")
                    }
                }

                override fun onFailure(call: Call<PerfilesDTO>, t: Throwable) {
                    Log.e("Tag", "Response body is dsafadfafdasf")
                }
            })
        }




    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.administrador, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_administrador)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
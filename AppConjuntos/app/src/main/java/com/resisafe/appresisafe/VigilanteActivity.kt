package com.resisafe.appresisafe

import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.resisafe.appresisafe.databinding.ActivityVigilanteBinding
import kotlinx.coroutines.launch

class VigilanteActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVigilanteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVigilanteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarVigilante.toolbarVigilante)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navViewVigilante
        val navController = findNavController(R.id.nav_host_fragment_content_vigilante)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.vigilanteHomeFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val context = this
        lifecycleScope.launch() {
            val PerfilActual = ManejadorDeTokens.cargarPerfilActual(context)

            val bundle = Bundle()
            bundle.putInt("idConjunto", PerfilActual?.idConjunto!!)
            bundle.putInt("idPrefilActual", PerfilActual?.idPerfil!!)

            navView.menu.findItem(R.id.registrarPaquetes).setOnMenuItemClickListener {
                navController.navigate(
                    R.id.action_vigilanteHomeFragment_to_vigilanteRegistrarPaqueteFragment,
                    bundle
                )
                true
            }

            navView.menu.findItem(R.id.historialPaquetes).setOnMenuItemClickListener {
                navController.navigate(
                    R.id.action_vigilanteHomeFragment_to_historialPaquetesFragment,
                    bundle
                )
                true
            }
            navView.menu.findItem(R.id.registroVisitantes).setOnMenuItemClickListener {
                navController.navigate(
                    R.id.action_vigilanteHomeFragment_to_vigilanteRegistroVisitanteFragment,
                    bundle
                )
                true
            }
            navView.menu.findItem(R.id.registrarVisita).setOnMenuItemClickListener {
                navController.navigate(
                R.id.action_vigilanteHomeFragment_to_vigilanteRegistroVisitaFragment, bundle)
                true
            }
            navView.menu.findItem(R.id.HistorialVisitas).setOnMenuItemClickListener {
                navController.navigate(
                    R.id.action_vigilanteHomeFragment_to_vigilanteListaVisitantesFragment, bundle

                )
                true

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.vigilante, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_vigilante)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
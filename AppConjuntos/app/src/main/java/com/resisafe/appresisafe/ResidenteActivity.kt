package com.resisafe.appresisafe

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.cardview.widget.CardView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.resisafe.appresisafe.databinding.ActivityResidenteBinding
import kotlinx.coroutines.launch

class ResidenteActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityResidenteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResidenteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarResidente.toolbarResidente)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navViewResidente
        val navController = findNavController(R.id.nav_host_fragment_content_residente)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.residenteHomeFragment
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

            navView.menu.findItem(R.id.QuejasReclamos).setOnMenuItemClickListener {
                navController.navigate(
                    R.id.action_residenteHomeFragment_to_residenteQuejasYReclamosFragment2, bundle
                )
                true
            }

            navView.menu.findItem(R.id.reservarzonacomun).setOnMenuItemClickListener {
                navController.navigate(
                    R.id.action_residenteHomeFragment_to_residenteReservarZonacomunFragment, bundle
                )
                true
            }
            navView.menu.findItem(R.id.HistorialVisitas).setOnMenuItemClickListener {
                navController.navigate(
                    R.id.action_residenteHomeFragment_to_vigilanteListaVisitantesFragment2,
                    bundle
                )
                true
            }
            navView.menu.findItem(R.id.reservas).setOnMenuItemClickListener {
                navController
                    .navigate(R.id.action_residenteHomeFragment_to_listaReservasFragment3, bundle)
                true
            }
            navView.menu.findItem(R.id.historialPaquetes).setOnMenuItemClickListener {
                navController.navigate(
                    R.id.action_residenteHomeFragment_to_historialPaquetesFragment2,
                    bundle
                )
                true

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.residente, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_residente)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
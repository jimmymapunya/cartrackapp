package co.cartrack.za.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import co.cartrack.za.R
import co.cartrack.za.db.OmdbDatabase
import co.cartrack.za.repository.OmdbRepository
import co.cartrack.za.viewmodel.OmdbViewModel
import kotlinx.android.synthetic.main.activity_omdb.*


class OmdbActivity : AppCompatActivity() {

    lateinit var viewModel: OmdbViewModel

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_omdb)

        val omdbRepository = OmdbRepository(OmdbDatabase(this))
        val viewModelProviderFactory = OmdbViewModelProviderFactory(omdbRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(OmdbViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setSupportActionBar(toolbar)
        bottomNavigationView.setupWithNavController(navController)


    }
}





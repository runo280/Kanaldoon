package app.runo.kanaldoon

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import app.runo.kanaldoon.databinding.ActivityMainBinding
import app.runo.kanaldoon.ui.main.MainViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        AppDatabase.get(application).close()
    }
}
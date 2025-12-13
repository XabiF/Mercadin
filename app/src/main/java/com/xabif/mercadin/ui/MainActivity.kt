package com.xabif.mercadin.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.xabif.mercadin.R
import com.xabif.mercadin.src.ProductSource
import com.xabif.mercadin.src.SourceManager
import com.xabif.mercadin.databinding.ActivityMainBinding
import com.xabif.mercadin.src.List
import com.xabif.mercadin.util.FileSystem

// https://icon.kitchen/i/H4sIAAAAAAAAAzWQQU%2FDMAyF%2F4u59rDCBlOvCHFFYjfEwWnsNCKtS5IOoWn%2FHTvAJXGene9Z7wJnTBsVGC7gMX%2BcJpoJBsZUqAMOjymumKu1C%2BkFnhi3VKGDOMqiAmOpLOLh2uYlSVb1hhyzf9AxDi%2FofVyCMaqsMPTHDnIMk%2BKsdFKrzL91Im6qslw4fa%2B6CoSMPtJili48%2Fz8UNppXb2Z3%2B%2Ft%2Bt9OBJt02yR3xYBIuISlmf2jM1wkbtHxuMY%2FasAWfmGmsmgFQojNWU6sF4f%2BS0K%2Bz%2BC1ZTm9K9Fmitwik6PlFDt6vP51tOc5KAQAA

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration;
    private lateinit var binding: ActivityMainBinding;
    private lateinit var navController: NavController;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root);

        setSupportActionBar(binding.appBarMain.toolbar);

        FileSystem.initialize(this);
        List.restore();

        binding.appBarMain.cartFab.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java);
            startActivity(intent);
        };

        val drawerLayout: DrawerLayout = binding.drawerLayout;
        val navView: NavigationView = binding.navView;
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment;
        navController = navHostFragment.navController;
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_query, R.id.nav_saved
            ), drawerLayout
        );

        setupActionBarWithNavController(navController, appBarConfiguration);
        navView.setupWithNavController(navController);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_enable_bm -> {
                item.isChecked = !item.isChecked;
                SourceManager.toggleSource(ProductSource.Bm, item.isChecked);
                true
            }
            R.id.item_enable_mercadona -> {
                item.isChecked = !item.isChecked;
                SourceManager.toggleSource(ProductSource.Mercadona, item.isChecked);
                true
            }
            R.id.item_enable_dia -> {
                item.isChecked = !item.isChecked;
                SourceManager.toggleSource(ProductSource.Dia, item.isChecked);
                true
            }
            R.id.item_enable_carrefour -> {
                item.isChecked = !item.isChecked;
                SourceManager.toggleSource(ProductSource.Carrefour, item.isChecked);
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main);
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp();
    }
}
package com.xabif.mercadin.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xabif.mercadin.R
import com.xabif.mercadin.databinding.ActivityCartBinding
import com.xabif.mercadin.src.List
import com.xabif.mercadin.src.ProductSource
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding

    private fun reload() {
        lifecycleScope.launch {
            val products = List.listAvailable().sortedBy { it.source.ordinal }
            binding.recyclerView.adapter = ProductInfoAdapter(this@CartActivity, products, true) {
                reload()
            }
            binding.cartInfoText.text = getString(R.string.cart_info, List.totalPrice(), List.count())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        reload()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed() // Go back to previous screen
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
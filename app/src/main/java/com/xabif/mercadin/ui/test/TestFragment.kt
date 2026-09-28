package com.xabif.mercadin.ui.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.xabif.mercadin.databinding.FragmentTestBinding
import com.xabif.mercadin.src.ProductSource
import kotlinx.coroutines.launch

class TestFragment : Fragment() {

    private var _binding: FragmentTestBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    fun set(str: String) {
        binding.textSlideshow.text = str
    }

    fun log(str: String) {
        binding.textSlideshow.text = str + "\n" + binding.textSlideshow.text.toString()
    }

    suspend fun runTests(query: String) {
        log("TEST START ($query)")

        for (source in ProductSource.entries) {
            val instance = source.create()
            val instanceName = requireContext().resources.getString(source.getNameResource())
            log("- $instanceName")

            try {
                val products = instance.queryProducts(query)

                log("---- ${products.size} products found")

                for (product in products) {
                    try {
                        val newProduct = instance.queryProductById(product.id)
                        if((newProduct != null) && (product != newProduct)) {
                            throw RuntimeException("Product mismatch between\n$product\nand\n$newProduct")
                        }
                    }
                    catch (e: Exception) {
                        set("!!! FAIL queryProductById (id=${product.id} -- ${product.name})\n${e.stackTraceToString()}")
                        return
                    }
                }

                log("---- OK!")
            }
            catch (e: Exception) {
                set("!!! FAIL queryProducts\n${e.stackTraceToString()}")
                return
            }
        }

        log("TEST END")

        Log.d("apitest", binding.textSlideshow.text.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonStart.setOnClickListener {
            set("")

            viewLifecycleOwner.lifecycleScope.launch {
                runTests("pan")
                runTests("ropa")
                runTests("caldo")
                runTests("camiseta")
                runTests("lavado")
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
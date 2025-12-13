package com.xabif.mercadin.ui.query

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xabif.mercadin.R
import com.xabif.mercadin.src.SourceManager
import com.xabif.mercadin.databinding.FragmentQueryBinding
import com.xabif.mercadin.src.List
import com.xabif.mercadin.ui.ProductInfoAdapter
import com.xabif.mercadin.util.QueryFilter
import com.xabif.mercadin.util.QuerySorting
import kotlinx.coroutines.launch

class QueryFragment : Fragment() {

    private var _binding: FragmentQueryBinding? = null;

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun executeQuery() {
        val query = binding.queryText.text.toString().trim();
        if(query.isBlank()) {
            return;
        }

        binding.recyclerView.adapter = null;
        val filter = QueryFilter.entries[binding.queryFilter.selectedItemPosition];
        val sorting = QuerySorting.entries[binding.querySorting.selectedItemPosition];

        binding.infoText.visibility = View.VISIBLE;
        binding.infoText.setText(R.string.query_info_loading);

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val products = SourceManager.queryProducts(query, filter, sorting);
                binding.recyclerView.adapter = ProductInfoAdapter(requireContext(), products, false) {};
                if(products.isEmpty()) {
                    binding.infoText.setText(R.string.query_info_empty);
                }
                else {
                    binding.infoText.visibility = View.INVISIBLE;
                }
            }
            catch (e: Exception) {
                AlertDialog.Builder(requireContext()) // 'this' is typically an Activity or a Context
                    .setTitle(R.string.exception_dialog_title)
                    .setMessage(e.message)
                    .setNeutralButton("Ok") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }
        };
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQueryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /////////////////////////////////////////////

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context);
        binding.infoText.setText(R.string.query_info_initial);

        val filters_adapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_spinner_item,
            QueryFilter.entries.map { this.requireContext().getText(it.resId) }
        );
        filters_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.queryFilter.adapter = filters_adapter;
        binding.queryFilter.setSelection(0);

        binding.queryFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                executeQuery();
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: handle when nothing is selected
            }
        }

        val sortings_adapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_spinner_item,
            QuerySorting.entries.map { this.requireContext().getText(it.resId) }
        );
        sortings_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.querySorting.adapter = sortings_adapter;
        binding.querySorting.setSelection(0);

        binding.querySorting.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                executeQuery();
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: handle when nothing is selected
            }
        }

        binding.queryText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                executeQuery();

                val imm = this@QueryFragment.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
                imm.hideSoftInputFromWindow(binding.queryText.windowToken, 0);
                binding.queryText.clearFocus();

                true
            }
            else {
                false
            }
        }

        return root;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
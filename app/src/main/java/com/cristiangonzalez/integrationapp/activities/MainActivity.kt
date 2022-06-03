package com.cristiangonzalez.integrationapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.cristiangonzalez.integrationapp.R
import com.cristiangonzalez.integrationapp.adapters.DonutAdapter
import com.cristiangonzalez.integrationapp.databinding.ActivityMainBinding
import com.cristiangonzalez.integrationapp.interfaces.DonutsAPI
import com.cristiangonzalez.integrationapp.models.Donut
import com.cristiangonzalez.integrationapp.utils.goToActivity
import com.cristiangonzalez.integrationapp.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DonutAdapter
    private val donuts = ArrayList<Donut>()
    private val donutsFilter = ArrayList<Donut>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainToolbar)
        initRecycleView()
        donuts()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        binding.mainToolbar.inflateMenu(R.menu.menu_activity_main)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Agregar acciones al pulsar item de menu
        when (item.itemId) {
            R.id.search -> {
                //Filtrar lista al ingresar cadena
                val searchView = item.actionView as SearchView
                searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?) = false

                    override fun onQueryTextChange(newText: String?): Boolean {
                        donutsFilter.clear() //Limpiar lista
                        if (newText.isNullOrEmpty()) {
                            donutsFilter.addAll(donuts) //Agregar todos
                            adapter.notifyDataSetChanged()
                        } else {
                            val search = newText.lowercase(Locale.ROOT)
                            donuts.forEach {
                                //Agregar coincidencias
                                if (it.name.lowercase(Locale.ROOT).contains(search)) donutsFilter.add(it)
                            }
                            adapter.notifyDataSetChanged()
                        }
                        return false
                    }
                })
            }
            R.id.logout -> {
                showDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecycleView() {
        adapter = DonutAdapter(donutsFilter) //Enviar lista filtrada
        binding.rvDonut.layoutManager = LinearLayoutManager(this)
        binding.rvDonut.adapter = adapter
    }

    //Mostrar material dialog
    private fun showDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.logout_dialog_title))
            .setMessage(resources.getString(R.string.logout_dialog_message))
            .setNegativeButton(resources.getString(R.string.logout_dialog_negative)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.logout_dialog_positive)) { _, _ ->
                goToActivity<LoginActivity> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
            .show()
    }

    private fun getRetrofit(): Retrofit {
        showProgressBar()
        return Retrofit.Builder()
            .baseUrl("https://mocki.io/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun donuts() {
        //Consumir api asincrona
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getRetrofit().create(DonutsAPI::class.java).getDonuts("0e91b8f1-5790-423a-868a-b249224ce1bb")
                val donutsResponse = call.body()
                runOnUiThread {
                    if (call.isSuccessful) {
                        //Limpiar lista
                        donuts.clear()
                        if (donutsResponse != null) {
                            //Agregar elementos de response a lista
                            donuts.addAll(donutsResponse)
                            donutsFilter.addAll(donuts)
                            adapter.notifyItemInserted(donutsResponse.size - 1)
                        }
                        hideProgressBar()
                    } else {
                        showError(R.string.error_unexpected)
                        hideProgressBar()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    showError(R.string.error_internet)
                }
            }
        }
    }

    private fun showError(text: Int) {
        toast(text)
        goToActivity<LoginActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun showProgressBar() {
        binding.loginProgressBar.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.loginProgressBar.progressBar.visibility = View.GONE
    }

}
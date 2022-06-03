package com.cristiangonzalez.integrationapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristiangonzalez.integrationapp.R
import com.cristiangonzalez.integrationapp.databinding.DonutItemBinding
import com.cristiangonzalez.integrationapp.models.Donut

class DonutAdapter(private val donuts: List<Donut>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val binding = DonutItemBinding.bind(itemView)

        //Cargar atributos de api
        fun bind(donut: Donut) = with(itemView){
            val lvTopping = binding.llTopping
            val lvBatter = binding.llBatter
            //Limpiar tv toppings
            lvTopping.removeAllViews()
            //Agregar tv toppings
            donut.topping.forEach {
                val rowTextView = TextView(context)
                val type = "- " + it.type
                rowTextView.text = type
                lvTopping.addView(rowTextView)
            }
            //limpiar tv batters
            lvBatter.removeAllViews()
            //Agregar tv batters
            donut.batters.batter.forEach {
                val rowTextView = TextView(context)
                val type = "- " + it.type
                rowTextView.text = type
                lvBatter.addView(rowTextView)
            }
            //Cargar datos en tv
            val price = "Q." + donut.ppu
            val name = donut.id.trimStart('0') + ". " + donut.name
            val type = "Type: " + donut.type.replaceFirstChar { it.uppercase() }
            binding.tvName.text = name
            binding.tvPrice.text = price
            binding.tvType.text = type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.donut_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(donuts[position])
    }

    override fun getItemCount() = donuts.size

}
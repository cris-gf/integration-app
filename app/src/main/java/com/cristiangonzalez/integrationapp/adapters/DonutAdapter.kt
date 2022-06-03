package com.cristiangonzalez.integrationapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristiangonzalez.integrationapp.R
import com.cristiangonzalez.integrationapp.databinding.DonutItemBinding
import com.cristiangonzalez.integrationapp.models.Batter
import com.cristiangonzalez.integrationapp.models.Donut


class DonutAdapter(private val donuts: List<Donut>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val binding = DonutItemBinding.bind(itemView)

        //Texto a mostrar
        fun bind(donut: Donut) = with(itemView){
            val lvTopping = binding.llTopping
            val lvBatter = binding.llBatter

            lvTopping.removeAllViews()
            donut.topping.forEach {
                val rowTextView = TextView(context)
                val type = "- " + it.type
                rowTextView.text = type
                lvTopping.addView(rowTextView)
            }

            lvBatter.removeAllViews()
            donut.batters.batter.forEach {
                val rowTextView = TextView(context)
                val type = "- " + it.type
                rowTextView.text = type
                lvBatter.addView(rowTextView)
            }

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
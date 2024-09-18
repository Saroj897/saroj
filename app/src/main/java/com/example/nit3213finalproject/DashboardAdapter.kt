package com.example.nit3213finalproject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

// Data class representing a Dish
data class Dish(
    val dishName: String,
    val origin: String,
    val mainIngredient: String,
    val mealType: String,
    val description: String
) : Serializable // Ensure Dish implements Serializable

class DashboardAdapter(
    private val context: Context,
    private val dishes: List<Dish> // Changed to Dish
) : RecyclerView.Adapter<DashboardAdapter.DishViewHolder>() {

    // Inner class representing the ViewHolder
    class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dishNameTextView: TextView = itemView.findViewById(R.id.dishNameTextView)
        val originTextView: TextView = itemView.findViewById(R.id.originTextView)
        val mainIngredientTextView: TextView = itemView.findViewById(R.id.mainIngredientTextView)
        val mealTypeTextView: TextView = itemView.findViewById(R.id.mealTypeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entity, parent, false)
        return DishViewHolder(view)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = dishes[position]
        holder.dishNameTextView.text = dish.dishName
        holder.originTextView.text = "Origin: ${dish.origin}"
        holder.mainIngredientTextView.text = "Main Ingredient: ${dish.mainIngredient}"
        holder.mealTypeTextView.text = "Meal Type: ${dish.mealType}"

        // Handle click event to navigate to DetailsActivity
        holder.itemView.setOnClickListener {
            // Displaying a Toast to check if click is working
            Toast.makeText(context, "Item clicked: ${dish.dishName}", Toast.LENGTH_SHORT).show()

            // Passing dish object to DetailsActivity
            val intent = Intent(context, DetailsActivity::class.java).apply {
                putExtra("dishDetails", dish) // Ensure Dish class implements Serializable
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = dishes.size
}

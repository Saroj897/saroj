package com.example.nit3213finalproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class DashboardActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private lateinit var recyclerView: RecyclerView
    private lateinit var dashboardAdapter: DashboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Retrieve keypass from Intent
        val keypass = intent.getStringExtra("keypass") ?: "food" // Default to 'food' if not provided

        // Log and confirm keypass is received
        Toast.makeText(this, "Using keypass: $keypass", Toast.LENGTH_SHORT).show()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchDashboardData(keypass)
    }

    private fun fetchDashboardData(keypass: String) {
        val url = "https://vu-nit3213-api.onrender.com/dashboard/$keypass"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@DashboardActivity, "Failed to load dashboard", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    try {
                        val jsonResponse = JSONObject(responseBody)
                        val entitiesJsonArray = jsonResponse.getJSONArray("entities")
                        runOnUiThread {
                            displayEntities(entitiesJsonArray)
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@DashboardActivity, "Error parsing response: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@DashboardActivity, "Error loading data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun displayEntities(entitiesJsonArray: JSONArray) {
        val dishes = mutableListOf<Dish>()
        for (i in 0 until entitiesJsonArray.length()) {
            val dishJson = entitiesJsonArray.getJSONObject(i)
            val dishName = dishJson.getString("dishName")
            val origin = dishJson.getString("origin")
            val mainIngredient = dishJson.getString("mainIngredient")
            val mealType = dishJson.getString("mealType")
            val description = dishJson.getString("description")

            dishes.add(Dish(dishName, origin, mainIngredient, mealType, description))
        }

        runOnUiThread {
            Toast.makeText(this, "Displaying ${dishes.size} dishes", Toast.LENGTH_SHORT).show()
            dashboardAdapter = DashboardAdapter(this, dishes)
            recyclerView.adapter = dashboardAdapter
        }
    }
}

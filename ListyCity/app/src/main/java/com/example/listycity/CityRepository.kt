package com.example.listycity

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CityRepository {
    private val db = Firebase.firestore
    private val citiesRef = db.collection("cities")

    init {
        citiesRef.addSnapshotListener { snapshots, exception ->
            if (exception != null) {
                return@addSnapshotListener
            }

            _cities.clear()

            snapshots?.documents?.forEach { document ->
                val city = document.toObject(City::class.java)
                if (city != null) {
                    _cities.add(city)
                }
            }
        }
    }

    private val _cities = mutableStateListOf(
        City("Edmonton", "AB"),
        City("Vancouver", "BC"),
        City("Toronto", "ON")
    )

    val cities: List<City>
        get() = _cities

    // Who's to say city.name is primary key?
    fun addCity(city: City) {
        citiesRef.document(city.name).set(city)
    }

    fun updateCity(oldCity: City, updatedCity: City) {
        citiesRef.document(oldCity.name).set(updatedCity)
    }
}
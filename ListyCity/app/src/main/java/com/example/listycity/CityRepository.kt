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

    fun addCity(city: City) {
        val doc = citiesRef.document()
        val fullCity = City(city.name, city.province, doc.id)
        doc.set(fullCity)
    }

    fun updateCity(oldCity: City, updatedCity: City) {
        val fullCity = City(updatedCity.name, updatedCity.province, oldCity.id)
        citiesRef.document(oldCity.id).set(fullCity)
    }

    fun deleteCity(city: City){
        citiesRef.document(city.id).delete()
    }
}
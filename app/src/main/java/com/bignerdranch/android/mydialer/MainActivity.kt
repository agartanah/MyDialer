package com.bignerdranch.android.mydialer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

data class Contact(val name : String, val phone : String, val type : String)
data class Wrapper(val contacts : ArrayList<Contact>)

class MainActivity : AppCompatActivity() {
    private val URL = "https://drive.google.com/u/0/uc?id=1-KO-9GA3NzSgIc1dkAsNm8Dqw0fuPxcR&export=download"
    private val okHttpClient : OkHttpClient = OkHttpClient()
    private var arrayContacts : MutableList<Contact> = mutableListOf()
    private var rviewContacts : MutableList<Contact> = mutableListOf()
    private lateinit var recyclerView : RecyclerView
    private lateinit var buttonSrch : Button
    private lateinit var editText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rView)
        buttonSrch = findViewById(R.id.buttonSrch)
        editText = findViewById(R.id.editTextSrch)

        val request : Request = Request.Builder().url(URL).build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonFromServer = response.body?.string()

                parseJson(jsonFromServer)
            }
        })

        buttonSrch.setOnClickListener() {
            if (editText.text.toString() != "") {
                arrayContacts.clear()

                for (i in 0 .. rviewContacts.size - 1) {
                    if (rviewContacts[i].name.contains(editText.text.toString(), ignoreCase = true) ||
                        rviewContacts[i].phone.contains(editText.text.toString(), ignoreCase = true) ||
                        rviewContacts[i].type.contains(editText.text.toString(), ignoreCase = true)) {

                        arrayContacts.add(rviewContacts[i])
                    }
                }

                recyclerView.adapter?.notifyDataSetChanged()
            } else {
                arrayContacts.clear()

                for (i in 0 .. rviewContacts.size - 1) {
                    arrayContacts.add(rviewContacts[i])
                }

                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun parseJson(jsonFromServer: String?) {
        val result = Gson().fromJson(jsonFromServer, Array<Contact> :: class.java)

        for (i in 0 .. result.size - 1) {
            // https://farm${Photo.farm}.staticflickr.com/${Photo.server}/${Photo.id}_${Photo.secret}_z.jpg

            arrayContacts += result[i]

            Timber.d("Contact name", arrayContacts[i].name)
            Timber.d("Contact phone", arrayContacts[i].phone)
            Timber.d("Contact type", arrayContacts[i].type)
        }

        rviewContacts = arrayContacts.toMutableList()

        runOnUiThread() {
            recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

            recyclerView.adapter = RecycleAdapter(this, arrayContacts)
        }
    }
}
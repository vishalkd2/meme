package com.example.meme

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener

class MainActivity : AppCompatActivity() {
    private val memeUrls: ArrayList<String> = ArrayList()
    private var currentMemeIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharebtn = findViewById<ImageButton>(R.id.share)
        val nextbtn = findViewById<Button>(R.id.nextbtn)
        val previousbtn = findViewById<Button>(R.id.previous)

        sharebtn.setOnClickListener {
            // Creating an Intent to share the meme with other apps.
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey, Checkout this cool meme ${memeUrls[currentMemeIndex]}"
            )
            val chooser = Intent.createChooser(intent, "Share this meme")
            startActivity(chooser)
        }

        nextbtn.setOnClickListener {
            loadMeme()
        }

        previousbtn.setOnClickListener {
            showPreviousMeme()
        }

        loadMeme()
    }

    private fun loadMeme() {
        // Initializing a progress bar which gets disabled whenever the image is loaded.
        val progressBar = findViewById<ProgressBar>(R.id.progressbar)
        progressBar.visibility = View.VISIBLE

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.com/gimme"

        // Request a JSON object response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val imageUrl = response.getString("url")
                memeUrls.add(imageUrl)
                currentMemeIndex = memeUrls.size - 1
                displayMeme()
                progressBar.visibility = View.GONE
            }
        ) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            progressBar.visibility = View.GONE
        }
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    private fun displayMeme() {
        val progressBar = findViewById<ProgressBar>(R.id.progressbar)
        val image = findViewById<ImageView>(R.id.image)

        Glide.with(this).load(memeUrls[currentMemeIndex])
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }
            }).into(image)
    }

    private fun showPreviousMeme() {
        if (currentMemeIndex > 0) {
            currentMemeIndex--
            displayMeme()
        } else {
            Toast.makeText(this, "No previous meme available", Toast.LENGTH_SHORT)
        }
    }
}
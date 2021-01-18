package com.sanxynet.kotlincoroutinebasics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonCoroutine = findViewById<Button>(R.id.buttonCoroutine)

        buttonCoroutine.setOnClickListener {
            setNewText("Starting Call........")

            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }

    }

    private fun setNewText(input: String){
        val textShowCoroutine = findViewById<TextView>(R.id.textShowCoroutine)

        val newText = textShowCoroutine.text.toString() + "\n$input"
        textShowCoroutine.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext (Main) {
            setNewText(input)
        }
    }

    private suspend fun fakeApiRequest() {
        logThread("fakeApiRequest")

        val result1 = getResult1FromApi() // wait until job is done

        if (result1 == "Result #1") {

            setTextOnMainThread("Got $result1")

            val result2 = getResult2FromApi() // wait until job is done

            if (result2 == "Result #2") {
                setTextOnMainThread("Got $result2")
            } else {
                setTextOnMainThread("Couldn't get Result #2")
            }
        } else {
            setTextOnMainThread("Couldn't get Result #1")
        }
    }


    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        /* Does not block thread. Just suspends the coroutine inside the thread */
        delay(1000)
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String {
        logThread("getResult2FromApi")
        delay(1000)
        return "Result #2"
    }

    private fun logThread(methodName: String){
        println("debug: ${methodName}: ${Thread.currentThread().name}")
        Log.d("debug", "Done: " + Thread.currentThread().name )
    }

}
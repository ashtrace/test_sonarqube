package com.world4tech.driveu

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.telephony.SmsManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.ncorti.slidetoact.SlideToActView
import com.world4tech.driveu.database.DBHelper
import com.world4tech.driveu.databinding.ActivitySosBinding
import java.lang.String
import kotlin.Exception
import kotlin.Long
import kotlin.toString


class SosActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySosBinding
    var counter = 10
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val i = intent?.extras?.getString("currentlocation").toString()
        val lat = intent?.extras?.getString("lat").toString()
        val lon = intent?.extras?.getString("lon").toString()
        //DB helper
        val db = DBHelper(this, null)
        // and add to name text view
        val cursor = db.getName()
        cursor!!.moveToFirst()
        binding.name.append(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl)) + "\n")
        binding.ephone1.append(cursor.getString(cursor.getColumnIndex(DBHelper.EPHONE_COL_ONE)) + "\n")
        binding.ephone2.append(cursor.getString(cursor.getColumnIndex(DBHelper.EPHONE_COL_TWO)) + "\n")
        while(cursor.moveToNext()){
            binding.name.append(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl)) + "\n")
            binding.ephone1.append(cursor.getString(cursor.getColumnIndex(DBHelper.EPHONE_COL_ONE)) + "\n")
            binding.ephone2.append(cursor.getString(cursor.getColumnIndex(DBHelper.EPHONE_COL_TWO)) + "\n")
        }
        cursor.close()
        println("Name is: ${binding.name.text} ephone1 is: ${binding.ephone1.text} ephone2 is: ${binding.ephone2.text}")
        val message = "${binding.name.text} ,Last location is $i & Latitude and Longitude is: $lat , $lon "
        binding.NameHere.text = binding.name.text.toString()
        //------------------
        val counttime: TextView = binding.countdown
        object : CountDownTimer(11000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                counttime.setText(String.valueOf(counter))
                counter--
            }
            override fun onFinish() {
                try {

                    val smsManager: SmsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(binding.ephone1.text.toString(), null, message, null, null)
                    smsManager.sendTextMessage(binding.ephone2.text.toString(), null, message, null, null)
                    makePhoneCall()
                    Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()

                } catch (e: Exception) {
                    Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }.start()
        binding.latitudeLocation.text = "$lat,$lon"
        binding.cancelNow.setOnClickListener {
            makePhoneCall()
        }
        binding.cancelNow.setOnClickListener {
            binding.countdown.visibility=View.INVISIBLE
            finish()

        }
        binding.accidentAttack.setOnClickListener {
            val name = binding.NameHere.text.toString()
            val i = Intent(this,HelpActivity::class.java)
            i.putExtra("btn_no","1")
            i.putExtra("name",name)
            startActivity(i)
        }
        binding.heartAttack.setOnClickListener {
            val name = binding.NameHere.text.toString()
            val i = Intent(this,HelpActivity::class.java)
            i.putExtra("btn_no","2")
            i.putExtra("name",name)
            startActivity(i)
        }
        binding.theftAttack.setOnClickListener {
            val name = binding.NameHere.text.toString()
            val i = Intent(this,HelpActivity::class.java)
            i.putExtra("btn_no","3")
            i.putExtra("name",name)
            startActivity(i)
        }
    }

    private fun makePhoneCall() {
        val phone_number: kotlin.String = binding.ephone1.text.toString()
        val phone_intent = Intent(Intent.ACTION_CALL)
        phone_intent.data = Uri.parse("tel:+91$phone_number")
        startActivity(phone_intent)
    }
}

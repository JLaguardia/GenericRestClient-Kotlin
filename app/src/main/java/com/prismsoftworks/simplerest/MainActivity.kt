package com.prismsoftworks.simplerest

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName
    val HOSTNAME_KEY = "hnPref"
    val PORT_KEY = "pPref"
    val ENDPOINT_KEY = "epPref"

    lateinit var userPrefs: SharedPreferences
    lateinit var lameTextListener: TextChangeListener
    var hostName = ""
    var port = ""
    var endpoint = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        init()
//        lameTextListener = TextChangeListener()
//        txtEndpoint.addTextChangedListener()
//        txtEndpoint.addTextChangedListener()
//        txtEndpoint.addTextChangedListener()
    }

    fun init(){
        userPrefs = getPreferences(Context.MODE_PRIVATE)
        setVars()
        setTexts()
        refreshUrl()
    }

    fun setVars(){
        hostName = userPrefs.getString(HOSTNAME_KEY, "")
        port = userPrefs.getString(PORT_KEY, "")
        endpoint = userPrefs.getString(ENDPOINT_KEY, "")
    }

    fun setTexts(){
        txtHostname.setText(hostName)
        txtPort.setText(port)
        txtEndpoint.setText(endpoint)
    }

    fun refreshUrl(){
        var res = "http://$hostName:$port"
        if(endpoint != "")
            res += "/$endpoint"

        txtUrlRaw.setText(res)
    }

    fun txtChange(Sender: View) {
        when (Sender.id) {

            R.id.txtHostname -> {
                hostName = (Sender as TextView).text.toString()
                refreshUrl()
            }
            R.id.txtPort -> {
                port = (Sender as TextView).text.toString()
                refreshUrl()
            }
            R.id.txtEndpoint -> {
                endpoint = (Sender as TextView).text.toString()
                refreshUrl()
            }
        }
    }

    private fun needPermission(permissions: Array<Int>): Boolean{
        for(pm in permissions){
            if(pm != PackageManager.PERMISSION_GRANTED){
                return true
            }
        }

        return false
    }

    fun checkPerms(permissions: Array<Int>, requested: Array<String>): Boolean{
        if(needPermission(permissions)){
            ActivityCompat.requestPermissions(this, requested, 0)
        } else {
            return true
        }

        return !needPermission(permissions)
    }

    fun btnGoClick(Sender: View){
        val perms = arrayOf(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET))
        val permsVal = arrayOf(Manifest.permission.INTERNET)

        hostName = txtHostname.text.toString()
        port = txtPort.text.toString()
        endpoint = txtEndpoint.text.toString()
        userPrefs.edit().putString(HOSTNAME_KEY, hostName).apply()
        userPrefs.edit().putString(PORT_KEY, port).apply()
        userPrefs.edit().putString(ENDPOINT_KEY, endpoint).apply()
        refreshUrl()
        if(!checkPerms(perms, permsVal)){
            return
        }
        //this or try retrofit and okhttp3
        val queue = Volley.newRequestQueue(this)
        val url = "${txtUrlRaw.text}"


        val jsonReq = JsonArrayRequest(Request.Method.GET, url, null, Response.Listener<JSONArray> { response ->
            lblRawOutput.text = "Response: \n$response"


        }, Response.ErrorListener { error ->  lblRawOutput.text = "error: \n${error.message}" })

//        val str = StringRequest(Request.Method.GET, url, Response.Listener { response ->
//            lblRawOutput.text = "output: \n$response"
//        }, Response.ErrorListener { err -> lblRawOutput.text = "error: ${err.message}" })

        queue.add(jsonReq)
    }

}

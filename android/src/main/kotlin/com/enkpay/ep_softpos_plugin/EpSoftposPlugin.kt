package com.enkpay.ep_softpos_plugin

import android.content.Intent
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import com.enkpay.ep_softpos_plugin.UI.activities.LaunchActivity
import com.enkpay.ep_softpos_plugin.utils.Encryption
import com.google.gson.Gson
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.util.*

/** EpSoftposPlugin */
class EpSoftposPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  private lateinit var channel : MethodChannel
  var activity: FragmentActivity? = null


  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "ep_softpos_plugin")
    channel.setMethodCallHandler(this)
  }


  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {


    when (call.method) {
        "getPlatformVersion" -> {
          result.success("Android ${android.os.Build.VERSION.RELEASE}")
        }
        "launchSDK" -> {
          val intent =   Intent(activity, LaunchActivity::class.java)
          activity?.startActivity(intent);
          result.success(null);
        }

      "processPayment" ->{
        val data  = call.arguments as HashMap<*, *>
        println(data)
//        val jsonString =
//          "{\"RRN\":\"202212033039\",\"STAN\":\"113800\",\"amount\":200,\"cardName\":\"CUSTOMER\",\"expireDate\":\"2607\",\"message\":\"Invalid response\",\"pan\":\"468588xxxxxx0125\",\"responseCode\":\"20\",\"terminalID\":\"2033ALZP\",\"transactionType\":\"PURCHASE\"}"
//        val encryptData =
//          Encryption.encrypt( jsonString);
//        val decryptData =  Encryption.decrypt( "SK6tSsbN4D2iHvUVFtujqBfq3CWoO18+lXzRg8GdCq5Q52PRw49YRzlk8bFO4dKn2Sy3tJrINMePm6poGz/YHdgvJufIgd/Cbcq9/St2TXvXPTZtoLVgYvrQNPr127hKsXuf5nziA+sU+iw6bI/QLCyqIdaUlF17hH/qAYVgvCk7ximyXKloDJ5GJtY+RfkpQblYxYglYgqAwc3THll607yHTfExfLWRgGchadHYf0REebWXllohRVPKSRzhd4mS6/eXz7yP6p8tb5FOBDeOA87nVkrSqI8sRsW6/znlvN0=")
////          Encryption.stringEncryption("encrypt", "hhhhhhhhhhhhhhhhhhh")
//
//        println(encryptData);
//        println(decryptData);
        val intent =   Intent(activity, EPaymentActivity::class.java)
        intent.putExtra("data", data);
        activity?.startActivity(intent);
        result.success(null);

        }
      "makePayment" ->{


      }


        else -> {

          result.notImplemented()
        }
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity as FragmentActivity
  }

  override fun onDetachedFromActivityForConfigChanges() {

  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity  as FragmentActivity
  }

  override fun onDetachedFromActivity() {
  }
}

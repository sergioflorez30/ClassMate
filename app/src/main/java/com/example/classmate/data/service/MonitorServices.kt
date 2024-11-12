package com.example.classmate.data.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.DecimalFormat
import android.net.Uri
import android.util.Log
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.OpinionsAndQualifications
import com.example.classmate.domain.model.RequestBroadcast
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.math.round
import kotlin.math.roundToInt

interface MonitorServices {

    suspend fun createMonitor(monitor: Monitor)
    suspend fun  getMonitorById(id:String): Monitor?
    suspend fun uploadProfileImage(id: String, uri: Uri, context: Context): String
    suspend fun updateMonitorField(id: String, field: String, value: Any)
    suspend fun updateMonitorImageUrl(id:String,url: String)
    suspend fun getMonitors(limit: Int, monitor: Monitor?):List<Monitor?>
    suspend fun getBroadRequest(limit: Int, broadRequest: RequestBroadcast?): List<RequestBroadcast>
    suspend fun calificateMonitor(opinionsAndQualifications: OpinionsAndQualifications,monitorId:String, )

}

class MonitorServicesImpl: MonitorServices {
    override suspend fun createMonitor(monitor: Monitor) {
        Firebase.firestore
            .collection("Monitor")
            .document(monitor.id)
            .set(monitor)
            .await()
    }

    override suspend fun getMonitorById(id: String): Monitor? {
        val user = Firebase.firestore
            .collection("Monitor")
            .document(id)
            .get()
            .await()
        val userObject = user.toObject(Monitor::class.java)
        return userObject
    }
    override suspend fun uploadProfileImage(id: String, uri: Uri, context: Context, ): String  {
        val storageRef = Firebase.storage.reference.child("images/monitors/$id.jpg")
        val bitmap= withContext(Dispatchers.IO){
            val inputStream = context.contentResolver.openInputStream(uri)

            BitmapFactory.decodeStream(inputStream)
        }
        val compressedBitmapStream= ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, compressedBitmapStream)

        val compressedData = compressedBitmapStream.toByteArray()

        storageRef.putBytes(compressedData).await()

        return storageRef.downloadUrl.await().toString()
    }

    override suspend fun updateMonitorField(id: String, field: String, value: Any) {
        Firebase.firestore
            .collection("Monitor")
            .document(id)
            .update(field, value)
            .await()
    }

    override suspend fun updateMonitorImageUrl(id:String,url: String) {
        Firebase.firestore
            .collection("Monitor")
            .document(id)
            .update("photoUrl", url)
            .await()
    }

    override suspend fun getMonitors(limit: Int, monitor: Monitor?): List<Monitor> {
        return try {
            val querySnapshot = Firebase.firestore.collection("Monitor")
                .orderBy("id")
                .startAfter(monitor?.id)
                .limit(limit.toLong())
                .get()
                .await()

            querySnapshot.documents.mapNotNull { it.toObject(Monitor::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    override suspend fun getBroadRequest(limit: Int, broadRequest: RequestBroadcast?): List<RequestBroadcast> {
        return try {
            val querySnapshot = Firebase.firestore.collection("requestBroadcast")
                .orderBy("id")
                .startAfter(broadRequest?.id)
                .limit(limit.toLong())
                .get()
                .await()
            querySnapshot.documents.mapNotNull { it.toObject(RequestBroadcast::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun calificateMonitor(
        opinionsAndQualifications: OpinionsAndQualifications,
        monitorId: String
    ) {
        Firebase.firestore.collection("Monitor")
            .document(monitorId)
            .collection("calificationsOpinions")
            .document(opinionsAndQualifications.id)
            .set(opinionsAndQualifications)
            .await()

        val califications = getCalifications(monitorId)

        val averageRating = califications.average()
            .toBigDecimal()
            .setScale(1, java.math.RoundingMode.HALF_UP)
            .toDouble()

        Firebase.firestore.collection("Monitor")
            .document(monitorId)
            .update("rating", averageRating)
            .await()

    }
    suspend fun getCalifications(monitorId: String): List<Int> {
        val calificationsList = mutableListOf<Int>()
        val querySnapshot = Firebase.firestore.collection("Monitor")
            .document(monitorId)
            .collection("calificationsOpinions")
            .get()
            .await()

        for (document in querySnapshot) {
            val calification = document.getLong("calification")?.toInt()
            if (calification != null) {
                calificationsList.add(calification)
            }
        }
        return calificationsList
    }
}

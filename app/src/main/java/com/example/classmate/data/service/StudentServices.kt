package com.example.classmate.data.service


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.Student
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


interface StudentServices {

    suspend fun createStudent(student: Student)
    suspend fun  getStudentById(id:String):Student?
    suspend fun uploadProfileImage(id: String,uri: Uri,context: Context): String
    suspend fun updateStudentField(id: String, field: String, value: Any)
    suspend fun updateStudentImageUrl(id:String,url: String)
}

class StudentServicesImpl: StudentServices {
    override suspend fun createStudent(student: Student) {
        Firebase.firestore
            .collection("student")
            .document(student.id)
            .set(student)
            .await()
    }

    override suspend fun getStudentById(id: String): Student? {
        val user = Firebase.firestore
            .collection("student")
            .document(id)
            .get()
            .await()
        val userObject = user.toObject(Student::class.java)
        return userObject
    }

    override suspend fun uploadProfileImage(id: String,uri: Uri,context: Context): String  {
        val storageRef = Firebase.storage.reference.child("images/students/$id.jpg")
        val bitmap = withContext(Dispatchers.IO) {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        }

        val compressedBitmapStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, compressedBitmapStream)

        val compressedData = compressedBitmapStream.toByteArray()

        storageRef.putBytes(compressedData).await()

        return storageRef.downloadUrl.await().toString()
    }

    override suspend fun updateStudentField(id: String, field: String, value: Any) {
        Firebase.firestore
            .collection("student")
            .document(id)
            .update(field, value)
            .await()
    }

    override suspend fun updateStudentImageUrl(id:String,url: String) {
        Firebase.firestore
            .collection("student")
            .document(id)
            .update("photo", url)
            .await()

    }


}
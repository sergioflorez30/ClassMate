package com.example.classmate.data.repository

import android.net.Uri
import com.example.classmate.data.service.AppointmentChatService
import com.example.classmate.data.service.AppointmentChatServiceImpl
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Message
import com.google.firebase.ktx.Firebase

import com.google.firebase.auth.ktx.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID

interface AppointmentChatRepository {
    suspend fun sendMessage(message: Message, uri: Uri?, appointmentId: String)
    suspend fun getMessages(appointmentId: String): List<Message?>
    suspend fun getLiveMessages(appointmentId: String, callback: suspend (Message) -> Unit)
    suspend fun getAppointmentById(appointmentId: String): Appointment

}
class AppointmentChatRepositoryImpl(val chatService: AppointmentChatService = AppointmentChatServiceImpl()) : AppointmentChatRepository {

    override suspend fun sendMessage(message: Message, uri: Uri?, appointmentId: String) {
        uri?.let {
            val imageID = UUID.randomUUID().toString()
            message.imageID = imageID
            chatService.uploadImage(it, imageID)
        }
        message.authorID = Firebase.auth.currentUser?.uid ?: ""
        chatService.sendMessage(message, appointmentId)
    }


    override suspend fun getMessages(appointmentId: String): List<Message?> {
        return chatService.getMessages(appointmentId)
    }

    override suspend fun getLiveMessages(appointmentId: String, callback: suspend (Message) -> Unit) {
        chatService.getLiveMessages(appointmentId) { doc ->
            val message = doc.toObject(Message::class.java)
            message?.imageID?.let {
                val url = chatService.getImageURLByID(it)
                message.imageURL = url
            }
            message.isMine = message.authorID == Firebase.auth.currentUser?.uid

            // Solo marca como leído si el mensaje no es del autor actual
            if (message.authorID != Firebase.auth.currentUser?.uid && !message.isRead) {
                chatService.markMessageAsRead(appointmentId, message.id, Firebase.auth.currentUser?.uid ?: "")
            }

            callback(message)
        }
    }


    override suspend fun getAppointmentById(appointmentId: String): Appointment {
        return chatService.getAppointmentById(appointmentId)
    }
}

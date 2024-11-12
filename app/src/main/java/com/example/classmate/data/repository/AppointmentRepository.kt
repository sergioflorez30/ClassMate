package com.example.classmate.data.repository

import com.example.classmate.data.service.AppointmentService
import com.example.classmate.data.service.AppointmentServiceImpl
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.RequestBroadcast
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID


interface  AppointmentRepository {
    suspend fun createAppoinment(appointment: Appointment)
    suspend fun verifyAppointment(date:Timestamp)
}
class AppointmentRepositoryImpl(
    val appointmentServices : AppointmentService = AppointmentServiceImpl()
): AppointmentRepository {

    override suspend fun createAppoinment(appointment: Appointment) {
        val appointmentId = UUID.randomUUID().toString()
        val appointmentWithId = appointment.copy(id = appointmentId)
        appointmentServices.createAppointmentInGeneral(appointmentWithId)
        appointmentServices.createAppointmentForStudent(appointmentWithId)
        appointmentServices.createAppointmentForMonitor(appointmentWithId)
    }

    override suspend fun verifyAppointment(date: Timestamp) {
        val user= Firebase.auth.currentUser!!.uid
        appointmentServices.verifyAppointmentForStudent(date,user)
    }
}
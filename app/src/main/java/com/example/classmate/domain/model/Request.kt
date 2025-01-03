package com.example.classmate.domain.model

import com.google.firebase.Timestamp

data class Request(
    var id: String = "",
    var idNotification: String = "",
    var mode_class: String = "",
    var type: String = "",
    var dateInitial: Timestamp = Timestamp.now(),
    var dateFinal: Timestamp = Timestamp.now(),
    var description: String = "",
    var place: String = "",
    var subjectId: String = "",
    var subjectname: String= "",
    var studentId: String = "",
    var studentName: String = "",
    var monitorId: String = "",
    var monitorName: String = ""
)

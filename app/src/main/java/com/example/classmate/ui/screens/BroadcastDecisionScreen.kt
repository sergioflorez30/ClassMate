package com.example.classmate.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.classmate.R
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.Type_Notification
import com.example.classmate.ui.components.DropdownMenuItemWithSeparator
import com.example.classmate.ui.viewModel.AppointmentViewModel
import com.example.classmate.ui.viewModel.RequestBroadcastStudentViewModel
import com.example.classmate.ui.viewModel.UnicastMonitoringViewModel
import com.google.firebase.Timestamp
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID


@Composable
fun BroadcastDecisionScreen(
    navController: NavController,
    request: String?,
    monitor: String?,
    appointmentViewModel: AppointmentViewModel = viewModel(),
    unicastMonitoringViewModel: UnicastMonitoringViewModel = viewModel(),
    requestBroadcastStudentViewmodel: RequestBroadcastStudentViewModel = viewModel()
    ) {
    val authState by appointmentViewModel.authState.observeAsState()


    val scope = rememberCoroutineScope()
    Log.e(">>>", request!!)
    val requestObj: RequestBroadcast = Gson().fromJson(request, RequestBroadcast::class.java)
    val monitorObj: Monitor = Gson().fromJson(monitor, Monitor::class.java)
    var image = monitor
    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color(0xFF209619)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(Modifier.align(Alignment.TopStart)) {
                        IconButton(
                            onClick = {
                                navController.navigate("HomeMonitorScreen")
                            },
                            modifier = Modifier
                                .size(50.dp)
                                .offset(y = (25.dp))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back Icon",
                                modifier = Modifier.size(50.dp),
                                tint = Color.White
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.classmatelogo),
                            contentDescription = "classMateLogo",
                            modifier = Modifier
                                .padding(start = 2.dp, bottom = 0.dp)
                                .width(150.dp)
                                .aspectRatio(1f),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 24.dp, top = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .aspectRatio(1f)
                                .background(Color.Transparent)
                                .clickable(onClick = { /* TODO: Acción de ayuda */ })
                        ) {
                            IconButton(
                                onClick = { },
                                modifier = Modifier
                                    .width(50.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.notifications),
                                    contentDescription = "Ayuda",
                                    tint = Color.White,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .width(50.dp)
                                .offset(y = 5.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.live_help),
                                contentDescription = "Ayuda",
                                tint = Color.White,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        IconButton(
                            onClick = { expanded = true },
                            modifier = Modifier
                                .clip(CircleShape)
                                .width(50.dp)
                                .aspectRatio(1f)
                        ) {
                            Column {
                                Image(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    painter = rememberAsyncImagePainter(
                                        image,
                                        error = painterResource(R.drawable.botonestudiante)
                                    ),
                                    contentDescription = "Foto de perfil",
                                    contentScale = ContentScale.Crop
                                )

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier
                                        .background(Color(0xFFCCD0CF))
                                        .border(1.dp, Color.Black)
                                        .padding(2.dp)
                                ) {
                                    DropdownMenuItemWithSeparator("Tu perfil", onClick = {
                                        navController.navigate("studentProfile")
                                    }, onDismiss = { expanded = false })

                                    DropdownMenuItemWithSeparator("Cerrar sesión", onClick = {
                                    }, onDismiss = { expanded = false })
                                }
                            }
                        }
                    }
                }
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start // Alineación a la izquierda
            ) {
                // Header section
                Text(
                    text = requestObj.studentName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = requestObj.subjectname,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tipo de ayuda: ${requestObj.type}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {

                // Schedule section

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start // Alineación a la izquierda
                ) {
                    Text(
                        text = "Horarios:",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Column {
                        val spanishLocale = Locale("es", "ES")
                        val dateFormatter = SimpleDateFormat(
                            "EEEE dd 'de' MMMM 'de' yyyy 'a las' HH:mm",
                            spanishLocale
                        )

                        Text(
                            text = dateFormatter.format(requestObj.dateInitial.toDate()),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = dateFormatter.format(requestObj.dateFinal.toDate()),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start // Alineación a la izquierda
                ) {
                    Column {
                        Text(
                            text = "Presencial/Virtual:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        if (requestObj.mode_class == "Virtual") {
                            Text(requestObj.mode_class)

                        } else {
                            Text("${requestObj.mode_class}, en : ${requestObj.mode_class}")

                        }

                    }

                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start // Alineación a la izquierda
                ) {
                    Text(
                        text = "Descripción:",
                        modifier = Modifier.padding(bottom = 4.dp),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    ) {
                        Text(
                            text = requestObj.description,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                }


            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Buttons section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        appointmentViewModel.createAppointment(
                            Appointment(
                                "",
                                requestObj.mode_class,
                                requestObj.type,
                                requestObj.dateInitial,
                                requestObj.dateFinal,
                                requestObj.description,
                                requestObj.place,
                                requestObj.subjectID,
                                requestObj.subjectname,
                                requestObj.studentId,
                                requestObj.studentName,
                                monitorObj.id,
                                monitorObj.name
                            )
                        )
                        requestBroadcastStudentViewmodel.deleteRequest(
                            requestObj.studentId,
                            requestObj.subjectID,
                            requestObj.id
                        )
                        requestBroadcastStudentViewmodel.createNotification(
                            Notification(
                                UUID.randomUUID().toString(),
                                Timestamp.now(),
                                requestObj.dateInitial,
                                "¡Se ha aceptado tu monitoria!",
                                requestObj.subjectname,
                                requestObj.studentId,
                                requestObj.studentName,
                                monitorObj.id,
                                monitorObj.name,
                                Type_Notification.ACEPTACION
                            )
                        )
                    },
                    modifier = Modifier
                        .size(width = 160.dp, height = 48.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(24.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE0E0E0),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Aceptar",
                        modifier = Modifier.size(24.dp)
                    )
                }

                Button(
                    onClick = {
                        requestBroadcastStudentViewmodel.deleteRequest(
                            requestObj.studentId,
                            requestObj.subjectID,
                            requestObj.id
                        )
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar("Monitoria Rechazada.")
                        }
                        navController.navigate("HomeMonitorScreen")
                    },
                    modifier = Modifier
                        .size(width = 160.dp, height = 48.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(24.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE0E0E0),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Rechazar",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }


            Box(modifier = Modifier.weight(0.1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color(0xFF209619)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.weight(0.1f))
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .background(color = Color(0xFF026900), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = { navController.navigate("MonitorRequest") }) {
                            Icon(
                                painter = painterResource(id = R.drawable.people),
                                contentDescription = "calendario",
                                modifier = Modifier
                                    .size(52.dp)
                                    .padding(4.dp),
                                tint = Color.White
                            )
                        }

                    }

                    Box(modifier = Modifier.weight(0.1f))

                    IconButton(onClick = { navController.navigate("HomeMonitorScreen") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_home),
                            contentDescription = "calendario",
                            modifier = Modifier
                                .size(52.dp)
                                .padding(2.dp)
                                .offset(y = -(2.dp)),
                            tint = Color.White
                        )
                    }

                    Box(modifier = Modifier.weight(0.1f))
                    IconButton(onClick = { navController.navigate("CalendarMonitor") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendario),
                            contentDescription = "calendario",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(4.dp),
                            tint = Color.White
                        )
                    }
                    Box(modifier = Modifier.weight(0.1f))
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.message),
                            contentDescription = "calendario",
                            modifier = Modifier
                                .size(52.dp)
                                .padding(2.dp),
                            tint = Color.White
                        )
                    }
                    Box(modifier = Modifier.weight(0.1f))
                }
            }
        }
        if (authState == 1) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }
        } else if (authState == 2) {

            LaunchedEffect(Unit) {
                scope.launch {
                    // Asegúrate de mostrar el Snackbar
                    snackbarHostState.currentSnackbarData?.dismiss()
                    val snackbarResult = snackbarHostState.showSnackbar(
                        "Ya tienes una cita a esa hora, monitoria rechazada.",
                        duration = SnackbarDuration.Long // Duración del Snackbar (Short, Long, Indefinite)
                    )

                    // Retraso adicional para asegurar que el usuario pueda leer el mensaje.
                    delay(2000) // 2 segundos extra

                    // Navega después del retraso.
                    navController.navigate("MonitorRequest")
                }
            }


        } else if (authState == 3) {

            LaunchedEffect(Unit) {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar("Monitoria Aceptada")
                }
            }
            navController.navigate("MonitorRequest")
        }

    }

}





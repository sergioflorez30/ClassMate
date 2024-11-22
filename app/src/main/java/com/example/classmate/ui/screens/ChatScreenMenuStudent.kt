package com.example.classmate.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.classmate.R
import com.example.classmate.domain.model.Student
import com.example.classmate.ui.components.DropdownMenuItemWithSeparator
import com.example.classmate.ui.viewModel.ChatMenuStudentViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@Composable
fun ChatScreenMenuStudent(navController: NavController,chatMenuStudentViewModel: ChatMenuStudentViewModel = viewModel()) {
    val studentObj: Student? by chatMenuStudentViewModel.student.observeAsState(initial = null)
    val studentState by chatMenuStudentViewModel.studentState.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val image:String? by chatMenuStudentViewModel.image.observeAsState()
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val appointmentsState by chatMenuStudentViewModel.appointmentList.observeAsState(emptyList())

    if(studentObj?.photo?.isNotEmpty() == true){
        studentObj?.let { chatMenuStudentViewModel.getStudentImage(it.photo) }
    }

    LaunchedEffect(true) {
        chatMenuStudentViewModel.getStudent()
    }
    LaunchedEffect(true) {
        chatMenuStudentViewModel.getAppointments()
    }


    Scaffold(modifier = Modifier.fillMaxSize()) { innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding),
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
                        .weight(1f)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.encabezadoestudaintes),
                        contentDescription = "Encabezado",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Image(
                        painter = painterResource(id = R.drawable.classmatelogo),
                        contentDescription = "classMateLogo",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 2.dp, top = 0.dp)
                            .width(200.dp)
                            .aspectRatio(1f),
                        contentScale = ContentScale.Fit
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 24.dp, top = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .aspectRatio(1f)
                                .background(Color.Transparent)
                                .clickable(onClick = { navController.navigate("HelpStudent") })
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.live_help),
                                contentDescription = "Ayuda",
                                tint = Color.White,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        // Botón de foto de perfil
                        IconButton(
                            onClick = { expanded = true },
                            modifier = Modifier
                                .clip(CircleShape)
                                .width(50.dp)
                                .aspectRatio(1f)
                        ) {
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
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .background(Color(0xFFCCD0CF))
                                .border(1.dp, Color.Black)
                                .padding(4.dp)
                        ) {
                            DropdownMenuItemWithSeparator("Tu perfil", onClick = {
                                navController.navigate("studentProfile")
                            }, onDismiss = { expanded = false })

                            DropdownMenuItemWithSeparator("Solicitud de monitoria", onClick = {
                                navController.navigate("requestBroadcast")
                            }, onDismiss = { expanded = false })

                            DropdownMenuItemWithSeparator("Cerrar sesión", onClick = {
                            }, onDismiss = { expanded = false })
                        }
                    }
                }
            }

            Column(modifier = Modifier.verticalScroll(scrollState)) {
                appointmentsState.forEach { appointment ->
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp,
                        ), modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                verticalArrangement = Arrangement.spacedBy((-5).dp)
                            ) {
                                androidx.compose.material3.Text(
                                    text = "${appointment?.monitorName}",
                                    color = Color.Blue,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                                androidx.compose.material3.Text(
                                    text = ("Materia: ${appointment?.subjectname}"),
                                    fontSize = 12.sp,
                                )
                            }
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(horizontal = 5.dp)
                                ) {
                                    IconButton(onClick = {
                                        val appointmentId = appointment?.id
                                        val type = true
                                        navController.navigate("AppointmentChat/$appointmentId/${type.toString()}")
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.PlayArrow,
                                            contentDescription = "Arrow",
                                            modifier = Modifier.size(50.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Box(modifier = Modifier.weight(0.1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color(0xFF3F21DB)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.weight(0.1f))

                        IconButton(onClick = { navController.navigate("CalendarStudent") }){
                            Icon(
                                painter = painterResource(id = R.drawable.calendario),
                                contentDescription = "calendario",
                                modifier = Modifier
                                    .size(52.dp)
                                    .padding(4.dp),
                                tint = Color.White
                            )
                        }

                    Box(modifier = Modifier.weight(0.1f))
                    IconButton(onClick = { navController.navigate("HomeStudentScreen") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_home),
                            contentDescription = "calendario",
                            modifier = Modifier
                                .size(52.dp)
                                .padding(4.dp),
                            tint = Color.White
                        )
                    }

                    Box(modifier = Modifier.weight(0.1f))
                    IconButton(onClick = {navController.navigate("notificationStudentPrincipal")}) {
                        Icon(
                            painter = painterResource(id = R.drawable.notifications),
                            contentDescription = "calendario",
                            modifier = Modifier
                                .size(52.dp)
                                .padding(4.dp),
                            tint = Color.White
                        )
                    }

                    Box(modifier = Modifier.weight(0.1f))
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .background(color = Color(0xFFCCD0CF), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.message),
                                contentDescription = "calendario",
                                modifier = Modifier
                                    .size(52.dp)
                                    .padding(4.dp),
                                tint = Color(0xFF3F21DB)
                            )
                        }
                    }
                    Box(modifier = Modifier.weight(0.1f))
                }
            }
        }
    }

}
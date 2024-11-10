package com.example.classmate.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.AuthRepositoryImpl
import com.example.classmate.data.repository.MonitorAuthRepository
import com.example.classmate.data.repository.MonitorAuthRepositoryImpl
import com.example.classmate.data.repository.StudentAuthRepository
import com.example.classmate.data.repository.SubjectRepository
import com.example.classmate.data.repository.SubjectRepositoryImpl
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Student
import com.example.classmate.domain.model.Subject
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonitorSignupViewModel(
    val repo: MonitorAuthRepository = MonitorAuthRepositoryImpl(),
    val repoSubjects: SubjectRepository = SubjectRepositoryImpl()
): ViewModel() {
    val authState = MutableLiveData(0)
    //0. Idle
    //1. Loading
    //2. Error
    //3. Success
    val subjects = MutableLiveData<List<Subject>>(emptyList())


    fun getSubject() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { }
            try {
                val subjectsList = repoSubjects.getAllSubjects()
                withContext(Dispatchers.Main) {
                    subjects.value = subjectsList
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                }
                ex.printStackTrace()
            }
        }
    }

    fun signup(monitor: Monitor, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { authState.value = 1 }
            try {
                repo.signup(monitor, password)
                withContext(Dispatchers.Main) { authState.value = 3 }
            } catch (ex: FirebaseAuthException) {
                withContext(Dispatchers.Main) { authState.value = 2 }
                ex.printStackTrace()
            }
        }
    }

}
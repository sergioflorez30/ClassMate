package com.example.classmate.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.MonitorRepository
import com.example.classmate.data.repository.MonitorRepositoryImpl
import com.example.classmate.data.repository.StudentRepository
import com.example.classmate.data.repository.StudentRepositoryImpl
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeStudentViewModel(val repo: StudentRepository = StudentRepositoryImpl(),
                            val repoMonitor: MonitorRepository = MonitorRepositoryImpl()): ViewModel() {
    private val _student = MutableLiveData<Student?>(Student())
    val student: LiveData<Student?> get() = _student
    val studentState = MutableLiveData<Int?>(0)
    private val _monitorList = MutableLiveData(listOf<Monitor?>())
    val monitorList: LiveData<List<Monitor?>> get() = _monitorList
    fun getStudent0() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {studentState.value = 1}
            try {
                val currentUser = repo.getCurrentStudent()
                withContext(Dispatchers.Main) {
                    if (currentUser != null) {
                        _student.value = currentUser
                        withContext(Dispatchers.Main) {studentState.value = 3}
                    } else {
                        _student.value = null
                        withContext(Dispatchers.Main) {studentState.value = 2}
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _student.value = null
                    studentState.value = 2
                    Log.e("ViewModel", "Error fetching student info: ${e.message}")
                }

            }
        }
    }
    fun getStudent() {
        viewModelScope.launch(Dispatchers.IO) {
            val me = repo.getCurrentStudent()
            withContext(Dispatchers.Main) {
                _student.value = me
            }
        }
    }
    fun getMonitors() {
        viewModelScope.launch(Dispatchers.IO) {
            val monitors = repoMonitor.getMonitors()
            withContext(Dispatchers.Main) {
                _monitorList.value = monitors
            }
        }
    }
}
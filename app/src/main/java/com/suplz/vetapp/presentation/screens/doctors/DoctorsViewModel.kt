package com.suplz.vetapp.presentation.screens.doctors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suplz.vetapp.domain.DeleteDoctorUseCase
import com.suplz.vetapp.domain.Doctor
import com.suplz.vetapp.domain.GetAllDoctorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorsViewModel @Inject constructor(
    private val getAllDoctorsUseCase: GetAllDoctorsUseCase,
    private val deleteDoctorUseCase: DeleteDoctorUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DoctorsState())
    val state = _state.asStateFlow()

    init {
        getAllDoctorsUseCase()
            .onEach { doctors -> _state.update { it.copy(doctors = doctors) } }
            .launchIn(viewModelScope)
    }

    fun processCommand(command: DoctorsCommand) {
        when (command) {
            is DoctorsCommand.DeleteDoctor -> {
                viewModelScope.launch {
                    deleteDoctorUseCase(command.doctorId)
                }
            }
        }
    }
}

sealed interface DoctorsCommand {
    data class DeleteDoctor(val doctorId: Int) : DoctorsCommand
}

data class DoctorsState(
    val doctors: List<Doctor> = emptyList()
)
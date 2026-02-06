package com.suplz.vetapp.presentation.screens.appointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suplz.vetapp.domain.AddAppointmentUseCase
import com.suplz.vetapp.domain.Doctor
import com.suplz.vetapp.domain.GetAllDoctorsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AddAppointmentViewModel.Factory::class)
class AddAppointmentViewModel @AssistedInject constructor(
    private val addAppointmentUseCase: AddAppointmentUseCase,
    private val getAllDoctorsUseCase: GetAllDoctorsUseCase,
    @Assisted("patientId") private val patientId: Int
) : ViewModel() {

    private val _state = MutableStateFlow<AddAppointmentState>(AddAppointmentState.Input())
    val state = _state.asStateFlow()

    init {
        getAllDoctorsUseCase()
            .onEach { doctors ->
                _state.update { prev ->
                    if (prev is AddAppointmentState.Input) prev.copy(availableDoctors = doctors) else prev
                }
            }
            .launchIn(viewModelScope)
    }

    fun processCommand(command: AddAppointmentCommand) {
        when (command) {
            AddAppointmentCommand.Back -> _state.update { AddAppointmentState.Finished }
            is AddAppointmentCommand.SelectDoctor -> updateState { it.copy(selectedDoctor = command.doctor) }
            is AddAppointmentCommand.InputDiagnosis -> updateState { it.copy(diagnosis = command.value) }
            is AddAppointmentCommand.InputPrescription -> updateState { it.copy(prescription = command.value) }
            AddAppointmentCommand.Save -> {
                val current = _state.value
                if (current is AddAppointmentState.Input && current.isSaveEnabled) {
                    viewModelScope.launch {
                        addAppointmentUseCase(
                            patientId = patientId,
                            doctorId = current.selectedDoctor!!.id,
                            diagnosis = current.diagnosis,
                            prescription = current.prescription
                        )
                        _state.update { AddAppointmentState.Finished }
                    }
                }
            }
        }
    }

    private fun updateState(update: (AddAppointmentState.Input) -> AddAppointmentState.Input) {
        _state.update { prev -> if (prev is AddAppointmentState.Input) update(prev) else prev }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("patientId") patientId: Int): AddAppointmentViewModel
    }
}

sealed interface AddAppointmentCommand {
    data class SelectDoctor(val doctor: Doctor) : AddAppointmentCommand
    data class InputDiagnosis(val value: String) : AddAppointmentCommand
    data class InputPrescription(val value: String) : AddAppointmentCommand
    data object Save : AddAppointmentCommand
    data object Back : AddAppointmentCommand
}

sealed interface AddAppointmentState {
    data class Input(
        val availableDoctors: List<Doctor> = emptyList(),
        val selectedDoctor: Doctor? = null,
        val diagnosis: String = "",
        val prescription: String = ""
    ) : AddAppointmentState {
        val isSaveEnabled: Boolean get() = selectedDoctor != null && diagnosis.isNotBlank() && prescription.isNotBlank()
    }
    data object Finished : AddAppointmentState
}
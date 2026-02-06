package com.suplz.vetapp.presentation.screens.editing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suplz.vetapp.domain.Appointment
import com.suplz.vetapp.domain.DeleteAppointmentUseCase
import com.suplz.vetapp.domain.DeletePatientUseCase
import com.suplz.vetapp.domain.EditPatientUseCase
import com.suplz.vetapp.domain.GetPatientAppointmentsUseCase
import com.suplz.vetapp.domain.GetPatientUseCase
import com.suplz.vetapp.domain.Patient
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

@HiltViewModel(assistedFactory = EditPatientViewModel.Factory::class)
class EditPatientViewModel @AssistedInject constructor(
    private val editPatientUseCase: EditPatientUseCase,
    private val getPatientUseCase: GetPatientUseCase,
    private val deletePatientUseCase: DeletePatientUseCase,
    private val getAppointmentsUseCase: GetPatientAppointmentsUseCase,
    private val deleteAppointmentUseCase: DeleteAppointmentUseCase,
    @Assisted("patientId") private val patientId: Int
) : ViewModel() {

    private val _state = MutableStateFlow<EditPatientState>(EditPatientState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val patient = getPatientUseCase(patientId)
            _state.update { EditPatientState.Editing(patient) }

            getAppointmentsUseCase(patientId)
                .onEach { appointments ->
                    _state.update { prev ->
                        if (prev is EditPatientState.Editing) prev.copy(appointments = appointments) else prev
                    }
                }
                .launchIn(this)
        }
    }

    fun processCommand(command: EditPatientCommand) {
        when (command) {
            EditPatientCommand.Back -> _state.update { EditPatientState.Finished }
            is EditPatientCommand.InputName -> updatePatient { it.copy(name = command.value) }
            is EditPatientCommand.InputSpecies -> updatePatient { it.copy(species = command.value) }
            is EditPatientCommand.InputBreed -> updatePatient { it.copy(breed = command.value) }
            is EditPatientCommand.InputOwner -> updatePatient { it.copy(ownerName = command.value) }
            is EditPatientCommand.InputPhone -> {
                val filteredPhone = command.value.filter { it.isDigit() }
                updatePatient { it.copy(phoneNumber = filteredPhone) }
            }
            EditPatientCommand.Save -> {
                val currentState = validate(isFinal = true)
                if (currentState is EditPatientState.Editing && currentState.isSaveEnabled) {
                    viewModelScope.launch {
                        editPatientUseCase(currentState.patient)
                        _state.update { EditPatientState.Finished }
                    }
                }
            }
            EditPatientCommand.Delete -> {
                viewModelScope.launch {
                    val currentState = _state.value
                    if (currentState is EditPatientState.Editing) {
                        deletePatientUseCase(currentState.patient.id)
                        _state.update { EditPatientState.Finished }
                    }
                }
            }
            is EditPatientCommand.DeleteAppointment -> {
                viewModelScope.launch {
                    deleteAppointmentUseCase(command.appointmentId)
                }
            }
        }
    }

    private fun updatePatient(update: (Patient) -> Patient) {
        _state.update { previous ->
            if (previous is EditPatientState.Editing) {
                validate(previous.copy(patient = update(previous.patient)))
            } else {
                previous
            }
        }
    }

    private fun validate(state: EditPatientState = _state.value, isFinal: Boolean = false): EditPatientState {
        if (state !is EditPatientState.Editing) return state
        val patient = state.patient
        val nameError = if (patient.name.isBlank() && isFinal) "Ошибка" else null
        val speciesError = if (patient.species.isBlank() && isFinal) "Ошибка" else null
        val ownerError = if (patient.ownerName.isBlank() && isFinal) "Ошибка" else null
        val phoneError = if (patient.phoneNumber.isBlank() && isFinal) "Ошибка" else null

        val newState = state.copy(
            nameError = nameError, speciesError = speciesError,
            ownerNameError = ownerError, phoneError = phoneError
        )
        if (!isFinal) _state.value = newState
        return newState
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("patientId") patientId: Int): EditPatientViewModel
    }
}

sealed interface EditPatientCommand {
    data class InputName(val value: String) : EditPatientCommand
    data class InputSpecies(val value: String) : EditPatientCommand
    data class InputBreed(val value: String) : EditPatientCommand
    data class InputOwner(val value: String) : EditPatientCommand
    data class InputPhone(val value: String) : EditPatientCommand
    data object Save : EditPatientCommand
    data object Delete : EditPatientCommand
    data object Back : EditPatientCommand
    data class DeleteAppointment(val appointmentId: Int) : EditPatientCommand
}

sealed interface EditPatientState {
    data object Initial : EditPatientState
    data class Editing(
        val patient: Patient,
        val appointments: List<Appointment> = emptyList(),
        val nameError: String? = null,
        val speciesError: String? = null,
        val ownerNameError: String? = null,
        val phoneError: String? = null
    ) : EditPatientState {
        val isSaveEnabled: Boolean
            get() = patient.name.isNotBlank() && patient.species.isNotBlank() && patient.ownerName.isNotBlank() &&
                    nameError == null && speciesError == null && ownerNameError == null && phoneError == null
    }
    data object Finished : EditPatientState
}
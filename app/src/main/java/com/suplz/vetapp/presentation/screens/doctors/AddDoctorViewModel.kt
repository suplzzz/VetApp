package com.suplz.vetapp.presentation.screens.doctors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suplz.vetapp.domain.AddDoctorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDoctorViewModel @Inject constructor(
    private val addDoctorUseCase: AddDoctorUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<AddDoctorState>(AddDoctorState.Input())
    val state = _state.asStateFlow()

    fun processCommand(command: AddDoctorCommand) {
        when (command) {
            AddDoctorCommand.Back -> _state.update { AddDoctorState.Finished }
            is AddDoctorCommand.InputName -> updateState { it.copy(name = command.value) }
            is AddDoctorCommand.InputSpecialty -> updateState { it.copy(specialty = command.value) }
            AddDoctorCommand.Save -> {
                val currentState = _state.value
                if (currentState is AddDoctorState.Input && currentState.isSaveEnabled) {
                    viewModelScope.launch {
                        addDoctorUseCase(currentState.name, currentState.specialty)
                        _state.update { AddDoctorState.Finished }
                    }
                }
            }
        }
    }

    private fun updateState(update: (AddDoctorState.Input) -> AddDoctorState.Input) {
        _state.update { prev -> if (prev is AddDoctorState.Input) update(prev) else prev }
    }
}

sealed interface AddDoctorCommand {
    data class InputName(val value: String) : AddDoctorCommand
    data class InputSpecialty(val value: String) : AddDoctorCommand
    data object Save : AddDoctorCommand
    data object Back : AddDoctorCommand
}

sealed interface AddDoctorState {
    data class Input(val name: String = "", val specialty: String = "") : AddDoctorState {
        val isSaveEnabled: Boolean get() = name.isNotBlank() && specialty.isNotBlank()
    }
    data object Finished : AddDoctorState
}
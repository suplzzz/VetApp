package com.suplz.vetapp.presentation.screens.creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suplz.vetapp.domain.AddPatientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPatientViewModel @Inject constructor(
    private val addPatientUseCase: AddPatientUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<AddPatientState>(AddPatientState.Creation())
    val state = _state.asStateFlow()

    fun processCommand(command: AddPatientCommand) {
        when (command) {
            AddPatientCommand.Back -> {
                _state.update { AddPatientState.Finished }
            }
            is AddPatientCommand.InputName -> {
                updateState { it.copy(name = command.value) }
            }
            is AddPatientCommand.InputSpecies -> {
                updateState { it.copy(species = command.value) }
            }
            is AddPatientCommand.InputBreed -> {
                updateState { it.copy(breed = command.value) }
            }
            is AddPatientCommand.InputOwner -> {
                updateState { it.copy(ownerName = command.value) }
            }
            is AddPatientCommand.InputPhone -> {
                val filteredPhone = command.value.filter { it.isDigit() }
                updateState { it.copy(phoneNumber = filteredPhone) }
            }
            AddPatientCommand.Save -> {
                val currentState = validate(isFinal = true)
                if (currentState.isSaveEnabled) {
                    viewModelScope.launch {
                        addPatientUseCase(
                            name = currentState.name,
                            species = currentState.species,
                            breed = currentState.breed,
                            ownerName = currentState.ownerName,
                            phoneNumber = currentState.phoneNumber
                        )
                        _state.update { AddPatientState.Finished }
                    }
                }
            }
        }
    }

    private fun updateState(update: (AddPatientState.Creation) -> AddPatientState.Creation) {
        _state.update { previousState ->
            if (previousState is AddPatientState.Creation) {
                validate(update(previousState))
            } else {
                previousState
            }
        }
    }

    private fun validate(
        state: AddPatientState.Creation = (_state.value as AddPatientState.Creation),
        isFinal: Boolean = false
    ): AddPatientState.Creation {
        val nameError = if (state.name.isBlank() && isFinal) "Кличка не может быть пустой" else null
        val speciesError = if (state.species.isBlank() && isFinal) "Вид не может быть пустым" else null
        val ownerError = if (state.ownerName.isBlank() && isFinal) "Имя владельца не может быть пустым" else null
        val phoneError = if (state.phoneNumber.isBlank() && isFinal) "Телефон не может быть пустым" else null

        val newState = state.copy(
            nameError = nameError,
            speciesError = speciesError,
            ownerNameError = ownerError,
            phoneError = phoneError
        )
        if (!isFinal) {
            _state.value = newState
        }
        return newState
    }
}

sealed interface AddPatientCommand {
    data class InputName(val value: String) : AddPatientCommand
    data class InputSpecies(val value: String) : AddPatientCommand
    data class InputBreed(val value: String) : AddPatientCommand
    data class InputOwner(val value: String) : AddPatientCommand
    data class InputPhone(val value: String) : AddPatientCommand
    data object Save : AddPatientCommand
    data object Back : AddPatientCommand
}

sealed interface AddPatientState {
    data class Creation(
        val name: String = "",
        val nameError: String? = null,
        val species: String = "",
        val speciesError: String? = null,
        val breed: String = "",
        val ownerName: String = "",
        val ownerNameError: String? = null,
        val phoneNumber: String = "",
        val phoneError: String? = null
    ) : AddPatientState {
        val isSaveEnabled: Boolean
            get() = name.isNotBlank() && species.isNotBlank() && ownerName.isNotBlank() && phoneNumber.isNotBlank() &&
                    nameError == null && speciesError == null && ownerNameError == null && phoneError == null
    }
    data object Finished : AddPatientState
}
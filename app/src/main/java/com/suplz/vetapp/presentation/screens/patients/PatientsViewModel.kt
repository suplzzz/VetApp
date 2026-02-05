package com.suplz.vetapp.presentation.screens.patients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suplz.vetapp.domain.GetPatientListUseCase
import com.suplz.vetapp.domain.Patient
import com.suplz.vetapp.domain.SearchPatientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class PatientsViewModel @Inject constructor(
    private val getPatientListUseCase: GetPatientListUseCase,
    private val searchPatientsUseCase: SearchPatientsUseCase
) : ViewModel() {

    private val query = MutableStateFlow("")

    private val _state = MutableStateFlow(PatientsScreenState())
    val state = _state.asStateFlow()

    init {
        query
            .onEach { input ->
                _state.update { it.copy(query = input) }
            }
            .flatMapLatest { input ->
                if (input.isBlank()) {
                    getPatientListUseCase()
                } else {
                    searchPatientsUseCase(input)
                }
            }
            .onEach { patients ->
                _state.update { it.copy(patients = patients) }
            }
            .launchIn(viewModelScope)
    }

    fun processCommand(command: PatientsCommand) {
        viewModelScope.launch {
            when (command) {
                is PatientsCommand.InputSearchQuery -> {
                    query.update { command.query.trim() }
                }
            }
        }
    }
}

sealed interface PatientsCommand {
    data class InputSearchQuery(val query: String) : PatientsCommand
}

data class PatientsScreenState(
    val query: String = "",
    val patients: List<Patient> = emptyList()
)
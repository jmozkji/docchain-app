package com.ufabc.docchain.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufabc.docchain.data.repository.AuthRepositoryI
import com.ufabc.docchain.data.repository.AuthRepositoryImpl
import com.ufabc.docchain.presentation.shared.ActivityStatus
import com.ufabc.docchain.presentation.shared.ActivityStatus.LOADING
import com.ufabc.docchain.presentation.shared.ActivityStatus.NORMAL
import com.ufabc.docchain.presentation.register.RegisterViewModelAction.ShowCreateUserFailToast
import com.ufabc.docchain.presentation.register.RegisterViewModelAction.ShowCreateUserSuccessToast
import com.ufabc.docchain.presentation.register.RegisterViewModelAction.ShowEmptyEmailInputToast
import com.ufabc.docchain.presentation.register.RegisterViewModelAction.ShowEmptyIdInputToast
import com.ufabc.docchain.presentation.register.RegisterViewModelAction.ShowEmptyNameInputToast
import com.ufabc.docchain.presentation.register.RegisterViewModelAction.ShowEmptyPasswordInputToast
import kotlinx.coroutines.launch

class RegisterViewModel() : ViewModel(), RegisterEvent {

    private val authRepository: AuthRepositoryI = AuthRepositoryImpl()

    private val _state = MutableLiveData<RegisterViewModelState>()

    private val _action = MutableLiveData<RegisterViewModelAction>()

    val state: LiveData<RegisterViewModelState>
        get() = _state

    val action: LiveData<RegisterViewModelAction>
        get() = _action

    init {
        _state.postValue(RegisterViewModelState())
    }

    override fun submitRegistration(name: String, id: String, email: String, password: String) {
        val success = validateInputs(name, id, email, password)

        if (success) {
            createUser(name, id, email, password)
        }
    }

    private fun validateInputs(name: String, id: String, email: String, password: String): Boolean {
        return if (name.isEmpty()) {
            postAction(ShowEmptyNameInputToast)
            false
        } else if (id.isEmpty()) {
            postAction(ShowEmptyIdInputToast)
            false
        } else if (email.isEmpty()) {
            postAction(ShowEmptyEmailInputToast)
            false
        } else if (password.isEmpty()) {
            postAction(ShowEmptyPasswordInputToast)
            false
        } else
            true
    }

    private fun createUser(name: String, id: String, email: String, password: String) {
        updateRegisterStatus(LOADING)

        viewModelScope.launch {
            val success = authRepository.createUser(name, id, email, password)

            if (success) {
                postAction(ShowCreateUserSuccessToast)
            } else {
                postAction(ShowCreateUserFailToast)
            }

            updateRegisterStatus(NORMAL)
        }
    }

    private fun updateRegisterStatus(status: ActivityStatus) {
        val currentState = _state.value ?: RegisterViewModelState()
        val newState = currentState.copy(registerStatus = status)

        postState(newState)
    }

    private fun postState(newState: RegisterViewModelState?) {
        if (newState != null) {
            _state.postValue(newState)
        }
    }

    private fun postAction(action: RegisterViewModelAction) {
        _action.value = action
    }
}
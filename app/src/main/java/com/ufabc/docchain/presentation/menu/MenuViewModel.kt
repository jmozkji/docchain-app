package com.ufabc.docchain.presentation.menu

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufabc.docchain.data.repository.AuthRepositoryI
import com.ufabc.docchain.data.repository.AuthRepositoryImpl
import com.ufabc.docchain.data.repository.BlockchainRepositoryI
import com.ufabc.docchain.data.repository.BlockchainRepositoryImpl
import kotlinx.coroutines.launch

class MenuViewModel: ViewModel(), MenuEvent {

    private val authRepository: AuthRepositoryI = AuthRepositoryImpl()

    private val blockchainRepository: BlockchainRepositoryI = BlockchainRepositoryImpl()

    private val _state = MutableLiveData<MenuViewModelState>()

    private var userId: String = EMPTY_STRING

    val state: LiveData<MenuViewModelState>
        get() = _state

    override fun setUserAuthUid(authUid: String) {
        viewModelScope.launch {
            val user = authRepository.retrieveUser(authUid)

            user?.let {
                userId = it.id

                val currentState = _state.value ?: MenuViewModelState()
                val newState = currentState.copy(userId = userId)
                postState(newState)
            }

            Log.d(LOG_TAG, "User Id set as [$userId]")
        }
    }

    override fun consultExams(context: Context) {
        Log.d(LOG_TAG, "Calling consultExams with userId: [$userId]")
        viewModelScope.launch {
            val examsList = blockchainRepository.getExams(context, userId)

            Log.d(LOG_TAG, "Retrieved exams list: [$examsList]")
        }
    }

    private fun postState(newState: MenuViewModelState?) {
        if (newState != null) {
            _state.postValue(newState)
        }
    }

    companion object {
        private const val LOG_TAG = "MenuViewModel"

        private const val EMPTY_STRING = ""
    }
}
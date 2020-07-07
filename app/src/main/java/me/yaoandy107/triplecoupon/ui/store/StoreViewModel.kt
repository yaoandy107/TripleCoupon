package me.yaoandy107.triplecoupon.ui.store

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import me.yaoandy107.triplecoupon.model.Store
import me.yaoandy107.triplecoupon.repository.StoreRepository

class StoreViewModel : ViewModel() {
    private val repository: StoreRepository = StoreRepository()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(true)

    private val _stores: MutableLiveData<List<Store>> = MutableLiveData()
    val stores: LiveData<List<Store>> = _stores

    fun fetchStores() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getStores()
                .onStart { isLoading.postValue(true) }
                .catch { exception ->
                    /* TODO: emit error state */
                    isLoading.postValue(false)
                }
                .collect { stores ->
                    _stores.postValue(stores)
                    isLoading.postValue(false)
                }
        }
    }
}
package com.slayer.rickandmorty.ui.fragments.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.slayer.data.dataSources.api.ApiService
import com.slayer.data.dataSources.api.dto.locations.LocationResult.Companion.toLocation
import com.slayer.data.dataSources.api.paging.LocationsPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {
    private var mPrevSearchValue: String? = null
    private var mCurrentSearchValue: String? = null

    private var pagingSource: LocationsPagingSource? = null
        get() {
            if (field == null || field?.invalid == true) {
                field = LocationsPagingSource(
                    apiService,
                    mCurrentSearchValue
                )
            }
            return field
        }

    val locationsFlow = Pager(
        config = PagingConfig(
            pageSize = 10
        ),
        pagingSourceFactory = { pagingSource!! }
    ).flow.cachedIn(viewModelScope).map {
        it.map { locationResult ->
            locationResult.toLocation()
        }
    }

    fun submitQuery(
        searchValue: String?,
    ) {
        mCurrentSearchValue = if (searchValue.isNullOrEmpty()) null else searchValue

        if (mPrevSearchValue != mCurrentSearchValue) {
            pagingSource?.invalidate()

            mPrevSearchValue = mCurrentSearchValue
        }
    }
}
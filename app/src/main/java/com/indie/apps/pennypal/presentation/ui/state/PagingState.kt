package com.indie.apps.pennypal.presentation.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

class PagingState<T : Any> {
    var isRefresh by mutableStateOf(false)
    private set

    var isLoadMore by mutableStateOf(false)
    private set

    fun update(lazyPagingData: LazyPagingItems<T>)
    {
        isRefresh = when (lazyPagingData.loadState.refresh) {
            is LoadState.Loading -> true
            is LoadState.Error -> false
            is LoadState.NotLoading -> false
        }

        isLoadMore = when (lazyPagingData.loadState.append) {
            is LoadState.Loading -> true
            is LoadState.Error -> false
            is LoadState.NotLoading -> false
        }

        isLoadMore = when (lazyPagingData.loadState.prepend) {
            is LoadState.Loading -> true
            is LoadState.Error -> false
            is LoadState.NotLoading -> false
        }
    }
}
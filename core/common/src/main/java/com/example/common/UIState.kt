package com.example.common

sealed class UIState {
    data object Initial : UIState()
    data object Ready : UIState()
}
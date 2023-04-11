package com.rrpvm.gif_loader.data.provider.cache_manager

import android.content.Context
import com.rrpvm.gif_loader.domain.entity.ICacheToolManager

interface ICacheToolManagerProvider {
    fun provideManager(context: Context): ICacheToolManager
}
package app.runo.kanaldoon.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import app.runo.kanaldoon.model.AppDatabase
import app.runo.kanaldoon.model.CategoryEntity
import app.runo.kanaldoon.model.ChannelEntity
import app.runo.kanaldoon.ui.category.manage.CategoryItem
import app.runo.kanaldoon.ui.channel.manage.ChannelItem
import app.runo.kanaldoon.utils.Targets
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db: AppDatabase by lazy {
        AppDatabase.get(application)
    }

    val categories = db.categoryStore().getCategories()

    fun getChannels(categoryId: Int) =
        db.channelStore().getChannels(categoryId).map {
            mutableListOf<ChannelItem>().apply {
                it.forEach {
                    val item = ChannelItem()
                    item.channel = it
                    add(item)
                }
            }
        }

    lateinit var category: CategoryEntity
    lateinit var targets: Targets
    lateinit var channel: ChannelEntity
    var isMultiselect: Boolean = false

    fun findChannel(id: Int, found: () -> Unit) = viewModelScope.launch {
        channel = db.channelStore().getChannel(id)
    }.invokeOnCompletion {
        found.invoke()
    }


    fun addNewItem(username: String) = viewModelScope.launch {
        db.channelStore().addChannel(
            ChannelEntity(
                username = username,
                categoryId = category.id,
                target = targets.name
            )
        )
    }

    fun addNewItemTmp(username: String) = viewModelScope.launch {
        db.channelStore().addChannel(
            ChannelEntity(
                username = username,
                categoryId = 1,
                target = "Telegram"
            )
        )
    }

    fun getCategoryPos(): Int {
        categories.value?.forEachIndexed { index, categoryEntity ->
            if (channel.categoryId == categoryEntity.id) {
                return index
            }
        }
        return 0
    }

    fun updateItem(username: String) = viewModelScope.launch {
        db.channelStore().updateChannel(
            ChannelEntity(
                id = channel.id,
                username = username,
                title = channel.title,
                desc = channel.desc,
                avatarUrl = channel.avatarUrl,
                viewCount = channel.viewCount,
                lastUpdate = channel.lastUpdate,
                lastView = channel.lastView,
                categoryId = category.id,
                target = targets.name
            )
        )
    }

    val categories2 = categories.map {
        mutableListOf<CategoryItem>().apply {
            it.forEach {
                val item = CategoryItem()
                item.category = it
                add(item)
            }
        }
    }

    fun delete(item: CategoryEntity) = viewModelScope.launch {
        try {
            db.categoryStore().deleteCategory(item)
        } catch (e: Exception) {
            //TODO failed to delete category
        }
    }

    fun addNewItem(item: CategoryEntity) = viewModelScope.launch {
        db.categoryStore().addCategory(item)
    }

    fun updateItem(item: CategoryEntity) = viewModelScope.launch {
        db.categoryStore().updateCategory(item)
    }
}
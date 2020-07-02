package app.runo.kanaldoon.ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.runo.kanaldoon.model.CategoryEntity
import app.runo.kanaldoon.ui.channel.manage.ChannelFragment

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private var items: List<CategoryEntity> = listOf()

    fun setItems(_items: List<CategoryEntity>) {
        items = _items
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun createFragment(position: Int) =
        ChannelFragment(items[position].id)

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        val item = items.find {
            it.id.toLong() == itemId
        }
        return if (item != null) {
            items.contains(item)
        } else {
            false
        }
    }
}
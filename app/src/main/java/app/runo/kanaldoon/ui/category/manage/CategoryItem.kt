package app.runo.kanaldoon.ui.category.manage

import android.view.LayoutInflater
import android.view.ViewGroup
import app.runo.kanaldoon.R
import app.runo.kanaldoon.databinding.CategoryListItemBinding
import app.runo.kanaldoon.model.CategoryEntity
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class CategoryItem : AbstractBindingItem<CategoryListItemBinding>() {
    var category: CategoryEntity? = null


    override val type: Int
        get() = R.id.tv_simple_list_item

    override fun bindView(binding: CategoryListItemBinding, payloads: List<Any>) {
        binding.tvSimpleListItem.text = "${category!!.title}"
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): CategoryListItemBinding {
        return CategoryListItemBinding.inflate(inflater, parent, false)
    }
}
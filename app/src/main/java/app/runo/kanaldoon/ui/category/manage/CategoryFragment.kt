package app.runo.kanaldoon.ui.category.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import app.runo.kanaldoon.R
import app.runo.kanaldoon.databinding.CategoryFragmentBinding
import app.runo.kanaldoon.model.CategoryEntity
import app.runo.kanaldoon.ui.main.MainViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.drag.SimpleDragCallback

class CategoryFragment : Fragment() {
    companion object {
        fun newInstance() =
            CategoryFragment()
    }

    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: CategoryFragmentBinding? = null
    private lateinit var itemAdapter: ItemAdapter<CategoryItem>
    private lateinit var fastAdapter: FastAdapter<CategoryItem>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CategoryFragmentBinding.inflate(inflater, container, false)
        activity?.title = getString(R.string.manage_categories)
        return binding.root
    }

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(rootView, savedInstanceState)

        itemAdapter = ItemAdapter()
        fastAdapter = FastAdapter.with(itemAdapter)

        fastAdapter.onClickListener = { view, adapter, item, position ->
            viewModel.category = item.category!!
            val edit = CategoryFragmentDirections
                .actionAddNewCategory(true)
            findNavController().navigate(edit)
            true
        }
        fastAdapter.onLongClickListener = { view, adapter, item, position ->
            delete(item.category!!)
            true
        }


        binding.listCategory.apply {
            val dragCallback = SimpleDragCallback()
            val touchHelper = ItemTouchHelper(dragCallback)
            touchHelper.attachToRecyclerView(this)
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

            adapter = fastAdapter
        }
        binding.floatingAddNew.setOnClickListener {
            findNavController().navigate(R.id.actionAddNewCategory)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.categories2.observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                itemAdapter.setNewList(list)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun delete(item: CategoryEntity) {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage(getString(R.string.ask_to_delete_item))
                setPositiveButton(
                    getString(R.string.yes)
                ) { dialog, id ->
                    viewModel.delete(item)
                    dialog.dismiss()
                }
                setNegativeButton(
                    getString(R.string.no)
                ) { dialog, id ->
                    dialog.cancel()
                }
            }
            builder.create()
        }
        alertDialog?.show()
    }
}
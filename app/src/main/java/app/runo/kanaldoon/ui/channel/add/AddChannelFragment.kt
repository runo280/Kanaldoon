package app.runo.kanaldoon.ui.channel.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.runo.kanaldoon.R
import app.runo.kanaldoon.databinding.AddChannelFragmentBinding
import app.runo.kanaldoon.model.CategoryEntity
import app.runo.kanaldoon.ui.main.MainViewModel
import app.runo.kanaldoon.utils.Targets


class AddChannelFragment : Fragment() {
    private val args: AddChannelFragmentArgs by navArgs()

    companion object {
        fun newInstance() = AddChannelFragment()
    }

    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: AddChannelFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddChannelFragmentBinding.inflate(inflater, container, false)
        activity?.title = if (args.editMode) getString(R.string.edit_channel) else getString(R.string.add_channel)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (args.editMode) {
            binding.etInput.editText?.setText(viewModel.channel.username)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        targetSpinner()
        categorySpinner()
        binding.btnManageCategories.setOnClickListener {
            findNavController().navigate(R.id.gActionManagerCategories)
        }
        binding.floatingAddNew.setOnClickListener {
            if (binding.etInput.editText?.text.toString().isEmpty()) return@setOnClickListener
            save(binding.etInput.editText?.text.toString())

        }

    }

    private fun save(str: String) {
        if (args.editMode)
            viewModel.updateItem(username = str).invokeOnCompletion {
                findNavController().popBackStack()
            }
        else
            viewModel.addNewItem(username = str).invokeOnCompletion {
                findNavController().popBackStack()
            }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun targetSpinner() {
        val list = Targets.values()
        val array = list.map {
            it.name
        }.toTypedArray()
        val adapter: ArrayAdapter<*> =
            ArrayAdapter(
                requireContext(),
                R.layout.category_list_item,
                R.id.tv_simple_list_item,
                array
            )
        adapter.setDropDownViewResource(R.layout.category_list_item)
        binding.spinTarget.adapter = adapter
        binding.spinTarget.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                viewModel.targets = list[position]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun categorySpinner() {
        val list = mutableListOf<CategoryEntity>()
        viewModel.categories.observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                val array = list.map { entity ->
                    entity.title
                }.toTypedArray()
                val adapter: ArrayAdapter<*> =
                    ArrayAdapter(
                        requireContext(),
                        R.layout.category_list_item,
                        R.id.tv_simple_list_item,
                        array
                    )
                adapter.setDropDownViewResource(R.layout.category_list_item)
                binding.spinCategory.adapter = adapter
                binding.spinCategory.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        adapterView: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.category = list[position]
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                }
                if (args.editMode) {
                    binding.spinCategory.setSelection(viewModel.getCategoryPos(), true)
                }
            }
        })


    }

}
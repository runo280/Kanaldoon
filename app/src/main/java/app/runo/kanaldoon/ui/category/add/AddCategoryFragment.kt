package app.runo.kanaldoon.ui.category.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.runo.kanaldoon.R
import app.runo.kanaldoon.databinding.AddCategoryFragmentBinding
import app.runo.kanaldoon.model.CategoryEntity
import app.runo.kanaldoon.ui.main.MainViewModel

class AddCategoryFragment : Fragment() {

    companion object {
        fun newInstance() = AddCategoryFragment()
    }

    private val args: AddCategoryFragmentArgs by navArgs()

    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: AddCategoryFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddCategoryFragmentBinding.inflate(inflater, container, false)
        activity?.title =
            if (args.editMode) getString(R.string.update_category) else getString(R.string.add_category)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingAddNew.setOnClickListener {
            if (binding.etInput.editText?.text.toString().isNotEmpty()) {
                save(binding.etInput.editText?.text.toString())
            }
        }

        if (args.editMode) {
            binding.etInput.editText?.setText(viewModel.category.title)
        }

    }

    private fun save(str: String) {
        if (args.editMode)
            viewModel.updateItem(
                CategoryEntity(
                    id = viewModel.category.id,
                    title = str
                )
            ).invokeOnCompletion {
                findNavController().popBackStack()
            }
        else
            viewModel.addNewItem(CategoryEntity(title = str)).invokeOnCompletion {
                findNavController().popBackStack()
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
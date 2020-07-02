package app.runo.kanaldoon.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import app.runo.kanaldoon.R
import app.runo.kanaldoon.databinding.MainFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: MainFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        activity?.title = getString(R.string.app_name)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.categories.observe(viewLifecycleOwner, Observer { list ->
            binding.pager.adapter = PagerAdapter(this)
            (binding.pager.adapter as PagerAdapter).setItems(list)
            Log.i("TAG", "onViewCreated: $list")
            TabLayoutMediator(binding.tabLayout, binding.pager) { tabs, position ->
                tabs.text = list[position].title
            }.attach()

        })


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.navigation_items, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.addChannel -> {
                findNavController().navigate(R.id.gActionAddChannel)
//                onetime("new.txt")
                true
            }
            R.id.manageCategories -> {
                findNavController().navigate(R.id.gActionManagerCategories)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onetime(fileName: String) {
        /*File(requireContext().filesDir, fileName).forEachLine {
            viewModel.addNewItemTmp(it)
        }*/
        requireContext().openFileInput("myfile").bufferedReader().useLines { lines ->
            lines.forEach {
                viewModel.addNewItemTmp(it)
            }
        }
        /*val filename = "myfile"
        val fileContents = "Hello world!"
        requireContext().openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }*/


    }
}
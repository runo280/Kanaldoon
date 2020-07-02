package app.runo.kanaldoon.ui.channel.manage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.runo.kanaldoon.ForegroundService
import app.runo.kanaldoon.databinding.ChannelFragmentBinding
import app.runo.kanaldoon.ui.channel.add.AddChannelFragmentDirections
import app.runo.kanaldoon.ui.main.MainViewModel
import app.runo.kanaldoon.utils.Targets
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.helpers.ActionModeHelper

class ChannelFragment(private val categoryId: Int = 0) : Fragment() {

    companion object {
        fun newInstance() = ChannelFragment()
        fun newInstanceWithCategory(id: Int) {
            ChannelFragment(id)
        }
    }

    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: ChannelFragmentBinding? = null

    private lateinit var itemAdapter: ItemAdapter<ChannelItem>
    private lateinit var fastAdapter: FastAdapter<ChannelItem>
    private lateinit var mActionModeHelper: ActionModeHelper<ChannelItem>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChannelFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(rootView, savedInstanceState)

        itemAdapter = ItemAdapter()
        fastAdapter = FastAdapter.with(itemAdapter)
        fastAdapter.setHasStableIds(true)

        /*val selectExtension = fastAdapter.getSelectExtension()
        selectExtension.apply {
            isSelectable = true
            multiSelect = true
            selectOnLongClick = true
            selectionListener = object : ISelectionListener<ChannelItem> {
                override fun onSelectionChanged(item: ChannelItem, selected: Boolean) {
                    Log.i(
                        "FastAdapter",
                        "SelectedCount: " + selectExtension.selections.size + " ItemsCount: " + selectExtension.selectedItems.size
                    )
                }
            }
        }

        fastAdapter.onPreClickListener =
            { _: View?, _: IAdapter<ChannelItem>, item: ChannelItem, _: Int ->
                //we handle the default onClick behavior for the actionMode. This will return null if it didn't do anything and you can handle a normal onClick
                val res = mActionModeHelper.onClick(item)
                res ?: false
            }

        fastAdapter.onClickListener =
            { v: View?, _: IAdapter<ChannelItem>, _: ChannelItem, _: Int ->
                if (v != null) {
                    Toast.makeText(
                        v.context,
                        "SelectedCount: " + selectExtension.selections.size + " ItemsCount: " + selectExtension.selectedItems.size,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                false
            }

        fastAdapter.onPreLongClickListener =
            { _: View, _: IAdapter<ChannelItem>, _: ChannelItem, position: Int ->
                val actionMode =
                    mActionModeHelper.onLongClick(activity as AppCompatActivity, position)
                if (actionMode != null) {
                    //we want color our CAB
//                findViewById<View>(R.id.action_mode_bar).setBackgroundColor(this@MultiselectSampleActivity.getThemeColor(R.attr.colorPrimary, R.color.colorPrimary))
                }
                //if we have no actionMode we do not consume the event
                actionMode != null
            }

        mActionModeHelper = ActionModeHelper(fastAdapter, 0,object : ActionModeHelper.ActionItemClickedListener {
            override fun onClick(mode: ActionMode, item: MenuItem): Boolean {
                TODO("Not yet implemented")
            }

        })
*/
        binding.channelList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

            adapter = fastAdapter
        }

        fastAdapter.onClickListener = { view, adapter, item, position ->
            openChannel(requireContext(), getTarget(item.channel?.target), item.channel?.username)
            true
        }
        fastAdapter.onLongClickListener = { view, adapter, item, position ->
            viewModel.channel = item.channel!!
            val edit = AddChannelFragmentDirections
                .gActionAddChannel(true)
            findNavController().navigate(edit)
//            ForegroundService.startService(requireContext(), item.channel!!.id)
            true
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getChannels(categoryId).observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                Log.i("Channels", list.toString())
                itemAdapter.setNewList(list)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun openChannel(context: Context, target: Targets, username: String?) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(target.schema(username))
                ).apply { setPackage(target.pkgName) })
        } catch (e: Exception) {
        }

    }

    fun getTarget(target: String?): Targets {
        Targets.values().forEach {
            if (it.name == target) {
                return it
            }
        }
        return Targets.Telegram
    }
//ForegroundService.startService(itemView.context, item.id)
}
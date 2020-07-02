package app.runo.kanaldoon.ui.channel.manage

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import app.runo.kanaldoon.R
import app.runo.kanaldoon.databinding.ChannelListItemBinding
import app.runo.kanaldoon.model.ChannelEntity
import coil.api.load
import coil.transform.CircleCropTransformation
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mikepenz.fastadapter.ui.utils.FastAdapterUIUtils

class ChannelItem : AbstractBindingItem<ChannelListItemBinding>() {
    var channel: ChannelEntity? = null

    override val type: Int
        get() = R.id.channel_list_item

    override fun bindView(binding: ChannelListItemBinding, payloads: List<Any>) {
        binding.tvChannelTitle.text = "${channel!!.title}"
        binding.tvChannelUsername.text = "@${channel!!.username}"
        binding.tvViewCount.text = "${channel!!.viewCount}"
        if (channel!!.avatarUrl != "none")
            binding.imgChannelIcon.load(channel!!.avatarUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_channel_24)
                transformations(CircleCropTransformation())
            }

        binding.channelListItem.background = FastAdapterUIUtils.getSelectableBackground(
            binding.channelListItem.context,
            Color.RED,
            true
        )
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ChannelListItemBinding {
        return ChannelListItemBinding.inflate(inflater, parent, false)
    }
}
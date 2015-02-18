package com.evandenerley.gauge.app.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.bindView
import com.evandenerley.gauge.R
import com.evandenerley.gauge.api.model.ItemTemperatureReading
import com.evandenerley.gauge.util.IntentLauncher.launchDisplayTemperature
import java.util.*

/**
 * An Adapter to handle displaying [ItemTemperatureReading]s.
 */
class ItemTemperatureReadingAdapter : RecyclerView.Adapter<ItemTemperatureReadingAdapter.ViewHolder>(), BaseViewHolder.ItemClickListener {

    val dataArray: ArrayList<ItemTemperatureReading> = ArrayList()
    private val lock = Object()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_temp_reading, parent, false)
        return ViewHolder.newInstance(view, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemTemperatureReading = getItemData(position)
        setLineItemViewData(holder, itemTemperatureReading)
    }

    /**
     * Set this ViewHolders underlying [ItemTemperatureReading] data.
     * @param holder                 [ItemTemperatureReading] [ViewHolder]
     * @param itemTemperatureReading the [ItemTemperatureReading] data
     */
    private fun setLineItemViewData(holder: ViewHolder, itemTemperatureReading: ItemTemperatureReading) {
        holder.itemTempReadingContainer.isClickable = true
        holder.itemTempReadingName.text = itemTemperatureReading.itemName
        holder.itemTempReadingDescription.text = itemTemperatureReading.itemDescription
        holder.itemTempReading.text = itemTemperatureReading.lastTemperature.toString()
    }

    override fun onItemClick(view: View, position: Int) {
        launchDisplayTemperature(view.context, dataArray[position]);
    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

    /**
     * Get the desired [ItemTemperatureReading] based off its position in a list.
     * @param position the position in the list
     * @return the desired [ItemTemperatureReading]
     */
    fun getItemData(position: Int): ItemTemperatureReading {
        return dataArray[position]
    }

    /**
     * Add [ItemTemperatureReading] to this  RecyclerView.Adapter}'s array data set at the end
     * of the array.
     * @param itemTemperatureReading the [ItemTemperatureReading]ading} to add
     */
    fun addItemData(itemTemperatureReading: ItemTemperatureReading) {
        synchronized (lock) {
            dataArray.add(dataArray.size, itemTemperatureReading)
        }
    }

    /**
     * Remove a [ItemTemperatureReading] from this ad [ItemTemperatureReading] Array.
     * @param position index of the the [ItemTemperatureReading]ading} to remove
     */
    fun removeItemData(position: Int) {
        synchronized (lock) {
            if (dataArray.size > 0) {
                dataArray.removeAt(position)
            }
        }
    }

    /**
     * ViewHolder for the entered [ItemTemperatureReading] data.
     */
    class ViewHolder private constructor(view: View, itemClickListener: BaseViewHolder.ItemClickListener) : BaseViewHolder(view, itemClickListener) {
        val itemTempReading: TextView by bindView(R.id.adapter_item_temp_reading)
        val itemTempReadingName: TextView by bindView(R.id.adapter_item_temp_reading_name)
        val itemTempReadingDescription: TextView by bindView(R.id.adapter_item_temp_reading_description)
        val itemTempReadingContainer: RelativeLayout by bindView(R.id.adapter_item_temp_reading_container)

        companion object {
            /**
             * Create a new Instance of the ViewHolder.
             * @param view inflated in [.onCreateViewHolder]
             * @return an [ItemTemperatureReading] ViewHolder instance
             */
            fun newInstance(view: View, itemClickListener: ItemClickListener): ViewHolder {
                return ViewHolder(view, itemClickListener)
            }
        }
    }
}

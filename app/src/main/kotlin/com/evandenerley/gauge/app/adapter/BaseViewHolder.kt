package com.evandenerley.gauge.app.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * ViewHolder for the entered view's data.
 */
abstract class BaseViewHolder : RecyclerView.ViewHolder, View.OnClickListener, View.OnLongClickListener {
    var view: View
    var itemPosition = if (layoutPosition > 0) layoutPosition else 0
    private var itemClickListener: ItemClickListener? = null
    private var itemLongClickListener: ItemLongClickListener? = null

    /**
     * Constructor for the holder.
     * @param view          the inflated view
     * @param clickListener ItemClickListener
     */
    protected constructor(view: View, clickListener: BaseViewHolder.ItemClickListener) : super(view) {
        this.view = view
        this.view.setOnClickListener(this)
        itemClickListener = clickListener
    }

    /**
     * Constructor for the holder.
     * @param view          the inflated view
     * @param clickListener ItemLongClickListener
     */
    protected constructor(view: View, clickListener: BaseViewHolder.ItemLongClickListener) : super(view) {
        this.view = view
        this.view.setOnClickListener(this)
        this.view.setOnLongClickListener(this)
        itemLongClickListener = clickListener
    }


    override fun onClick(v: View) {
        itemClickListener?.onItemClick(v, itemPosition)
        itemLongClickListener?.onItemClick(v, itemPosition)
    }

    override fun onLongClick(v: View): Boolean {
        itemLongClickListener?.onItemLongClick(v, itemPosition)
        return true
    }


    /**
     * Click listener for all adapter items
     */
    interface ItemClickListener {
        /**
         * ItemClick event.
         * @param view     clicked
         * @param position in list
         */
        fun onItemClick(view: View, position: Int)
    }

    /**
     * Long Click listener for all adapter items
     */
    interface ItemLongClickListener : ItemClickListener {
        /**
         * ItemLongClick event.
         * @param view     clicked
         * @param position in list
         */
        fun onItemLongClick(view: View, position: Int)
    }
}

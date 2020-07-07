package me.yaoandy107.triplecoupon.ui.store

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_store.view.*
import me.yaoandy107.triplecoupon.R
import me.yaoandy107.triplecoupon.model.Store

class StoreAdapter(private val stores: List<Store>) : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val storeName = view.tv_store_name
        val address = view.tv_address
        val phone = view.tv_phone
        val total = view.tv_total
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stores.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            storeName.text = stores[position].storeNm
            address.text = stores[position].addr
            phone.text = stores[position].tel
            total.text = "庫存：${stores[position].total}"
        }

    }
}
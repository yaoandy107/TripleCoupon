package me.yaoandy107.triplecoupon.ui.store

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_store.view.*
import me.yaoandy107.triplecoupon.R
import me.yaoandy107.triplecoupon.model.Store


class StoreAdapter(private val stores: List<Store>) :
    RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val storeName: TextView = view.tv_store_name
        val address: TextView = view.tv_address
        val businessTime: TextView = view.tv_business_time
        val phone: TextView = view.tv_phone
        val total: TextView = view.tv_total
        val mapButton: ImageButton = view.btn_map
        val phoneButton: ImageButton = view.btn_phone

        fun bind(store: Store) {
            storeName.text = store.storeNm
            address.text = store.addr
            businessTime.text =
                Regex(pattern = "[0-9]{2}:[0-9]{2}-[0-9]{2}:[0-9]{2}").find(store.busiTime)?.value
            phone.text = store.tel
            total.text = "庫存：${store.total}"
            mapButton.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/search/?api=1&query=${store.storeNm}")
                )
                itemView.context.startActivity(intent)
            }
            phoneButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${store.tel}")
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stores.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stores[position])
    }
}
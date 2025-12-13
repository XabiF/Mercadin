package com.xabif.mercadin.ui

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xabif.mercadin.R
import com.xabif.mercadin.src.ProductInfo
import coil.load
import com.xabif.mercadin.src.List
import com.xabif.mercadin.src.ProductSource
import com.xabif.mercadin.src.SourceManager

class ProductInfoAdapter(val context: Context, products: kotlin.collections.List<ProductInfo>, val checkable: Boolean, val onListChange: () -> Unit) : RecyclerView.Adapter<ProductInfoAdapter.ItemViewHolder>() {
    private val products: MutableList<ProductInfo> = products.toMutableList();

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val baseCard: CardView = itemView as CardView;
        val icon: ImageView = itemView.findViewById(R.id.icon_item);
        val title: TextView = itemView.findViewById(R.id.text_item_title);
        val source: TextView = itemView.findViewById(R.id.text_source);
        val unit_price: TextView = itemView.findViewById(R.id.text_item_unit_price);
        val ref_price: TextView = itemView.findViewById(R.id.text_item_ref_price);
        val sale_unit_price: TextView = itemView.findViewById(R.id.text_item_sale_unit_price);
        val sale_ref_price: TextView = itemView.findViewById(R.id.text_item_sale_ref_price);
        val button_add: ImageButton = itemView.findViewById(R.id.button_list_add);
        val button_remove: ImageButton = itemView.findViewById(R.id.button_list_remove);
        val text_count: TextView = itemView.findViewById(R.id.text_list_count);

        fun setSource(context: Context, name: Int, colorRes: Int) {
            // this.setSourceColorImpl(context, this.button_add, colorRes);
            // this.setSourceColorImpl(context, this.button_remove, colorRes);
            this.source.setText(name);
            this.source.setTextColor(ContextCompat.getColor(context, colorRes));
        }

        fun updateActiveListStatus(info: ProductInfo) {
            this.text_count.text = List.getProductCount(info).toString();
            this.button_remove.isEnabled = List.canRemoveProduct(info);
        }

        fun updateChecked(info: ProductInfo) {
            if(List.hasCheckedValue(info)) {
                if(List.isChecked(info)) {
                    Util.crossText(title);
                    Util.italicText(title);
                    return;
                }
            }

            Util.uncrossText(title);
            Util.boldText(title);
        }

        fun toggleChecked(info: ProductInfo) {
            if(List.hasCheckedValue(info)) {
                if(List.isChecked(info)) {
                    List.setChecked(info, false);
                    this.updateChecked(info);
                }
                else {
                    List.setChecked(info, true);
                    this.updateChecked(info);
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_info_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val info = products[position];

        holder.title.text = info.name;
        holder.updateActiveListStatus(info);
        holder.updateChecked(info);
        if(checkable) {
            holder.baseCard.setOnClickListener {
                Log.d("click", "card");
                holder.toggleChecked(info);
            }
        }

        holder.icon.setOnClickListener {
            SourceManager.open(context, info);
        }

        holder.button_add.setOnClickListener {
            Log.d("click", "add")
            List.addProduct(info);
            onListChange();
            holder.updateActiveListStatus(info);
        }
        holder.button_remove.setOnClickListener {
            Log.d("click", "remove")
            List.removeProduct(info);
            onListChange();
            holder.updateActiveListStatus(info);
        }

        holder.unit_price.setText(info.price.formatProductPrice());
        holder.ref_price.setText(info.price.formatUnitPrice());
        if(info.isSale()) {
            holder.sale_unit_price.setText(info.sale_price!!.formatProductPrice());
            holder.sale_ref_price.setText(info.sale_price!!.formatUnitPrice());
            holder.sale_unit_price.visibility = View.VISIBLE;
            holder.sale_ref_price.visibility = View.VISIBLE;
            Util.crossText(holder.ref_price);
            Util.crossText(holder.unit_price);
        }
        else {
            Util.uncrossText(holder.ref_price);
            Util.uncrossText(holder.unit_price);
            holder.sale_unit_price.visibility = View.INVISIBLE;
            holder.sale_ref_price.visibility = View.INVISIBLE;
        }

        when(info.source) {
            ProductSource.Bm -> holder.setSource(context, R.string.super_bm, R.color.colorBm);
            ProductSource.Mercadona -> holder.setSource(context, R.string.super_mercadona, R.color.colorMercadona);
            ProductSource.Dia -> holder.setSource(context, R.string.super_dia, R.color.colorDia);
            ProductSource.Carrefour -> holder.setSource(context, R.string.super_carrefour, R.color.colorCarrefour);
        }

        holder.icon.load(info.image_url) {
            placeholder(R.drawable.product_img_loading)
            error(R.drawable.product_img_error)

            target { drawable ->
                val bitmap = (drawable as BitmapDrawable).bitmap
                holder.icon.setImageBitmap(bitmap)
            }
        }
    }

    override fun getItemCount(): Int = products.size
}

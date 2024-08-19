package com.angellira.petvital1.recyclerview.adapter

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.angellira.petvital1.databinding.ActivityRecyclerBinding
import com.angellira.petvital1.model.Pet
import com.angellira.petvital1.model.Petshops

class ListaPetshops(
    private val propaganda: List<Petshops>,
    private val onItemClickListener: (Petshops) -> Unit
) : RecyclerView.Adapter<ListaPetshops.ViewHolder>(), Parcelable {

    inner class ViewHolder(
        private val preferencesUsuarioBinding: ActivityRecyclerBinding
    ) :
        RecyclerView.ViewHolder(preferencesUsuarioBinding.root) {

        private lateinit var umPetshop: Petshops

        init {
            preferencesUsuarioBinding.root.setOnClickListener {
                if (::umPetshop.isInitialized) {
                    onItemClickListener(umPetshop)
                }
            }
        }

        fun bind(preferencia: Petshops) {
            this.umPetshop = preferencia
            preferencesUsuarioBinding.imageRecycler.load(umPetshop.imagem)
            preferencesUsuarioBinding.nomePet.setText(umPetshop.name)
        }

    }

    constructor(parcel: Parcel) : this(
        TODO("propaganda"),
        TODO("onItemClickListener")
    ) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =
        ViewHolder(
            ActivityRecyclerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = propaganda.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val preferencia = propaganda[position]
        holder.bind(preferencia!!)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListaPetshops> {
        override fun createFromParcel(parcel: Parcel): ListaPetshops {
            return ListaPetshops(parcel)
        }

        override fun newArray(size: Int): Array<ListaPetshops?> {
            return arrayOfNulls(size)
        }
    }
}
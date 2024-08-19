package com.angellira.petvital1.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.angellira.petvital1.databinding.ActivityRecyclerBinding
import com.angellira.petvital1.model.Pet

    class ListaFotos(
    private val posts: List<Pet>,
    private val onItemClickListener: (Pet) -> Unit
) : RecyclerView.Adapter<ListaFotos.ViewHolder>() {

    inner class ViewHolder(
        private val preferencesUsuarioBinding: ActivityRecyclerBinding
    ) :
        RecyclerView.ViewHolder(preferencesUsuarioBinding.root) {

        private lateinit var cadaPet: Pet

        init {
            preferencesUsuarioBinding.root.setOnClickListener {
                if (::cadaPet.isInitialized) {
                    onItemClickListener(cadaPet)
                }
            }
        }

        fun bind(preferencia: Pet) {
            this.cadaPet = preferencia
            preferencesUsuarioBinding.imageRecycler.load(cadaPet.imagem)
            preferencesUsuarioBinding.nomePet.setText(cadaPet.name)
        }

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

    override fun getItemCount(): Int = posts.size


    override fun onBindViewHolder(holder: ViewHolder,   position: Int) {
        val preferencia = posts[position]
        holder.bind(preferencia!!)
    }
}
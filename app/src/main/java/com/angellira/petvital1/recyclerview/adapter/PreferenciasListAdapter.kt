package com.angellira.petvital1.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.angellira.petvital1.databinding.PreferencesUsuarioBinding

class PreferenciasListAdapter(
    private val preferencias: List<String>
) : RecyclerView.Adapter<PreferenciasListAdapter.ViewHolder>() {

    class ViewHolder(
        private val preferencesUsuarioBinding: PreferencesUsuarioBinding
    ) :
        RecyclerView.ViewHolder(preferencesUsuarioBinding.root) {

        fun bind(preferencia: String) {
            preferencesUsuarioBinding.recyclerView.text = preferencia
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =
        ViewHolder(
            PreferencesUsuarioBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = preferencias.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val preferencia = preferencias[position]
        holder.bind(preferencia)
    }
}
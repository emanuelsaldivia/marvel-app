package com.esaldivia.marvelheroes.ui.character

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esaldivia.marvelheroes.R
import com.esaldivia.marvelheroes.data.model.character.Character
import com.esaldivia.marvelheroes.databinding.CharacterItemLayoutBinding

class CharacterAdapter(private val characterList: List<Character>) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding =
            CharacterItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characterList[position])
    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    class CharacterViewHolder(private val binding: CharacterItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) {
            binding.characterName.text = character.name
            if (!character.description.isNullOrEmpty()) {
                binding.characterDescription.text = character.description
            } else {
                binding.characterDescription.text =
                    binding.root.context.getString(R.string.description_unavailable)
            }
        }
    }
}
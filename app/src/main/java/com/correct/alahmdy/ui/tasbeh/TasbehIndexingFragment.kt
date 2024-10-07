package com.correct.alahmdy.ui.tasbeh

import TasbehAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.correct.alahmdy.R
import com.correct.alahmdy.data.user.Tasbeh
import com.correct.alahmdy.databinding.FragmentTasbehIndexingBinding
import com.correct.alahmdy.helper.ClickListener
import com.correct.alahmdy.helper.Constants.CAST_ERROR
import com.correct.alahmdy.helper.FragmentChangeListener
import com.correct.alahmdy.helper.displayDialog
import com.correct.alahmdy.helper.onBackPressed
import com.correct.alahmdy.room.PrayDB
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch


class TasbehIndexingFragment : Fragment(), ClickListener {

    private lateinit var binding: FragmentTasbehIndexingBinding
    private lateinit var changeListener: FragmentChangeListener
    private lateinit var prayDB: PrayDB
    private lateinit var list: MutableList<Tasbeh>
    private lateinit var adapter: TasbehAdapter

    override fun onResume() {
        super.onResume()
        changeListener.onFragmentChangeListener(R.id.tasbehIndexingFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            changeListener = context
        } else {
            throw ClassCastException(CAST_ERROR)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTasbehIndexingBinding.inflate(inflater, container, false)
        prayDB = PrayDB.getDBInstance(requireContext())

        list = mutableListOf()
        adapter = TasbehAdapter(list,this)
        binding.tasbehRecyclerView.adapter = adapter

        lifecycleScope.launch {
            list.addAll(prayDB.tasbehDao().getAll())
            adapter.updateAdapter(list)
        }

        binding.headerLayout.txtTitle.text = resources.getString(R.string.tasbeh)
        binding.headerLayout.btnBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        onBackPressed {
            findNavController().navigate(R.id.homeFragment)
        }

        binding.addingCard.setOnClickListener {
            val dialog = displayDialog(R.layout.add_zekr_dialog)
            val txt = dialog.findViewById<EditText>(R.id.txt)
            val action = dialog.findViewById<View>(R.id.layout_actions)
            val buttonDone = action.findViewById<MaterialButton>(R.id.btn_add)
            val buttonCancel = action.findViewById<MaterialButton>(R.id.btn_cancel)
            buttonDone.text = resources.getString(R.string.done)

            buttonDone.setOnClickListener {
                val zekr = txt.text.toString().trim()
                if (zekr.isEmpty()) {
                    Log.v("Zekr mohamed", "Empty")
                } else {
                    lifecycleScope.launch {
                        var id = prayDB.tasbehDao().getNextID() ?: 0
                        id += 1
                        val tasbeh = Tasbeh(id, 1, zekr)
                        list.add(tasbeh)
                        adapter.updateAdapter(list)
                        prayDB.tasbehDao().insert(tasbeh)
                        Log.v("Zekr mohamed", zekr)
                        dialog.dismiss()
                        dialog.cancel()
                        findNavController().navigate(R.id.sebhaFragment)
                    }
                }
            }

            buttonCancel.setOnClickListener {
                dialog.dismiss()
                dialog.cancel()
                findNavController().navigate(R.id.sebhaFragment)
            }

            dialog.show()
        }

        return binding.root
    }

    override fun onItemClickListener(position: Int, extras: Bundle?) {

    }

    override fun onItemLongClickListener(position: Int, extras: Bundle?) {

    }
}
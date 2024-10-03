package com.kel11.inote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kel11.inote.adapter.NotesAdapter
import com.kel11.inote.database.NotesDatabase
import com.kel11.inote.entities.Notes
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

// Mendefinisikan kelas HomeFragment yang merupakan subclass dari BaseFragment
class HomeFragment : BaseFragment() {
    //deklarasi variabel
    var arrNotes = ArrayList<Notes>()
    // Mendeklarasikan objek adapter untuk catatan
    var notesAdapter: NotesAdapter = NotesAdapter()

    // Metode yang dipanggil saat fragment dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    // Metode yang dipanggil saat tampilan fragment dibuat
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    // Metode yang digunakan untuk membuat instance dari HomeFragment
    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.setHasFixedSize(true)

        recycler_view.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        launch {
            context?.let {
                var notes = NotesDatabase.getDatabase(it).noteDao().getAllNotes()
                notesAdapter!!.setData(notes)
                arrNotes = notes as ArrayList<Notes>
                recycler_view.adapter = notesAdapter
            }
        }

        // Menetapkan OnClickListener untuk adapter catatan
        notesAdapter!!.setOnClickListener(onClicked)

        // Mengatur OnClickListener untuk tombol fabBtnCreateNote
        fabBtnCreateNote.setOnClickListener {
            replaceFragment(CreateNoteFragment.newInstance(),false)
        }

        // Menetapkan listener untuk search_view
        search_view.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                var tempArr = ArrayList<Notes>()

                for (arr in arrNotes){
                    if (arr.title!!.toLowerCase(Locale.getDefault()).contains(p0.toString())){
                        tempArr.add(arr)
                    }
                }

                // Menetapkan data baru ke adapter dan memberi tahu perubahan
                notesAdapter.setData(tempArr)
                notesAdapter.notifyDataSetChanged()
                return true
            }

        })


    }

    // OnClickListener untuk item catatan pada adapter
    private val onClicked = object :NotesAdapter.OnItemClickListener{
        override fun onClicked(notesId: Int) {


            var fragment :Fragment
            var bundle = Bundle()
            bundle.putInt("noteId",notesId)
            fragment = CreateNoteFragment.newInstance()
            fragment.arguments = bundle

            replaceFragment(fragment,false)
        }

    }

    // Metode untuk mengganti fragment di dalam frame_layout
    fun replaceFragment(fragment:Fragment, istransition:Boolean){
        val fragmentTransition = activity!!.supportFragmentManager.beginTransaction()

        if (istransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransition.replace(R.id.frame_layout,fragment).addToBackStack(fragment.javaClass.simpleName).commit()
    }


}
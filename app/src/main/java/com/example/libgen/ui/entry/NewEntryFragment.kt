package com.example.libgen.ui.entry

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.libgen.BaseFragment
import com.example.libgen.R
import com.example.libgen.data.models.Book
import com.example.libgen.databinding.FragmentNewEntryBinding
import com.example.libgen.utli.Constants.CODE_PICK_IMAGE
import com.example.libgen.utli.Constants.PERMISSION_REQUEST_CODE
import com.example.libgen.utli.Constants.REQUEST_FAILED_MESSAGE
import com.example.libgen.utli.extensions.*
import com.example.libgen.utli.getOutputMediaFile
import java.util.*

class NewEntryFragment : BaseFragment() {

    companion object {
        private val TAG = NewEntryFragment::class.java.simpleName
    }

    //Global
    private lateinit var binding: FragmentNewEntryBinding
    private val viewModel by lazy { requireActivity().getMainViewModel() }
    private val glide by lazy { glide() }
    private val calendar by lazy { Calendar.getInstance() }
    private val authors by lazy {
        listOf(
            "Amish Tripathi",
            "George R. R. Martin",
            "J. K. Rowling",
            "Mitch Albom",
            "William Shakespeare"
        )
    }
    private var adapter: CoverGridAdapter? = null
    private var name: String? = null
    private var authorIndex = 0
    private var price: String? = null
    private var doi: Date? = null
    private val covers = mutableListOf<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_entry, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()

        setupAuthorsSpinner()

        setupRecycler()

    }

    private fun setListeners() {

        binding.imgBtnBack.setOnClickListener { onBackPressed() }

        binding.spinnerAuthors.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p: AdapterView<*>?, v: View?, index: Int, id: Long) {
                    authorIndex = index
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.editBookPrice.setOnEditorActionListener { _, _, _ ->
            clearFocus()
            hideKeyboard()
            true
        }

        binding.editIssuedOn.isFocusable = false
        binding.editIssuedOn.setOnClickListener {
            clearFocus()
            hideKeyboard()
            DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, y, m, d ->
                    calendar.set(Calendar.YEAR, y)
                    calendar.set(Calendar.MONTH, m)
                    calendar.set(Calendar.DAY_OF_MONTH, d)
                    doi = calendar.time
                    if (doi != null) binding.editIssuedOn.setText(doi!!.formatDate())
                },
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        binding.btnAddCovers.setOnClickListener { checkPermissions() }

        binding.btnSubmit.setOnClickListener { submitData() }

    }

    private fun setupAuthorsSpinner() {

        binding.spinnerAuthors.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, authors)
                .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

    }

    private fun setupRecycler() {

        adapter = CoverGridAdapter(glide)

        binding.recyclerCovers.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerCovers.adapter = adapter

    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            .apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
        // Check if device can handle Gallery Intent
        if (galleryIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivityForResult(
                Intent.createChooser(galleryIntent, "Select Picture"),
                CODE_PICK_IMAGE
            )
        }
    }

    private fun submitData() {
        if (isValid()) {
            val book = Book(
                _id = UUID.randomUUID().toString(),
                name = name!!,
                author = authors[authorIndex],
                price = price!!,
                doi = doi!!.time,
                covers = covers.joinToString(separator = ",")
            )
            viewModel.addBook(book)
            onBackPressed()
        }
    }

    private fun isValid(): Boolean {
        var isValid = true

        name = binding.editBookName.text?.toString()?.trim()
        if (name.isNullOrEmpty()) {
            binding.tilBookName.error = " "
            isValid = false
        }

        price = binding.editBookPrice.text?.toString()?.trim()
        if (price.isNullOrEmpty()) {
            binding.tilBookPrice.error = " "
            isValid = false
        }

        if (doi == null) {
            binding.tilIssuedOn.error = " "
            isValid = false
        }

        return isValid
    }

    override fun onBackPressed() {
        hideKeyboard()
        super.onBackPressed()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = requireActivity().requiredPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )

            when (permissions.size) {
                0 -> openGallery()
                else -> {
                    if (requireActivity().shouldShowRational(permissions))
                        requireActivity().showRationalDialog(resources.getString(R.string.msg_permissions))
                    else
                        requestPermissions(permissions, PERMISSION_REQUEST_CODE)
                }
            }
        } else openGallery()
    }

    override fun onRequestPermissionsResult(rC: Int, p: Array<out String>, gR: IntArray) {
        checkPermissions()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CODE_PICK_IMAGE -> {
                    if (data == null) return

                    val cd = data.clipData
                    val d = data.data

                    when {
                        cd != null && cd.itemCount > 0 -> {
                            val list = mutableListOf<Uri>()
                            for (index in 0 until cd.itemCount)
                                list.add(cd.getItemAt(index).uri)

                            convertCovers(list)
                        }
                        d != null -> convertCovers(listOf(d))
                        else -> showShortSnackBar(REQUEST_FAILED_MESSAGE)
                    }

                }
            }
        }
    }

    private fun convertCovers(list: List<Uri>) {
        lifecycleScope.launchWhenStarted {
            list.forEach { uri ->
                val bitmap = uri.toBitmap(requireContext().contentResolver)
                val file = bitmap.toFile(
                    getOutputMediaFile(requireContext(), "covers")
                )
                covers.add(file.toUri())
            }
            adapter?.swapData(covers)
        }
    }

}
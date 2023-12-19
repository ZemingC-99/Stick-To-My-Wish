package com.example.StickToMyWish

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BasicGridItem
import com.afollestad.materialdialogs.customview.customView
import com.example.StickToMyWish.MyApplication.Companion.darkMode
import com.example.StickToMyWish.adpater.ListAdapter
import com.example.StickToMyWish.adpater.TabsAdapter
import com.example.StickToMyWish.databinding.ActivityMainBinding
import com.example.StickToMyWish.databinding.LayoutTitleBinding
import com.example.StickToMyWish.entity.Project
import com.example.StickToMyWish.entity.Tab
import com.example.StickToMyWish.util.DialogUtil
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID
import java.util.function.Consumer
import kotlin.streams.toList


class MainActivity : BaseActivity() {

    private val TAG = "MainActivity"

    private var tabAdapter: TabsAdapter = TabsAdapter()
    private var listAdapter: ListAdapter = ListAdapter()
    private lateinit var binding: ActivityMainBinding
    private lateinit var titleBinding: LayoutTitleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        titleBinding = LayoutTitleBinding.bind(binding.titleLayout.root)
        setContentView(binding.root)

        titleBinding.title.text = "Your Challenges"

        val items = arrayListOf(
            BasicGridItem(R.drawable.tab, "Add tab"),
            BasicGridItem(R.drawable.project, "Add project"),
        )

        binding.mainRecyclerTab.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.mainRecyclerTab.adapter = tabAdapter
        tabAdapter.itemClickListener = Consumer { str ->

            if (TextUtils.isEmpty(str)) {
                flashData()
            } else {
                listAdapter.projectList = listAdapter.projectList!!.stream().filter {
                    it.tab.equals(str)
                }.toList()
                listAdapter.notifyDataSetChanged()
            }
        }
        binding.mainRecyclerList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        listAdapter.itemClickListener = View.OnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        binding.mainRecyclerList.adapter = listAdapter

        flashData()

        titleBinding.imageMore.visibility = View.VISIBLE
        titleBinding.imageMore.setOnClickListener {
            DialogUtil().showBottomGridDialog(this, items) { dialog, index, item ->
                kotlin.run {
                    if (index == 1) {
                        val dialogRoot: View =
                            LayoutInflater.from(this).inflate(R.layout.dialog_project, null, false)
                        val title =
                            dialogRoot.findViewById<TextInputLayout>(R.id.dialog_input_title_layout)
                        val tab =
                            dialogRoot.findViewById<TextInputLayout>(R.id.dialog_input_tab_layout)
                        val start =
                            dialogRoot.findViewById<TextInputLayout>(R.id.dialog_input_start_layout)
                        val end =
                            dialogRoot.findViewById<TextInputLayout>(R.id.dialog_input_end_layout)
                        val content =
                            dialogRoot.findViewById<TextInputLayout>(R.id.dialog_input_content_layout)

                        start.editText!!.setOnClickListener {
                            val ca = Calendar.getInstance()
                            var mYear = ca[Calendar.YEAR]
                            var mMonth = ca[Calendar.MONTH]
                            var mDay = ca[Calendar.DAY_OF_MONTH]

                            val datePickerDialog = DatePickerDialog(
                                this, { _, year, month, dayOfMonth ->
                                    mYear = year
                                    mMonth = month
                                    mDay = dayOfMonth
                                    val mDate = "${year}-${month + 1}-${dayOfMonth}"
                                    start.editText!!.setText(mDate)
                                }, mYear, mMonth, mDay
                            )
                            datePickerDialog.show()
                        }

                        end.editText!!.setOnClickListener {
                            val ca = Calendar.getInstance()
                            var mYear = ca[Calendar.YEAR]
                            var mMonth = ca[Calendar.MONTH]
                            var mDay = ca[Calendar.DAY_OF_MONTH]

                            val datePickerDialog = DatePickerDialog(
                                this, { _, year, month, dayOfMonth ->
                                    mYear = year
                                    mMonth = month
                                    mDay = dayOfMonth
                                    val mDate = "${year}-${month + 1}-${dayOfMonth}"
                                    end.editText!!.setText(mDate)
                                }, mYear, mMonth, mDay
                            )
                            datePickerDialog.show()
                        }

                        MaterialDialog(this).title(null, "Add project")
                            .customView(view = dialogRoot, scrollable = true)
                            .positiveButton(text = "confirm") {

                                val id = UUID.randomUUID().toString()
                                val title = title.editText!!.text.toString()
                                val tab = tab.editText!!.text.toString()
                                val start = start.editText!!.text.toString()
                                val end = end.editText!!.text.toString()
                                val content = content.editText!!.text.toString()

                                if (TextUtils.isEmpty(title)) {
                                    Toast.makeText(this, "title can't be empty", Toast.LENGTH_SHORT)
                                        .show()
                                    return@positiveButton
                                }

                                if (TextUtils.isEmpty(tab)) {
                                    Toast.makeText(this, "tab can't be empty", Toast.LENGTH_SHORT)
                                        .show()
                                    return@positiveButton
                                }

                                if (TextUtils.isEmpty(start)) {
                                    Toast.makeText(this, "start can't be empty", Toast.LENGTH_SHORT)
                                        .show()
                                    return@positiveButton
                                }

                                if (TextUtils.isEmpty(end)) {
                                    Toast.makeText(this, "end can't be empty", Toast.LENGTH_SHORT)
                                        .show()
                                    return@positiveButton
                                }

                                if (TextUtils.isEmpty(content)) {
                                    Toast.makeText(
                                        this, "content can't be empty", Toast.LENGTH_SHORT
                                    ).show()
                                    return@positiveButton
                                }

                                val project = Project(
                                    id = id,
                                    title = title,
                                    tab = tab,
                                    start = start,
                                    end = end,
                                    content = content
                                )

                                lifecycleScope.launch(Dispatchers.IO) {
                                    MyDataBase.instance!!.projectDao().insert(project)
                                    runOnUiThread {
                                        flashData()
                                    }
                                }
                            }.show()
                    } else {
                        val editText = EditText(this)
                        val builder =
                            AlertDialog.Builder(this).setTitle("Add tab").setView(editText)
                                .setPositiveButton("Add a new tab") { dialogInterface, i ->
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        val tab = Tab(
                                            id = UUID.randomUUID().toString(),
                                            name = editText.text.toString()
                                        )
                                        MyDataBase.instance!!.tabDao().insert(tab)
                                        runOnUiThread {
                                            flashData()
                                        }
                                    }
                                }
                        builder.create().show()
                    }
                    dialog.dismiss()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        val backgroundColor: Int =
            ContextCompat.getColor(this, if (darkMode) R.color.black else R.color.white)
        val displayColor: Int =
            ContextCompat.getColor(this, if (darkMode) R.color.white else R.color.black)

        binding.titleLayout.title.setTextColor(displayColor)
        val moreImage = ContextCompat.getDrawable(this, R.drawable.more)
        binding.titleLayout.imageMore.setImageDrawable(moreImage)
        binding.view7.setBackgroundColor(backgroundColor)
    }

    private fun flashData() {
        lifecycleScope.launch(Dispatchers.IO) {
            tabAdapter.tabList = MyDataBase.instance!!.tabDao().getTabList()
            listAdapter.projectList = MyDataBase.instance!!.projectDao().getProjectList()
            runOnUiThread {
                tabAdapter.notifyDataSetChanged()
                listAdapter.notifyDataSetChanged()
            }
        }
    }

}
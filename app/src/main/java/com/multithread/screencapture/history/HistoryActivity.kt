package com.multithread.screencapture.history

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.multithread.screencapture.R
import com.multithread.screencapture.databinding.ActivityHistoryBinding
import com.multithread.screencapture.history.adapter.HistoryAdapter
import com.multithread.screencapture.home.view.MainActivity
import com.multithread.screencapture.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {

    private val historyViewModel: HistoryViewModel by viewModels()
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var mAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.label_history)
        binding.historyViewModel = historyViewModel
        init()
        initObserver()
        historyViewModel.getAllImageList()
    }

    private fun initObserver() {
        historyViewModel.imageHistoryLiveData.observe(this, {
            if (historyViewModel.imageHistoryLiveData.value != null) {
                historyViewModel.isEmpty.value = historyViewModel.imageHistoryLiveData.value.isNullOrEmpty()
                if (historyViewModel.isEmpty.value == true)
                    binding.tvEmptyView.visibility = View.VISIBLE
                else
                    binding.tvEmptyView.visibility = View.GONE
                mAdapter.replaceData(historyViewModel.imageHistoryLiveData.value)
            }
        })
        historyViewModel.onDeleteEvent.observe(this, {
            if (historyViewModel.onDeleteEvent.value != null && historyViewModel.onDeleteEvent.value!!) {
                Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                historyViewModel.getAllImageList()
            }

        })
        historyViewModel.onItemClickEvent.observe(this, {
            if (historyViewModel.onItemClickEvent.value != null) {
                val intent = Intent(this, MainActivity::class.java);
                intent.putExtra(Constants.INTENT_DATA, historyViewModel.onItemClickEvent.value)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        })
    }

    private fun init() {
        setupAdapter()
    }

    private fun setupAdapter() {
        val rv: RecyclerView = binding.rvHistory
        rv.layoutManager = GridLayoutManager(this, 2)
        mAdapter = HistoryAdapter(ArrayList(0), historyViewModel, this)
        rv.adapter = mAdapter
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
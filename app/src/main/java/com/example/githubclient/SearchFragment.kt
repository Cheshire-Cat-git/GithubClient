package com.example.githubclient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubclient.data.RetrofitClient
import com.example.githubclient.databinding.FragmentSearchBinding
import com.example.githubclient.ui.RepoAdapter
import com.example.githubclient.ui.RepoDetailsFragment
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RepoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RepoAdapter { repo ->
            val owner = repo.owner?.login.orEmpty()
            val repoName = repo.name
            if (owner.isNotEmpty() && repoName.isNotEmpty()) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main, RepoDetailsFragment.newInstance(owner, repoName))
                    .addToBackStack(null)
                    .commit()
            }
        }

        binding.rvRepos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRepos.adapter = adapter

        binding.btnSearch.setOnClickListener {
            val orgName = binding.etOrgName.text.toString().trim()
            if (orgName.isNotEmpty()) {
                performSearch(orgName)
            }
        }
    }

    private fun performSearch(orgName: String) {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getOrgRepos(orgName)
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val repos = response.body().orEmpty()
                    if (repos.isEmpty()) {
                        adapter.updateList(emptyList())
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.no_repos_found_for_org, orgName),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        adapter.updateList(repos)
                    }
                } else {
                    adapter.updateList(emptyList())
                }
            } catch (_: Exception) {
                binding.progressBar.visibility = View.GONE
                adapter.updateList(emptyList())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
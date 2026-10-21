package com.example.githubclient.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.githubclient.R
import com.example.githubclient.data.RepoDto
import com.example.githubclient.data.RetrofitClient
import com.example.githubclient.databinding.FragmentRepoDetailsBinding
import kotlinx.coroutines.launch

class RepoDetailsFragment : Fragment() {

    private var _binding: FragmentRepoDetailsBinding? = null
    private val binding get() = _binding!!

    private var owner: String = ""
    private var repoName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            owner = it.getString(ARG_OWNER, "")
            repoName = it.getString(ARG_REPO, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (owner.isNotEmpty() && repoName.isNotEmpty()) {
            fetchRepoDetails(owner, repoName)
        }
    }

    private fun fetchRepoDetails(owner: String, repo: String) {
        binding.progressBarDetails.visibility = View.VISIBLE
        binding.scrollViewDetails.visibility = View.GONE
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getRepoDetails(owner, repo)
                binding.progressBarDetails.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    binding.scrollViewDetails.visibility = View.VISIBLE
                    displayRepoDetails(response.body()!!)
                }
            } catch (_: Exception) {
                binding.progressBarDetails.visibility = View.GONE
            }
        }
    }

    private fun displayRepoDetails(repo: RepoDto) {
        binding.tvDetailsName.text = repo.fullName ?: repo.name

        if (repo.description.isNullOrBlank()) {
            binding.tvDetailsDescription.text = getString(R.string.no_description_message)
        } else {
            binding.tvDetailsDescription.text = repo.description
        }

        binding.tvDetailsForks.text = getString(R.string.forks_format, repo.forksCount)
        binding.tvDetailsWatchers.text = getString(R.string.watchers_format, repo.watchersCount)
        binding.tvDetailsOpenIssues.text = getString(R.string.open_issues_format, repo.openIssuesCount)

        if (repo.fork && !repo.parent?.fullName.isNullOrEmpty()) {
            binding.tvDetailsParent.visibility = View.VISIBLE
            binding.tvDetailsParent.text = getString(R.string.forked_from_format, repo.parent.fullName)
        } else {
            binding.tvDetailsParent.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_OWNER = "arg_owner"
        private const val ARG_REPO = "arg_repo"

        fun newInstance(owner: String, repo: String): RepoDetailsFragment {
            return RepoDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_OWNER, owner)
                    putString(ARG_REPO, repo)
                }
            }
        }
    }
}
package su.usatu.navigator.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import su.usatu.navigator.R
import su.usatu.navigator.data.repository.PreferenceRepository
import su.usatu.navigator.databinding.FragmentMainBinding
import su.usatu.navigator.epoxy.history
import su.usatu.navigator.models.QueryModel
import su.usatu.navigator.ui.MainActivity
import su.usatu.navigator.util.getCurrentDate
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : Fragment() {
    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    private lateinit var binding: FragmentMainBinding
    private lateinit var from: String
    private lateinit var to: String

    private val mainViewModel: MainViewModel by viewModels()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.updateHistory()

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            (activity as MainActivity).hideKeyboard()

            val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, false)
            binding.root.let {
                TransitionManager.beginDelayedTransition(it, sharedAxis)
            }

            if (it)
                binding.loading.visibility = View.VISIBLE
            else
                binding.loading.visibility = View.GONE
        }

        binding.historyRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)

        mainViewModel.history.observe(viewLifecycleOwner) {
            if (it.size > 0) {
                binding.empty.visibility = View.GONE
                binding.historyRecycler.visibility = View.VISIBLE
                binding.historyRecycler.withModels {
                    it.forEachIndexed { index, queryModel ->
                        history {
                            id(index)
                            queryModel(queryModel)
                            onHistoryClickListener(object : OnHistoryClickListener {
                                override fun onHistoryClick(item: QueryModel, position: Long) {
                                    from = item.from
                                    to = item.to
                                    mainViewModel.navigationPointsList.postValue(item.pointsList)
                                }
                            })
                            from(queryModel.from)
                            to(queryModel.to)
                        }
                    }
                }
            } else {
                binding.empty.visibility = View.VISIBLE
                binding.historyRecycler.visibility = View.GONE
            }
        }

        mainViewModel.pointsCount.observe(viewLifecycleOwner) {
            val currentDate = getCurrentDate()
            if (preferenceRepository.lastDate != currentDate) {
                mainViewModel.updatePointsList()
                preferenceRepository.lastDate = currentDate
            } else
                mainViewModel.loadPointsFromCache()
        }

        mainViewModel.pointsList.observe(viewLifecycleOwner) { newPointsList ->
            (binding.from.editText as AutoCompleteTextView).setAdapter(
                PointsAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    android.R.id.text1,
                    newPointsList
                )
            )
            (binding.to.editText as AutoCompleteTextView).setAdapter(
                PointsAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    newPointsList
                )
            )

            binding.ndaAppbar.setEndButtonOnClickListener {
                val popup = PopupMenu(requireContext(), it)

                popup.menuInflater
                    .inflate(R.menu.popup_menu, popup.menu)

                popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        return when (item.itemId) {
                            R.id.clear_history -> {
                                mainViewModel.deleteAllHistory()
                                true
                            }
                            R.id.update -> {
                                mainViewModel.updatePointsList()
                                true
                            }
                            R.id.clear_cache -> {
                                Observable.create<() -> Unit> { subscriber ->
                                    subscriber.onNext {
                                        Glide.get(requireContext())
                                            .clearDiskCache()
                                    }
                                }
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe {
                                        Glide.get(requireContext())
                                            .clearMemory()

                                        Toast.makeText(
                                            context,
                                            R.string.cache_cleared,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                true
                            }
                            else ->
                                false
                        }
                    }
                })
                popup.setForceShowIcon(true)
                popup.show()
            }

            binding.ndaAppbar.setStartButtonOnClickListener {
                Toast.makeText(context, R.string.made_with_love, Toast.LENGTH_SHORT).show()
            }

            binding.searchButton.setOnClickListener {
                from = binding.from.editText?.text.toString()
                to = binding.to.editText?.text.toString()

                newPointsList.map { it.title }.toList().let { titlesList ->
                    val fromInTitlesList = from in titlesList
                    val toInTitlesList = to in titlesList

                    if (!fromInTitlesList && !toInTitlesList) {
                        Toast.makeText(
                            context,
                            R.string.no_such_points,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    if (!fromInTitlesList) {
                        Toast.makeText(
                            context,
                            R.string.no_such_start_point,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    if (!toInTitlesList) {
                        Toast.makeText(
                            context,
                            R.string.no_such_end_point,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    if (from.equals(to)) {
                        Toast.makeText(
                            context,
                            R.string.points_are_equal,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }
                }
                mainViewModel.loadNavigationPointsList(
                    from,
                    to
                )


            }
        }

        mainViewModel.navigationPointsList.observe(viewLifecycleOwner) {
            if (it.size > 0) {
                mainViewModel.addToHistory(
                    from,
                    to
                )
                this@MainFragment.findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToPicturesViewerFragment(
                        it.map { it.imageCode }.toTypedArray(),
                        from,
                        to
                    )
                )
                mainViewModel.clearNavigation()
            }
        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (preferenceRepository.animations) {
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
            returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        }
    }
}
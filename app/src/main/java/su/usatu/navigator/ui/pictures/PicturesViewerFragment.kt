package su.usatu.navigator.ui.pictures

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionManager
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import su.usatu.navigator.R
import su.usatu.navigator.data.repository.PreferenceRepository
import su.usatu.navigator.databinding.FragmentPicturesViewerBinding
import javax.inject.Inject
import kotlin.properties.Delegates


@AndroidEntryPoint
class PicturesViewerFragment : Fragment() {
    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    private lateinit var binding: FragmentPicturesViewerBinding
    private lateinit var viewPagerAdapter: PictureViewPagerAdapter
    private val compositeDisposable = CompositeDisposable()
    private val args by navArgs<PicturesViewerFragmentArgs>()
    var systemUI by Delegates.notNull<Int>()

    private var size = 0

    private val onPageChangeCallback = object : OnPageChangeCallback() {
        @SuppressLint("SetTextI18n")
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.stage.text =
                "${getString(R.string.step)} ${position + 1} ${getString(R.string.of_steps)} ${size}"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPicturesViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this)
            .asGif()
            .load(getString(R.string.swipe_gif_url))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.swipeGif)

        viewPagerAdapter =
            PictureViewPagerAdapter(
                context
            )

        viewPagerAdapter.submitList(args.pictures.toList())

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.fromText.text = args.from
        binding.toText.text = args.to
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback)

        binding.leftClick.setOnClickListener {
            binding.viewPager.currentItem.let {
                if (it > 0)
                    binding.viewPager.currentItem = it - 1
            }
        }

        binding.rightClick.setOnClickListener {
            binding.viewPager.currentItem.let {
                if (it < args.pictures.size - 1)
                    binding.viewPager.currentItem = it + 1
            }
        }

        binding.swipeGuide.setOnClickListener {
            val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, false)
            binding.root.let {
                TransitionManager.beginDelayedTransition(it, sharedAxis)
            }
            binding.swipeGuide.visibility = View.GONE

            if (!preferenceRepository.swipe)
                preferenceRepository.swipe = true
        }

        binding.help.setOnClickListener {
            val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, false)
            binding.root.let {
                TransitionManager.beginDelayedTransition(it, sharedAxis)
            }
            binding.swipeGuide.visibility = View.VISIBLE
        }

        binding.from.setOnClickListener {
            binding.viewPager.currentItem = 0
        }

        binding.to.setOnClickListener {
            binding.viewPager.currentItem = args.pictures.size - 1
        }

        if (!preferenceRepository.swipe) {
            binding.help.performClick()
        }

        requireActivity().window.decorView.let {
            systemUI = it.systemUiVisibility
            it.systemUiVisibility =
                it.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun onPause() {
        val t = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (t != Configuration.UI_MODE_NIGHT_YES) {
            requireActivity().window.decorView.let {
                it.systemUiVisibility =
                    it.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        super.onPause()
    }

    override fun onResume() {
        requireActivity().window.decorView.let {
            systemUI = it.systemUiVisibility
            it.systemUiVisibility =
                it.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        size = args.pictures.size

        if (preferenceRepository.animations) {
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
            returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        }
    }

}
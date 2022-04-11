package su.usatu.navigator.ui.pictures

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import su.usatu.navigator.data.PreferenceRepository
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

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            binding.stage.text = "Шаг ${position + 1} из ${args.pictures.size}"
        }

        override fun onPageSelected(position: Int) {}

        override fun onPageScrollStateChanged(state: Int) {}

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
        viewPagerAdapter =
            PictureViewPagerAdapter(
                context,
                args.pictures.toList()
            )

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.fromText.text = args.from
        binding.toText.text = args.to
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.addOnPageChangeListener(onPageChangeListener)
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

        if (preferenceRepository.animations) {
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
            returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        }
    }

}
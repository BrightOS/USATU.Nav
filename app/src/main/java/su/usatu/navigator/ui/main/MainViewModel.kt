package su.usatu.navigator.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.glueme.api.USATUNavigatorAPI
import su.usatu.navigator.data.repository.HistoryRepository
import su.usatu.navigator.data.repository.PointsRepository
import su.usatu.navigator.models.PointModel
import su.usatu.navigator.models.QueryModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val pointsRepository: PointsRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _history = MutableLiveData(listOf<QueryModel>())
    val history: LiveData<List<QueryModel>>
        get() = _history

    private val _pointsList = MutableLiveData(listOf<PointModel>())
    val pointsList: LiveData<List<PointModel>>
        get() = _pointsList

    private val _navigationPointsList = MutableLiveData(listOf<PointModel>())
    val navigationPointsList: LiveData<List<PointModel>>
        get() = _navigationPointsList

    fun clearNavigation() {
        _navigationPointsList.value = listOf()
    }

    val pointFrom = MutableLiveData(PointModel(0, "", false, ""))
    val pointTo = MutableLiveData(PointModel(0, "", false, ""))

    val historySize = historyRepository.getHistorySize()
    val pointsCount = pointsRepository.getPointsSize()

    private val compositeDisposable = CompositeDisposable()

    fun deleteAllHistory() {
        _isLoading.value = true
        Observable.create<Unit> {
            it.onNext(
                historyRepository.deleteAllHistory()
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _isLoading.value = false
                updateHistory()
            }
            .addTo(compositeDisposable)
    }

    fun addToHistory(from: String, to: String) {
        Observable.create<Unit> {
            it.onNext(
                historyRepository.addQuery(
                    QueryModel(
                        from = from,
                        to = to
                    )
                )
            )
        }
            .subscribeOn(Schedulers.io())
            .subscribeBy(onError = {
                it.printStackTrace()
            }, onNext = {
                println("kek")
            })
            .addTo(compositeDisposable)
    }

    fun updateHistory() {
        Observable.create<List<QueryModel>> {
            it.onNext(
                historyRepository.getAllHistory()
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                println(it.size)
                _history.value = it
            }
            .addTo(compositeDisposable)
    }

    fun loadPointsFromCache() {
        Observable.create<List<PointModel>> { subscriber ->
            subscriber.onNext(
                pointsRepository.getAllPoints()
            )
        }
            .subscribeOn(Schedulers.io())
            .concatMapSingle {
                (it as ArrayList).removeAll { !it.isVisible }
                Single.just(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _pointsList.value = it
            }
            .addTo(compositeDisposable)
    }

    fun updatePointsList() {
        _isLoading.value = true
        try {
            Observable.create<List<PointModel>> { subscriber ->
                subscriber.onNext(USATUNavigatorAPI.getAllPoints())
            }
                .subscribeOn(Schedulers.io())
                .concatMap {
                    pointsRepository.addPoints(it)
                    Observable.just(false)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onError = {
                    _isLoading.value = true
                }, onNext = {
                    _isLoading.value = it
                })
                .addTo(compositeDisposable)
        } catch (e: Exception) {
            _isLoading.value = true
        }
    }

    fun loadNavigationPointsList(fromTitle: String, toTitle: String) {
        _isLoading.value = true
        Observable.create<List<PointModel>> { subscriber ->
            subscriber.onNext(
                USATUNavigatorAPI.findWayByPointsId(
                    pointsList.value!!.first { it.title.contains(fromTitle) }.id,
                    pointsList.value!!.first { it.title.contains(toTitle) }.id
                )
            )
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                _navigationPointsList.value = it
                _isLoading.value = false
            }, onError = {
                it.printStackTrace()
                _isLoading.value = false
            })
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}
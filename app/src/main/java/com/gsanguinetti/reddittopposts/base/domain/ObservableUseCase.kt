package com.gsanguinetti.reddittopposts.base.domain

import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

abstract class ObservableUseCase<T, in PARAMS> : AbstractDisposableUseCase() {

    protected abstract fun buildUseCaseObservable(params: PARAMS? = null): Observable<T>

    open fun execute(observer: DisposableObserver<T>, params: PARAMS? = null) {
        disposables.clear()

        val observer = this.buildUseCaseObservable(params)
            .setUpForUseCase()
            .subscribeWith(observer)

        observer.run { addDisposable(this) }
    }

    fun <T> Observable<T>.setUpForUseCase(): Observable<T> = subscribeOn(Schedulers.io()).observeOn(
        Schedulers.computation()
    )
}
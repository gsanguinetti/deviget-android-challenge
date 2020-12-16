package com.gsanguinetti.reddittopposts.base

import com.gsanguinetti.reddittopposts.mockedModules
import org.koin.test.AutoCloseKoinTest

abstract class AbstractModuleTest : AutoCloseKoinTest() {
    protected val mockedContextModule = mockedModules
}
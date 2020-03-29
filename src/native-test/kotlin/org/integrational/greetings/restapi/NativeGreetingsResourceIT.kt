package org.integrational.greetings.restapi

import io.quarkus.test.junit.NativeImageTest

@NativeImageTest
open class NativeGreetingsResourceIT : GreetingsResourceTest()
package org.integrational.greetings.restapi

import io.quarkus.test.junit.NativeImageTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags

@NativeImageTest
@Tags(Tag("integration"), Tag("restapi"), Tag("native"))
class NativeGreetingsAPIIT : GreetingsAPITest()
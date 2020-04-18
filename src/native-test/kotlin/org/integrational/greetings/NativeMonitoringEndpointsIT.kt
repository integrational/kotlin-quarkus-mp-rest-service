package org.integrational.greetings

import io.quarkus.test.junit.NativeImageTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags

@NativeImageTest
@Tags(Tag("integration"), Tag("monitoring"), Tag("native"))
class NativeMonitoringEndpointsIT : MonitoringEndpointsTest()
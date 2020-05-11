package com.vesna.stackexchange.testutils

import com.vesna.stackexchange.di.AppModule
import com.vesna.stackexchange.presentation.DependencyGraph
import com.vesna.stackexchange.presentation.Injector
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import javax.inject.Named

class MockServerTestRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                mockServer = MockWebServer()
                mockServer.start()
                // TODO do this better
                DependencyGraph.injector = DaggerTestComponent.builder()
                    .testConfigModule(TestConfigModule(mockServer.url("/").toString()))
                    .build()
                try {
                    base.evaluate()
                } finally {
                    mockServer.shutdown()
                }
            }
        }
    }

    private lateinit var mockServer: MockWebServer

    fun enqueueResponse(body: String, responseCode: Int) {
        mockServer.enqueue(
            MockResponse()
                .setResponseCode(responseCode)
                .setBody(body)
        )
    }
}

@Component(modules = [ AppModule::class, TestConfigModule::class ])
interface TestComponent : Injector

@Module
class TestConfigModule(private val baseUrl: String) {

    @Provides
    @Named("api")
    fun baseUrl(): String {
        return baseUrl
    }
}

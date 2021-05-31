package com.gorlah.apisim.simulation

import com.gorlah.apisim.simulation.SimulationDefinition.Delay
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.Random
import kotlin.math.roundToLong

@Service
class SimulationDelayService {

    private val random = Random()

    fun delayIfNecessary(delay: Delay?): Mono<Void> =
        if (delay != null) {
            gaussianDuration(delay.average, delay.variance).let { Mono.delay(it) }.then()
        } else {
            Mono.empty()
        }

    private fun gaussianDuration(average: Long, variance: Long) =
        (average + (random.nextGaussian() * variance))
            .roundToLong()
            .let { Duration.ofMillis(it) }
}
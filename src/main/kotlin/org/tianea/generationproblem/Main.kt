package org.tianea.generationproblem

import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Main")

fun main() {
    logger.info("Starting genetic algorithm...")
    val algorithm = GenerationAlgorithm()
    algorithm.calculate()
    logger.info("Genetic algorithm completed.")
}

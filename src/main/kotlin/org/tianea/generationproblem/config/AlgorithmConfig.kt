package org.tianea.generationproblem.config

data class AlgorithmConfig(
    val maxGenerationCount: Int = 100,
    val eliteRatio: Double = 0.3,
    val crossRatio: Double = 0.5,
    val initSkuKindCount: Int = 1000,
    val initOrderCount: Int = 1000,
) 
package org.tianea.generationproblem

import org.slf4j.LoggerFactory
import org.tianea.generationproblem.config.AlgorithmConfig
import org.tianea.generationproblem.model.Individual
import org.tianea.generationproblem.model.Order
import org.tianea.generationproblem.model.OrderSku
import kotlin.random.Random

class GenerationAlgorithm(
    private val config: AlgorithmConfig = AlgorithmConfig(),
    private val random: Random = Random.Default,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun cross(i1: Individual, i2: Individual): MutableList<Individual> {
        val mixedOrders = (i1.orders + i2.orders).toMutableList()
        mixedOrders.shuffle(random)
        
        return mutableListOf(
            Individual(mixedOrders.take(mixedOrders.size / 2).toMutableList()),
            Individual(mixedOrders.takeLast(mixedOrders.size / 2).toMutableList()),
        )
    }

    fun mutation(i1: Individual, i2: Individual): MutableList<Individual> {
        val u = i1.orders[random.nextInt(i1.orders.size)]
        val v = i2.orders[random.nextInt(i2.orders.size)]

        i1.orders.remove(u)
        i2.orders.remove(v)

        i1.orders.add(v)
        i2.orders.add(u)

        // 적합도 캐시 무효화
        i1.invalidateFitness()
        i2.invalidateFitness()

        return mutableListOf(i1, i2)
    }

    fun calculate() {
        val skuIds = (0..config.initSkuKindCount).map { "sku_$it" }

        // 1 ~ 5 까지 랜덤한 개수의 스큐를 가지는 주문 생성
        val orders = (0..config.initOrderCount).map {
            Order(
                skus = (0 until random.nextInt(1, 6))
                    .map {
                        OrderSku(skuId = skuIds[random.nextInt(skuIds.size)], quantity = random.nextLong(1, 11))
                    }
                    .toMutableList()
            )
        }

        // 10개 씩 나누어서 첫번째 세대 생성
        var initialPopulation = (0..orders.lastIndex step 10)
            .map { i -> Individual(orders.slice(i until minOf(i + 10, orders.size)).toMutableList()) }
            .toMutableList()

        // 현재 점수 평가
        val initialMaxIndividual = initialPopulation.maxBy { it.fitness }
        logger.info("Initial maximum individual fitness: ${initialMaxIndividual.fitness}")

        for (i in 0 until config.maxGenerationCount) {
            val newPopulation = mutableListOf<Individual>()

            val sortedPopulation = initialPopulation.sortedBy { it.fitness }

            // 상위 30퍼센트는 생존
            val topSize = (orders.size * config.eliteRatio).coerceAtLeast(1.0).toInt()
            val remainSize = orders.size - topSize
            val topPopulation = sortedPopulation.take(topSize)
            newPopulation.addAll(topPopulation)

            // 하위 70퍼센트 중에서 절반은 교차, 절반은 돌연변이
            val lowPopulation = sortedPopulation.takeLast(remainSize)

            for (j in 0 until lowPopulation.size / 2) {
                if (j >= lowPopulation.lastIndex - j) {
                    newPopulation.add(lowPopulation[j])
                    break
                }

                val u = lowPopulation[j]
                val v = lowPopulation[lowPopulation.lastIndex - j]

                // 교차와 돌연변이 확률 계산
                if (random.nextDouble() < config.crossRatio) {
                    newPopulation.addAll(cross(u, v))
                } else {
                    newPopulation.addAll(mutation(u, v))
                }
            }

            val generationMaxIndividual = newPopulation.maxBy { it.fitness }
            logger.info("[Generation $i] Maximum individual fitness: ${generationMaxIndividual.fitness}")
            initialPopulation = newPopulation
        }
    }
} 
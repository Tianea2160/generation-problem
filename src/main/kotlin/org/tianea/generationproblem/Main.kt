package org.tianea.generationproblem

import java.lang.Math.random
import java.util.*

fun main() {
    val algorithm = GenerationAlgorithm()

    algorithm.calculate()
}

class GenerationAlgorithm(
    private val config: Config = Config(),
) {
    data class Config(
        val maxGenerationCount: Int = 100,
        val eliteRatio: Double = 0.3,
        val crossRatio: Double = 0.5,
        val initSkuKindCount: Int = 1000,
        val initOrderCount: Int = 1000,
    )

    fun cross(i1: Individual, i2: Individual): MutableList<Individual> {
        val mixedOrders = (i1.orders + i2.orders).toMutableList()

        mixedOrders.shuffle()

        return mutableListOf(
            Individual(mixedOrders.take(mixedOrders.size / 2).toMutableList()),
            Individual(mixedOrders.takeLast(mixedOrders.size / 2).toMutableList()),
        )
    }

    fun mutation(i1: Individual, i2: Individual): MutableList<Individual> {
        val u = i1.orders.random()
        val v = i2.orders.random()

        i1.orders.remove(u)
        i2.orders.remove(v)

        i1.orders.add(v)
        i2.orders.add(u)

        return mutableListOf(i1, i2)
    }

    fun calculate() {
        val skuIds = (0..config.initSkuKindCount).map { "sku_$it" }

        // 1 ~ 10 까지 랜덤한 개수의 스큐를 가지는 주문 1000개 생성
        val orders = (0..config.initOrderCount).map {
            Order(
                skus = (0 until (1..5).random())
                    .map {
                        OrderSku(skuId = skuIds.random(), quantity = (1L..10L).random())
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
        println("Maximum individual: ${initialMaxIndividual.fitness}")

        for (i in 0 until config.maxGenerationCount) {
            val newPopulation = mutableListOf<Individual>()

            val sortedPopulation = initialPopulation.sortedBy { it.fitness }

            // 상위 30퍼센트는 생존
            val topSize = (orders.size * config.eliteRatio).coerceAtLeast(1.0).toInt()
            val remainSize = orders.size - topSize
            val topPopulation = sortedPopulation.take(topSize)
            newPopulation.addAll(topPopulation)

            // 하위 70퍼센트 중에서 절반은 교차, 절반은 돌연변이?
            val lowPopulation = sortedPopulation.takeLast(remainSize)

            for (j in 0 until lowPopulation.size / 2) {

                if (j >= lowPopulation.lastIndex - j) {
                    newPopulation.add(lowPopulation[j])
                    break
                }

                val u = lowPopulation[j]
                val v = lowPopulation[lowPopulation.lastIndex - j]

                // 교차와 돌연변이 확률 계산
                if (random() < config.crossRatio) {
                    newPopulation.addAll(cross(u, v))
                } else {
                    newPopulation.addAll(mutation(u, v))
                }
            }

            val generationMaxIndividual = newPopulation.maxBy { it.fitness }
            println("[Generation $i]  Maximum individual: ${generationMaxIndividual.fitness}")
            initialPopulation = newPopulation
        }
    }
}


class Order(
    val orderId: String = uuid(),
    val skus: MutableList<OrderSku>,
)

class OrderSku(
    val skuId: String,
    val quantity: Long,
)


class Individual(
    val orders: MutableList<Order>,
) {
    val fitness: Long = calculateFitness()

    fun calculateFitness(): Long = orders
        .flatMap { it.skus }
        .map { it.skuId }
        .toSet().size.toLong()
}

fun uuid() = UUID.randomUUID().toString()

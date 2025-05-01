package org.tianea.generationproblem.model

class Individual(
    val orders: MutableList<Order>,
) {
    private var _fitness: Long? = null
    val fitness: Long
        get() {
            if (_fitness == null) {
                _fitness = calculateFitness()
            }
            return _fitness!!
        }

    private fun calculateFitness(): Long = orders
        .flatMap { it.skus }
        .map { it.skuId }
        .toSet().size.toLong()

    fun invalidateFitness() {
        _fitness = null
    }
} 
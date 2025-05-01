package org.tianea.generationproblem.model

import java.util.*

class Order(
    val orderId: String = uuid(),
    val skus: MutableList<OrderSku>,
)

class OrderSku(
    val skuId: String,
    val quantity: Long,
)

fun uuid() = UUID.randomUUID().toString() 
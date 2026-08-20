package com.erp.product;

/**
 * Distinguishes how a product enters inventory:
 *
 * FINISHED  — manufactured in-house from raw materials
 * TRADING   — purchased from a supplier and sold directly (no manufacturing)
 */
public enum ProductType {
    FINISHED,
    TRADING
}

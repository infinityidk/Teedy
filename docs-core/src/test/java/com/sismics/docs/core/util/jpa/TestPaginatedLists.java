package com.sismics.docs.core.util.jpa;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for paginated list defaults and bounds.
 */
public class TestPaginatedLists {

    @Test
    public void createWithNullDefaultsTest() {
        PaginatedList<Object> paginatedList = PaginatedLists.create(null, null);

        Assert.assertEquals(10, paginatedList.getLimit());
        Assert.assertEquals(0, paginatedList.getOffset());
    }

    @Test
    public void createWithMaxLimitClampTest() {
        PaginatedList<Object> paginatedList = PaginatedLists.create(101, 7);

        Assert.assertEquals(100, paginatedList.getLimit());
        Assert.assertEquals(7, paginatedList.getOffset());
    }

    @Test
    public void createWithNormalValuesTest() {
        PaginatedList<Object> paginatedList = PaginatedLists.create(25, 3);

        Assert.assertEquals(25, paginatedList.getLimit());
        Assert.assertEquals(3, paginatedList.getOffset());
    }

    @Test
    public void createNoArgDefaultTest() {
        PaginatedList<Object> paginatedList = PaginatedLists.create();

        Assert.assertEquals(10, paginatedList.getLimit());
        Assert.assertEquals(0, paginatedList.getOffset());
    }
}

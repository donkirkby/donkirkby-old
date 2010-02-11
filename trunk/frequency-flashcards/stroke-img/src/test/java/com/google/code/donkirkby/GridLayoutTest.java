package com.google.code.donkirkby;

import org.junit.Assert;
import org.junit.Test;

public class GridLayoutTest {
	@Test
	public void testLayouts() throws Exception {
		int items = 5;
		int expectedRows = 1;
		int expectedColumns = 5;
		testOneLayout(items, expectedRows, expectedColumns);
		testOneLayout(3, 1, 3);
		testOneLayout(6, 1, 6);
		testOneLayout(8, 1, 8);
		testOneLayout(24, 2, 12);
		testOneLayout(7, 1, 7);
		testOneLayout(9, 2, 5);
	}

	private void testOneLayout(int items, int expectedRows, int expectedColumns) {
		GridLayout layout = new GridLayout();
		layout.setItems(items);
		
		Assert.assertEquals(
				"Rows should match for " + items + " items.",
				expectedRows,
				layout.getRows());
		Assert.assertEquals(
				"Columns should match for " + items + " items.",
				expectedColumns,
				layout.getColumns());
	}
}

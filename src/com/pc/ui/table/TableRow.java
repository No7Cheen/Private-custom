/*
 */
package com.pc.ui.table;

/**
 * 表格的一行.
 */
public class TableRow {

	// 行的所有列
	public TableCell[] cells;

	// 行高
	public int height;

	// 该行单元格的背景
	public int backgroundResource;

	// 字体大小
	public int textSize;

	// 字体颜色
	public int textColor;

	/**
	 * 表格的一行构造
	 * @param cells 行的所有列
	 * @param height 行高
	 * @param textSize 字体大小
	 * @param textColor 字体颜色
	 * @param backgroundResource 该行单元格的背景
	 */
	public TableRow(TableCell[] cells, int height, int textSize, int textColor, int backgroundResource) {
		this.cells = cells;
		this.height = height;
		this.textSize = textSize;
		this.textColor = textColor;
		this.backgroundResource = backgroundResource;
	}

	/**
	 * 行中的单元格数
	 * @return the cell size
	 */
	public int getCellSize() {
		return cells.length;
	}

	/**
	 * 根据列索引回去列的值.
	 * @param index 从0开始
	 * @return 单元格对象
	 */
	public TableCell getCellValue(int index) {
		if (index >= cells.length) return null;
		return cells[index];
	}

}

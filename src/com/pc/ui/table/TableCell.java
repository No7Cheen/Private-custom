package com.pc.ui.table;

/**
 * 表格的一个单元格
 */
public class TableCell {

	// 按列类型取值
	public Object value;

	// 列宽
	public int width;

	// 单元格类型
	public EmCellType type;

	/**
	 * 一个单元格.
	 * @param value 单元格的值
	 * @param width 列宽
	 * @param type 单元格类型
	 */
	public TableCell(Object value, int width, EmCellType type) {
		this.value = value;
		this.width = width;
		this.type = type;
	}

}

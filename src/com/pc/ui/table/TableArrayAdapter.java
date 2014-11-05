/*
 */
package com.pc.ui.table;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * TableView Adapter
 */

public class TableArrayAdapter extends BaseAdapter {

	/** The context. */
	private Context context;
	// 缓存View
	/** The table view. */
	private ArrayList<View> tableView;

	/** 标题内容数组. */
	private String[] titles;
	// 表格内容
	/** The contents. */
	private List<String[]> contents;
	// 单元格数
	/** The columns. */
	private int columns;
	// 单元格宽度
	/** The cell width. */
	private int[] cellWidth;

	/** The cell types. */
	private EmCellType[] cellTypes;
	// 表格资源 （索引0标题背景，1内容列表背景。2表格背景）
	/** The table resource. */
	private int[] tableResource;
	// 行高度
	/** The row height. */
	private int[] rowHeight;
	// 行文字大小（索引0标题，1内容列表）
	/** The row text size. */
	private int[] rowTextSize;
	// 行文字颜色（索引0标题，1内容列表）
	/** The row text color. */
	private int[] rowTextColor;

	/** The table. */
	private TableProperty table;

	/**
	 * Table控件适配器.
	 * @param context the context
	 * @param table Table对象
	 */
	public TableArrayAdapter(Context context, TableProperty table) {
		this.context = context;
		tableView = new ArrayList<View>();
		setTable(table);
	}

	/**
	 * 更新Table内容.
	 * @param table the new table
	 */
	public void setTable(TableProperty table) {
		this.table = table;
		this.titles = table.getTitles();
		this.contents = table.getContents();
		this.cellTypes = table.getCellTypes();
		this.cellWidth = table.getCellWidth();
		this.rowHeight = table.getRowHeight();
		this.rowTextSize = table.getRowTextSize();
		this.rowTextColor = table.getRowTextColor();
		this.tableResource = table.getTableResource();
		this.columns = this.cellTypes.length;
		tableView.clear();
	}

	/**
	 * 获取数量.
	 * @return the count
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return contents.size() + 1;
	}

	/**
	 * 获取位置.
	 * @param position the position
	 * @return the item id
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 获取索引位置的View.
	 * @param position the position
	 * @return the item
	 * @see android.widget.Adapter#getItem(int)
	 */
	public TableItemView getItem(int position) {
		return (TableItemView) tableView.get(position);
	}

	/**
	 * 绘制View.
	 * @param position the position
	 * @param convertView the convert view
	 * @param parent the parent
	 * @return the view
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// 标题
			if (position == 0) {
				TableCell[] tableCells = new TableCell[columns];
				for (int j = 0; j < columns; j++) {
					tableCells[j] = new TableCell(titles[j], cellWidth[j], cellTypes[j]);
				}
				convertView = new TableItemView(context, this, position, new TableRow(tableCells, rowHeight[0],
						rowTextSize[0], rowTextColor[0], tableResource[1]), table);
				convertView.setBackgroundResource(tableResource[0]);
			} else {
				// 内容
				TableCell[] tableCells = new TableCell[columns];
				String[] content = contents.get(position - 1);
				int size = contents.size();
				if (size > 0) {
					for (int j = 0; j < columns; j++) {
						tableCells[j] = new TableCell(content[j], cellWidth[j], cellTypes[j]);
					}
					convertView = new TableItemView(context, this, position, new TableRow(tableCells, rowHeight[1],
							rowTextSize[1], rowTextColor[1], tableResource[3]), table);
				} else {
					// 默认显示一行无数据
				}
				convertView.setBackgroundResource(tableResource[2]);
			}
		} else {
			if (position == 0) {
				// 将值重置
				TableItemView rowView = (TableItemView) convertView;
				// 内容
				TableCell[] tableCells = new TableCell[columns];
				for (int j = 0; j < columns; j++) {
					tableCells[j] = new TableCell(titles[j], cellWidth[j], cellTypes[j]);
				}
				rowView.setTableRowView(position, new TableRow(tableCells, rowHeight[0], rowTextSize[0],
						rowTextColor[0], tableResource[1]));
				convertView.setBackgroundResource(tableResource[0]);
			} else {
				// 将值重置
				TableItemView rowView = (TableItemView) convertView;
				// 内容
				TableCell[] tableCells = new TableCell[columns];
				String[] content = contents.get(position - 1);
				int size = contents.size();
				if (size > 0) {
					for (int j = 0; j < columns; j++) {
						tableCells[j] = new TableCell(content[j], cellWidth[j], cellTypes[j]);
					}
					rowView.setTableRowView(position, new TableRow(tableCells, rowHeight[1], rowTextSize[1],
							rowTextColor[1], tableResource[3]));
				} else {
					// 默认显示一行无数据
				}
				convertView.setBackgroundResource(tableResource[2]);
			}
		}
		// 将新的View维护到tableView
		if (tableView.size() > position) {
			tableView.set(position, convertView);
		} else {
			tableView.add(position, convertView);
		}
		return convertView;
	}

	/**
	 * 增加一行.
	 * @param row 行的数据
	 */
	public void addItem(String[] row) {
		contents.add(row);
		this.notifyDataSetChanged();
	}

}

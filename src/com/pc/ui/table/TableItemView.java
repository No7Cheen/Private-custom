/*
 */
package com.pc.ui.table;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pc.utils.log.PcLog;
import com.pc.view.listener.AbOnItemClickListener;

/**
 * 表格一行的视图.
 */
public class TableItemView extends LinearLayout {

	/** The tag. */
	private static String TAG = "TableItemView";

	/** The m context. */
	private Context mContext;
	// 该行的单元格数量
	/** The cell count. */
	private int cellCount;
	// 该行单元格文本数组
	/** The row cell. */
	private View[] rowCell;

	/** View在列表中的位置. */
	private int mPosition;

	/** The m table. */
	private TableProperty mTable = null;

	/** The m adapter. */
	private TableArrayAdapter mAdapter = null;

	/**
	 * 创建一行的View.
	 * @param context Context
	 * @param adapter the adapter
	 * @param position the position
	 * @param tableRow 行数据
	 * @param table the table
	 */
	public TableItemView(Context context, TableArrayAdapter adapter, int position, TableRow tableRow,
			TableProperty table) {
		super(context);
		mPosition = position;
		mContext = context;
		mTable = table;
		mAdapter = adapter;
		// 水平排列
		this.setOrientation(LinearLayout.HORIZONTAL);
		// 初始化行中列的数量
		cellCount = tableRow.getCellSize();
		// 初始化该行单元格文本View数组
		rowCell = new View[cellCount];
		// 将单元格逐个添加到行
		LinearLayout.LayoutParams layoutParams = null;
		for (int i = 0; i < cellCount; i++) {
			final int index = i;
			final TableCell tableCell = tableRow.getCellValue(index);
			// 按照格单元指定的大小设置空间
			layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, tableRow.height);
			if (tableCell.type == EmCellType.type_string) {
				TextView textCell = new TextView(mContext);
				textCell.setMinimumWidth(tableCell.width);
				textCell.setMinimumHeight(tableRow.height);
				if (PcLog.isPrint) PcLog.d(TAG, "行高：" + tableRow.height);
				textCell.setLines(1);
				textCell.setGravity(Gravity.CENTER);
				textCell.setTextColor(tableRow.textColor);
				if (mPosition == 0) {
					if (PcLog.isPrint) PcLog.d(TAG, "标题栏的颜色：" + tableRow.textColor);
					// 粗体
					textCell.setTypeface(Typeface.DEFAULT_BOLD);
					textCell.setBackgroundResource(mTable.getTableResource()[1]);
				} else {
					// 普通字体
					textCell.setTypeface(Typeface.DEFAULT);
					textCell.setBackgroundResource(mTable.getTableResource()[3]);
				}
				// 设置单元格内容
				textCell.setText(String.valueOf(tableCell.value));
				textCell.setTextSize(tableRow.textSize);
				rowCell[i] = textCell;
				addView(textCell, layoutParams);
			}
			// 如果格单元是图像内容
			else if (tableCell.type == EmCellType.type_image) {
				// 按照格单元指定的大小设置空间
				LinearLayout mLinearLayout = new LinearLayout(mContext);
				mLinearLayout.setMinimumWidth(tableCell.width);
				ImageView imgCell = new ImageView(mContext);
				if (mPosition == 0) {
					imgCell.setImageDrawable(null);
					mLinearLayout.setGravity(Gravity.CENTER);
					mLinearLayout.addView(imgCell, layoutParams);
					mLinearLayout.setBackgroundResource(mTable.getTableResource()[1]);
					addView(mLinearLayout, layoutParams);
				} else {
					imgCell.setImageResource((int) Integer.parseInt((String) tableCell.value));
					mLinearLayout.setGravity(Gravity.CENTER);
					mLinearLayout.addView(imgCell, layoutParams);
					mLinearLayout.setBackgroundResource(mTable.getTableResource()[3]);
					addView(mLinearLayout, layoutParams);
					imgCell.setOnTouchListener(new View.OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								AbOnItemClickListener mAbOnItemClickListener = mTable.getItemCellTouchListener();
								if (mAbOnItemClickListener != null) {
									mAbOnItemClickListener.onClick(mPosition);
								}
							}
							return false;
						}

					});
				}
				rowCell[i] = imgCell;

				// 如果格单元是复选框
			} else if (tableCell.type == EmCellType.type_checkbox) {

				LinearLayout mLinearLayout = new LinearLayout(mContext);
				mLinearLayout.setMinimumWidth(tableCell.width);
				final CheckBox mCheckBox = new CheckBox(context);
				mCheckBox.setGravity(Gravity.CENTER);
				// 必须先清空事件
				mCheckBox.setOnCheckedChangeListener(null);
				mCheckBox.setFocusable(false);
				int isCheck = Integer.parseInt(String.valueOf(tableCell.value));
				if (isCheck == 1) {
					mCheckBox.setChecked(true);
				} else {
					mCheckBox.setChecked(false);
				}
				if (mPosition == 0) {
					mLinearLayout.setGravity(Gravity.CENTER);
					mLinearLayout.addView(mCheckBox, layoutParams);
					mLinearLayout.setBackgroundResource(mTable.getTableResource()[1]);
					addView(mLinearLayout, layoutParams);
				} else {
					mLinearLayout.setGravity(Gravity.CENTER);
					mLinearLayout.addView(mCheckBox, layoutParams);
					mLinearLayout.setBackgroundResource(mTable.getTableResource()[3]);
					addView(mLinearLayout, layoutParams);
				}

				mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

						if (mPosition == 0) {
							// 全选
							if (isChecked) {
								mTable.getTitles()[index] = "1";
								for (int i = 0; i < mTable.getContents().size(); i++) {
									mTable.getContents().get(i)[index] = "1";
								}
							} else {
								mTable.getTitles()[index] = "0";
								for (int i = 0; i < mTable.getContents().size(); i++) {
									mTable.getContents().get(i)[index] = "0";
								}
							}
						} else {
							// 单条
							if (isChecked) {
								mTable.getContents().get(mPosition - 1)[index] = "1";
							} else {
								mTable.getContents().get(mPosition - 1)[index] = "0";
							}
						}
						mAdapter.notifyDataSetChanged();
						AbOnItemClickListener itemCellCheckListener = mTable.getItemCellCheckListener();
						if (itemCellCheckListener != null) {
							itemCellCheckListener.onClick(mPosition);
						}

					}
				});
				rowCell[index] = mCheckBox;
			}
		}
	}

	/**
	 * 更新表格一行内容.
	 * @param position the position
	 * @param tableRow 行的数据
	 */
	public void setTableRowView(int position, TableRow tableRow) {
		mPosition = position;
		for (int i = 0; i < cellCount; i++) {
			final int index = i;
			final TableCell tableCell = tableRow.getCellValue(index);
			if (tableCell.type == EmCellType.type_string) {
				TextView textCell = (TextView) rowCell[index];
				textCell.setMinimumWidth(tableCell.width);
				textCell.setMinimumHeight(tableRow.height);
				textCell.setLines(1);
				textCell.setGravity(Gravity.CENTER);
				textCell.setText(String.valueOf(tableCell.value));
				textCell.setTextColor(tableRow.textColor);
				textCell.setTextSize(tableRow.textSize);
				if (mPosition == 0) {
					// 粗体
					textCell.setTypeface(Typeface.DEFAULT_BOLD);
					textCell.setBackgroundResource(mTable.getTableResource()[1]);
				} else {
					// 普通字体
					textCell.setTypeface(Typeface.DEFAULT);
					textCell.setBackgroundResource(mTable.getTableResource()[3]);
				}
				// 如果格单元是图像内容
			} else if (tableCell.type == EmCellType.type_image) {
				if (mPosition == 0) {
					ImageView imgCell = (ImageView) rowCell[index];
					imgCell.setImageDrawable(null);
					((LinearLayout) imgCell.getParent()).setBackgroundResource(mTable.getTableResource()[1]);
				} else {
					ImageView imgCell = (ImageView) rowCell[index];
					((LinearLayout) imgCell.getParent()).setBackgroundResource(mTable.getTableResource()[3]);
					imgCell.setImageResource((int) Integer.parseInt((String) tableCell.value));
					imgCell.setOnTouchListener(new View.OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								AbOnItemClickListener mAbOnItemClickListener = mTable.getItemCellTouchListener();
								if (mAbOnItemClickListener != null) {
									mAbOnItemClickListener.onClick(mPosition);
								}
							}
							return false;
						}

					});
				}

			} else if (tableCell.type == EmCellType.type_checkbox) {
				final CheckBox mCheckBox = (CheckBox) rowCell[index];
				// 必须先清空事件
				mCheckBox.setOnCheckedChangeListener(null);
				int isCheck = Integer.parseInt(String.valueOf(tableCell.value));

				if (isCheck == 1) {
					mCheckBox.setChecked(true);
				} else {
					mCheckBox.setChecked(false);
				}
				if (mPosition == 0) {
					((LinearLayout) mCheckBox.getParent()).setBackgroundResource(mTable.getTableResource()[1]);
				} else {
					((LinearLayout) mCheckBox.getParent()).setBackgroundResource(mTable.getTableResource()[3]);
				}
				mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (mPosition == 0) {
							// 全选
							if (isChecked) {
								mTable.getTitles()[index] = "1";
								for (int i = 0; i < mTable.getContents().size(); i++) {
									mTable.getContents().get(i)[index] = "1";
								}
							} else {
								mTable.getTitles()[index] = "0";
								for (int i = 0; i < mTable.getContents().size(); i++) {
									mTable.getContents().get(i)[index] = "0";
								}
							}
						} else {
							// 单条
							if (isChecked) {
								mTable.getContents().get(mPosition - 1)[index] = "1";
							} else {
								mTable.getContents().get(mPosition - 1)[index] = "0";
							}
						}
						mAdapter.notifyDataSetChanged();
						AbOnItemClickListener itemCellCheckListener = mTable.getItemCellCheckListener();
						if (itemCellCheckListener != null) {
							itemCellCheckListener.onClick(mPosition);
						}
					}
				});
				mCheckBox.setFocusable(false);
			}
		}
	}
}

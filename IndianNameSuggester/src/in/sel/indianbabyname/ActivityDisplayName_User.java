package in.sel.indianbabyname;

import in.sel.adapter.NameAdapter;
import in.sel.adapter.NameCursorAdapter;
import in.sel.model.M_Name;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActionBar.Tab;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityDisplayName_User extends Activity {
	String TAG = "ActivityDisplayName";

	ListView lsName;
	String alphabet;

	/* adapter */
	NameCursorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_name_list);
		alphabet = getIntent().getStringExtra(ActivityMain.ALPHA);

		/** DBHelper Object */
		final DBHelper dbHelper = new DBHelper(this);

		/** just to check total entry inside table */
		//int count = dbHelper.getTableRowCount(TableContract.Name.TABLE_NAME, null);

		/** Update list before Any transaction*/
		updateGenderCast();
		
		/** This is will select only those which are not marked */
		String where = TableContract.Name.NAME_EN + " like '" + alphabet + "%' AND " + TableContract.Name.GENDER_CAST
				+ " = ''" + " ORDER BY " + TableContract.Name.NAME_FRE + " DESC";

		Cursor c = dbHelper.getTableValue(TableContract.Name.TABLE_NAME, new String[] { TableContract.Name.AUTO_ID,
				TableContract.Name.NAME_EN, TableContract.Name.NAME_MA, TableContract.Name.NAME_FRE,
				TableContract.Name.GENDER_CAST }, where);

		/** Parse */
		displayListWithCustomCursor(c);

		/* */
		TextView tvTotal = (TextView) findViewById(R.id.tvTotal);
		tvTotal.setText("Total unique word in this group is " + c.getCount());

		/* Sorting on Name based on English Name */
		TextView tvEnName = (TextView) findViewById(R.id.tvEnglish);
		tvEnName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				/** First Update List */
				updateGenderCast();
				
				/** This is will select only those which are not marked */
				String where = TableContract.Name.NAME_EN + " like '" + alphabet + "%' AND "
						+ TableContract.Name.GENDER_CAST + " = ''" + " ORDER BY " + TableContract.Name.NAME_EN + " ASC";

				Cursor c = dbHelper.getTableValue(TableContract.Name.TABLE_NAME, new String[] {
						TableContract.Name.AUTO_ID, TableContract.Name.NAME_EN, TableContract.Name.NAME_MA,
						TableContract.Name.NAME_FRE, TableContract.Name.GENDER_CAST }, where);

				/** At the time of publishing keep this one as code */
				// adapter.swapCursor(c);
				// adapter.notifyDataSetChanged();

				/** At the time of Developemnt keep this in Code */
				
				lsName.setAdapter(new NameCursorAdapter(ActivityDisplayName_User.this, c));
				lsName.invalidate();
			}
		});

		/* Sorting on Name based on Marathi Name */
		TextView tvHinName = (TextView) findViewById(R.id.tvHindi);
		tvHinName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				/** First Update List */
				updateGenderCast();
				
				/** This is will select only those which are not marked */
				String where = TableContract.Name.NAME_EN + " like '" + alphabet + "%' AND "
						+ TableContract.Name.GENDER_CAST + " = ''" + " ORDER BY " + TableContract.Name.NAME_MA + " ASC";

				Cursor c = dbHelper.getTableValue(TableContract.Name.TABLE_NAME, new String[] {
						TableContract.Name.AUTO_ID, TableContract.Name.NAME_EN, TableContract.Name.NAME_MA,
						TableContract.Name.NAME_FRE, TableContract.Name.GENDER_CAST }, where);

				// adapter.swapCursor(c);
				// adapter.notifyDataSetChanged();

				lsName.setAdapter(new NameCursorAdapter(ActivityDisplayName_User.this, c));
				lsName.invalidate();
			}
		});

		/* Sorting on Name based on Frequency */
		TextView tvFrequ = (TextView) findViewById(R.id.tvFrequency);
		tvFrequ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/** First Update List */
				updateGenderCast();
				
				/** This is will select only those which are not marked */
				String where = TableContract.Name.NAME_EN + " like '" + alphabet + "%' AND "
						+ TableContract.Name.GENDER_CAST + " = ''" + " ORDER BY " + TableContract.Name.NAME_FRE
						+ " DESC";

				Cursor c = dbHelper.getTableValue(TableContract.Name.TABLE_NAME, new String[] {
						TableContract.Name.AUTO_ID, TableContract.Name.NAME_EN, TableContract.Name.NAME_MA,
						TableContract.Name.NAME_FRE, TableContract.Name.GENDER_CAST }, where);

				// adapter.swapCursor(c);
				// adapter.notifyDataSetChanged();

				/** */
				lsName.setAdapter(new NameCursorAdapter(ActivityDisplayName_User.this, c));
				lsName.invalidate();
			}
		});
	}

	public void displayList(List<M_Name> name) {
		lsName = (ListView) findViewById(R.id.lv_alphabet);
		final NameAdapter na = new NameAdapter(this, name);
		lsName.setAdapter(na);
	}

	/** For Testing of Cursor Adapter */
	public void displayListWithCustomCursor(Cursor c) {
		lsName = (ListView) findViewById(R.id.lv_alphabet);

		adapter = new NameCursorAdapter(this, c);
		lsName.setAdapter(adapter);
		
		//listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

//	@Override
//	protected void onPause() {
//		super.onPause();
//
//		int index = lsName.getFirstVisiblePosition();
//
//		View v = lsName.getChildAt(index);
//		int top = (v == null) ? 0 : (v.getTop() - lsName.getPaddingTop());
//
//		// restore index and position
//		lsName.setSelectionFromTop(index, top);
//		ContentValues cv = new ContentValues();
//		cv.put(TableContract.SavedStatus.LETTER, alphabet);
//		cv.put(TableContract.SavedStatus.INDEX, index);
//		cv.put(TableContract.SavedStatus.POSITION, top);
//
//		DBHelper db = new DBHelper(this);
//		long i = db.insertInTable(TableContract.SavedStatus.TABLE_NAME, TableContract.SavedStatus.LETTER, cv);
//		db.close();
//		if (i > 0)
//			Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
//
//		Log.i(TAG, "onPause index-->" + index + " top-->" + top + " lsName.getPaddingTop()-->" + lsName.getPaddingTop());
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//
//		DBHelper db = new DBHelper(this);
//
//		String where = TableContract.SavedStatus.LETTER + " = '" + alphabet + "'";
//		Cursor c = db.getTableValue(TableContract.SavedStatus.TABLE_NAME, new String[] {
//				TableContract.SavedStatus.INDEX, TableContract.SavedStatus.POSITION }, where);
//
//		if (c != null && c.getCount() > 0) {
//			c.moveToFirst();
//			int index = c.getInt(0);
//			int pos = c.getInt(1);
//			Log.i(TAG, "onResume index-->" + index + " pos-->" + pos);
//
//			lsName.setSelectionFromTop(index, pos);
//		}
//
//		/** */
//		if (c != null)
//			c.close();
//
//		db.close();
//	}

	/** Update Table with gender_cast Value marked By User*/
	public void updateGenderCast() {
		if (NameCursorAdapter.lsNameMarked.size() > 0) {
			DBHelper dbtemp = new DBHelper(this);

			for (Map.Entry<Integer, String> name : NameCursorAdapter.lsNameMarked.entrySet()) {
				ContentValues cv = new ContentValues();
				cv.put(TableContract.Name.GENDER_CAST, name.getValue());

				/** Where clause */
				String where = TableContract.Name.AUTO_ID + " = " + name.getKey();
				int i = dbtemp.updateTable(TableContract.Name.TABLE_NAME, cv, where);
				
				if (i > 0) {
					Log.i("Updated", i + "");
					//Toast.makeText(this, "Text Updated-->"+name.getKey(), Toast.LENGTH_SHORT).show();
				}
				NameCursorAdapter.lsNameMarked.remove(name);
			}
		}

	}

}

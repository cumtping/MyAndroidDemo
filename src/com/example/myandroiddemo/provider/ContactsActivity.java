package com.example.myandroiddemo.provider;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.example.myandroiddemo.R;

public class ContactsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_layout);
		
		Cursor c = queryFavoriteContacts();
		if(c != null){
			if(c.getCount() == 0){
				Toast.makeText(this, "favorite is empty", Toast.LENGTH_LONG).show();
			}else{
				StringBuffer sb = new StringBuffer();
				for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
					String str = c.getString(0);
					sb.append(str).append("\n\r");
				}
				Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(this, "favorite cursor is null", Toast.LENGTH_LONG).show();
		}
		
	}

	/**
	 * 
	 * @return
	 */
	public Cursor queryFavoriteContacts(){
		ContentResolver cr = getContentResolver();
		String []projects = new String[]{ContactsContract.Data.DISPLAY_NAME};
		String selections = ContactsContract.Data.STARRED + "=?";
		String[] selectionArgs = new String[]{"1"};
		Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, projects, selections, selectionArgs, null); 
		return c;
	}
}

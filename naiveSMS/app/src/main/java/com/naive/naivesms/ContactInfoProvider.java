package com.naive.naivesms;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class ContactInfoProvider {

	/**
	 * ��ȡϵͳ����ϵ����Ϣ
	 * 
	 * @param context
	 *            ������
	 * @return
	 */
	public static List<ContactInfo> getContactInfos(Context context) {
		// �õ����ݽ�����
		ContentResolver resolver = context.getContentResolver();
		// 1.��ѯraw_contacts �� ��ȡ���е���ϵ�˵�id
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri dataUri = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
				null, null, null);
		List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
		while (cursor.moveToNext()) {
			String id = cursor.getString(0);
			// 2.������ϵ�˵�id��ѯ ��ϵ�˵����� ��ѯdata��
			if (id != null) {
				ContactInfo contactInfo = new ContactInfo();
				Cursor dataCursor = resolver.query(dataUri, null,
						"raw_contact_id=?", new String[] { id }, null);
				while (dataCursor.moveToNext()) {
					String data1 = dataCursor.getString(dataCursor
							.getColumnIndex("data1"));
					String mimetype = dataCursor.getString(dataCursor
							.getColumnIndex("mimetype"));
					if ("vnd.android.cursor.item/email_v2".equals(mimetype)) {
						contactInfo.setEmail(data1);
					} else if ("vnd.android.cursor.item/name".equals(mimetype)) {
						contactInfo.setName(data1);
					} else if ("vnd.android.cursor.item/phone_v2"
							.equals(mimetype)) {
						contactInfo.setPhone(data1);
					}

				}
				dataCursor.close();
				contactInfos.add(contactInfo);
				contactInfo = null;
			}
		}
		cursor.close();
		return contactInfos;
	}
}

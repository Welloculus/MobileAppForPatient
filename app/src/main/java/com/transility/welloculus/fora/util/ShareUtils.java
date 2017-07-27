package com.transility.welloculus.fora.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import com.transility.welloculus.R;

public class ShareUtils {
	public static boolean sendTextToMail(Context context, String subject, String content) {
		if (!InternetUtils.isOnLine(context)) {
			new AlertDialog.Builder(context)
				.setTitle(R.string.common_confirm)
            	.setMessage(R.string.common_connect_internet_first)
            	.show();
            
			return false;
		}
		
		if (subject != null && context != null && content != null && content.length() > 0) {
		 	Intent intent = new Intent(android.content.Intent.ACTION_SEND);
         	intent.setType("text/html");
         	intent.putExtra(Intent.EXTRA_SUBJECT, subject);
         	intent.putExtra(Intent.EXTRA_TEXT, content);
         	context.startActivity(Intent.createChooser(intent, "Send email..."));
			 	
			return true;
		}
		
		return false;
	}
}

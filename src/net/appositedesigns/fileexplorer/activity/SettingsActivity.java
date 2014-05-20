package net.appositedesigns.fileexplorer.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import net.appositedesigns.fileexplorer.R;
import net.appositedesigns.fileexplorer.util.FileActionsHelper;

import java.util.List;

//import com.actionbarsherlock.app.SherlockPreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

	private static final boolean ALWAYS_SIMPLE_PREFS = false;

	

	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setupSimplePreferencesScreen();
	}
	
	/**
	 * Shows the simplified settings UI if the device configuration if the
	 * device configuration dictates that a simplified, single-pane UI should be
	 * shown.
	 */
	private void setupSimplePreferencesScreen() {
		if (!isSimplePreferences(this)) {
			return;
		}

		// In the simplified UI, fragments are not used at all and we instead
		// use the older PreferenceActivity APIs.

		// Add 'general' preferences.
		//addPreferencesFromResource(R.xml.view_prefs);
		addPreferencesFromResource(R.xml.view_prefs);
		
		// Add 'ViewPreferences"' preferences, and a corresponding header.
		PreferenceCategory fakeHeader = new PreferenceCategory(this);
		//fakeHeader.setTitle(R.string.pref_cat_display);
		//getPreferenceScreen().addPreference(fakeHeader);
		//addPreferencesFromResource(R.xml.view_prefs);

		// Add 'NavigationPreferences' preferences, and a corresponding header.
		fakeHeader = new PreferenceCategory(this);
		fakeHeader.setTitle(R.string.pref_cat_nav);
		getPreferenceScreen().addPreference(fakeHeader);
		addPreferencesFromResource(R.xml.nav_prefs);
		
		// Add 'GeekyPreferences' preferences, and a corresponding header.
		fakeHeader = new PreferenceCategory(this);
		fakeHeader.setTitle(R.string.pref_cat_geeky);
		getPreferenceScreen().addPreference(fakeHeader);
		addPreferencesFromResource(R.xml.geeky_prefs);
		
		// Add 'AboutPreferences' preferences, and a corresponding header.
		fakeHeader = new PreferenceCategory(this);
		fakeHeader.setTitle(R.string.pref_cat_misc);
		getPreferenceScreen().addPreference(fakeHeader);
		addPreferencesFromResource(R.xml.about_prefs);
		
		
		Preference rescan = findPreference("pref_opt_rescan");
        rescan.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                FileActionsHelper.rescanMedia(SettingsActivity.this);
                return true;
            }
        });
        Preference share = (Preference)findPreference("pref_opt_share");
		 share.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				
				final Intent intent = new Intent(Intent.ACTION_SEND);
		    	intent.setType("text/plain");
		    	String text = getString(R.string.share_text);
		    	intent.putExtra(Intent.EXTRA_TEXT, text);
		    	intent.putExtra(Intent.EXTRA_SUBJECT, "FileExplorer");

		    	startActivity(Intent.createChooser(intent,
		    			getString(R.string.share_via)));
				return true;
			}
		});
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean onIsMultiPane() {
		return isXLargeTablet(this) && !isSimplePreferences(this);
	}

	/**
	 * Helper method to determine if the device has an extra-large screen. For
	 * example, 10" tablets are extra-large.
	 */
	private static boolean isXLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	/**
	 * Determines whether the simplified settings UI should be shown. This is
	 * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
	 * doesn't have newer APIs like {@link PreferenceFragment}, or the device
	 * doesn't have an extra-large screen. In these cases, a single-pane
	 * "simplified" settings UI should be shown.
	 */
	private static boolean isSimplePreferences(Context context) {
		return ALWAYS_SIMPLE_PREFS
				|| Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
				|| !isXLargeTablet(context);
	}
	
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//
//		if(item.getItemId()== android.R.id.home)
//		{
//			onBackPressed();
//			return true;
//		}
//		else
//		{
//			return false;
//		}
//	}
	
	/** {@inheritDoc} */
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		if (!isSimplePreferences(this)) {
			loadHeadersFromResource(R.xml.prefs, target);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class NavigationPreferences extends PreferenceFragment  {
		 @Override
		 public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			 addPreferencesFromResource(R.xml.nav_prefs);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class GeekyPreferences extends PreferenceFragment {
		 @Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			 addPreferencesFromResource(R.xml.geeky_prefs);
             Preference rescan = findPreference("pref_opt_rescan");
             rescan.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                 @Override
                 public boolean onPreferenceClick(Preference preference) {
                     FileActionsHelper.rescanMedia(GeekyPreferences.this.getActivity());
                     return true;
                 }
             });
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class ViewPreferences extends PreferenceFragment  {
		
		 @Override
		 public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			 addPreferencesFromResource(R.xml.view_prefs);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class AboutPreferences extends PreferenceFragment  {
		
		 @Override
		 public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			 addPreferencesFromResource(R.xml.about_prefs);
			 
			 Preference share = (Preference)findPreference("pref_opt_share");
			 share.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				
				@Override
				public boolean onPreferenceClick(Preference preference) {
					
					final Intent intent = new Intent(Intent.ACTION_SEND);
			    	intent.setType("text/plain");
			    	String text = getString(R.string.share_text);
			    	intent.putExtra(Intent.EXTRA_TEXT, text);
			    	intent.putExtra(Intent.EXTRA_SUBJECT, "FileExplorer");

			    	startActivity(Intent.createChooser(intent,
			    			getString(R.string.share_via)));
					return true;
				}
			});
		}
	}
	
}
package ca.ualberta.views;

import ca.ualberta.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Activity that responsible for showing the Help information
 *  to User
 * */
public class HelpActivity extends Activity{
	
	private TextView help_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		help_text = (TextView) findViewById(R.id.help);
		help_text.setText("\t\t\t\t\t\t\t\t\tHelp Info"+
				"\n\n\nIn TagGallery:"+
				"\n\tShort click go inside the tag group,"+
				"\n\tLong Click to delete whole tag group." + 
				"\n\nTaking An New Photo:"+
				"\n\tNeed to write a tag for the photo first."+
				"\n\nCompare Function:"+
				"\n\tVisible if at least two photos in that tag."+
				"\n\nArchive Function:"+
				"\n\tVisible if there's an SD Card in the cellphone."+
				"\n\nSearch Inside the Storage:"+
				"\n\tGo to main page first and perform the search."+
				"\n\nSearch Google Result:"+
				"\n\tAny Where but not inside Main Page."+
				"\n\n\n\n\n\n\nProgrammed By:\n\tC,D,M,S");
		
	}
}

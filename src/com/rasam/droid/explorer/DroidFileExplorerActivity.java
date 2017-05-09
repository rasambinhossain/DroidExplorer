package com.rasam.droid.explorer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;




import com.rasam.droid.explorer.IconifiedText;
import com.rasam.droid.explorer.IconifiedTextListAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.StaticLayout;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
//import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class DroidFileExplorerActivity extends ListActivity {
    /** Called when the activity is first created. */
	TextView textViewDirectory;
	String test1;
	public String var; 
	public String selectedFileString;
	final int MENU_DETAILS = 1;
	final int DIALOG_DETAILS = 2;
	final int MENU_DELETE = 3;
	final int DIALOG_DELETE = 4;
	int newid;
	Comparator<? super File> comparator;
	public File longClickedFile = null;
	
   
	
	private enum DISPLAYMODE{ ABSOLUTE, RELATIVE; }
    
    private final DISPLAYMODE displayMode = DISPLAYMODE.RELATIVE;
    private List<IconifiedText> directoryEntries = new ArrayList<IconifiedText>();
    private File currentDirectory = new File("/");
    public String myDirectory = null;

    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	registerForContextMenu(getListView());
    	//setContentView(R.layout.main);
    	//textViewDirectory = (TextView)findViewById(R.id.textViewDirectory);
    	
		browseToRoot();
		this.setSelection(0);
    } 
    
    /*Browsing to the directory of the system */
	private void browseToRoot() {
		String root = Environment.getExternalStorageDirectory().getPath();
		browseTo(new File(root));
		
	}
	
	/*Browsing to upOneLevel of the system */
	public void upOneLevel(){
		if(this.currentDirectory.getParent() !=null)
			this.browseTo(this.currentDirectory.getParentFile());
	}
	 @Override  
	    public void onBackPressed()   
	    {  
		 if(this.currentDirectory.getParent() == null){
			 
	        //do whatever you want the 'Back' button to do   
			 System.exit(0);
		 }
			upOneLevel();
	        return;  
	    } 

	public void browseTo( final File aDirectory) {	
		
		myDirectory = aDirectory.getAbsolutePath();
	//	if(this.displayMode == DISPLAYMODE.RELATIVE){
	//		this.setTitle(aDirectory.getAbsolutePath() + " >> " + 
	//	getString(R.string.app_name));
		//textViewDirectory.setText(aDirectory.getAbsolutePath());
	//	test1=aDirectory.getAbsolutePath().toString();
		//String newText = directoryText.getText().toString()	;
		//directoryText.setText("");
			
		
			
		if(aDirectory.isDirectory()){
			this.currentDirectory = aDirectory;
			fill(aDirectory.listFiles());
	
		}
		else{
			
			OnClickListener okButtonListener = new OnClickListener() {
				
				public void onClick(DialogInterface arg0,int arg1) {
					// TODO Auto-generated method stub
					
					// Starting an intent to view the file that was clicked
					try {
                        // Lets start an intent to View the file, that was clicked...
                        //Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW,
                        //                Uri.parse("file://"
                        //                                + aDirectory.getAbsolutePath()));
						
						try
						{
						Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW);
					 	// var = aDirectory.getName();
						File file = new File(aDirectory.getAbsolutePath());
						String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
						
						String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
						myIntent.setDataAndType(Uri.fromFile(file),mimetype);
						startActivity(myIntent);
						}
						catch (Exception e)
						{
						e.getMessage();
						}
					} catch (Exception e) {
                        e.printStackTrace();
					}				
				}		
			};

			OnClickListener cancelButtonListener = new OnClickListener(){
				public void onClick(DialogInterface arg0, int arg1){
					//do nothing
				}
			
			};
			
			 AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			       
			         alertDialog.setTitle("Do you want to open that file?");
			         alertDialog.setMessage(aDirectory.getName());
                     alertDialog.setButton2("OK", okButtonListener);
                     alertDialog.setButton("Cancel", cancelButtonListener);
                     alertDialog.show();
		}
	}

	
	public void fill(File[] files) {
		// TODO Auto-generated method stub
		this.directoryEntries.clear();
        
		if (files==null){
            files = new File[0];
         }

        try {
            Thread.sleep(10);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

		//we have to add "." == "current directory"
	//	this.directoryEntries.add(new IconifiedText(
	//		getString(R.string.current_dir),
		//		getResources().getDrawable(R.drawable.folder)));

		//we have to add ".." == "up_one_level"
	//	if(this.currentDirectory.getParent() !=null){
	//		 this.directoryEntries.add(new IconifiedText(
	//				 getString(R.string.up_one_level)
	//				 ,getResources().getDrawable(R.drawable.uponelevel)));   
	//	}
		
		Drawable currentIcon = null;
		for(File currentFile : files){
			if(currentFile.isDirectory()){
				currentIcon = getResources().getDrawable(R.drawable.folder);
			}
			else{
				String fileName = currentFile.getName();
				 if(checkEndsWithInStringArray(fileName, getResources().
							getStringArray(R.array.fileEndingImage))){
						currentIcon = getResources().getDrawable(R.drawable.image);
				 }else if(checkEndsWithInStringArray(fileName, getResources().
						getStringArray(R.array.fileEndingWebText))){
					currentIcon = getResources().getDrawable(R.drawable.webtext);
				}else if(checkEndsWithInStringArray(fileName, getResources().
						getStringArray(R.array.fileEndingPackage))){
						currentIcon = getResources().getDrawable(R.drawable.packed);
				}else if(checkEndsWithInStringArray(fileName, getResources().
						getStringArray(R.array.fileEndingAudio))){
						currentIcon = getResources().getDrawable(R.drawable.audio);
				}else if(checkEndsWithInStringArray(fileName, getResources().
						getStringArray(R.array.fileEndingVideo))){
						currentIcon = getResources().getDrawable(R.drawable.video);
				}else{
						currentIcon = getResources().getDrawable(R.drawable.text);
				}				
				
			}
		
		
		switch(this.displayMode){
		case ABSOLUTE:
			this.directoryEntries.add(new IconifiedText(currentFile
					.getPath(), currentIcon));
			
			break;
		case RELATIVE: 
			/* On relative Mode, we have to cut the
			 * current-path at the beginning */
			int currentPathStringLenght = this.currentDirectory.
											getAbsolutePath().length();
			
			this.directoryEntries.add(new IconifiedText(
					currentFile.getAbsolutePath().
					substring(currentPathStringLenght),
					currentIcon));
			
			break;
		}
	}
		/*ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this,
                 R.layout.main);

         this.setListAdapter(directoryList); */ 
        Collections.sort(this.directoryEntries);
        
       
        	    
        	 
 		
 		IconifiedTextListAdapter itla = new IconifiedTextListAdapter(this);
 		itla.setListItems(this.directoryEntries);
 	//	itla.test(test1);
 		this.setListAdapter(itla);
		
	}

	
	protected void onListItemClick(ListView l, View v, int position, long id){
		//int selectionRowID = (int)id;
		super.onListItemClick(l, v, position, id);
		int selectionRowID = (int) id;
		String selectedFileString = this.directoryEntries.get(selectionRowID).getText();
		if (selectedFileString.equals(getString(R.string.current_dir))) {
			// Refresh
			this.browseTo(this.currentDirectory);
		} else if(selectedFileString.equals(getString(R.string.up_one_level))){
			this.upOneLevel();
		} else {
			File clickedFile = null;
			switch(this.displayMode){
				case RELATIVE:
					clickedFile = new File(this.currentDirectory.getAbsolutePath() 
												+ this.directoryEntries.get(selectionRowID).getText());
					break;
				case ABSOLUTE:
					clickedFile = new File(this.directoryEntries.get(selectionRowID).getText());
					break;
			}
			if(clickedFile != null)
				this.browseTo(clickedFile);
		}

	}
	protected void onListItemLongClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		
	}
	
	
  
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		
		menu.add(Menu.NONE, MENU_DETAILS, Menu.NONE, "Details"  );
		menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete"  );
		AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        newid = (int) getListAdapter().getItemId(info.position);
        selectedFileString = this.directoryEntries.get(newid).getText();

        longClickedFile = new File(this.currentDirectory.getAbsolutePath() 
				+ this.directoryEntries.get(newid).getText());
        
        
        
	}
	public boolean onContextItemSelected(MenuItem item){
	//	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	 //   float l = (getListAdapter().getItemId(info.position));
	//	float str = l;
		
		switch(item.getItemId()){
		case MENU_DETAILS:
			showDialog(DIALOG_DETAILS); 
			break;
		
		case MENU_DELETE:
			showDialog(DIALOG_DELETE);
			break;
		}
		 return(super.onOptionsItemSelected(item));
	}
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DIALOG_DETAILS:
				 AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				       
				         alertDialog.setTitle("Details");
				         alertDialog.setMessage("Directory  :  " +longClickedFile.getParentFile()+
				        		              "\nName         :  " +longClickedFile.getName()+
				        		              "\nSize           :  " +longClickedFile.length()+" bytes"
				        		              );
				         //alertDialog.setMessage("Directory :  "+ myDirectory);
	                     alertDialog.setButton("Close",cancelButtonListener());
	                     alertDialog.show(); 	
	                     break;
	         		
		case DIALOG_DELETE:
			new AlertDialog.Builder(this).setMessage("Do u really want to delete this item?")
	            	.setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(
						android.R.string.ok, new OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								 try {
									 if(currentDirectory.isDirectory()){
									longClickedFile.getCanonicalFile().delete();
									 fill(currentDirectory.listFiles());
									 }
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
							
						}).setNegativeButton(android.R.string.cancel, new OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// Cancel should not do anything.
							}
							
						}).create().show();
			
			break;
			

			 
		}
		
		
		//browseTo(currentDirectory);
		return null;
	}
	
	

	private Message cancelButtonListener() {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean checkEndsWithInStringArray(String checkItsEnd,
			 String[] fileEndings) {
		// TODO Auto-generated method stub
		for(String aEnd : fileEndings){
			if(checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}
	
	
}  

	

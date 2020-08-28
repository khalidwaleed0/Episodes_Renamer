package Episode_Renamer;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Renamer {
	static PrintWriter writer;
	static File folder = new File(System.getProperty("user.dir"));
	static ArrayList<File> files = new ArrayList<File>(Arrays.asList(folder.listFiles()));
	static List<String> extensions = Arrays.asList(new String[] {"mp4","m4a","m4v","f4v","f4a","mov","3gp","3gp2","3gpp","3gpp2","mpg",
			"mpeg","rmvb","wmv","wma","mkv","webm","flv","avi","amv","srt","sub"});
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		filter();
		if(files.isEmpty())
			{
				JOptionPane.showMessageDialog(null, "Couldn't find videos in this folder", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		String buttons[] = {"Rename", "Remove URL only", "Restore"};
		int response = JOptionPane.showOptionDialog(null, "What can I help you with ?", "Episode_ReNamerV1.1", JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[2]);
		if(response == JOptionPane.YES_OPTION)	
			{
				writer = new PrintWriter("log(Do not delete).txt", "UTF-8");
				writer.println("old Name -----------------------------------------> new Name");
				writer.println("If episodes were not renamed correctly leave this and open the program again to restore old names");
				rename();
				writer.close();
			}
		else if(response == JOptionPane.NO_OPTION) 
			{
				writer = new PrintWriter("log(Do not delete).txt", "UTF-8");
				writer.println("old Name -----------------------------------------> new Name");
				writer.println("If episodes were not renamed correctly leave this and open the program again to restore old names");
				removeURL();
				writer.close();
			}
		else if(response == JOptionPane.CANCEL_OPTION) restore();
		thanks();
	}
	
	public static void filter()
	{
		String extension;
		for(int i=0 ; i<files.size() ; i++)
		{
			extension = files.get(i).getName().substring(files.get(i).getName().length()-3 , files.get(i).getName().length());
			if(!extensions.contains(extension))
				{
					files.remove(i);
					i--;
				}
		}
	}
	
	public static void rename() throws FileNotFoundException, UnsupportedEncodingException
																		    //detects and renames episodes if written as the following patterns
																								
	{
		Pattern pat1 = Pattern.compile("(e|E)(p|P)?(\\d\\d\\d?)");			//detects episodes epxxx , EPxxx , Exxx
		Pattern pat2 = Pattern.compile("(-)(\\s)?(\\d\\d\\d?)");			//detects episodes -xxx , - xxx
		Matcher mat1;
		File extension,renamer;
		boolean finished = false;
		int counter = 0;
		while(!finished)
		{
			for(int i=0 ; i<files.size() ; i++)
			{
				mat1 = pat1.matcher(files.get(i).getName().replaceAll("\\[[^]]+\\]", ""));
				if(mat1.find())
				{
					 extension = new File(files.get(i).getName().substring(files.get(i).getName().length()-4, files.get(i).getName().length()));
					 renamer = new File(mat1.group(3)+extension);
					 log(files.get(i).getName(), renamer.getName());
					 files.get(i).renameTo(renamer);
					 files.set(i, renamer);
					 counter++;
				}
			}
			if(counter==files.size()) finished = true;
			else if(!pat1.equals(pat2)) pat1=pat2;
			else	
			{
				int response = JOptionPane.showConfirmDialog(null, "Not all episodes were renamed .. Try the Beta Method ?");
				if(response != JOptionPane.YES_OPTION)	finished = true;
				else 
				{
					finished = true;
					rename_beta();
				}
			}
		}
	}
	
	public static void rename_beta() throws FileNotFoundException, UnsupportedEncodingException	 //another method of renaming but not so practical
																								 //can be used if the first method didn't work
	{
		Pattern pat3 = Pattern.compile("(\\d\\d\\d\\d)|(\\d\\d\\d?)");		//the second regex group detects any sequential two or three numbers
		Matcher mat3 = null;												//the first regex group is to remove the year of production if exists
		File extension;
		File renamer;
		for(int i=0 ; i<files.size() ; i++)
		{
			if(files.get(i).getName().matches("\\d\\d\\d?\\.\\w\\w\\w"))
						continue;
			mat3 = pat3.matcher(files.get(i).getName().replaceAll("\\[[^]]+\\]", ""));
			if(mat3.find())
			{
				extension = new File(files.get(i).getName().substring(files.get(i).getName().length()-4, files.get(i).getName().length()));
				renamer = new File(mat3.group(2)+extension);
				log(files.get(i).getName(), renamer.getName());
				files.get(i).renameTo(renamer);
			}
		}
	}
	
	public static void removeURL() throws FileNotFoundException, UnsupportedEncodingException     
															//deletes anything between brackets  [uploaded by...] Dark S1E1 [720p][english]
	{
		File remover;
		for(int i=0 ; i<files.size() ; i++)
		{
			if(files.get(i).getName().startsWith("["))
			{
				remover = new File(files.get(i).getName().replaceAll("\\[[^]]+\\]", ""));
				files.get(i).renameTo(remover);
				log(files.get(i).getName(),remover.getName());
				files.set(i, remover);
			}
		}
	}
	
	public static void log(String oldName , String newName) throws FileNotFoundException, UnsupportedEncodingException
	{
		writer.println(oldName+"<------------------->"+newName);
	}
	
	public static void restore() throws IOException
	{
		Pattern oldpat = Pattern.compile("(.+)\\<");
		Pattern newpat = Pattern.compile("\\>(.+\\.\\w\\w\\w\\w?)");
		Matcher oldmat,newmat;
		File log = new File(System.getProperty("user.dir")+"\\log(Do not delete).txt");
		FileReader fr = new FileReader(log);
		BufferedReader br = new BufferedReader(fr);
		String line;
		br.readLine();
		br.readLine();
		while( (line = br.readLine())!=null )
		{
			System.out.println(line);
			oldmat = oldpat.matcher(line);
			newmat = newpat.matcher(line);
			if(newmat.find() && oldmat.find())
				{
					files.get(files.indexOf(new File(System.getProperty("user.dir")+"\\"+newmat.group(1)))).renameTo(new File(oldmat.group(1)));
				}
		}
		br.close();
	}
	
	public static void thanks() throws IOException, URISyntaxException
	{
		String []buttons2 = {"my FB profile","send E-mail","Donate by Paypal"};
		ImageIcon happyIcon = new ImageIcon(Renamer.class.getResource("/smile.png"));
		while(true)
			{
				int response = JOptionPane.showOptionDialog(null, "Thanks for using my program\nE-mail: khalidwaleed0@outlook.com",
						"Episode_Renamer V1.1", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, happyIcon ,buttons2,buttons2[2]);
				if(response == JOptionPane.YES_OPTION)
					Desktop.getDesktop().browse(new URI("www.facebook.com/khalidwaleed0"));
				else if	(response == JOptionPane.NO_OPTION)
				{
					StringSelection stringSelection = new StringSelection("khalidwaleed0@outlook.com");
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(stringSelection, null);
					JOptionPane.showMessageDialog(null, "E-mail copied Successfully");
				}
				else if (response == JOptionPane.CANCEL_OPTION)
				{
					Desktop.getDesktop().browse(new URI("www.paypal.me/khalidwaleed0"));
				}
				else if(response == JOptionPane.CLOSED_OPTION)
					System.exit(0);
			}
	}
}
package Episode_Renamer;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Gui extends JFrame {

	private static final long serialVersionUID = 5245030778666818777L;
	private JPanel contentPane;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Gui() {
		UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/appIcon.png")));
		setTitle("Episodes Renamer");
		setSize(371, 156);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnRename = new JButton("Rename");
		btnRename.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Renamer.writer = new PrintWriter("log(Do not delete).txt", "UTF-8");
					Renamer.writer.println("old Name -----------------------------------------> new Name");
					Renamer.writer.println("If episodes were not renamed correctly leave this and open the program again to restore old names");
					Renamer.rename();
					Renamer.writer.close();
				}catch(Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRename.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnRename.setBounds(10, 52, 89, 23);
		contentPane.add(btnRename);
		
		JButton btnRemoveUrl = new JButton("Remove URL");
		btnRemoveUrl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Renamer.writer = new PrintWriter("log(Do not delete).txt", "UTF-8");
					Renamer.writer.println("old Name -----------------------------------------> new Name");
					Renamer.writer.println("If episodes were not renamed correctly leave this and open the program again to restore old names");
					Renamer.removeURL();
					Renamer.writer.close();
				}catch(Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRemoveUrl.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnRemoveUrl.setBounds(116, 52, 112, 24);
		contentPane.add(btnRemoveUrl);
		
		JButton btnRestore = new JButton("Restore");
		btnRestore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Renamer.restore();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRestore.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnRestore.setBounds(246, 52, 89, 24);
		contentPane.add(btnRestore);
		
		JLabel lblNewLabel = new JLabel("What can I help you with ?");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setBounds(10, 11, 177, 23);
		contentPane.add(lblNewLabel);
		
		JLabel lblProfile = new JLabel();
		lblProfile.setHorizontalAlignment(SwingConstants.RIGHT);
		lblProfile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://www.facebook.com/khalidwaleed0"));
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		lblProfile.setText("<html>"+"By : "+"<a href=\"\">Khalid Waleed</a>"+"</html>");
		lblProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblProfile.setBounds(215, 87, 120, 14);
		contentPane.add(lblProfile);
		
		JLabel lblGithub = new JLabel("");
		lblGithub.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://github.com/khalidwaleed0"));
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		lblGithub.setText("<html>"+"<a href=\"\">visit my github</a>"+"</html>");
		lblGithub.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblGithub.setBounds(10, 87, 89, 14);
		contentPane.add(lblGithub);
		
	}
}

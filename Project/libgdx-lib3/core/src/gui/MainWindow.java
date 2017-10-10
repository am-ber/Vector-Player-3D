package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;

public class MainWindow extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setResizable(false);
		setTitle("Vector Viewer 3D");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblVectorViewerd = new JLabel("Vector Viewer 3D");
		lblVectorViewerd.setFont(new Font("Tahoma", Font.BOLD, 32));
		lblVectorViewerd.setBounds(10, 11, 291, 45);
		contentPane.add(lblVectorViewerd);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(10, 67, 200, 394);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblSongInfo = new JLabel("Song Name:");
		lblSongInfo.setBounds(10, 11, 63, 14);
		panel.add(lblSongInfo);
		
		JLabel lblSongName = new JLabel("");
		lblSongName.setBounds(71, 11, 119, 14);
		panel.add(lblSongName);
		
		JLabel lblSongInfo2 = new JLabel("Album Art:");
		lblSongInfo2.setBounds(10, 61, 73, 14);
		panel.add(lblSongInfo2);
		
		JLabel lblAlbumArt = new JLabel("");
		lblAlbumArt.setBounds(10, 86, 180, 180);
		panel.add(lblAlbumArt);
		
		JLabel lblSongInfo3 = new JLabel("Artist:");
		lblSongInfo3.setBounds(10, 36, 37, 14);
		panel.add(lblSongInfo3);
		
		JLabel lblArtist = new JLabel("");
		lblArtist.setBounds(46, 36, 144, 14);
		panel.add(lblArtist);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.setBounds(628, 11, 106, 45);
		contentPane.add(btnPlay);
		
		JButton btnAddSong = new JButton("Add Song");
		btnAddSong.setBounds(539, 11, 79, 45);
		contentPane.add(btnAddSong);
		
		JButton btnChooseSongFolder = new JButton("Choose Song Folder");
		btnChooseSongFolder.setBounds(311, 11, 129, 45);
		contentPane.add(btnChooseSongFolder);
		
		JList list = new JList();
		list.setBackground(Color.LIGHT_GRAY);
		list.setBounds(220, 67, 514, 394);
		contentPane.add(list);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(450, 11, 79, 45);
		contentPane.add(btnRefresh);
	}
}
